declare global {
  interface PluginRegistry {
    NFCPlugin?: NFCPluginPlugin;
  }
}

export interface NFCPluginPlugin {
  echo(options: { value: string }): Promise<{value: string}>;
}
