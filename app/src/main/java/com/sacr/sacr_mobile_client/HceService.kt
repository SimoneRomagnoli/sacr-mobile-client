package com.sacr.sacr_mobile_client

import android.nfc.cardemulation.HostApduService
import android.os.Bundle

class HceService : HostApduService() {

    val error = "error"
    val selectionByte = 11
    val part1 = 1
    val part2 = 2
    val part3 = 3

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
        return if (p0 != null) {
            val code = GreenPassStorage.getGreenPass(this)
            val firstBlock: Int = code.length / 3
            val secondBlock: Int = firstBlock * 2
            when (p0[selectionByte].toInt()) {
                part1 -> code.substring(0, firstBlock).toByteArray()
                part2 -> code.substring(firstBlock, secondBlock).toByteArray()
                part3 -> code.substring(secondBlock, code.length).toByteArray()
                else -> error.toByteArray()
            }
        } else {
            error.toByteArray()
        }
    }

    override fun onDeactivated(p0: Int) {
        println("Service Deactivated")
    }


}