declare global {
  interface PluginRegistry {
    NFCPlugin?: NFCPlugin;
  }
}

export interface NFCPlugin {
  // echo(options: { value: string }): Promise<{ value: string }>;
  checkStatus(): Promise<{ value: string }>;
}
