package com.xingjiezheng.nestedscrollingdemo.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingjiezheng.nestedscrollingdemo.R;

/**
 * Created by XingjieZheng
 * on 2016/9/9.
 */
public class HeaderTabView extends AbstractHeaderTabView implements View.OnClickListener,
        ViewPager.OnPageChangeListener {
    private TextView txtNickname;
    private ImageView imgAvatar;
    private ImageView imgLeft;
    private ImageView imgRight;
    private ImageView imgNotice;
    private ImageView imgClose;

    private View layoutTitleLeft;
    private View layoutTitleRight;
    private TextView txtNumberLeft;
    private TextView txtTitleLeft;
    private TextView txtNumberRight;
    private TextView txtTitleRight;
    private View viewTitleLeft;
    private View viewTitleRight;

    private int textMoveDistance;
    private int textSpaceDistance;

    private int layoutTitleLeftLocationLeft = 0;

    private int layoutTitleLeftMoveMaxLocationLeft = 0;

    private int colorSelected;
    private int colorUnselected;

    private IconOnClickListener iconOnClickListener;
    private ViewPager viewPager;

    private static final float mResistance = 0.5f;

    public HeaderTabView(Context context) {
        this(context, null, 0);
    }

    public HeaderTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        findViews(context);
        setListeners();
    }

    @Override
    public int getHeaderTop() {
        return mHeaderYScroll - mHeaderYScrollRange;
    }

    @Override
    public int moveBy(int deltaY) {
        int headerMove = 0;
        if (deltaY > 0) {
            isScrollUp = true;
            if (mHeaderYScroll > 0) {
                headerMove = (int) (deltaY * mResistance);
                int newYScroll = mHeaderYScroll - headerMove;
                if (newYScroll >= 0) {
                    mHeaderYScroll = newYScroll;
                } else {
                    headerMove = mHeaderYScroll;
                    mHeaderYScroll = 0;
                }
                offsetTopAndBottom(-headerMove);

            }
        } else {
            isScrollUp = false;
            if (mHeaderYScroll < mHeaderYScrollRange) {
                headerMove = (int) (deltaY * mResistance);
                int newYScroll = mHeaderYScroll - headerMove;
                if (newYScroll <= mHeaderYScrollRange) {
                    mHeaderYScroll = newYScroll;
                } else {
                    headerMove = -(mHeaderYScrollRange - mHeaderYScroll);
                    mHeaderYScroll = mHeaderYScrollRange;
                }
                offsetTopAndBottom(-headerMove);
            }
        }
        if (-headerMove != 0) {
            onOffsetChanged(mHeaderYScroll, -headerMove, mHeaderYScrollRange, isScrollUp);
        }
        return -headerMove;
    }

    private void init() {
        textSpaceDistance = 120;
        colorSelected = 0xffffffff;
        colorUnselected = 0xffc9aef0;

        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_tab_view_height);
        mFixHeight = getResources().getDimensionPixelSize(R.dimen.header_tool_bar_height);
        mHeaderYScrollRange = mHeaderHeight - mFixHeight;
        mHeaderYScroll = mHeaderYScrollRange;
    }


    private void findViews(Context context) {
        View mContentView = LayoutInflater.from(context).inflate(R.layout.layout_header_tab_view, this);
        layoutTitleLeft = mContentView.findViewById(R.id.layoutTitleLeft);
        layoutTitleRight = mContentView.findViewById(R.id.layoutTitleRight);
        txtNumberLeft = (TextView) mContentView.findViewById(R.id.txtNumberLeft);
        txtTitleLeft = (TextView) mContentView.findViewById(R.id.txtTitleLeft);
        txtNumberRight = (TextView) mContentView.findViewById(R.id.txtNumberRight);
        txtTitleRight = (TextView) mContentView.findViewById(R.id.txtTitleRight);
        viewTitleLeft = mContentView.findViewById(R.id.viewTitleLeft);
        viewTitleRight = mContentView.findViewById(R.id.viewTitleRight);
    }

    @Override
    public void setToolBarViewToHeaderView(@NonNull HeaderTabToolBarView headerTabToolBarView) {
        super.setToolBarViewToHeaderView(headerTabToolBarView);
        imgNotice = headerTabToolBarView.getImgNotice();
        imgClose = headerTabToolBarView.getImgClose();
        imgLeft = headerTabToolBarView.getImgLeft();
        imgRight = headerTabToolBarView.getImgRight();
        imgAvatar = headerTabToolBarView.getImgAvatar();
        txtNickname = headerTabToolBarView.getTxtNickname();

        if (imgNotice != null) {
            imgNotice.setOnClickListener(this);
        }
        if (imgClose != null) {
            imgClose.setOnClickListener(this);
        }
        if (imgLeft != null) {
            imgLeft.setAlpha(0f);
            imgLeft.setOnClickListener(this);
        }
        if (imgRight != null) {
            imgRight.setAlpha(0f);
            imgRight.setOnClickListener(this);
        }
    }

    private void setListeners() {
        layoutTitleLeft.setOnClickListener(this);
        layoutTitleRight.setOnClickListener(this);
    }

    private void onOffsetChanged(int offsetPosition, int offsetDistance, int offsetRange, boolean isUp) {

        calculate();
        float alpha = (float) offsetPosition / (float) offsetRange;
        float alphaOpposite = 1 - alpha;

        if (imgLeft != null) {
            imgLeft.setAlpha(alphaOpposite);
        }
        if (imgRight != null) {
            imgRight.setAlpha(alphaOpposite);
        }
        layoutTitleLeft.setAlpha(alpha);
        layoutTitleRight.setAlpha(alpha);

        float percent = (float) offsetDistance / (float) offsetRange;
        int textMoveDistanceNow = (int) (percent * textMoveDistance * 2);
        if (alpha >= 0.5f) {
            if (isUp) {
                if (layoutTitleLeft.getLeft() - textMoveDistanceNow > layoutTitleLeftMoveMaxLocationLeft) {
                    textMoveDistanceNow = layoutTitleLeftMoveMaxLocationLeft - layoutTitleLeft.getLeft();
                }
                layoutTitleLeft.offsetLeftAndRight(-textMoveDistanceNow);
                layoutTitleRight.offsetLeftAndRight(textMoveDistanceNow);
            } else {
                if (layoutTitleLeft.getLeft() - textMoveDistanceNow < layoutTitleLeftLocationLeft) {
                    textMoveDistanceNow = layoutTitleLeft.getLeft() - layoutTitleLeftLocationLeft;
                }
                layoutTitleLeft.offsetLeftAndRight(-textMoveDistanceNow);
                layoutTitleRight.offsetLeftAndRight(textMoveDistanceNow);
            }
            if (txtNickname != null) {
                txtNickname.setAlpha(1 - (1 - alpha) * 2f);
            }
        } else {
            if (txtNickname != null) {
                txtNickname.setAlpha(0);
            }
        }
    }


    private void calculate() {
        if (textMoveDistance == 0) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            layoutTitleLeftLocationLeft = 0;
            textMoveDistance = (int) (screenWidth / 4f - textSpaceDistance / 2f);
            layoutTitleLeftMoveMaxLocationLeft = textMoveDistance;
        }
    }

    private void setSelectTitle(boolean isLeft) {
        if (isLeft) {
            txtTitleLeft.setTextColor(colorSelected);
            txtTitleRight.setTextColor(colorUnselected);
            txtNumberLeft.setTextColor(colorSelected);
            txtNumberRight.setTextColor(colorUnselected);
            viewTitleLeft.setVisibility(VISIBLE);
            viewTitleRight.setVisibility(INVISIBLE);
            if (imgLeft != null) {
                imgLeft.setImageResource(R.drawable.ic_contact_phone_white_24dp);
            }
            if (imgRight != null) {
                imgRight.setImageResource(R.drawable.ic_contact_mail_gray_24dp);
            }
        } else {
            txtTitleLeft.setTextColor(colorUnselected);
            txtTitleRight.setTextColor(colorSelected);
            txtNumberLeft.setTextColor(colorUnselected);
            txtNumberRight.setTextColor(colorSelected);
            viewTitleLeft.setVisibility(INVISIBLE);
            viewTitleRight.setVisibility(VISIBLE);
            if (imgLeft != null) {
                imgLeft.setImageResource(R.drawable.ic_contact_phone_gray_24dp);
            }
            if (imgRight != null) {
                imgRight.setImageResource(R.drawable.ic_contact_mail_white_24dp);
            }
        }
    }

    private void setTitleText(String txtLeft, String txtRight) {
        if (txtTitleLeft != null && !TextUtils.isEmpty(txtLeft)) {
            txtTitleLeft.setText(txtLeft);
        }
        if (txtTitleRight != null && !TextUtils.isEmpty(txtRight)) {
            txtTitleRight.setText(txtRight);
        }
    }

    public void setTitleLeftNumber(String numLeft) {
        if (txtNumberLeft != null && !TextUtils.isEmpty(numLeft)) {
            txtNumberLeft.setText(numLeft);
        }
    }

    public void setTitleRightNumber(String numRight) {
        if (txtNumberRight != null && !TextUtils.isEmpty(numRight)) {
            txtNumberRight.setText(numRight);
        }
    }


    @Override
    public void onClick(View v) {
        if (iconOnClickListener != null) {
            iconOnClickListener.onClick(v.getId());
        }
        switch (v.getId()) {
            case R.id.layoutTitleRight: {
                if (viewPager != null) {
                    if (viewPager.getCurrentItem() != 1) {
                        viewPager.setCurrentItem(1);
                    }
                }
            }
            break;
            case R.id.layoutTitleLeft: {
                if (viewPager != null) {
                    if (viewPager.getCurrentItem() != 0) {
                        viewPager.setCurrentItem(0);
                    }
                }
            }
            break;
            case R.id.imgLeft: {
                if (viewPager != null) {
                    if (viewPager.getCurrentItem() != 0) {
                        viewPager.setCurrentItem(0);
                    }
                }
            }
            break;
            case R.id.imgRight: {
                if (viewPager != null) {
                    if (viewPager.getCurrentItem() != 1) {
                        viewPager.setCurrentItem(1);
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelectTitle(position == 0);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface IconOnClickListener {
        void onClick(int resourceId);
    }

    public void setIconOnClickListener(IconOnClickListener iconOnClickListener) {
        this.iconOnClickListener = iconOnClickListener;
    }

    public void setupWithViewPager(@NonNull ViewPager viewPager) {
        this.viewPager = viewPager;
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
        } else {
            viewPager.addOnPageChangeListener(this);
            setTitleText(String.valueOf(adapter.getPageTitle(0)), String.valueOf(adapter.getPageTitle(1)));
        }
    }

    public void setNickname(String nickname) {
        if (txtNickname != null && !TextUtils.isEmpty(nickname)) {
            txtNickname.setText(nickname);
        }
    }

}

