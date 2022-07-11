# opbeans-android

An opbeans demo app for the Android Agent. This project is intended to be used in conjunction
with [apm-integration-testing](https://github.com/elastic/apm-integration-testing) and the other
opbeans applications.

This app points to your localhost endpoint by default, which you can change in the
file `app/build.gradle`
by setting a new value to the `BASE_URL` buildConfigField defined within the `android.defaultConfig`
block. The endpoint you'll find there by default is `"http://10.0.2.2:3000"`, where `10.0.2.2` is an
alias that Android emulators define for their host machines (e.g. it's your dev machine's localhost)
. More info [here](https://developer.android.com/studio/run/emulator-networking#networkaddresses).