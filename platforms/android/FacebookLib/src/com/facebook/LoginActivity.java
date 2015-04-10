/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.android.R;
import com.facebook.appevents.AppEventsLogger;

/**
 * This Activity is a necessary part of the overall Facebook login process
 * but is not meant to be used directly. Add this activity to your
 * AndroidManifest.xml to ensure proper handling of Facebook login.
 * <pre>
 * {@code
 * <activity android:name="com.facebook.LoginActivity"
 *           android:theme="@android:style/Theme.Translucent.NoTitleBar"
 *           android:label="@string/app_name" />
 * }
 * </pre>
 * Do not start this activity directly.
 */
public class LoginActivity extends Activity {
    static final String RESULT_KEY = "com.facebook.LoginActivity:Result";

    private static final String TAG = LoginActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(getApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();
    	AppEventsLogger.deactivateApp(getApplicationContext());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
