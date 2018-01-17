package com.kashmoney.tipcalculator.Fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.kashmoney.tipcalculator.R;

public class SettingsFragment extends PreferenceFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CheckBoxPreference mDollarCheckBox;
    private CheckBoxPreference mPoundCheckBox;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        addPreferencesFromResource(R.xml.preferences);

        mDollarCheckBox = (CheckBoxPreference) findPreference("dollar_check_box_pref");
        mPoundCheckBox = (CheckBoxPreference) findPreference("pound_check_box_pref");

        mDollarCheckBox.setOnPreferenceClickListener(dollarPrefClickListener);
        mPoundCheckBox.setOnPreferenceClickListener(poundPrefClickListener);
    }

    public Preference.OnPreferenceClickListener dollarPrefClickListener =
            new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String key = preference.getKey();
                    if (key.equals("dollar_check_box_pref")) {
                        if (mDollarCheckBox.isChecked()) {
                            mPoundCheckBox.setChecked(false);
                            return true;
                        }
                        else {
                            mPoundCheckBox.setChecked(true);
                            return true;
                        }
                    }
                    return true;
                }
            };

    public Preference.OnPreferenceClickListener poundPrefClickListener =
            new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String key = preference.getKey();
                    if (key.equals("pound_check_box_pref")) {
                        if (mPoundCheckBox.isChecked()) {
                            mDollarCheckBox.setChecked(false);
                            return true;
                        }
                        else {
                            mDollarCheckBox.setChecked(true);
                            return true;
                        }
                    }
                    return true;
                }
            };
}
