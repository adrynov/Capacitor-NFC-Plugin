import { WebPlugin } from '@capacitor/core';
import { NFCPlugin, NfcStatus, NfcOptions } from './definitions';
export declare class NFCPluginWeb extends WebPlugin implements NFCPlugin {
    constructor();
    startScan(options: NfcOptions): Promise<void>;
    stopScan(): Promise<void>;
    getStatus(): Promise<NfcStatus | any>;
    showSettings(): Promise<void>;
}
declare const NFC: NFCPluginWeb;
export { NFC };
