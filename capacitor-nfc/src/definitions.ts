declare module "@capacitor/core" {
  interface PluginRegistry {
    NFC: NFCPlugin;
  }
}

/**
 * The NFC plugin allows you to read NFC tags.
 *
 * Use it to:
 * - read data from NFC tags
 *
 * TODO:
 * - write data to NFC tags
 * - send data to other NFC enabled devices
 * - receive data from NFC devices
 */
export interface NFCPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
