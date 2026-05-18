package com.example.leanmass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leanmass.data.DatabaseHelper
import com.example.leanmass.databinding.ActivityHistoryBinding
import com.example.leanmass.utils.AuthManager

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)

        val userId  = AuthManager.currentUserId(this)
        val records = db.getRecordsByUser(userId).toMutableList()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = HistoryAdapter(records) { record ->
            db.deleteRecord(record.id)
        }
    }
}