import { WebPlugin } from '@capacitor/core';
import { NFCPlugin, NfcStatus, NfcOptions } from './definitions';
// import { uuid4 } from './utils';

// declare var navigator: any;

export class NFCPluginWeb extends WebPlugin implements NFCPlugin {

    constructor() {
        super({
            name: 'NFC',
            platforms: ['web']
        });
    }

    startScan(options: NfcOptions): Promise<void> {
        console.log('NFC options', options);
        return Promise.resolve();
    }

    stopScan(): Promise<void> {
        return Promise.resolve();
    }

    getStatus(): Promise<NfcStatus | any> {
        return Promise.resolve('none');
    }

    // getTagInfo(): Promise<NfcTag> {
    //     return Promise.resolve({
    //         tagId: this.getTagIt(),
    //         manufacturer: navigator.vendor,
    //         techList: ['web'],
    //         type: 'TAG_DISCOVERED'
    //     });
    // }

    showSettings(): Promise<void> {
        return Promise.resolve();
    }

    // private getTagId() {
    //     let uid = window.localStorage.getItem('_capuid');
    //     if (uid) {
    //         return uid;
    //     }

    //     uid = uuid4();
    //     window.localStorage.setItem('_capuid', uid);
    //     return uid;
    // }
}

const NFC = new NFCPluginWeb();

export { NFC };


// export class NFCWeb extends WebPlugin implements  {

//   async echo(options: { value: string }): Promise<{value: string}> {
//     console.log('ECHO', options);
//     return Promise.resolve({ value: options.value });
//   }
// }

