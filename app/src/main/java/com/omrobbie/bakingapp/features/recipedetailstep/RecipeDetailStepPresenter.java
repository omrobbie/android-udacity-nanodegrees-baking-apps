package com.omrobbie.bakingapp.features.recipedetailstep;

import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.omrobbie.bakingapp.basemvp.BasePresenter;
import com.omrobbie.bakingapp.database.pojo.Step;
import com.omrobbie.bakingapp.util.ContentTypeUtils;
import com.omrobbie.bakingapp.util.URLUtils;

import java.io.IOException;

public class RecipeDetailStepPresenter implements BasePresenter<RecipeDetailStepView> {

    private static final String TAG = RecipeDetailStepPresenter.class.getSimpleName();

    private RecipeDetailStepView view;
    private Gson gson = new Gson();
    private SimpleExoPlayer player;
    private Thread thread;
    private String contentType;

    @Override
    public void onAttach(RecipeDetailStepView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        if (player != null) {
            player.release();
            player = null;
        }

        thread = null;
        gson = null;
        view = null;
    }

    void getStepModel(String json) {
        view.bindData(gson.fromJson(json, Step.class));
    }

    void setupPlayer(Uri uri) {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(view.getContextFromFragment()),
                new DefaultTrackSelector()
        );

        String userAgent = Util.getUserAgent(view.getContextFromFragment(), TAG);
        MediaSource mediaSource = new ExtractorMediaSource(
                uri,
                new DefaultDataSourceFactory(view.getContextFromFragment(), userAgent),
                new DefaultExtractorsFactory(),
                null,
                null
        );

        view.onPlayerSet(player, mediaSource);
    }

    void setupImage(String imageURL) {
        Glide.with(view.getContextFromFragment())
                .load(imageURL)
                .into(view.getImageView());
        view.onImageSet();
    }

    void contentChecker(final String url, final String type) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    contentType = URLUtils.getContentType(url);
                    if (type.equals("video")) {
                        if (ContentTypeUtils.isVideo(contentType)) {
                            setupPlayer(Uri.parse(url));
                        } else if (ContentTypeUtils.isImage(contentType)) {
                            setupImage(url);
                        }
                    } else if (type.equals("image")) {
                        if (ContentTypeUtils.isImage(contentType)) {
                            setupImage(url);
                        } else if (ContentTypeUtils.isVideo(contentType)) {
                            setupPlayer(Uri.parse(url));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    void checkMedia(String videoURL, String thumbURL) {
        if (videoURL.equals("") || videoURL.length() == 0) {
            if (thumbURL.equals("") || thumbURL.length() == 0) {
                view.onNoMediaAvailable();
            } else {
                contentChecker(thumbURL, "image");
            }
        } else {
            contentChecker(videoURL, "video");
        }
    }
}
