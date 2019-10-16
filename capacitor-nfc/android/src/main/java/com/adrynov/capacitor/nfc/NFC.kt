import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
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
    private val MESSAGE_OK = "OK"
    private val MESSAGE_NO_NFC = "NFC is not support"
    private val MESSAGE_NFC_DISABLED = "Unable to use NFC, user denied request"
    private val MESSAGE_UNABLE_TO_PROCESS_TAG = "Unable to process NFC tag"
    private val EVENT_TAG_DISCOVERED = "TAG_DISCOVERED"
    private val EVENT_TECH_DISCOVERED = "TECH_DISCOVERED"
    private val EVENT_NDEF_DISCOVERED = "NDEF_DISCOVERED"
    private val EVENT_NDEF_FORMATTABLE_DISCOVERED = "NDEF_FORMATTABLE_DISCOVERED"

    private val STATUS_NFC_OK = "ok"
    private val STATUS_NO_NFC = "none"
    private val STATUS_NFC_DISABLED = "disabled"

    // NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
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

        if (intent != null) {
            // process the data from the scanned NFC tag
//            val tagFromIntent: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
//            Log.i(TAG, "Hello")
        }
    }

    override fun handleOnPause() {
        val callingActivity = this.bridge.activity

        launch {
            disableForegroundDispatch(callingActivity)
        }
    }

    override fun handleOnStop() {
        cancel() // cancel any coroutine jobs
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
        // Check NFC permission
        if (!hasPermission(Manifest.permission.NFC)) {
            saveCall(call)
            pluginRequestAllPermissions()
            return
        }

        if (this.nfcAdapter == null) {
            call.error(MESSAGE_NO_NFC)
            return
        }

        val ret = JSObject().apply {
            put("enabled", true)
        }

        return call.success(ret)
    }

    // https://developer.android.com/guide/topics/connectivity/nfc/advanced-nfc#foreground-dispatch
    private fun enableForegroundDispatch() {
        if (this.nfcAdapter == null) {
            Log.e(TAG, MESSAGE_NO_NFC);
            return
        }

        val activity = this.bridge.activity;

        val intent = Intent(context, activity.javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        // The Android system can populate it with the details of the tag when it is scanned.
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

        val intentFilters = arrayOf(ndef)

        // Supported NFC technologies
        val techList = arrayOf(
//                arrayOf(MifareUltralight::class.java.name, Ndef::class.java.name, NfcA::class.java.name),
//                arrayOf(MifareClassic::class.java.name, Ndef::class.java.name, NfcA::class.java.name),
                arrayOf<String>(NfcA::class.java.name)
        )

        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, intentFilters, techList)
    }

    private fun disableForegroundDispatch(callingActivity: Activity) {
        callingActivity.runOnUiThread {
            try {
                nfcAdapter?.disableForegroundDispatch(callingActivity)
            } catch (e: IllegalStateException) {
                Log.w(TAG, e)
            }
        }
    }
}
