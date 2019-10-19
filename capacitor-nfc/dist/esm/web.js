import { WebPlugin } from '@capacitor/core';
export class NFCPluginWeb extends WebPlugin {
    constructor() {
        super({
            name: 'NFC',
            platforms: ['web']
        });
    }
    getTagInfo() {
        const tag = { tagId: '043A98CAB32B80', type: 'default' };
        return Promise.resolve(tag);
    }
    getStatus() {
        console.debug('NFC is not supported in the browser');
        return Promise.resolve({ status: 'none' });
    }
    startScanning(options) {
        console.log('Options', options);
        return Promise.resolve();
    }
    showSettings() {
        return Promise.reject('NFC not supported in the browser');
    }
}
const NFC = new NFCPluginWeb();
export { NFC };
import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(NFC);
//# sourceMappingURL=web.js.map