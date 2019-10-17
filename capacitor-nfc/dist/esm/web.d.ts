import { WebPlugin } from '@capacitor/core';
import { NFCPlugin } from './definitions';
import { NfcTag, NfcStatus, NfcSettings } from './models';
export declare class NFCPluginWeb extends WebPlugin implements NFCPlugin {
    constructor();
    getTagInfo(): Promise<NfcTag>;
    getStatus(): Promise<{
        status: NfcStatus;
    }>;
    startScanning(options?: NfcSettings): Promise<void>;
    showSettings(): Promise<void>;
}
declare const NFC: NFCPluginWeb;
export { NFC };
