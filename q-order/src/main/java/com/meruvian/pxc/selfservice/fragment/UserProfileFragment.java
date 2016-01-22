package com.meruvian.pxc.selfservice.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageVariables;

/**
 * Created by akm on 05/12/15.
 */
public class UserProfileFragment extends Fragment {

    private TextView PointStatus;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        PointStatus = (TextView) view.findViewById(R.id.user_point);
        preferences = getActivity().getSharedPreferences(SignageVariables.PREFS_SERVER, 0);
        PointStatus.setText("Anda memiliki " + String.valueOf(preferences.getLong("default_point", 0)) + " Point");


        return view;
    }
}
