package com.meruvian.pxc.selfservice.fragment.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.activity.SyncCampaignActivity;

/**
 * Created by meruvian on 01/04/15.
 */
public class CampaignFragment extends PreferenceFragment {
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        final SharedPreferences.Editor editor = preferences.edit();
        PreferenceManager.setDefaultValues(getActivity(), R.xml.campaign_data, false);

        addPreferencesFromResource(R.xml.campaign_data);

        final EditTextPreference editSite = (EditTextPreference) findPreference("site_name");
        final EditTextPreference editCampaign = (EditTextPreference) findPreference("campaign_name");
        Preference preference = findPreference("start_sync_campaign");

        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                editor.putString("campaign_name", editCampaign.getText().toString());
                editor.putString("site_name", editSite.getText().toString());
                Intent intent = new Intent(getActivity(), SyncCampaignActivity.class);
                intent.putExtra("just_sync", true);
                startActivity(intent);

                return false;
            }
        });

    }
}
