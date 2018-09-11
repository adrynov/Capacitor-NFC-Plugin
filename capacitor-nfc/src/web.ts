import { WebPlugin } from '@capacitor/core';
import { NfcStatus } from './definitions';

export class NFCPluginWeb extends WebPlugin {

    constructor() {
        super({
            name: 'NFC',
            platforms: ['web']
        });
    }

    async echo(options: { value: string }): Promise<{ value: string }> {
        console.log('ECHO', options);
        return Promise.resolve({ value: options.value });
    }

    getStatus(): Promise<NfcStatus | any> {
        return Promise.resolve('none');
    }

    showSettings(): Promise<void> {
        return Promise.resolve();
    }
}

// Instantiate the plugin
const NFC = new NFCPluginWeb();

// Export the plugin
export { NFC };
