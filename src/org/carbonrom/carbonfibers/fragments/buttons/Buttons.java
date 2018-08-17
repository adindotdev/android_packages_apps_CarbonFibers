/*
 * Copyright (C) 2018 CarbonROM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.carbonrom.carbonfibers.fragments.buttons;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import com.android.internal.util.cr.CrUtils;
import com.android.settings.R;
import com.android.settings.carbon.CustomSettingsPreferenceFragment;

public class Buttons extends CustomSettingsPreferenceFragment {
    private static final String TAG = "Buttons";
    private static final String VOLUME_BUTTON_MUSIC_CONTROL = "volume_button_music_control";
    private static final String TORCH_POWER_BUTTON_GESTURE = "torch_power_button_gesture";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.buttons);
        addCustomPreference(findPreference(VOLUME_BUTTON_MUSIC_CONTROL), SYSTEM_TWO_STATE, STATE_OFF);

        PreferenceScreen prefSet = getPreferenceScreen();
        if (!CrUtils.deviceSupportsFlashLight(getContext())) {
            Preference toRemove = prefSet.findPreference(TORCH_POWER_BUTTON_GESTURE);
            if (toRemove != null) {
                prefSet.removePreference(toRemove);
            }
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (findPreference(TORCH_POWER_BUTTON_GESTURE).equals(preference)) {
            int torchPowerButtonValue = Settings.Secure.getInt(getActivity().getContentResolver(),
                    Settings.Secure.TORCH_POWER_BUTTON_GESTURE, 0);
            int doubleTapCameraGesture = Settings.Secure.getInt(getActivity().getContentResolver(),
                    Settings.Secure.CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED, 1);
            if (torchPowerButtonValue == 1 && doubleTapCameraGesture == 0) {
                // if double-tap for torch is enabled, switch off double-tap for camera
                Settings.Secure.putInt(getActivity().getContentResolver(),
                        Settings.Secure.CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED, 1);
                Toast.makeText(getActivity(),
                        (R.string.torch_power_button_gesture_dt_toast),
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }
}
