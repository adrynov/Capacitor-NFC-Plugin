import { WebPlugin } from '@capacitor/core';
import { NFCPlugin } from './definitions';

// declare var navigator: any;

export class NFCPluginWeb extends WebPlugin implements NFCPlugin {

  constructor() {
    super({
      name: 'NFC',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{ value: string }> {
    return new Promise((resolve) => {
      console.log('Hello World from NFC plugin', options);
      resolve(options);
    })
  }

  isNfcAvailable(): Promise<{ enabled: boolean; }> {
    debugger;
    return Promise.resolve({ enabled: true });
  }

  // showSettings(): Promise<void> {
  //   return Promise.resolve();
  // }

}

const NFC = new NFCPluginWeb();

export { NFC };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(NFC);
