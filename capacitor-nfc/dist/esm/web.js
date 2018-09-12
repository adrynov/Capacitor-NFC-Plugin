import { WebPlugin } from '@capacitor/core';
// import { uuid4 } from './utils';
// declare var navigator: any;
export class NFCPluginWeb extends WebPlugin {
    constructor() {
        super({
            name: 'NFC',
            platforms: ['web']
        });
    }
    startScan(options) {
        console.log('NFC options', options);
        return Promise.resolve();
    }
    stopScan() {
        return Promise.resolve();
    }
    getStatus() {
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
    showSettings() {
        return Promise.resolve();
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
//# sourceMappingURL=web.js.map