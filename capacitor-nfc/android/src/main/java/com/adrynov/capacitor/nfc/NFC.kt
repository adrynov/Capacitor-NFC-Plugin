package com.adrynov.capacitor.nfc

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.nfc.tech.NfcA
import android.util.Log
import com.getcapacitor.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private const val NFC_REQUEST_PERMISSION = 9089

/**
 * Capacitor plugin that access NFC capabilities of a device.
 *
 * https://developer.android.com/guide/topics/connectivity/nfc/advanced-nfc#tag-tech
 */
@NativePlugin(
        permissions = [Manifest.permission.NFC],
        permissionRequestCode = NFC_REQUEST_PERMISSION
)
class NFC : Plugin(), CoroutineScope by MainScope() {

    private val TAG = NFC::class.java.simpleName

    private val PERMISSION_NOT_SET = Manifest.permission.NFC + " permission is not set in AndroidManifest.xml"

    private val NFC_REQUEST_PERMISSIONS = 9020

    private val NDEF = "ndef"
    private val NDEF_MIME = "ndef-mime"
    private val NDEF_FORMATABLE = "ndef-formatable"

    // Message constants
    private val MESSAGE_UNABLE_TO_PROCESS_TAG = "Unable to process NFC tag"
    private val EVENT_TAG_DISCOVERED = "TAG_DISCOVERED"
    private val EVENT_TECH_DISCOVERED = "TECH_DISCOVERED"
    private val EVENT_NDEF_DISCOVERED = "NDEF_DISCOVERED"
    private val EVENT_NDEF_FORMATTABLE_DISCOVERED = "NDEF_FORMATTABLE_DISCOVERED"

    // TAG_DISCOVERED, TECH_DISCOVERED and/or NDEF_DISCOVERED
    private val intentFilters = ArrayList<IntentFilter>()

    // Supported NFC technologies (note: MIFARE Classic cards are detected as NfcA)
    private val techLists = ArrayList<Array<String>>()

    private val EVENT_TOKEN_ERROR = "error"

    /**
     * The default NFC adapter
     */
    private var nfcAdapter: NfcAdapter? = null

    // <editor-fold desc="Adapter Status">

    private val STATUS_NFC_OK = "enabled"
    private val STATUS_NFC_DISABLED = "disabled"
    private val STATUS_NO_NFC = "none"

    private val adapterStatus: String
        get() = when {
            // the phone does not have NFC hardware
            nfcAdapter == null -> STATUS_NO_NFC
            // the NFC hardware is guaranteed not to generate or respond to any NFC communication over its NFC radio.
            nfcAdapter?.isEnabled == false -> STATUS_NFC_DISABLED
            else -> STATUS_NFC_OK
        }
    // </editor-fold>

    private var settings = NfcSettings()

    internal var watchingCalls: Map<String, PluginCall> = HashMap()

    override fun handleOnStart() {
        super.handleOnStart()

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(context)?.let { it }
    }

    override fun handleOnResume() {
        super.handleOnResume()
        enableForegroundDispatch()
    }

    override fun handleOnPause() {
        launch {
            disableForegroundDispatch()
        }

        super.handleOnPause()
    }

    override fun handleOnStop() {
        cancel() // cancel any coroutine jobs
        super.handleOnStop()
    }

    override fun handleRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults)

        if (savedCall == null) {
            return
        }

        grantResults.forEach {
            if (it == PackageManager.PERMISSION_DENIED) {
                savedCall.error("User denied NFC permission")
                return
            }
        }

        if (savedCall.methodName.equals(NFC::getStatus.name)) {
            getStatus(savedCall)
        } else {
            savedCall.resolve()
            savedCall.release(bridge)
        }
    }

    override fun handleOnNewIntent(intent: Intent?) {
        super.handleOnNewIntent(intent)

        val callingActivity = this.bridge.activity

        if (intent != null) {
            callingActivity.intent = intent
            processTag(intent)
        }
    }

    private fun processTag(intent: Intent) {
        if (intent.action.isNullOrBlank()) {
            return
        }

//        val tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
//        val nfcTag = NfcTag(tag)

        when (intent.action) {

            NfcAdapter.ACTION_NDEF_DISCOVERED -> {
                // Ndef ndef = Ndef.get(tag);
                // Parcelable[] messages =
                // intent.getParcelableArrayExtra((NfcAdapter.EXTRA_NDEF_MESSAGES));
                // fireNdefEvent(NDEF_MIME, ndef, messages);
            }

            NfcAdapter.ACTION_TECH_DISCOVERED -> {
                // for (String tagTech : tag.getTechList()) {
                // Log.d(TAG, tagTech);
                // if (tagTech.equals(NdefFormatable.class.getName())) {
                // fireNdefFormatableEvent(tag);
                // } else if (tagTech.equals(Ndef.class.getName())) { //
                // Ndef ndef = Ndef.get(tag);
                // fireNdefEvent(NDEF, ndef, messages);
                // }
                // }
            }

            NfcAdapter.ACTION_TAG_DISCOVERED -> {
//                sendEvent(EVENT_TAG_DISCOVERED, tag);
            }
        }

//        this.bridge.activity.intent = Intent()
    }

    // <editor-fold desc="Plugin Methods">

    /**
     * Checks whether NFC is enabled and turned on.
     */
    @PluginMethod
    fun getStatus(call: PluginCall) {
        // Check NFC permission is granted
        if (!hasPermission(Manifest.permission.NFC)) {
            saveCall(call)
            pluginRequestAllPermissions()
            return
        }

        val ret = JSObject().apply {
            put("status", adapterStatus)
        }

        return call.success(ret)
    }

    /**
     * Returns information about the touched NFC tag.
     */
    @PluginMethod
    fun getTagInfo(call: PluginCall) {
        call.unimplemented()
//
//        call.save()
//
//        if (!hasPermission(Manifest.permission.NFC)) {
//            saveCall(call)
//            pluginRequestAllPermissions()
//            return
//        }

        // TODO
//        watchingCalls.put(call.getCallbackId(), call)
    }

    @PluginMethod
    fun startScanning(call: PluginCall) {
        if (adapterStatus != STATUS_NFC_OK) {
            // No NFC hardware or NFC is disabled by the user
            call.reject(adapterStatus)
            return
        }

        saveCall(call)
        parseSettings(call)

        // TAG_DISCOVERED - most generic
        intentFilters.add(IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED))

//        if (settings.techEnabled()) {
//            intentFilters.add(new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED))
//            techLists.add(new String[] { "android.nfc.tech.NfcA" })
//        }
//
//        if (settings.ndefEnabled()) {
//            IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
//
//            try {
//                intentFilter.addDataType("*/*")
//            } catch (IntentFilter.MalformedMimeTypeException e) {
//                e.printStackTrace()
//            }
//
//            techLists.add(new String[] { "android.nfc.tech.NfcA" })
//            techLists.add(new String[] { Ndef.class.getName() })
//
//            if (settings.ndefFormattableEnabled()) {
//                techLists.add(new String[] { NdefFormatable.class.getName() })
//            }
//        }

        restartForegroundDispatch()
        call.success()
    }

    @PluginMethod
    fun stopScanning(call: PluginCall) {
        removeTagDiscoveredFilter()
        removeTechFilter()

        if (settings.ndefEnabled) {
//            removeIntentFilter("*/*")
//            this.removeFromTechList(new String[] { Ndef.class.getName() })
        }

        disableForegroundDispatch()
        call.success()
    }

    @PluginMethod
    fun showSettings(call: PluginCall) {
        val intent = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
            Intent(android.provider.Settings.ACTION_NFC_SETTINGS) else
            Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)

        val callingActivity = this.bridge.activity
        callingActivity.startActivity(intent)

        call.success()
    }

    // </editor-fold>

    // <editor-fold desc="Foreground Dispatch">

    // https://developer.android.com/guide/topics/connectivity/nfc/advanced-nfc#foreground-dispatch
    private fun enableForegroundDispatch() {
        Log.d(TAG, "Enabling foreground dispatch")

        // No NFC hardware
        if (this.nfcAdapter == null) {
            Log.e(TAG, STATUS_NO_NFC)
            return
        }

        val callingActivity = this.bridge.activity

        if (callingActivity == null || callingActivity.isFinishing) {
            Log.w(TAG, "Activity is not running")
            return
        }

        val intent = Intent(context, callingActivity.javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        // The Android system can populate this with the details of the tag when it is scanned.
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // Declare the intents that you want to intercept: TAG_DISCOVERED, TECH_DISCOVERED or NDEF_DISCOVERED
        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            try {
                /* Handles all MIME based dispatches.You should specify only the ones that you need. */
                addDataType("*/*")
            } catch (e: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("fail", e)
            }
        }

        val intentFilters = intentFilters.toTypedArray() // arrayOf(ndef)

        // Supported NFC technologies
        val techList = arrayOf(
//                arrayOf(MifareUltralight::class.java.name, Ndef::class.java.name, NfcA::class.java.name),
//                arrayOf(MifareClassic::class.java.name, Ndef::class.java.name, NfcA::class.java.name),
                arrayOf<String>(NfcA::class.java.name)
        )

        if (intentFilters.isNotEmpty() || techList.isNotEmpty()) {
            nfcAdapter?.enableForegroundDispatch(callingActivity, pendingIntent, intentFilters, techList)
        }
    }

    /**
     * Stop listening for NFC data.
     */
    private fun disableForegroundDispatch() {
        Log.d(TAG, "Stopping NFC")

        val callingActivity = this.bridge.activity

        try {
            nfcAdapter?.disableForegroundDispatch(callingActivity)
        } catch (e: IllegalStateException) {
            Log.w(TAG, e)
        }

//        callingActivity.runOnUiThread {
//            try {
//                nfcAdapter?.disableForegroundDispatch(callingActivity)
//            } catch (e: IllegalStateException) {
//                Log.w(TAG, e)
//            }
//        }
    }

    private fun restartForegroundDispatch() {
        Log.d(TAG, "Restarting NFC")

        val callingActivity = this.bridge.activity

        callingActivity.runOnUiThread {
            try {
                nfcAdapter?.disableForegroundDispatch(callingActivity)
                enableForegroundDispatch()
            } catch (e: IllegalStateException) {
                Log.w(TAG, e)
            }
        }
    }

    // </editor-fold>

    private fun getIntentFilters(): Array<IntentFilter> {
        return intentFilters.toTypedArray()
    }

    /**
     * Parse the call's options into {@see NfcSettings}
     */
    private fun parseSettings(call: PluginCall?) {
        if (call != null) {
            settings.techEnabled = call.getBoolean("techEnabled", true)
            settings.ndefEnabled = call.getBoolean("ndefEnabled", false)
            settings.ndefFormattable = call.getBoolean("ndefFormattable", false)
        } else {
            settings.techEnabled = false
            settings.ndefEnabled = false
            settings.ndefFormattable = false
        }
    }

    private fun removeTagDiscoveredFilter() {
        intentFilters.forEach {
            if (it.getAction(0).equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
                intentFilters.remove(it)
            }
        }
//        Iterator<IntentFilter> iterator = intentFilters.iterator()
    }

    private fun removeTechFilter() {
//        Iterator<IntentFilter> iterator = intentFilters.iterator()
//        while (iterator.hasNext()) {
//            IntentFilter intentFilter = iterator.next()
//            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intentFilter.getAction(0))) {
//                iterator.remove()
//            }
//        }
    }

    private fun sendError(error: String) {
        val data = JSObject()
        data.put("error", error)
        notifyListeners(EVENT_TOKEN_ERROR, data, true)
    }
}
