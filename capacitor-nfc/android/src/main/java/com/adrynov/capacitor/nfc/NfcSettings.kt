package com.adrynov.capacitor.nfc

class NfcSettings {

    /**
     * Allows to scan tags that contain NDEF payload.
     * NDEF is a NFC Forum standard (NFC Data Exchange Format)
     */
    var ndefEnabled: Boolean = false

    /**
     * Allows to scan NDEF-formattable tags.
     */
    var ndefFormattable: Boolean = false

    /**
     * Allows to scan tags that contain NDEF data that cannot be mapped to a MIME type or URI,
     * or when the tag does not contain NDEF data but is of a known tag technology e.g. Mifare Classic
     */
    var techEnabled: Boolean = false
}
