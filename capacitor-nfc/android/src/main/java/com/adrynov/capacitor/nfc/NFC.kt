package com.adrynov.capacitor.nfc

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.NfcF
import android.util.Log
import com.getcapacitor.*
import java.util.*


/**
 * Capacitor plugin that access NFC capabilities of a device.
 */
@NativePlugin(permissions = [Manifest.permission.NFC]) // permissionRequestCode = PluginRequestCodes.NFC
class NFC : Plugin() {
    private val TAG = NFC::class.java.simpleName

    private val PERMISSION_NOT_SET = Manifest.permission.NFC + " permission is not set in AndroidManifest.xml"

    private val NFC_REQUEST_PERMISSIONS = 9020

    private val NDEF = "ndef"
    private val NDEF_MIME = "ndef-mime"
    private val NDEF_FORMATABLE = "ndef-formatable"

    // Message constants
    private val MESSAGE_OK = "OK"
    private val MESSAGE_NO_NFC = "Device does not support NFC"
    private val MESSAGE_NFC_DISABLED = "Unable to use NFC, user denied request"
    private val MESSAGE_UNABLE_TO_PROCESS_TAG = "Unable to process NFC tag"
    private val EVENT_TAG_DISCOVERED = "TAG_DISCOVERED"
    private val EVENT_TECH_DISCOVERED = "TECH_DISCOVERED"
    private val EVENT_NDEF_DISCOVERED = "NDEF_DISCOVERED"
    private val EVENT_NDEF_FORMATTABLE_DISCOVERED = "NDEF_FORMATTABLE_DISCOVERED"

    private val STATUS_NFC_OK = "ok"
    private val STATUS_NO_NFC = "none"
    private val STATUS_NFC_DISABLED = "disabled"

    private var nfcAdapter: NfcAdapter? = null

    internal var watchingCalls: Map<String, PluginCall> = HashMap()

    override fun handleOnStart() {
        super.handleOnStart()

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(context)?.let { it }
    }

    override fun handleOnResume() {
        super.handleOnResume()
        enableForegroundDispatch()
    }

    override fun handleOnNewIntent(intent: Intent?) {
        super.handleOnNewIntent(intent)
    }

    override fun handleOnPause() {
        super.handleOnPause()

        nfcAdapter?.disableForegroundDispatch(this.bridge.getActivity())
    }

    override fun handleOnStop() {
        super.handleOnStop()
    }

    @PluginMethod
    fun echo(call: PluginCall) {
        val value = call.getString("value")

        val ret = JSObject()
        ret.put("value", value)

        call.success(ret)
    }

    /**
     * Checks whether NFC is enabled and turned on
     */
    @PluginMethod()
    fun isNfcEnabled(call: PluginCall) {
        if (!hasPermission(Manifest.permission.NFC)) {
            saveCall(call)
            pluginRequestAllPermissions()
            return
        }

        if (this.nfcAdapter != null) {
            call.error(MESSAGE_NO_NFC)
            return
        }

        val ret = JSObject().apply {
            put("enabled", true)
        }

        return call.success(ret)
    }

    private fun enableForegroundDispatch() {
        if (this.nfcAdapter == null) {
            Log.e(TAG, MESSAGE_NO_NFC);
            return
        }
        // other apps should not be able to intercept NFC while our app is active.
        val activity = this.bridge.activity;

        val intent = Intent(context, activity.javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        // The Android system can populate it with the details of the tag when it is scanned.
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // Handle the intents that you want to intercept: TAG_DISCOVERED, TECH_DISCOVERED and/or NDEF_DISCOVERED
        val intentFilters = arrayOfNulls<IntentFilter>(1)

        // Supported NFC technologies
        val techList = arrayOf<Array<String>>()

        intentFilters[0] = IntentFilter()

        with(intentFilters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                /* Handles all MIME based dispatches. You should specify only the ones that you need. */
                this?.addDataType("text/plain")
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("Check your MIME type")
            }
        }

        // TODO:  obtain the class of the technology that you want to support.
        val techListsArray = arrayOf(arrayOf<String>(NfcF::class.java.name))


        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, intentFilters, techList)
    }
}
