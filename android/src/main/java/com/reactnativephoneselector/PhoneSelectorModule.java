package com.reactnativephoneselector;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.CredentialsOptions;
import com.google.android.gms.auth.api.credentials.HintRequest;

public class PhoneSelectorModule extends ReactContextBaseJavaModule {
    public static final String NAME = "PhoneSelector";

    private Promise myPromise;
    private static final int CREDENTIAL_PICKER_REQUEST = 1;
    public static final int RESULT_OK = -1;

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            Log.d("LOGGING","INSIDE onActivityResult");


            if (requestCode == CREDENTIAL_PICKER_REQUEST) {
                Log.d("LOGGING","CREDENTIAL_PICKER_REQUEST");
                if (myPromise != null) {
                    Log.d("LOGGING","myPromise != null");
                    if (resultCode == Activity.RESULT_CANCELED) {
                        Log.d("LOGGING","resultCode == Activity.RESULT_CANCELED");
                        myPromise.reject("Activity was cancelled");
                    } else if (resultCode == Activity.RESULT_OK) {
                        Log.d("LOGGING","resultCode == Activity.RESULT_OK");
                        Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                        Log.d("LOGGING",credential.getId());
                        myPromise.resolve(credential.getId());
                        // credential.getId();  <-- will need to process phone number string

                    } else {

                        myPromise.reject("Activity was cancelled");
                        Log.d("LOGGING",String.valueOf(resultCode));
                    }

                    myPromise = null;
                }
            }
        }
    };

    public PhoneSelectorModule(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(mActivityEventListener);

    }
    @ReactMethod
    public void getPhoneNumber(Promise promise) {
        myPromise = promise;
        startHinter();
    }

    private void startHinter(){
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        CredentialsOptions options = new CredentialsOptions.Builder()
                .forceEnableSaveDialog()
                .build();

        // Then, pass the HintRequest object to
        // credentialsClient.getHintPickerIntent()
        // to get an intent to prompt the user to
        // choose a phone number.
        CredentialsClient credentialsClient = Credentials.getClient(getReactApplicationContext(), options);
        PendingIntent intent = credentialsClient.getHintPickerIntent(hintRequest);
        try {
            Activity currentActivity = getCurrentActivity();
            currentActivity.startIntentSenderForResult(intent.getIntentSender(),
                    CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0);

        } catch (IntentSender.SendIntentException e) {
            myPromise.reject("Falied to load Hinter");
            e.printStackTrace();
        }
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }


    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    public void multiply(int a, int b, Promise promise) {
        promise.resolve(a * b);
    }

    public static native int nativeMultiply(int a, int b);
}
