package com.example.mediapicker;

import android.app.Activity;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MediaPicker extends CordovaPlugin {
    private CallbackContext callbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;

        if (action.equals("pickAndroid")) {
            int maxCount = args.getJSONObject(0).getInt("multiple");
            pickMedia(maxCount);
            return true;
        }

        return false;
    }

    private void pickMedia(int maxCount) {
        Activity activity = cordova.getActivity();

        ActivityResultLauncher<PickVisualMediaRequest> launcher =
            ((MainActivity) activity).getActivityResultRegistry()
            .register("picker", new ActivityResultContracts.PickMultipleVisualMedia(), uris -> {
                ArrayList<String> result = new ArrayList<>();
                for (Uri uri : uris) {
                    result.add(uri.toString());
                }
                callbackContext.success(new JSONArray(result));
            });

        launcher.launch(new PickVisualMediaRequest(new ActivityResultContracts.PickVisualMedia.ImageAndVideo()));
    }
}
