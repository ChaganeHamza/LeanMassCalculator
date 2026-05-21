package com.example.leanmass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.leanmass.databinding.ItemRecordBinding
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private val records: MutableList<DocumentSnapshot>,        // ✅ was LbmRecord
    private val onDelete: (DocumentSnapshot) -> Unit           // ✅ was LbmRecord
) : RecyclerView.Adapter<HistoryAdapter.VH>() {

    inner class VH(val b: ItemRecordBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = records.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val doc = records[position]

        // ✅ Read fields from Firestore document
        val gender = doc.getString("gender") ?: "-"
        val weight = doc.getDouble("weight") ?: 0.0
        val height = doc.getDouble("height") ?: 0.0
        val lbm    = doc.getDouble("lbm") ?: 0.0
        val ok     = doc.getBoolean("isSatisfactory") ?: false
        val date   = doc.getDate("date")

        val dateStr = if (date != null)
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date)
        else "-"

        holder.b.tvItemInfo.text = "$gender · %.1f kg · %.1f cm".format(weight, height)
        holder.b.tvItemDate.text = dateStr
        holder.b.tvItemLbm.text  = "%.1f kg".format(lbm)

        if (ok) {
            holder.b.tvItemLbm.setTextColor(0xFF1D9E75.toInt())
            holder.b.ivItemStatus.setImageResource(android.R.drawable.checkbox_on_background)
        } else {
            holder.b.tvItemLbm.setTextColor(0xFFBA7517.toInt())
            holder.b.ivItemStatus.setImageResource(android.R.drawable.ic_dialog_alert)
        }

        holder.b.btnItemDelete.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onDelete(records[pos])
                records.removeAt(pos)
                notifyItemRemoved(pos)
            }
        }
    }
}