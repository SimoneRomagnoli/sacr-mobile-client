package com.sacr.sacr_mobile_client.fragments

import COSE.Encrypt0Message
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.iot.cbor.CborMap
import com.sacr.sacr_mobile_client.R
import nl.minvws.encoding.Base45
import java.io.ByteArrayOutputStream
import java.util.zip.Inflater

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

        //SAVING CODE IN SHARED PREFERENCES
        val preferences = root.context.getSharedPreferences("com.sacr.sacr-mobile-client", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("green_pass", greenPassCode)
        editor.commit()

        //SET UP BUTTONS
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

        //DECODING
        val code = greenPassCode.substring(4)
        val codeInflater = Inflater()
        codeInflater.setInput(Base45.getDecoder().decode(code))

        val output = ByteArrayOutputStream()
        val buffer = ByteArray(100000)

        while (!codeInflater.finished()) {
            val count = codeInflater.inflate(buffer)
            output.write(buffer, 0 , count)
        }

        val message = Encrypt0Message.DecodeFromBytes(output.toByteArray())
        val cborMap = CborMap.createFromCborByteArray(message.GetContent())
        val jsonString = cborMap.entrySet().last().value.toJsonString()
        val json = Gson().fromJson(jsonString, JsonObject::class.java).getAsJsonObject("1")
        val name = json.getAsJsonObject("nam")
        val givenName = name.getAsJsonPrimitive("gnt").asString
        val familyName = name.getAsJsonPrimitive("fnt").asString

        val greenPassName = root.findViewById<TextView>(R.id.green_pass_name)
        greenPassName.text = "$familyName $givenName"


        return root
    }
}