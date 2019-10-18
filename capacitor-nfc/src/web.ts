import { WebPlugin } from '@capacitor/core';
import { NFCPlugin } from './definitions';
import { NfcTag, NfcStatus, NfcSettings } from './models';

export class NFCPluginWeb extends WebPlugin implements NFCPlugin {

  constructor() {
    super({
      name: 'NFC',
      platforms: ['web']
    });
  }

  getTagInfo(): Promise<NfcTag> {
    const tag: NfcTag = { tagId: '043A98CAB32B80', type: 'default' };
    return Promise.resolve(tag);
  }

  getStatus(): Promise<{ status: NfcStatus; }> {
    console.debug('NFC is not supported in the browser');
    return Promise.resolve({ status: 'none' });
  }

  startScanning(options?: NfcSettings): Promise<void> {
    console.log('Options', options);
    return Promise.resolve();
  }

  showSettings(): Promise<void> {
    return Promise.reject('NFC not supported in the browser');
  }

}

const NFC = new NFCPluginWeb();

export { NFC };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(NFC);
