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
import com.sacr.sacr_mobile_client.GreenPassStorage
import com.sacr.sacr_mobile_client.R

class GreenPassFragment(private var manager: FragmentManager, private val greenPassCode: String) : Fragment() {

    private lateinit var layout: FrameLayout
    private lateinit var changeGreenPassButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root: View = inflater.inflate(R.layout.green_pass_layout, container, false)

        //SAVING CODE
        context?.let { GreenPassStorage.setGreenPass(it, greenPassCode) }

        //SET UP BUTTONS
        this.layout = root.findViewById(R.id.fragment_green_pass)
        this.changeGreenPassButton = root.findViewById(R.id.change_green_pass_button)
        this.changeGreenPassButton.setOnClickListener {
            val transaction: FragmentTransaction = manager.beginTransaction()
            val scanButtonFragment = ScanButtonFragment(manager)
            transaction.replace(R.id.fragment_container, scanButtonFragment)
            transaction.commit()
            context?.let { GreenPassStorage.resetGreenPass(it) }
        }

        //DECODING
        /*
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
        */

        return root
    }


}