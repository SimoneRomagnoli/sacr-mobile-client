package com.sacr.sacr_mobile_client.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sacr.sacr_mobile_client.R

class GreenPassFragment(private var manager: FragmentManager, private val greenPassCode: String) : Fragment() {

    private lateinit var layout: FrameLayout
    private lateinit var nfcButton: Button
    private lateinit var changeGreenPassButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root: View = inflater.inflate(R.layout.green_pass_layout, container, false)

        val preferences = root.context.getSharedPreferences("com.sacr.sacr-mobile-client", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("green_pass", greenPassCode)
        editor.commit()

        this.layout = root.findViewById(R.id.fragment_green_pass)
        this.nfcButton = root.findViewById(R.id.nfc_button)
        this.nfcButton.setOnClickListener {
            Toast.makeText(activity, "Yey", Toast.LENGTH_LONG).show()
        }
        this.changeGreenPassButton = root.findViewById(R.id.change_green_pass_button)
        this.changeGreenPassButton.setOnClickListener {
            val transaction: FragmentTransaction = manager.beginTransaction()
            val scanButtonFragment = ScanButtonFragment(manager)
            transaction.replace(R.id.fragment_container, scanButtonFragment)
            transaction.commit()
            editor.remove("green_pass")
            editor.commit()
        }

        return root
    }
}