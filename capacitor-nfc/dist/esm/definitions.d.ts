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
     * Simply returns a value that it was given.
     * Learning how to create a Capacitor plugin.
     */
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
    /**
     * Query the current NFC status.
     */
    getStatus(): Promise<NfcStatus>;
    /**
     * Return information about the underlying tag technology.
     */
    getTagInfo(): Promise<TagInfo>;
    /**
     * Opens the device’s NFC settings.
     */
    showSettings(): Promise<void>;
}
export interface NfcStatus {
    status: 'ok' | 'disabled' | 'none';
}
export interface TagInfo {
    /**
    * The UUID of the attached NFC tag.
    */
    id: string;
    /**
    * The manufacturer of the NFC tag.
    */
    manufacturer?: string;
}
/**
 *
 Pending items:

    Instance Members
    beginSession(onSuccess, onFailure)
    Starts the NFCNDEFReaderSession allowing iOS to scan NFC tags.

    Param	Type	Details
    onSuccess
    onFailure
    Returns: Observable<any>

    addNdefListener(onSuccess, onFailure)
    Registers an event listener for any NDEF tag.

    Param	Type	Details
    onSuccess
    onFailure
    Returns: Observable<any>

    addTagDiscoveredListener(onSuccess, onFailure)
    Registers an event listener for tags matching any tag type.

    Param	Type	Details
    onSuccess
    onFailure
    Returns: Observable<any>

    addMimeTypeListener(mimeType, onSuccess, onFailure)
    Registers an event listener for NDEF tags matching a specified MIME type.

    Param	Type	Details
    mimeType
    onSuccess
    onFailure
    Returns: Observable<any>

    addNdefFormatableListener(onSuccess, onFailure)
    Registers an event listener for formatable NDEF tags.

    Param	Type	Details
    onSuccess
    onFailure
    Returns: Observable<any>

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

    bytesToHexString(bytes)
    Convert byte array to hex string

 */
