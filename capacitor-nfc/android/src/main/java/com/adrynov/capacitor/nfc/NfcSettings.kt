package com.adrynov.capacitor.nfc

class NfcSettings {

    /**
     * Scan tags that contain NDEF data that cannot be mapped to a MIME type or URI,
     * or if the tag does not contain NDEF data but is of a known tag technology
     */
    private var enableTech: Boolean = false

    /**
     * Scan tags that contain an NDEF payload.
     */
    private var enableNdef: Boolean = false

    /**
     * Scan NDEF-formattable tags.
     */
    private var enableNdefFormattable: Boolean = false

    fun techEnabled(): Boolean {
        return enableTech
    }

    fun setTechEnabled(enableTech: Boolean) {
        this.enableTech = enableTech
    }

    fun ndefEnabled(): Boolean {
        return enableNdef
    }

    fun setNdefEnabled(enableNdef: Boolean) {
        this.enableNdef = enableNdef
    }

    fun ndefFormattableEnabled(): Boolean {
        return enableNdefFormattable
    }

    fun setNdefFormattableEnabled(enableNdefFormattable: Boolean) {
        this.enableNdefFormattable = enableNdefFormattable
    }
}
