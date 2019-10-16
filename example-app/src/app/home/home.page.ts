import { Component } from '@angular/core';

import { Plugins, Capacitor } from '@capacitor/core';
const { NFC } = Plugins;

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {

  constructor() {
    if (Capacitor.isPluginAvailable('NFC')) {
      NFC.echo({ value: 'Hello World' });

      console.log('NFC is enabled', NFC.isNfcEnabled());
    }
  }


}
