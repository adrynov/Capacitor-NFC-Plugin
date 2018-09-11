package com.adrynov.capacitor;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.TagTechnology;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * This NFC plugin allows to read (and in future write) NFC tags.
 */
@NativePlugin(permissions = {Manifest.permission.NFC})
public class NFCPlugin extends Plugin {

    private static final String TAG = NFCPlugin.class.getSimpleName();

    private static final String PERMISSION_NOT_SET = Manifest.permission.NFC + " permission is not set in AndroidManifest.xml";

    public static final String NFC_TAG_SCAN_EVENT = "NFC_TAG_SCANNED";
    public static final String NFC_NDEF_SCAN_EVENT = "NFC_NDEF_SCANNED";
    public static final String NFC_NDEF_FORMATTED_SCAN_EVENT = "NFC_NDEF_FORMATTED_SCANNED";

    private static final String ACTION_SHOW_SETTINGS = "SHOW_SETTINGS";

    private static final String NDEF = "ndef";
    private static final String NDEF_MIME = "ndef-mime";
    private static final String NDEF_FORMATABLE = "ndef-formatable";

    private static final String STATUS_NFC_OK = "ok";
    private static final String STATUS_NO_NFC = "none";
    private static final String STATUS_NFC_DISABLED = "disabled";

    // Message constants
    private static final String MESSAGE_OK = "OK";
    private static final String MESSAGE_NO_NFC = "Your device does not have NFC hardware available";
    private static final String MESSAGE_NFC_DISABLED = "Unable to use NFC, user denied request";
    private static final String MESSAGE_UNABLE_TO_PROCESS_TAG = "Unable to process NFC tag";

    private NfcAdapter nfcAdapter;

    private final List<IntentFilter> intentFilters = new ArrayList<>();
    private ArrayList<String[]> techLists = new ArrayList<>();

    private TagTechnology tagTechnology = null;
    private Class<?> tagTechnologyClass;

    private PendingIntent pendingIntent = null;
    private Intent savedIntent = null;

    private boolean scanning = false;

    @Override
    public void load() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        createPendingIntent();
    }

    /**
     * Get current NfC status.
     */
    @PluginMethod()
    @SuppressWarnings("MissingPermission")
    public void getStatus(PluginCall call) {
        if (hasRequiredPermissions()) {
            JSObject ret = new JSObject();
            ret.put("status", getNfcStatus());
            call.success(ret);

        } else {
            call.reject(PERMISSION_NOT_SET);
        }
    }

    /**
     * Opens the device’s NFC settings.
     */
    @PluginMethod()
    public void showSettings(PluginCall call) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NFC_SETTINGS);
            getActivity().startActivity(intent);
        } else {
            Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            getActivity().startActivity(intent);
        }

        call.success();
    }

    private void createPendingIntent() {
        if (pendingIntent == null) {
            Activity activity = getActivity();
            Intent intent = new Intent(activity, activity.getClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
        }
    }

    private String getNfcStatus() {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        if (nfcAdapter == null) {
            return STATUS_NO_NFC;
        } else if (!nfcAdapter.isEnabled()) {
            return STATUS_NFC_DISABLED;
        } else {
            return STATUS_NFC_OK;
        }
    }


}
