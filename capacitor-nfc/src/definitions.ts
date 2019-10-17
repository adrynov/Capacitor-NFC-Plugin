import { NfcTag, NfcStatus, NfcSettings } from './models';

declare module "@capacitor/core" {
  interface PluginRegistry {
    NFC: NFCPlugin;
  }
}

/**
 * Allows to access and exchange the data with the Near-Field Communication devices, such as NFC tags.
 *
 * Use it to:
 * - read data from NFC tags
 *
 * TODO:
 * - write data to NFC tags
 * - send data to other NFC enabled devices
 * - receive data from NFC devices
 * - set up a watch that informs when the NFC tag matching the options has appeared in the device sensor proximity.
 * - push a message via NFC interface.
 */
export interface NFCPlugin {
  /**
   * Checks whether NFC is enabled and turned on.
   */
  getStatus(): Promise<{ status: NfcStatus }>;

  /**
   * Returns information about the touched NFC tag.
   */
  getTagInfo(): Promise<NfcTag>;

  /**
   * Set up a watch for NFC data.
   */
  startScanning(options?: NfcSettings): Promise<void>;

  /**
   * Opens a settings page to allow the user to enable NFC.
   */
  showSettings(): Promise<void>;
}
