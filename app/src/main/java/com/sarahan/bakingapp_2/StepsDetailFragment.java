package com.sarahan.bakingapp_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sarahan.bakingapp_2.POJOItems.StepsItem;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepsDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StepsDetailFragment extends Fragment {
    private static final String LOG_TAG = StepsDetailFragment.class.getSimpleName();

    private static int position;
    private ArrayList<StepsItem> stepsItems;
    private boolean isTwoPane;

    private TextView tv_step_details_title;
    private TextView tv_long_description;
    private Button btn_previous;
    private Button btn_next;
    private PlayerView playerView;

    private int stepId;
    private String longDes;
    private String videoURL;
    private String thumbnailURL;


    private SimpleExoPlayer simpleExoPlayer;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private String mediaURL = "";

    private OnFragmentInteractionListener mListener;
    private Context context;
    private String userAgent = "Mozilla/5.0 (Linux; Android 6.0.1; SM-N910V Build/MMB29M) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36";

    private String userAgent2 = "Mozilla/5.0 (Linux; Android 4.4.2; Lenovo A3500-FL Build/KOT49H) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Safari/537.36";

    private String userAgent3= "Mozilla/5.0 (Linux; Android 8.0.0; Lenovo K8 Note Build/OMB27.43-20)" +
            " AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.111 Mobile Safari/537.36";



    private static final String ADAPTER_POSITION = "position";
    private static final String STEPS_ITEMS = "stepsItems";
    private static final String TWO_PANE = "isTwoPane";


    public StepsDetailFragment() {
        // Required empty public constructor
    }

    public static StepsDetailFragment newInstance(int position, ArrayList<StepsItem> stepsItems, boolean isTwoPane){
        Log.d(LOG_TAG, "Fragment new Instance method");
        StepsDetailFragment fragment = new StepsDetailFragment();
        Bundle arguments = new Bundle();
        //selected position on the steps list and the array list of step items.
        arguments.putInt(ADAPTER_POSITION, position);
        arguments.putParcelableArrayList(STEPS_ITEMS, stepsItems);
        arguments.putBoolean(TWO_PANE, isTwoPane);
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "on Create method running");
        context  = getContext();
        if((getArguments() != null) &&
            getArguments().containsKey(STEPS_ITEMS)&&
            getArguments().containsKey(ADAPTER_POSITION)){

        stepsItems = getArguments().getParcelableArrayList(STEPS_ITEMS);
        position = getArguments().getInt(ADAPTER_POSITION);
        isTwoPane = getArguments().getBoolean(TWO_PANE);
        }

        if(savedInstanceState != null){
            mediaURL = savedInstanceState.getString("mediaURL");
            playWhenReady = savedInstanceState.getBoolean("playWhenReady", false);
            playbackPosition = savedInstanceState.getLong("playbackPosition", 0);
            currentWindow = savedInstanceState.getInt("currentWindow", 0);
            stepsItems = savedInstanceState.getParcelableArrayList("stepsItems");
            position = savedInstanceState.getInt("position");
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int orientation = this.getResources().getConfiguration().orientation;
        Log.d(LOG_TAG, "user orientation : "  + orientation);

        Log.d(LOG_TAG, "on create view method running");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        //xml 파일의 뷰 요소들을 아이디로 찾아와서 정보를 디스플레이
        //step 을 클릭해서 받은 스텝 아이템에서 정보를 추출

        stepId = stepsItems.get(position).getId();
        longDes = stepsItems.get(position).getLongDes();
        videoURL = stepsItems.get(position).getVideoURL();
        thumbnailURL = stepsItems.get(position).getThumbnailURL();

        if((orientation == Configuration.ORIENTATION_PORTRAIT && !isTwoPane)
            || isTwoPane) {
            //when in phone & portrait mode || when in tablet mode (both landscape and portrait)
            tv_step_details_title = rootView.findViewById(R.id.tv_step_details_title);
            btn_previous = rootView.findViewById(R.id.btn_previous);
            btn_next = rootView.findViewById(R.id.btn_next);//Null error
            onButtonClicked();
        }
        playerView = rootView.findViewById(R.id.video_view);
        if(isTwoPane){
            //when in tablet mode, set the playerView height to 500dp
            playerView.getLayoutParams().height = 500;
        }
        tv_long_description = rootView.findViewById(R.id.tv_long_step_description);
        tv_long_description.setText(longDes);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart()");
        if(Util.SDK_INT > 23){
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        Log.d(LOG_TAG, "onResume()");
        if(Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("mediaURL", mediaURL);
        outState.putBoolean("playWhenReady", playWhenReady);
        outState.putLong("playbackPosition", playbackPosition);
        outState.putInt("currentWindow", currentWindow);
        outState.putParcelableArrayList("stepsItems", stepsItems);
        outState.putInt("position", position);

        Log.d(LOG_TAG, "on save instance state, outState :" + outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "on activity created, savedInstanceState : " + savedInstanceState);

    }
    //버튼 클릭할때마다 정보를 Map 에다가 저장해서 플레이어 초기화 할 때 넘겨주기?
    //버튼 클릭 - 바뀌는 정보 어디다가 저장하면 좋을지 ...?

    private void onButtonClicked(){
        Log.d(LOG_TAG, "on button clicked method running");
        btn_previous.setOnClickListener(v -> {
                releasePlayer();
                if(position == 0){
                    position = 0;
                }else{
                    position-- ;
                }
                setStepDetailInfo(position);
        });

        btn_next.setOnClickListener(v -> {
            releasePlayer();
            if (position == stepsItems.size() - 1) {
                position = stepsItems.size() - 1;
            } else {
                position++; //10
            }
            setStepDetailInfo(position);
        });

    }

    //display new information based on the position of the items.
    private void setStepDetailInfo(int position){
        // 버튼을 눌렀을 때 바뀐 상태 - stepsItems 랑 position 으로 찾아오는 것이므로, 이 둘의 값을 onSaveInstanceState 에다가 저장한다.
        longDes = stepsItems.get(position).getLongDes();
        videoURL = stepsItems.get(position).getVideoURL();
        thumbnailURL = stepsItems.get(position).getThumbnailURL();
        tv_long_description.setText(longDes);
        mediaURL = setMediaUrl(videoURL, thumbnailURL);

        //initialize player state again, or else it will retain the previous video's playback position.
        playWhenReady = true;
        playbackPosition = 0;
        currentWindow = 0;
        if(simpleExoPlayer != null){
            releasePlayer();
        }
        initializePlayer();
    }


    //set media Url based on json videoURL value or thumbnailURL value.
    private String setMediaUrl(String videoURL, String thumbnailURL){
        if(!videoURL.equals("")){
            mediaURL = videoURL;
        }else if(!thumbnailURL.equals("")){
            mediaURL = thumbnailURL;
        }else{
            mediaURL = "";
        }
        return mediaURL;
    }

    private boolean initializePlayer(){
        Log.d(LOG_TAG, "initializing exo player");
        mediaURL = setMediaUrl(videoURL, thumbnailURL);
        if(mediaURL.equals("")){
            playerView.setVisibility(View.GONE);
            return false;
        }else {
            playerView.setVisibility(View.VISIBLE);
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context,
                    new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl()
            );
            playerView.setPlayer(simpleExoPlayer);
            //playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            simpleExoPlayer.setPlayWhenReady(playWhenReady);
            simpleExoPlayer.seekTo(currentWindow, playbackPosition);

            Uri uri = Uri.parse(mediaURL);
            MediaSource mediaSource = buildMediaSource(uri);
            simpleExoPlayer.prepare(mediaSource, false, true);
        }
        return true;
    }

    private MediaSource buildMediaSource(Uri uri){
        return new ProgressiveMediaSource.Factory(
                new DefaultHttpDataSourceFactory(userAgent3))
                .createMediaSource(uri);
    }




    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause()");
        //save current state of the player in onPause() for both SDK > 23 and SDK <= 23
        //because onSaveInstanceState() is called after onPause and before onStop.
        retrieveCurrentPlayerState();

        if(Util.SDK_INT <= 23){
            releasePlayer();
        }
    }
    //for SDK > 23, player state must be saved in onPause
    //onPause() -> retrieveCurrentPlayerState() ->onSaveInstanceState()
    // -> onStop() -> releasePlayer()

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop()");
        if(Util.SDK_INT > 23 || simpleExoPlayer != null){
            releasePlayer();
        }
    }
    //액티비티가 회전 후 다시 시작하면 플레이어가 release 되기 때문에 ...

    private void releasePlayer(){
        Log.d(LOG_TAG, "releasePlayer()");
        if(simpleExoPlayer != null){
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    private void retrieveCurrentPlayerState(){
        if(simpleExoPlayer != null){

            playbackPosition = simpleExoPlayer.getCurrentPosition();
            currentWindow = simpleExoPlayer.getCurrentWindowIndex();
            playWhenReady = simpleExoPlayer.getPlayWhenReady();

        }
    }

    public void onButtonPressed(int position, ArrayList<StepsItem> stepsItems) {
        if (mListener != null) {
            mListener.onFragmentInteraction(position, stepsItems);
        }
    }

    void setOnFragmentInteractionListener(OnFragmentInteractionListener callback){
        mListener = callback;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "on attach fragment");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int position, ArrayList<StepsItem> stepsItems);
    }


}
