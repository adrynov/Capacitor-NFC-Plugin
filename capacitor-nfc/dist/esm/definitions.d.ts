import { PluginListenerHandle } from '@capacitor/core';
declare global {
    interface PluginRegistry {
        NFC?: NFCPlugin;
    }
}
/**
 * The NFC plugin allows you to read and write NFC tags. You can also beam to, and receive from, other NFC enabled devices.
 *
 * Use to:
 * - read data from NFC tags
 * - write data to NFC tags
 * - send data to other NFC enabled devices
 * - receive data from NFC devices
 *
 * This plugin uses NDEF (NFC Data Exchange Format) for maximum compatibilty between NFC devices, tag types, and operating systems.
 */
export interface NFCPlugin {
    /**
     * Query the current NFC status.
     */
    getStatus(): Promise<NfcStatus>;
    /**
     * Opens the device’s NFC settings.
     */
    showSettings(): Promise<void>;
    /**
     * Start scanning.
     */
    startScan(options: NfcOptions): Promise<void>;
    /**
     * Stops scanning.
     */
    stopScan(): Promise<void>;
    /**
    * Listen for TAG_DISCOVERED events.
    */
    addListener(eventName: 'tagDiscovered', listenerFunc: (tag: NfcTag) => void): PluginListenerHandle;
}
export interface NfcOptions {
    /**
     * Scan tags that contain NDEF data that cannot be mapped to a MIME type or URI,
     * or if the tag does not contain NDEF data but is of a known tag technology
     */
    enableTech?: boolean;
    /**
     * Scan tags that contain an NDEF payload.
     */
    enableNdef?: boolean;
    /**
     * Scan NDEF-formattable tags.
     */
    enableNdefFormattable?: boolean;
}
export interface NfcStatus {
    status: 'ok' | 'disabled' | 'none';
}
export interface NfcTag {
    /**
    * UUID of the attached NFC tag.
    */
    tagId: string;
    /**
     * Tag type: TAG_DEFAULT, NDEF, NDEF_MIME, NDEF_FORMATABLE.
     */
    type: string;
    /**
    * The manufacturer of the NFC tag.
    */
    manufacturer?: string;
    /**
     * List of NFC technologies that the tag supports.
     */
    techList?: string[];
}
/**
 *
    beginSession(onSuccess, onFailure)
    Starts the NFCNDEFReaderSession allowing iOS to scan NFC tags.

    Param	Type	Details
    onSuccess
    onFailure
    Returns: Observable<any>

    addMimeTypeListener(mimeType, onSuccess, onFailure)
    Registers an event listener for NDEF tags matching a specified MIME type.

    write(message)
    Writes an NdefMessage(array of ndef records) to a NFC tag.

    Param	Type	Details
    message	any[]
    Returns: Promise<any>

    makeReadyOnly()
    Makes a NFC tag read only. Warning this is permanent.

    Returns: Promise<any>

    share(message)
    Shares an NDEF Message(array of ndef records) via peer-to-peer.

    Param	Type	Details
    message
    An array of NDEF Records.

    Returns: Promise<any>

    unshare()
    Stop sharing NDEF data via peer-to-peer.

    Returns: Promise<any>

    erase()
    Erase a NDEF tag

    handover(uris)
    Send a file to another device via NFC handover.

    Param	Type	Details
    uris
    A URI as a String, or an array of URIs.

    Returns: Promise<any>

    stopHandover()
    Stop sharing NDEF data via NFC handover.

    Returns: Promise<any>


    enabled()
    Check if NFC is available and enabled on this device.

    Returns: Promise<any>

    bytesToString(bytes)
    Param	Type	Details
    bytes	number[]
    Returns: string

    stringToBytes(str)
    Convert string to byte array.

    Param	Type	Details
    str	string
    Returns: number[]

    (bytes)


 */
