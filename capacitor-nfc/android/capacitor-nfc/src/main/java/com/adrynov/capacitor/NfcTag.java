package com.adrynov.capacitor;

import android.nfc.Tag;

public class NfcTag {

    public enum TagType {
        DEFAULT,
        NDEF,
        NDEF_MIME,
        NDEF_FORMATABLE
    }

    /**
     * UUID of the attached NFC tag.
     */
    private String tagId;

    /**
     * Tag type.
     */
    private TagType tagType;

    /**
     * The manufacturer of the NFC tag.
     */
    private String manufacturer;

//    private String tagNumber;
//    private Tag tag;
//
//    private String[] techList;
//
//    private MifareClassic mifareClassic;
//    private MifareUltralight mifareUltralight;
//
//    private Action action;
//    private Message message;
//
//    private byte[] payload;
//
//    private Intent tagIntent;


    /**
     * List of NFC technologies that the tag supports.
     */
    private String[] techList;

    public NfcTag(Tag tag) {
        this.tagId = NfcUtils.bytesToString(tag.getId());
        this.techList = tag.getTechList();

//        for (int i = 0; i < techList.length; i++) {
//            if (techList[i].equals(MifareClassic.class.getName())) {
//                mifareClassic = MifareClassic.get(tag);
//                break;
//
//            } else if (techList[i].equals(MifareUltralight.class.getName())) {
//                mifareUltralight = MifareUltralight.get(tag);
//                break;
//            }
//        }
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public TagType getTagType() {
        return tagType;
    }

    public void setTagType(TagType tagType) {
        this.tagType = tagType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String[] getTechList() {
        return techList;
    }

    public void setTechList(String[] techList) {
        this.techList = techList;
    }

    //    public void setAction(String nfcIntent) {
//        if (nfcIntent.equals(NfcAdapter.ACTION_NDEF_DISCOVERED))
//            action = Action.NDEF_DISCOVERED;
//        else if (nfcIntent.equals(NfcAdapter.ACTION_TECH_DISCOVERED))
//            action = Action.TECH_SUPPORTED;
//        else
//            action = Action.UNSUPPORTED;
//    }


    /*public Object getCard() {
        if (isClassic())
            return mifareClassic;
        else if (isUltralight())
            return mifareUltralight;

        return null;
    }*/
//
//    public boolean isClassic() {
//        return mifareClassic != null;
//    }
//
//    public boolean isUltralight() {
//        return mifareUltralight != null;
//    }
//
//    public boolean techNotSupported() {
//        return !isClassic() && !isUltralight();
//    }
}
