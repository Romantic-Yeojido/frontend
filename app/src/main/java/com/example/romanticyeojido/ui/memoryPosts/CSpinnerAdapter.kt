package com.example.romanticyeojido.ui.memoryPosts

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.romanticyeojido.R
import com.example.romanticyeojido.databinding.ItemSpinnerDropdownBinding
import com.example.romanticyeojido.databinding.ItemSpinnerDefaultBinding

class CSpinnerAdapter(
    context: Context,
    private val items: List<String>
) : ArrayAdapter<String>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            ItemSpinnerDefaultBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ItemSpinnerDefaultBinding.bind(convertView)
        }

        binding.defaultTv.text = items[position]

        if (items[position].all {it.isDigit()}) {
            binding.defaultTv.setTextColor(context.getColor(R.color.G800))
            binding.defaultTv.gravity = Gravity.CENTER
        }

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            ItemSpinnerDropdownBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ItemSpinnerDropdownBinding.bind(convertView)
        }

        binding.spinnerTv.text = items[position]
        binding.root.setBackgroundResource(R.drawable.spinner_dropdown_selector)

        return binding.root
    }
}