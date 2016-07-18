package com.tal952.intent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.SparseArray;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeMap;

public class IntentModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    final ReactApplicationContext reactContext;
    final SparseArray<Promise> promises = new SparseArray<>();

    public IntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "Intent";
    }

    @ReactMethod
    public void startActivityForResult(final String action, final int requestCode, final Promise promise) {
        Intent intent = new Intent(action);
        if (intent.resolveActivity(this.reactContext.getPackageManager()) != null) {
            this.reactContext.startActivityForResult(intent, requestCode, null);
        }
        promises.put(requestCode, promise);
    }

    @ReactMethod
    public boolean isAppInstalled(final String uri) {
        final Activity activity = getCurrentActivity();
        final PackageManager pm = activity.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }

        return installed;
    }

    @ReactMethod
    public void startIntentForPackage(final String uri) {
        final Activity activity = getCurrentActivity();
        final PackageManager pm = activity.getPackageManager();
        Intent i = pm.getLaunchIntentForPackage(uri);
        activity.startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        WritableNativeMap obj = new WritableNativeMap();

        obj.putInt("requestCode", requestCode);
        obj.putInt("resultCode", resultCode);
        obj.putString("data", data != null ? data.getDataString() : null);

        promises.get(requestCode).resolve(obj);
        promises.remove(requestCode);
    }
}
