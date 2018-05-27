package com.example.user.arrowstest;

import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout container;

    @Retention(SOURCE)
    @IntDef({SELECTED_STATE, FAILED_STATE, DEFAULT_STATE})
    public @interface ARROW_STATE {
    }

    public static final int SELECTED_STATE = 0;
    public static final int FAILED_STATE = 1;
    public static final int DEFAULT_STATE = 2;

    private final int NUMBER_OF_STAGES = 12;
    private int selectedStage = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.arrow_container);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonClicked();
            }
        });

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextButtonClicked();
            }
        });
        initializeBar();
    }

    private void initializeBar() {
        drawStageBar(NUMBER_OF_STAGES, selectedStage, false);
    }

    private int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private void onNextButtonClicked() {
        container.removeAllViews();
        drawStageBar(NUMBER_OF_STAGES, selectedStage != NUMBER_OF_STAGES ? ++selectedStage : selectedStage, false);
    }

    private void onBackButtonClicked() {
        container.removeAllViews();
        drawStageBar(NUMBER_OF_STAGES, selectedStage - 1 > 0 ? --selectedStage : selectedStage, false);
    }

    private void drawStageBar(int numberOfStages, int selectedStage, boolean failed) {
        int arrowSize = computeOverlappedArrowSize(numberOfStages);
        int arrowSizeNotOverlapped = computeArrowSize(numberOfStages);
        for (int position = numberOfStages; position > 0; position--) {
            int leftMargin = computeLeftMargin(arrowSizeNotOverlapped, position);
            drawArrow(arrowSize, getArrowState(selectedStage, position, failed), leftMargin);// TODO replace with the real state
        }
    }

    @ARROW_STATE
    private int getArrowState(int selectedStage, int stagePosition, boolean failed) {
        return !failed ? stagePosition <= selectedStage ? SELECTED_STATE : DEFAULT_STATE : FAILED_STATE;
    }

    private int computeLeftMargin(int arrowSize, int position) {
        return arrowSize * (position - 1);
    }

    private int computeArrowSize(int numberOfArrows) {
        int screenWidth = getScreenWidth();
        return screenWidth / numberOfArrows;
    }

    private int computeOverlappedArrowSize(int numberOfArrows) {
        int initialSize = computeArrowSize(numberOfArrows);
        return (int) (initialSize * 1.25);
    }

    private void drawArrow(int arrowSize, @ARROW_STATE int state, int leftMargin) {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(getArrowDrawable(state));
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(arrowSize,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.setMargins(leftMargin, 0, 0, 0); //substitute parameters for left, top, right, bottom
        imageView.setLayoutParams(params);
        container.addView(imageView);
    }

    @DrawableRes
    private int getArrowDrawable(int state) {
        switch (state) {
            case SELECTED_STATE:
                return R.drawable.green_arrow;
            default:
                return R.drawable.gray_arrow;
        }
    }
}
