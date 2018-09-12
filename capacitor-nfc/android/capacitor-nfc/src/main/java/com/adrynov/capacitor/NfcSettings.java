package com.adrynov.capacitor;

public class NfcSettings {

    /**
     * Scan tags that contain NDEF data that cannot be mapped to a MIME type or URI,
     * or if the tag does not contain NDEF data but is of a known tag technology
     */
    private boolean enableTech;

    /**
     * Scan tags that contain an NDEF payload.
     */
    private boolean enableNdef;

    /**
     * Scan NDEF-formattable tags.
     */
    private boolean enableNdefFormattable;

    public boolean techEnabled() {
        return enableTech;
    }

    public void setTechEnabled(boolean enableTech) {
        this.enableTech = enableTech;
    }

    public boolean ndefEnabled() {
        return enableNdef;
    }

    public void setNdefEnabled(boolean enableNdef) {
        this.enableNdef = enableNdef;
    }

    public boolean ndefFormattableEnabled() {
        return enableNdefFormattable;
    }

    public void setNdefFormattableEnabled(boolean enableNdefFormattable) {
        this.enableNdefFormattable = enableNdefFormattable;
    }
}
