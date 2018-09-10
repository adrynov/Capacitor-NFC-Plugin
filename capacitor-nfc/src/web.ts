import { WebPlugin } from '@capacitor/core';
import { NFCPluginPlugin } from './definitions';

export class NFCPluginWeb extends WebPlugin implements NFCPluginPlugin {
  constructor() {
    super({
      name: 'NFCPlugin',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{value: string}> {
    console.log('ECHO', options);
    return Promise.resolve({ value: options.value });
  }
}

const NFCPlugin = new NFCPluginWeb();

export { NFCPlugin };
