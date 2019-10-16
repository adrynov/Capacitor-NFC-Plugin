Capacitor NFC Plugin
==========================

Read NFC tags.

Supported Platforms
-------------------

* Android

Sorry, I do not have nor plan to acquire an iPhone. iOS contributions are welcome.


### Supported Platforms

- Android

### Android Notes

This API requires the following permission be added to your AndroidManifest.xml:

```xml
<uses-permission android:name="android.permission.NFC" />
<uses-feature android:name="android.hardware.nfc" android:required="true" />
```

## Example


```js
import { Plugins } from '@capacitor/core';
const { NFC } = Plugins;

class NFCExample {
  async enableNfc() {
    // TODO
  }
}
```

### API

#### showSettings

Show the NFC settings on the device.

    NFC.showSettings();
