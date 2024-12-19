package com.example.romanticyeojido.ui.memoryPosts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.romanticyeojido.databinding.ItemDropdownBinding

class DropdownAdapter(
    private val items: List<String>,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<DropdownAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDropdownBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.dropdownItemText.text = item
            binding.root.setOnClickListener { onItemClicked(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDropdownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
