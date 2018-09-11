import { WebPlugin } from '@capacitor/core';
import { NFCPlugin, NfcStatus, TagInfo } from './definitions';
import { uuid4 } from './utils';

declare var navigator: any;

export class NFCPluginWeb extends WebPlugin implements NFCPlugin {

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

    getTagInfo(): Promise<TagInfo> {
        return Promise.resolve({
            id: this.getUid(),
            manufacturer: navigator.vendor
        });
    }

    showSettings(): Promise<void> {
        return Promise.resolve();
    }

    private getUid() {
        let uid = window.localStorage.getItem('_capuid');
        if (uid) {
            return uid;
        }

        uid = uuid4();
        window.localStorage.setItem('_capuid', uid);
        return uid;
    }
}

const NFC = new NFCPluginWeb();

export { NFC };


// export class NFCWeb extends WebPlugin implements  {

//   async echo(options: { value: string }): Promise<{value: string}> {
//     console.log('ECHO', options);
//     return Promise.resolve({ value: options.value });
//   }
// }

