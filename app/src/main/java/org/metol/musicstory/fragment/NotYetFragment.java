package org.metol.musicstory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.metol.musicstory.R;

/**
 * Created by broccoli on 2017/4/21.
 */

public class NotYetFragment extends Fragment{
    public static Fragment newInstance() {
        return new NotYetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_yet, container, false);
        return view;
    }
}
