package org.apache.cordova.facebook;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookDialogException;
import com.facebook.FacebookSdk;
import com.facebook.FacebookServiceException;
import com.facebook.appevents.AppEventsLogger;

public class ConnectPlugin extends CordovaPlugin {

    private static final int INVALID_ERROR_CODE = -2; //-1 is FacebookRequestError.INVALID_ERROR_CODE
    private static final String PUBLISH_PERMISSION_PREFIX = "publish";
    private static final String MANAGE_PERMISSION_PREFIX = "manage";
    @SuppressWarnings("serial")
    private static final Set<String> OTHER_PUBLISH_PERMISSIONS = new HashSet<String>() {
        {
            add("ads_management");
            add("create_event");
            add("rsvp_event");
        }
    };
    private final String TAG = "ConnectPlugin";

    private AppEventsLogger logger;
    private String applicationId = null;
    private CallbackContext showDialogContext = null;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        //Initialize UiLifecycleHelper

        // Init logger
    	FacebookSdk.sdkInitialize(cordova.getActivity().getApplicationContext());
        logger = AppEventsLogger.newLogger(cordova.getActivity());

        int appResId = cordova.getActivity().getResources().getIdentifier("fb_app_id", "string", cordova.getActivity().getPackageName());
        applicationId = cordova.getActivity().getString(appResId);

        // Set up the activity result callback to this class
        cordova.setActivityResultCallback(this);
        super.initialize(cordova, webView);
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        // Developers can observe how frequently users activate their app by logging an app activation event.
        AppEventsLogger.activateApp(cordova.getActivity());
    }

    protected void onSaveInstanceState(Bundle outState) {
    }

    public void onPause() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "activity result in plugin: requestCode(" + requestCode + "), resultCode(" + resultCode + ")");
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	if (action.equals("logEvent")) {
            if (args.length() == 0) {
                // Not enough parameters
                callbackContext.error("Invalid arguments");
                return true;
            }
            String eventName = args.getString(0);
            if (args.length() == 1) {
                logger.logEvent(eventName);
            } else {
                // Arguments is greater than 1
                JSONObject params = args.getJSONObject(1);
                Bundle parameters = new Bundle();

                Iterator<?> iterator = params.keys();
                while (iterator.hasNext()) {
                    try {
                        // Try get a String
                        String key = (String) iterator.next();
                        String value = params.getString(key);
                        parameters.putString(key, value);
                    } catch (Exception e) {
                        // Maybe it was an int
                        Log.w(TAG, "Type in AppEvent parameters was not String for key: " + (String) iterator.next());
                        try {
                            String key = (String) iterator.next();
                            int value = params.getInt(key);
                            parameters.putInt(key, value);
                        } catch (Exception e2) {
                            // Nope
                            Log.e(TAG, "Unsupported type in AppEvent parameters for key: " + (String) iterator.next());
                        }
                    }
                }
                if (args.length() == 2) {
                    logger.logEvent(eventName, parameters);
                }
                if (args.length() == 3) {
                    double value = args.getDouble(2);
                    logger.logEvent(eventName, value, parameters);
                }
            }
            callbackContext.success();
            return true;
        } 
        return false;
    }

    public JSONObject getErrorResponse(Exception error, String message, int errorCode) {

        if (error instanceof FacebookServiceException) {
            return null;
        }

        String response = "{";

        if (error instanceof FacebookDialogException) {
            errorCode = ((FacebookDialogException) error).getErrorCode();
        }

        if (errorCode != INVALID_ERROR_CODE) {
            response += "\"errorCode\": \"" + errorCode + "\",";
        }

        if (message == null) {
            message = error.getMessage();
        }

        response += "\"errorMessage\": \"" + message + "\"}";

        try {
            return new JSONObject(response);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return new JSONObject();
    }
}
