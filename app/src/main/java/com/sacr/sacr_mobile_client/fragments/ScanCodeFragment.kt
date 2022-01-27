package com.sacr.sacr_mobile_client.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.budiyev.android.codescanner.*
import com.sacr.sacr_mobile_client.GreenPassStorage
import com.sacr.sacr_mobile_client.R

class ScanCodeFragment(var manager: FragmentManager) : Fragment() {

    val CAMERA_REQUEST_CODE = 101

    lateinit var layout: FrameLayout
    lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root: View = inflater.inflate(R.layout.scan_code_layout, container, false)

        this.layout = root.findViewById(R.id.fragment_scan_code)

        setupPermissions(root.context)
        val scannerView = root.findViewById<CodeScannerView>(R.id.scanner)
        codeScanner = CodeScanner(root.context, scannerView)
        startCodeScanner()
        codeScanner.startPreview()

        return root
    }

    private fun startCodeScanner() {
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                val transaction: FragmentTransaction = manager.beginTransaction()
                val greenPassFragment = GreenPassFragment(manager, it.text)
                transaction.replace(R.id.fragment_container, greenPassFragment)
                transaction.commit()
            }
        }
    }

    private fun setupPermissions(ctx: Context) {
        val permission = ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        activity?.let { ActivityCompat.requestPermissions(it, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE ->
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
        }
    }
}