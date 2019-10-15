import { WebPlugin } from '@capacitor/core';
import { NFCPlugin } from './definitions';
export declare class NFCPluginWeb extends WebPlugin implements NFCPlugin {
    constructor();
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
    isNfcAvailable(): Promise<{
        enabled: boolean;
    }>;
}
declare const NFC: NFCPluginWeb;
export { NFC };
