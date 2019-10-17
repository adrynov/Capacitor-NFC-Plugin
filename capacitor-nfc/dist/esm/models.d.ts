export interface NfcSettings {
    /**
     * Scan tags that contain NDEF data that cannot be mapped to a MIME type or URI,
     * or if the tag does not contain NDEF data but is of a known tag technology
     */
    techEnabled?: boolean;
    /**
     * Allows to scan tags that contain NDEF payload.
     */
    ndefEnabled?: boolean;
    /**
     * Allows to scan NDEF-formattable tags.
     */
    ndefFormattable?: boolean;
}
export declare type NfcStatus = 'enabled' | 'disabled' | 'none';
export interface NfcTag {
    /**
    * NFC chips have a manufacturer supplied unique identifier that can be read by most NFC devices.
    * An NFC tag’s UID can not be changed or erased.
    * Usually this UID is 7 bytes.
    */
    tagId: string;
    /**
     * NFC tag type formats are based on ISO 14443 Types A and B and Sony FeliCa which conforms to ISO 18092.0
     *
     * https://www.tagnfc.com/en/info/11-nfc-tags-specs
     *
     * The Tag 1 Type is based on the ISO14443A standard. These NFC tags are read and re-write capable and
     * users can configure the tag to become read-only. Memory availability is 96 bytes which is more than sufficient
     * to store a website URL or other small amount of data. However the memory size is expandable up to 2 kbyte.
     * The communication speed of this NFC tag is 106 kbit/s. As a result of its simplicity this tag type is cost
     * effective and ideal for many NFC applications.
     *
     * Example: Innovision Topaz
     *
     * The NFC Tag 2 Type is also based on ISO14443A. These NFC tags are read and re-write capable and users can
     * configure the tag to become read-only. The basic memory size of this tag type is only 48 bytes although
     * this can be expanded to 2 kbyte. Again the communication speed is 106 kbit/s.
     *
     * Example: NXP Mifare Ultralight, NXP Mifare Ultralight
     *
     * The NFC Tag 3 Type is based on the Sony FeliCa system. It currently has a 2 kbyte memory capacity and
     * the data communications speed is 212 kbit/s. Accordingly this NFC tag type is more applicable for more complex
     * applications, although there is a higher cost per tag.
     *
     *  Example: Sony Felica
     *
     * The NFC Tag 4 Type is defined to be compatible with ISO14443A and B standards. These NFC tags are pre-configured
     * at manufacture and they can be either read / re-writable, or read-only. The memory capacity can be up to
     * 32 kbytes and the communication speed is between 106 kbit/s and 424 kbit/s.
     *
     *  Example: NXP DESfire, NXP SmartMX with JCOP
     *
     * The Mifare Classic are not included in the NFC standard.
     */
    type: 'default' | 'ntag' | 'type1' | 'type2' | 'type3' | 'type4';
    /**
    * The manufacturer of the NFC tag.
    */
    manufacturer?: string;
    /**
     * List of NFC technologies that the tag supports.
     */
    techList?: string[];
}
