package com.tal952.intent;

import android.content.Intent;
import android.util.SparseArray;
import com.facebook.react.bridge.*;

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
    public void startActivityForResult(String action, int requestCode, Promise promise) {
        Intent intent = new Intent(action);
        if (intent.resolveActivity(this.reactContext.getPackageManager()) != null) {
            this.reactContext.startActivityForResult(intent, requestCode, null);
        }
        promises.put(requestCode, promise);
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
