MOTS mobile app
----------------------
![Build status](https://www.bitrise.io/app/7e00617b3eb26295.svg?token=VCMRS804K0dwuyMpH_SFNg&branch=develop)

Installing
----------------------

### Requirements:
- JDK 8
- Android SDK
- React Native
- Watchman

### Installing Android Studio
- Download AS from https://developer.android.com/studio/index.html
- During installation, make sure the boxes next to all of the following are checked:
    - Android SDK
    - Android SDK Platform
    - Android Virtual Device

### Installing Android SDK:
- Android Studio should install the latest Android SDK by default.
- The SDK Manager can be accessed from the "Welcome to Android Studio" screen. Click on "Configure", then select "SDK Manager".
- If the build tools are missing, add them with the following command:
` android update sdk -u -a -t build-tools-23.0.3`

### Configuring SDK:
- Android 6.0 (API 23)
- Extras
    - Android Support Repository
    - Android Support Library
    - Google Play services
    - Google Repository
    - Intel x86 Emulator Accelerator

### Configure the ANDROID_HOME environment variable:
> export ANDROID_HOME=:/path/to/android-sdk

> export PATH=$PATH:$ANDROID_HOME/tools

> export PATH=$PATH:$ANDROID_HOME/platform-tools

### Working directory:
> mots/mobile/

### Updating npm & node:
```sh
sudo npm install -g npm
sudo npm install
```

### Installing React Native:
```sh
sudo npm install -g react-native-cli
```

### Installing Watchman:
```sh
git clone https://github.com/facebook/watchman.git
cd watchman
git checkout v4.5.0  # the latest stable release
./autogen.sh
./configure
make
sudo make install
sudo sysctl -w fs.inotify.max_user_watches=524288
sudo sysctl -w fs.inotify.max_user_instances=1024
```

### Creating android virtual machine:
- Android Studio > Tools > Android > AVD Manager
- click ```+ Create Virtual Device...``` button
- set desired AVD configuration

### Adding config
In `mobile` directory, copy the example config:
```sh
cp config.example.js config.js
```

### Starting up application:
- Start Android Virtual Machine from AVD Manager
```sh
react-native start --reset-cache
react-native run-android
```

### In case of any troubles with some installation steps:
Go to: https://facebook.github.io/react-native/docs/getting-started.html and select:
- Building Projects with Native Code
- Development OS (Linux)
- Target OS (Android)
