/**
 *
import { NFC } from 'capacitor-nfc';

NFC.addNdefListener(() => {
  console.log('successfully attached ndef listener');
}, (err) => {
  console.log('error attaching ndef listener', err);
}).subscribe((event) => {
  console.log('received ndef message. the tag contains: ', event.tag);
  console.log('decoded tag id', this.nfc.bytesToHexString(event.tag.id));

  let message = this.ndef.textRecord('Hello world');
  this.nfc.share([message]).then(onSuccess).catch(onError);
});


Instance Members
beginSession(onSuccess, onFailure)
Starts the NFCNDEFReaderSession allowing iOS to scan NFC tags.

Param	Type	Details
onSuccess
onFailure
Returns: Observable<any>

addNdefListener(onSuccess, onFailure)
Registers an event listener for any NDEF tag.

Param	Type	Details
onSuccess
onFailure
Returns: Observable<any>

addTagDiscoveredListener(onSuccess, onFailure)
Registers an event listener for tags matching any tag type.

Param	Type	Details
onSuccess
onFailure
Returns: Observable<any>

addMimeTypeListener(mimeType, onSuccess, onFailure)
Registers an event listener for NDEF tags matching a specified MIME type.

Param	Type	Details
mimeType
onSuccess
onFailure
Returns: Observable<any>

addNdefFormatableListener(onSuccess, onFailure)
Registers an event listener for formatable NDEF tags.

Param	Type	Details
onSuccess
onFailure
Returns: Observable<any>

write(message)
Writes an NdefMessage(array of ndef records) to a NFC tag.

Param	Type	Details
message	any[]
Returns: Promise<any>

makeReadyOnly()
Makes a NFC tag read only. Warning this is permanent.

Returns: Promise<any>

share(message)
Shares an NDEF Message(array of ndef records) via peer-to-peer.

Param	Type	Details
message
An array of NDEF Records.

Returns: Promise<any>

unshare()
Stop sharing NDEF data via peer-to-peer.

Returns: Promise<any>

erase()
Erase a NDEF tag

handover(uris)
Send a file to another device via NFC handover.

Param	Type	Details
uris
A URI as a String, or an array of URIs.

Returns: Promise<any>

stopHandover()
Stop sharing NDEF data via NFC handover.

Returns: Promise<any>


enabled()
Check if NFC is available and enabled on this device.

Returns: Promise<any>

bytesToString(bytes)
Param	Type	Details
bytes	number[]
Returns: string

stringToBytes(str)
Convert string to byte array.

Param	Type	Details
str	string
Returns: number[]

bytesToHexString(bytes)
Convert byte array to hex string

Param	Type	Details
bytes	number[]
Retu
 */
//# sourceMappingURL=definitions.js.map