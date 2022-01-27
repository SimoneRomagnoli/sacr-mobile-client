package com.sacr.sacr_mobile_client

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sacr.sacr_mobile_client.fragments.GreenPassFragment
import com.sacr.sacr_mobile_client.fragments.ScanButtonFragment

class MainActivity : AppCompatActivity() {
    private lateinit var manager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.manager = supportFragmentManager

        val preferences = this.getSharedPreferences("com.sacr.sacr-mobile-client", Context.MODE_PRIVATE)
        val greenPass: String? = preferences.getString("green_pass", null)

        val transaction: FragmentTransaction = manager.beginTransaction()

        if (greenPass != null) {
            val greenPassFragment = GreenPassFragment(manager, greenPass)
            transaction.add(R.id.fragment_container, greenPassFragment)
        } else {
            val scanButtonFragment = ScanButtonFragment(manager)
            transaction.add(R.id.fragment_container, scanButtonFragment)
        }

        transaction.commit()
    }
}