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
      const status = NFC.getStatus();
      console.log('NFC is enabled', status);

      if (status !== 'enabled') {
        NFC.showSettings();
      }
    }
  }


}
