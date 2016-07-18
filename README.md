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

  ......
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) { // <------ add this method to your MainActivity class
    super.onActivityResult(requestCode, resultCode, data);

    this.mReactInstanceManager.onActivityResult(requestCode, resultCode, data);
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

const package = 'com.facebook.katana';

if (Intent.isAppInstalled(package)) {
  Intent.startIntentForPackage(package);
} else {
  const url = `market://details?id=${package}`;
  Linking.canOpenURL(url).then(supported => {
    if (supported) {
      Linking.openURL(url);
    } else {
      Linking.openURL(`https://play.google.com/store/apps/details?id=${package}`);
    }
  })
}

```


### TODO
* Promises ?
* Check if the activity result thing actually works
