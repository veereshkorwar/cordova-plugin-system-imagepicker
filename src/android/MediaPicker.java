package com.example.mediapicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MediaPicker extends CordovaPlugin {

    private static final int PICK_MEDIA_REQUEST = 1001;
    private CallbackContext callbackContext;
    private int maxCount = 1;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("pickAndroid".equals(action)) {
            this.callbackContext = callbackContext;
            this.maxCount = args.getJSONObject(0).optInt("multiple", 1);
            openSystemPicker();
            return true;
        }
        return false;
    }

    private void openSystemPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        if (maxCount > 1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }

        // Let Android grant temporary URI access
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        cordova.startActivityForResult(this, intent, PICK_MEDIA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode != PICK_MEDIA_REQUEST) {
            return;
        }

        if (resultCode == Activity.RESULT_OK && intent != null) {
            ArrayList<String> results = new ArrayList<>();

            if (intent.getClipData() != null) {
                int count = intent.getClipData().getItemCount();
                for (int i = 0; i < count && i < maxCount; i++) {
                    Uri uri = intent.getClipData().getItemAt(i).getUri();
                    results.add(uri.toString());
                }
            } else if (intent.getData() != null) {
                results.add(intent.getData().toString());
            }

            callbackContext.success(new JSONArray(results));
        } else {
            callbackContext.error("Image selection cancelled");
        }
    }
}
