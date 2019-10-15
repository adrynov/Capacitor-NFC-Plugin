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
    * The UUID of the attached NFC tag.
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

