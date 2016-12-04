# shk-android

This is an Android part for "shk" project.

In this repo We are working in Android application, this App should do:
- User login
- NFC tag writing
- See user connections, photos...

# How to build an execute
Execute and build this app is too easy, simply:

1. Download Android Studio (https://developer.android.com/studio/index.html)
2. Download this project, unzip it and put in your favourite folder
3. Open and configure AS
4. Import project
5. Simply click on "play" button for execute, or "Hammer" for build

# How to configure Firebase

Configure firebase is easy:

1. Go to firebase.google.com and do login
2. Create new application
3. At this moment, you should access to all firebase features, congrats! :)
4. Create an Android app, this step is really important!!
  Please, be sure You insert correct the Android App Id and SHA1 key (for generate key  http://stackoverflow.com/questions/27609442/how-to-get-the-sha-1-fingerprint-certificate-in-android-studio-for-debug-mode)
5. Firebase will give you an google-services.json, please download it and put into /data folder
6. Go to authentication and enable Google Auth
7. That's all!! :) Congrats, right now you should use this app

Extra: Firebase permits You to make custom auth, database rules. Explore them!

Disclaimer: We recommended to change app id BEFORE configure firebase

# License

![his work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License](https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png)

Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License


Feel free to improve this repo!
