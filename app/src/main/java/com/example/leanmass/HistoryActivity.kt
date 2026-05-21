package com.example.leanmass

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leanmass.databinding.ActivityHistoryBinding
import com.example.leanmass.utils.AuthManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = AuthManager.getCurrentUserId()
        if (uid == null) {
            finish(); return
        }

        loadRecords(uid)
    }

    private fun loadRecords(uid: String) {
        firestore.collection("records")
            .whereEqualTo("userId", uid)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val records = snapshot.documents.toMutableList()

                if (records.isEmpty()) {
                    Toast.makeText(this, "Aucun historique trouvé", Toast.LENGTH_SHORT).show()
                }

                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                binding.recyclerView.adapter = HistoryAdapter(records) { document ->
                    // ✅ Delete from Firestore by document ID
                    firestore.collection("records").document(document.id)
                        .delete()
                        .addOnFailureListener {
                            Toast.makeText(this, "Erreur suppression: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erreur chargement: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}