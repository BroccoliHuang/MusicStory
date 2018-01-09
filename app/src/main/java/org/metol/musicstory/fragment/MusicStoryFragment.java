package org.metol.musicstory.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.metol.musicstory.R;
import org.metol.musicstory.model.MusicStory;
import org.metol.musicstory.model.Constants;

/**
 * Created by Broccoli.Huang on 2018/1/4.
 */

public class MusicStoryFragment extends Fragment{
    private static Callback_Sheet callback_Sheet;
    private MusicStory musicStory;

    public static Fragment newInstanceForSheet(MusicStory musicStory, Callback_Sheet callback_Sheet) {
        MusicStoryFragment.callback_Sheet = callback_Sheet;

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ARGUMENTS_CATEGORY, Constants.CATEGORY_SHEET);
        bundle.putParcelable(Constants.ARGUMENTS_MUSICSTORY, musicStory);

        MusicStoryFragment albumListFragment = new MusicStoryFragment();
        albumListFragment.setArguments(bundle);
        return albumListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicStory = getArguments().getParcelable(Constants.ARGUMENTS_MUSICSTORY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.cardview_musicstory, container, false);

        ((TextView)v.findViewById(R.id.tv_story_content)).setText(musicStory.getStoryContent());

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(callback_Sheet!=null) callback_Sheet.onUpdate();
    }

    public interface Callback_Sheet{
        void onUpdate();
    }
}
