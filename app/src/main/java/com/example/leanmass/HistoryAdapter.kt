package com.example.leanmass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.leanmass.data.LbmRecord
import com.example.leanmass.databinding.ItemRecordBinding

class HistoryAdapter(
    private val records: MutableList<LbmRecord>,
    private val onDelete: (LbmRecord) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.VH>() {

    inner class VH(val b: ItemRecordBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = records.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val r = records[position]
        holder.b.tvItemInfo.text = "${r.gender} · ${r.weight} kg · ${r.height} cm"
        holder.b.tvItemDate.text = r.date
        holder.b.tvItemLbm.text  = "%.1f kg".format(r.lbm)

        if (r.isSatisfactory) {
            holder.b.tvItemLbm.setTextColor(0xFF1D9E75.toInt())
            holder.b.ivItemStatus.setImageResource(android.R.drawable.checkbox_on_background)
        } else {
            holder.b.tvItemLbm.setTextColor(0xFFBA7517.toInt())
            holder.b.ivItemStatus.setImageResource(android.R.drawable.ic_dialog_alert)
        }

        holder.b.btnItemDelete.setOnClickListener {
            val pos = holder.adapterPosition
            onDelete(records[pos])
            records.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }
}