package com.sacr.sacr_mobile_client

import android.content.Context
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.widget.Toast

class HceService : HostApduService() {
    /**
     * This method is called whenever a NFC reader sends
     * an Application Protocol Data Unit (APDU) to your
     * service. APDUs are the application-level packets
     * being exchanged between the NFC reader and your
     * HCE service. That application-level protocol is
     * half-duplex: the NFC reader sends you a command APDU,
     * and it waits for you to send a response APDU in return.
     */
    override fun processCommandApdu(p0: ByteArray?, p1: Bundle?): ByteArray {
        println("received adpu DIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")
        return "messaggio di prova".toByteArray()
    }

    override fun onDeactivated(p0: Int) {
        println("DIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO SOMAROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
    }

}