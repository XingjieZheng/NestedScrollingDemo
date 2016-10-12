package com.xingjiezheng.nestedscrollingdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingjiezheng.nestedscrollingdemo.R;

/**
 * Created by XingjieZheng
 * on 2016/9/9.
 */
public class HeaderTabToolBarView extends FrameLayout {

    private TextView txtNickname;
    private ImageView imgLeft;
    private ImageView imgRight;
    private ImageView imgNotice;
    private ImageView imgClose;
    private ImageView imgAvatar;

    public HeaderTabToolBarView(Context context) {
        this(context, null, 0);
    }

    public HeaderTabToolBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderTabToolBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        findViews(context);
        initViews();
    }

    private void findViews(Context context) {
        View mContentView = LayoutInflater.from(context).inflate(R.layout.layout_header_tab_tool_bar_view, this);
        txtNickname = (TextView) mContentView.findViewById(R.id.txtNickname);
        imgLeft = (ImageView) mContentView.findViewById(R.id.imgLeft);
        imgRight = (ImageView) mContentView.findViewById(R.id.imgRight);
        imgNotice = (ImageView) mContentView.findViewById(R.id.imgNotice);
        imgClose = (ImageView) mContentView.findViewById(R.id.imgClose);
        imgAvatar = (ImageView) mContentView.findViewById(R.id.imgAvatar);
    }

    private void initViews() {
        imgLeft.setAlpha(0f);
        imgRight.setAlpha(0f);
    }

    public ImageView getImgLeft() {
        return imgLeft;
    }

    public ImageView getImgRight() {
        return imgRight;
    }

    public ImageView getImgClose() {
        return imgClose;
    }

    public ImageView getImgNotice() {
        return imgNotice;
    }

    public ImageView getImgAvatar() {
        return imgAvatar;
    }

    public TextView getTxtNickname() {
        return txtNickname;
    }

}

