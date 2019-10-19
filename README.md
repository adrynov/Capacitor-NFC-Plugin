Capacitor NFC Plugin
==========================

Native [Capacitor](https://capacitor.ionicframework.com/) plugin to read NFC tags.

To install the plugin in your project, run:

`npm install --save capacitor-nfc@latest`

## Supported Platforms

* Android
* <del>iOS</del> (Sorry, I do not have nor plan to acquire an iPhone. iOS contributions are welcome.)


### Android Notes

This API requires the following permission be added to your AndroidManifest.xml:

```xml
<uses-permission android:name="android.permission.NFC" />
<uses-feature android:name="android.hardware.nfc" android:required="true" />
```

### iOS Notes

Specify the reason for your app to use the device’s NFC reader in Info.plist:

Name: Privacy - NFC Scan Usage Description
Key: NFCReaderUsageDescription

## Example

```js
import { Plugins } from '@capacitor/core';
const { NFC } = Plugins;

class NFCExample {

  async checkNfcStatus() {
     if (Capacitor.isPluginAvailable('NFC')) {
      const status = NFC.getStatus();
      console.log('NFC is enabled', status);

      if (status !== 'enabled') {
        NFC.showSettings();
      }
    }
  }

}
```

## API

**getStatus**

Checks whether NFC is enabled and turned on.

**getTagInfo**

(TODO) Returns information about the touched NFC tag.

**showSettings**

Opens a settings page to allow the user to enable NFC.

```js
NFC.showSettings();
```
