package org.metol.musicstory.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.OnSheetDismissedListener;
import com.flipboard.bottomsheet.commons.BottomSheetFragment;

import org.metol.musicstory.R;
import org.metol.musicstory.model.MusicStory;
import org.metol.musicstory.model.Constants;
import org.metol.musicstory.util.GlideManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Broccoli.Huang on 2018/1/4.
 */

public class CardBottomSheetFragment extends BottomSheetFragment {
    BottomSheetLayout bsl_bottomsheet;
    @BindView(R.id.rl_bar)              RelativeLayout  rl_bar;
    @BindView(R.id.iv_bar_close)        ImageView       iv_bar_close;
    @BindView(R.id.tv_bar_sheet_name)   TextView        tv_bar_sheet_name;

    @BindView(R.id.fl_header)           FrameLayout     fl_header;
    @BindView(R.id.iv_sheet_cover)      ImageView       iv_sheet_cover;
    @BindView(R.id.iv_sheet_background) ImageView       iv_sheet_background;
    @BindView(R.id.tv_sheet_name)       TextView        tv_sheet_name;
    @BindView(R.id.tv_sheet_artist_and_songname)TextView tv_sheet_artist_and_songname;

    private Unbinder unbinder;
    private Callback_BottomSheet callback_bottomSheet;
    private MusicStory mMusicStory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.sheetview, container, false);
        unbinder = ButterKnife.bind(this, v);

        mMusicStory = getArguments().getParcelable(Constants.ARGUMENTS_MUSICSTORY);

        getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.ll_sheetview_fragment, getMusicStoryFragment(mMusicStory, new MusicStoryFragment.Callback_Sheet() {
                    @Override
                    public void onUpdate() {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if(bsl_bottomsheet != null) bsl_bottomsheet.peekSheet();
                            }
                        }, 0);
                    }
                }))
                .commit();
        GlideManager.setSongImage(getActivity(), mMusicStory.getCoverUrl(), iv_sheet_cover);
        GlideManager.setBackgroundImageWithGaussianBlur(getActivity(), mMusicStory.getCoverUrl(), iv_sheet_background);
        tv_sheet_name.setText(mMusicStory.getStoryTitle());
        tv_bar_sheet_name.setText(mMusicStory.getStoryTitle());
        View.OnClickListener onClick_TextMarquee = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
            }
        };
        tv_sheet_name.setOnClickListener(onClick_TextMarquee);
        tv_bar_sheet_name.setOnClickListener(onClick_TextMarquee);
        tv_sheet_artist_and_songname.setOnClickListener(onClick_TextMarquee);
        tv_sheet_artist_and_songname.setText(mMusicStory.getArtistName()+" - "+mMusicStory.getSongName());

        iv_bar_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsl_bottomsheet.dismissSheet();
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        bsl_bottomsheet = (BottomSheetLayout) getView().getParent();

        if(bsl_bottomsheet==null) return;
        if(onSheetStateChangeListener!=null) bsl_bottomsheet.addOnSheetStateChangeListener(onSheetStateChangeListener);
        if(onSheetDismissedListener!=null) bsl_bottomsheet.addOnSheetDismissedListener(onSheetDismissedListener);
    }

    private BottomSheetLayout.OnSheetStateChangeListener onSheetStateChangeListener = new BottomSheetLayout.OnSheetStateChangeListener() {
        @Override
        public void onSheetStateChanged(BottomSheetLayout.State state) {
            if(state==BottomSheetLayout.State.PREPARING){
                rl_bar.setVisibility(View.GONE);
                fl_header.setVisibility(View.VISIBLE);
            }else if(state==BottomSheetLayout.State.PEEKED){
                rl_bar.setVisibility(View.GONE);
                fl_header.setVisibility(View.VISIBLE);
            }else if(state==BottomSheetLayout.State.EXPANDED){
                if(bsl_bottomsheet.getMaxSheetTranslation()==bsl_bottomsheet.getHeight()) {
                    rl_bar.setVisibility(View.VISIBLE);
                    fl_header.setVisibility(View.GONE);
                }
            }else if(state==BottomSheetLayout.State.HIDDEN){
                rl_bar.setVisibility(View.GONE);
                fl_header.setVisibility(View.VISIBLE);
            }
        }
    };
    OnSheetDismissedListener onSheetDismissedListener = new OnSheetDismissedListener() {
        @Override
        public void onDismissed(BottomSheetLayout bottomSheetLayout) {
            callback_bottomSheet.onDismiss();
        }
    };

    public void setCallback(Callback_BottomSheet callback){
        callback_bottomSheet = callback;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(bsl_bottomsheet!=null && onSheetStateChangeListener!=null) bsl_bottomsheet.removeOnSheetStateChangeListener(onSheetStateChangeListener);
        if(bsl_bottomsheet!=null && onSheetDismissedListener!=null) bsl_bottomsheet.removeOnSheetDismissedListener(onSheetDismissedListener);
        unbinder.unbind();
    }

    public interface Callback_BottomSheet{
        void onDismiss();
    }

    private Fragment getMusicStoryFragment(MusicStory musicStory, MusicStoryFragment.Callback_Sheet callback_Sheet) {
        return MusicStoryFragment.newInstanceForSheet(musicStory, callback_Sheet);
    }
}
