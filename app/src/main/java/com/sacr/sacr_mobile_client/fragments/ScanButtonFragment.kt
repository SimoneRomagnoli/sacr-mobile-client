package com.sacr.sacr_mobile_client.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sacr.sacr_mobile_client.R

class ScanButtonFragment(var manager: FragmentManager) : Fragment() {

    lateinit var layout: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.scan_button_layout, container, false)

        this.layout = root.findViewById(R.id.fragment_scan_button)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}