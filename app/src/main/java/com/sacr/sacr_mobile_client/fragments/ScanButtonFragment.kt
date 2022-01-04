package com.sacr.sacr_mobile_client.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sacr.sacr_mobile_client.R

class ScanButtonFragment(private var manager: FragmentManager) : Fragment() {

    private lateinit var layout: FrameLayout
    private lateinit var scanButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root: View = inflater.inflate(R.layout.scan_button_layout, container, false)

        this.layout = root.findViewById(R.id.fragment_scan_button)
        this.scanButton = root.findViewById(R.id.scan_button)
        this.scanButton.setOnClickListener {
            val transaction: FragmentTransaction = manager.beginTransaction()
            val scanCodeFragment = ScanCodeFragment(manager)
            transaction.replace(R.id.fragment_container, scanCodeFragment)
            transaction.commit()
        }

        return root
    }
}