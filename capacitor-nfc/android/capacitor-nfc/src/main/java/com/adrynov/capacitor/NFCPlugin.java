package com.adrynov.capacitor;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.TagTechnology;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This NFC plugin allows to read (and in future write) NFC tags.
 * Version 0.0.10
 */
@NativePlugin(permissions = { Manifest.permission.NFC })
public class NFCPlugin extends Plugin {

    private static final String TAG = NFCPlugin.class.getSimpleName();

    private static final String PERMISSION_NOT_SET = Manifest.permission.NFC + " permission is not set in AndroidManifest.xml";

    public static final String EVENT_TAG_DISCOVERED = "TAG_DISCOVERED";
    public static final String EVENT_TECH_DISCOVERED = "TECH_DISCOVERED";
    public static final String EVENT_NDEF_DISCOVERED = "NDEF_DISCOVERED";
    public static final String EVENT_NDEF_FORMATTABLE_DISCOVERED = "NDEF_FORMATTABLE_DISCOVERED";

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

    // TAG_DISCOVERED, TECH_DISCOVERED and/or NDEF_DISCOVERED
    private final List<IntentFilter> intentFilters = new ArrayList<>();

    // Supported NFC technologies (note: MIFARE Classic cards are detected as NfcA)
    private ArrayList<String[]> techLists = new ArrayList<>();

    private TagTechnology tagTechnology = null;
    private Class<?> tagTechnologyClass;

    private PendingIntent pendingIntent = null;
    private Intent savedIntent = null;

    private NfcSettings settings = new NfcSettings();

    @Override
    protected void handleOnPause() {
        super.handleOnPause();
        Log.d(TAG, "onPause " + getIntent());

        stopNfc();
    }

    @Override
    protected void handleOnResume() {
        super.handleOnResume();
        Log.d(TAG, "onResume " + getIntent());

        startNfc();
    }

    @Override
    protected void handleOnNewIntent(Intent intent) {
        super.handleOnNewIntent(intent);
        Log.d(TAG, "onNewIntent " + intent);

        setIntent(intent);
        savedIntent = intent;
        handleIntent();
    }

    /**
     * Starts listening for NFC events.
     */
    @PluginMethod()
    @SuppressWarnings("MissingPermission")
    public void startScan(PluginCall call) {
        String nfcStatus = this.getNfcStatus();

        // NFC is disabled, show settings
        if (nfcStatus.equals(STATUS_NFC_DISABLED)) {
            this.showSettings(call);

            if (call != null) {
                call.reject(MESSAGE_NFC_DISABLED);
            }
            return;
        }

        if (!nfcStatus.equals(STATUS_NFC_OK)) {
            if (call != null) {
                call.reject(MESSAGE_NO_NFC);
            }
            return;
        }

        saveCall(call);
        settings = getSettings(call);

        // TAG_DISCOVERED
        intentFilters.add(new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED));

        if (settings.techEnabled()) {
            intentFilters.add(new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED));
            techLists.add(new String[] { "android.nfc.tech.NfcA" });
        }

        if (settings.ndefEnabled()) {
            IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

            try {
                intentFilter.addDataType("*/*");
            } catch (IntentFilter.MalformedMimeTypeException e) {
                e.printStackTrace();
            }

            techLists.add(new String[] { "android.nfc.tech.NfcA" });
            techLists.add(new String[] { Ndef.class.getName() });

            if (settings.ndefFormattableEnabled()) {
                techLists.add(new String[] { NdefFormatable.class.getName() });
            }
        }

        restartNfc();

        if (call != null) {
            call.success();
        }
    }

    @PluginMethod()
    public void stopScan(PluginCall call) {
        removeTagFilter();
        removeTechFilter();

        if (settings.ndefEnabled()) {
            removeIntentFilter("*/*");
            this.removeFromTechList(new String[] { Ndef.class.getName() });
        }

        stopNfc();

        if (call != null) {
            call.success();
        }
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
            Activity activity = getBridge().getActivity();
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

    private void handleIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();

        if (action == null)
            return;

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NfcTag nfcTag = new NfcTag(tag);

        if (action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            // Ndef ndef = Ndef.get(tag);
            // Parcelable[] messages =
            // intent.getParcelableArrayExtra((NfcAdapter.EXTRA_NDEF_MESSAGES));
            // fireNdefEvent(NDEF_MIME, ndef, messages);
        }

        if (action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
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

        if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            sendEvent(EVENT_TAG_DISCOVERED, tag);
        }

        setIntent(new Intent());

    }

    private NfcSettings getSettings(PluginCall call) {
        NfcSettings settings = new NfcSettings();

        if (call != null) {
            settings.setTechEnabled(call.getBoolean("enableTech", true));
            settings.setNdefEnabled(call.getBoolean("enableNdef", false));
            settings.setNdefFormattableEnabled(call.getBoolean("enableNdefFormattable", false));

        } else {
            settings.setTechEnabled(false);
            settings.setNdefEnabled(false);
            settings.setNdefFormattableEnabled(false);
        }
        //
        // settings.setResultType(getResultType(call.getString("resultType")));
        // settings.setSaveToGallery(call.getBoolean("saveToGallery",
        // CameraSettings.DEFAULT_SAVE_IMAGE_TO_GALLERY));
        // settings.setAllowEditing(call.getBoolean("allowEditing", false));
        // settings.setQuality(call.getInt("quality", CameraSettings.DEFAULT_QUALITY));
        // settings.setWidth(call.getInt("width", 0));
        // settings.setHeight(call.getInt("height", 0));
        // settings.setShouldResize(settings.getWidth() > 0 || settings.getHeight() >
        // 0);
        // settings.setShouldCorrectOrientation(call.getBoolean("correctOrientation",
        // CameraSettings.DEFAULT_CORRECT_ORIENTATION));
        // try {
        // settings.setSource(CameraSource.valueOf(call.getString("source",
        // CameraSource.PROMPT.getSource())));
        // } catch (IllegalArgumentException ex) {
        // settings.setSource(CameraSource.PROMPT);
        // }

        return settings;
    }

    private void sendEvent(String action, Tag tag) {
        PluginCall call = getSavedCall();

        JSObject data = new JSObject();
        data.put("type", action);
        data.put("tagId", tag.getId());

        if (call != null) {
            notifyListeners("tagDiscovered", data);
        }
    }

    private void startNfc() {
        Log.d(TAG, "Starting NFC");
        createPendingIntent();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

                if (nfcAdapter != null && !getActivity().isFinishing()) {
                    try {
                        IntentFilter[] intentFilters = getIntentFilters();
                        String[][] techLists = getTechLists();

                        if (intentFilters.length > 0 || techLists.length > 0) {
                            nfcAdapter.enableForegroundDispatch(getActivity(), getPendingIntent(), intentFilters,
                                    techLists);
                        }

                    } catch (IllegalStateException e) {
                        // issue 110 - user exits app with home button while nfc is initializing
                        Log.w(TAG, e);
                    }

                }
            }
        });
    }

    private void stopNfc() {
        Log.d(TAG, "Stopping NFC");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

                if (nfcAdapter != null) {
                    try {
                        nfcAdapter.disableForegroundDispatch(getActivity());
                    } catch (IllegalStateException e) {
                        Log.w(TAG, e);
                    }
                }
            }
        });

    }

    private void restartNfc() {
        this.stopNfc();
        this.startNfc();
    }

    private Intent getIntent() {
        return getActivity().getIntent();
    }

    private PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    private void setIntent(Intent intent) {
        getActivity().setIntent(intent);
    }

    // <editor-fold desc="NFC Tech">

    private IntentFilter[] getIntentFilters() {
        return intentFilters.toArray(new IntentFilter[intentFilters.size()]);
    }

    private String[][] getTechLists() {
        return techLists.toArray(new String[0][0]);
    }

    private void removeIntentFilter(String mimeType) {
        Iterator<IntentFilter> iterator = intentFilters.iterator();
        while (iterator.hasNext()) {
            IntentFilter intentFilter = iterator.next();
            String mt = intentFilter.getDataType(0);
            if (mimeType.equals(mt)) {
                iterator.remove();
            }
        }
    }

    private void removeTagFilter() {
        Iterator<IntentFilter> iterator = intentFilters.iterator();
        while (iterator.hasNext()) {
            IntentFilter intentFilter = iterator.next();
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intentFilter.getAction(0))) {
                iterator.remove();
            }
        }
    }

    private void removeTechFilter() {
        Iterator<IntentFilter> iterator = intentFilters.iterator();
        while (iterator.hasNext()) {
            IntentFilter intentFilter = iterator.next();
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intentFilter.getAction(0))) {
                iterator.remove();
            }
        }
    }

    private void removeFromTechList(String[] techs) {
        Iterator<String[]> iterator = techLists.iterator();
        while (iterator.hasNext()) {
            String[] list = iterator.next();
            if (Arrays.equals(list, techs)) {
                iterator.remove();
            }
        }
    }

    // </editor-fold>

}
