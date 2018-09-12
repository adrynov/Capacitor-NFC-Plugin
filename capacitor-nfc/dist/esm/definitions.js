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
//# sourceMappingURL=definitions.js.map