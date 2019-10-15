import { WebPlugin } from '@capacitor/core';
import { NFCPlugin } from './definitions';

export class NFCWeb extends WebPlugin implements NFCPlugin {
  constructor() {
    super({
      name: 'NFC',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{value: string}> {
    console.log('ECHO', options);
    return options;
  }
}

const NFC = new NFCWeb();

export { NFC };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(NFC);
