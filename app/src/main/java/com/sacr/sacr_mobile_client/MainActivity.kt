package com.sacr.sacr_mobile_client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sacr.sacr_mobile_client.fragments.ScanButtonFragment

class MainActivity : AppCompatActivity() {
    lateinit var manager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.manager = supportFragmentManager

        val scanButtonFragment = ScanButtonFragment(manager)
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.add(R.id.fragment_container, scanButtonFragment)
        transaction.commit()

    }
}