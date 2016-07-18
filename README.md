# react-native-Intent
React Native Android module to use Android's Intent actions.

## Installation

```bash
npm install react-native-intent --save
```
### Add it to your android project

#### Automatically with rnpm
* `rnpm link`

#### Manually
* In `android/setting.gradle`

```gradle
...
include ':IntentModule', ':app'
project(':IntentModule').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-intent')
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':IntentModule')
}
```

* Register Module (in MainActivity.java)

```java
import com.tal952.intent.IntentPackage;  // <--- import

public class MainActivity extends ReactActivity {
  ......

  /**
     * A list of packages used by the app. If the app uses additional views
     * or modules besides the default ones, add more packages here.
     */
    @Override
    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            new IntentPackage() // <-- add this line
        );
    }
}
```

## Example / Usage of video capture
```javascript
import Intent from 'react-native-intent';

const ACTION_VIDEO_CAPTURE = "android.media.action.VIDEO_CAPTURE";
const ACTION_VIDEO_CAPTURE_REQUEST_ID = 20001;

const data = await Intent.startActivityForResult(ACTION_VIDEO_CAPTURE, ACTION_VIDEO_CAPTURE_REQUEST_ID);
```


## Example / launch another app
```javascript
import Intent from 'react-native-intent';

const pkgName = 'com.facebook.katana';
const installed = await Intent.isAppInstalled(pkgName);
if (installed) {
  Intent.startIntentForPackage(pkgName);
} else {
  const url = `market://details?id=${pkgName}`;
  Linking.canOpenURL(url).then(supported => {
    if (supported) {
      // Open in play store
      Linking.openURL(url);
    } else {
      // If play store is not installed, open it in browser
      Linking.openURL(`https://play.google.com/store/apps/details?id=${pkgName}`);
    }
  })
}

```
