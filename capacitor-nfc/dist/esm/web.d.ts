import { WebPlugin } from '@capacitor/core';
import { NFCPluginPlugin } from './definitions';
export declare class NFCPluginWeb extends WebPlugin implements NFCPluginPlugin {
    constructor();
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
}
declare const NFCPlugin: NFCPluginWeb;
export { NFCPlugin };
