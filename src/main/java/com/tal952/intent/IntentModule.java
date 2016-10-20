package com.tal952.intent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

// import com.facebook.react.bridge.ActivityEventListener;

public class IntentModule extends ReactContextBaseJavaModule {

    public static final String TAG = "IntentModule";

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

    @ReactMethod
    public void getLaunchIntentPath(Promise promise) {
        Log.d(TAG, "getLaunchIntentPath: moi moi :)");
        final Activity activity = getCurrentActivity();
        Intent intent;
        try {
            intent = activity.getIntent();
        } catch (NullPointerException e) {
            Log.e(TAG, "Activity didn't have an intent", e);
            promise.reject(e);
            return;
        }

        Uri uri = intent.getData();
        if (uri != null) {
            Log.d(TAG, "getLaunchIntentPath: " + uri.getPath());
            promise.resolve(uri.getPath());
        } else {
            Log.d(TAG, "getLaunchIntentPath: Uri was null");
        }
    }

    @ReactMethod
    public void getLaunchIntentArguments(Promise promise) {
        Log.d(TAG, "getLaunchIntentArguments: moi moi :)");
        final Activity activity = getCurrentActivity();
        Intent intent;
        try {
            intent = activity.getIntent();
        } catch (NullPointerException e) {
            Log.e(TAG, "Activity didn't have an intent", e);
            promise.reject(e);
            return;
        }

        // Map<String, String> params = new ArrayMap<>();

        Uri uri = intent.getData();
        if (uri != null) {
            Log.d(TAG, "getLaunchIntentArguments: " + uri.getEncodedQuery());
            promise.resolve(uri.getEncodedQuery());
        } else {
            Log.d(TAG, "getLaunchIntentArguments: Uri was null");
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
