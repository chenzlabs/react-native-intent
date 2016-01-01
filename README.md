# react-native-Intent
React Native Android module to use Android's Intent actions.

## Installation

```bash
npm install react-native-intent --save
```
### Add it to your android project

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

public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {

  private ReactInstanceManager mReactInstanceManager;
  ......

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mReactRootView = new ReactRootView(this);

    mReactInstanceManager = ReactInstanceManager.builder()
      .setApplication(getApplication())
      .setBundleAssetName("index.android.bundle")
      .setJSMainModuleName("index.android")
      .addPackage(new MainReactPackage())
      .addPackage(new IntentPackage()) // <------ add this line to your MainActivity class
      .setUseDeveloperSupport(BuildConfig.DEBUG)
      .setInitialLifecycleState(LifecycleState.RESUMED)
      .build();

    mReactRootView.startReactApplication(mReactInstanceManager, "Sample", null);

    setContentView(mReactRootView);
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
var Intent = require('react-native-intent');

var ACTION_VIDEO_CAPTURE = "android.media.action.VIDEO_CAPTURE";
var ACTION_VIDEO_CAPTURE_REQUEST_ID = 20001;

var data = await Intent.startActivityForResult(ACTION_VIDEO_CAPTURE, ACTION_VIDEO_CAPTURE_REQUEST_ID);
```
