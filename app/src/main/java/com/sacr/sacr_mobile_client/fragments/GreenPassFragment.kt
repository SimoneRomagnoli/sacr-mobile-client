package com.sacr.sacr_mobile_client.fragments

import COSE.Encrypt0Message
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.nfc.*
import android.nfc.tech.Ndef
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.iot.cbor.CborMap
import com.sacr.sacr_mobile_client.MainActivity
import com.sacr.sacr_mobile_client.R
import nl.minvws.encoding.Base45
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.Inflater

class GreenPassFragment(private var manager: FragmentManager, private val greenPassCode: String) : Fragment() {

    private lateinit var layout: FrameLayout
    private lateinit var nfcButton: Button
    private lateinit var changeGreenPassButton: Button
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var tag: Tag

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
            Toast.makeText(activity, "Sending NFC Green Pass", Toast.LENGTH_LONG).show()
            //tag = (activity as MainActivity).getTag()
            //writeOnTag(greenPassCode, tag)
            nfcAdapter.invokeBeam(activity)
            Toast.makeText(activity, "Green Pass Sent", Toast.LENGTH_LONG).show()
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

        // SETUP NFC ADAPTER
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
        nfcAdapter.apply {
            nfcAdapter.setBeamPushUrisCallback(FileUriCallback(), activity)
        }
        if (nfcAdapter == null) {
            Toast.makeText(activity, "This device does not support NFC.", Toast.LENGTH_LONG).show()
        } else {
            //var pendingIntent = PendingIntent.getActivity(activity, 0, Intent(activity, javaClass), 0)
            //var tag = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
            //tag.addCategory(Intent.CATEGORY_DEFAULT)
            //var writeTagFilters = List(1) { tag }
        }

        return root
    }

    private fun readFromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action
            || NfcAdapter.ACTION_TECH_DISCOVERED == action
            || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val messages: MutableList<NdefMessage> = mutableListOf()
            if (rawMessages != null) {
                for (msg in rawMessages) {
                    messages.add(msg as NdefMessage)
                }
            }
        }
    }

    private fun writeOnTag(text: String, tag: Tag) {
        val records = listOf(createRecords(text))
        val message = NdefMessage(records.toTypedArray())
        val ndef = Ndef.get(tag)
        ndef.connect()
        ndef.writeNdefMessage(message)
        ndef.close()
    }

    private fun createRecords(text: String): NdefRecord {
        val bytes = text.byteInputStream().readBytes()
        return NdefRecord(text.byteInputStream().readBytes())
    }

    private inner class FileUriCallback : NfcAdapter.CreateBeamUrisCallback {
        override fun createBeamUris(event: NfcEvent): Array<Uri> {
            context?.openFileOutput("code.txt", Context.MODE_PRIVATE).use { output ->
                output?.write(greenPassCode.toByteArray())
            }
            val file = File("code.txt")
            return listOf(file.toUri()).toTypedArray()
        }
    }
}