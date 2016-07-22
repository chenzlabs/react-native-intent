package com.tal952.intent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.SparseArray;

// import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

public class IntentModule extends ReactContextBaseJavaModule {

    final ReactApplicationContext reactContext;
    final SparseArray<Promise> promises = new SparseArray<>();

    public IntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        // this.reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "Intent";
    }

    // @ReactMethod
    // public void startActivityForResult(final String action, final int requestCode, final Promise promise) {
    //     Intent intent = new Intent(action);
    //     if (intent.resolveActivity(this.reactContext.getPackageManager()) != null) {
    //         this.reactContext.startActivityForResult(intent, requestCode, null);
    //     }
    //     promises.put(requestCode, promise);
    // }

    @ReactMethod
    public void isAppInstalled(final String uri, final Promise promise) {
        final Activity activity = getCurrentActivity();
        final PackageManager pm = activity.getPackageManager();
        WritableMap map = Arguments.createMap();

        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            map.putBoolean("installed", true);
            promise.resolve(map);
        } catch (PackageManager.NameNotFoundException e) {
            map.putBoolean("installed", false);
            promise.resolve(map);
        }
    }

    @ReactMethod
    public void startIntentForPackage(final String uri, final Promise promise) {
        final Activity activity = getCurrentActivity();
        final PackageManager pm = activity.getPackageManager();
        WritableMap map = Arguments.createMap();

        Intent i = pm.getLaunchIntentForPackage(uri);
        if (i == null) {
            map.putBoolean("launched", false);
            promise.resolve(map);
        } else {
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(i);
            map.putBoolean("launched", true);
            promise.resolve(map);
        }
    }

    // @Override
    // public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //     WritableNativeMap obj = new WritableNativeMap();
    //
    //     obj.putInt("requestCode", requestCode);
    //     obj.putInt("resultCode", resultCode);
    //     obj.putString("data", data != null ? data.getDataString() : null);
    //
    //     promises.get(requestCode).resolve(obj);
    //     promises.remove(requestCode);
    // }
}
