package com.example.romanticyeojido.ui.locker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.romanticyeojido.databinding.FragmentPictureBinding

class LockerPictureFragment: Fragment() {
    lateinit var binding: FragmentPictureBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        binding = FragmentPictureBinding.inflate(inflater, container, false)

        val locker = Intent(activity, LockerActivity::class.java)

        binding.btnBack.setOnClickListener {
            startActivity(locker)
            activity?.finish()
        }

        return binding.root
    }
}