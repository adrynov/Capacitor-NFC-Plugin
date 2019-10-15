
  Pod::Spec.new do |s|
    s.name = 'CapacitorNfc'
    s.version = '0.1.0'
    s.summary = 'Read NFC tags'
    s.license = 'MIT'
    s.homepage = 'https://github.com/adrynov/Capacitor-NFC-Plugin'
    s.author = 'Andrei Drynov'
    s.source = { :git => 'https://github.com/adrynov/Capacitor-NFC-Plugin', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
  end
