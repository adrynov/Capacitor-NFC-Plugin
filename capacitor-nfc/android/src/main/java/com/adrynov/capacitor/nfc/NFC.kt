package com.adrynov.capacitor.nfc

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.util.Log
import com.getcapacitor.*
import java.util.*

/**
 * Capacitor plugin that access NFC capabilities of a device.
 */
@NativePlugin(permissions = [Manifest.permission.NFC]) // permissionRequestCode = PluginRequestCodes.NFC
class NFC : Plugin() {
    private val TAG = NFC::class.java.simpleName

    private val NFC_REQUEST_PERMISSIONS = 9020

    private var nfcAdapter: NfcAdapter? = null

    private val nfcAvailable: Boolean = this.nfcAdapter != null

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

        if (!nfcAvailable) {
            call.error("NFC unavailable")
            return
        }

        val ret = JSObject().apply {
            put("enabled", true)
        }

        return call.success(ret)
    }

    private fun enableForegroundDispatch() {
        if (!nfcAvailable) {
            Log.e(TAG, "NFC is not available");
            return
        }

        val activity = this.bridge.getActivity();

        val intent = Intent(context, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        filters[0] = IntentFilter()

        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType("text/plain")
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("Check your MIME type")
            }
        }

        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }
}
