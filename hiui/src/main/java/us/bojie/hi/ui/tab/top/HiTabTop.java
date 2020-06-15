package us.bojie.hi.ui.tab.top;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import us.bojie.hi.ui.R;
import us.bojie.hi.ui.tab.common.IHiTab;

public class HiTabTop extends RelativeLayout implements IHiTab<HiTabTopInfo<?>> {

    private HiTabTopInfo<?> tabInfo;
    private ImageView tabImageView;
    private TextView tabNameView;
    private View indicator;

    public HiTabTop(Context context) {
        this(context, null);
    }

    public HiTabTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_top, this);
        tabImageView = findViewById(R.id.iv_image);
        tabNameView = findViewById(R.id.tv_name);
        indicator = findViewById(R.id.tab_top_indicator);
    }

    @Override
    public void setHiTabInfo(@NonNull HiTabTopInfo<?> data) {
        this.tabInfo = data;
        inflateInfo(false, true);
    }

    private void inflateInfo(boolean selected, boolean init) {
        if (tabInfo.getTabType() == HiTabTopInfo.TabType.TEXT) {
            if (init) {
                tabImageView.setVisibility(GONE);
                tabNameView.setVisibility(VISIBLE);
                if (!TextUtils.isEmpty(tabInfo.getName())) {
                    tabNameView.setText(tabInfo.getName());
                }
            }
            if (selected) {
                indicator.setVisibility(VISIBLE);
                tabNameView.setTextColor(getTextColor(tabInfo.getTintColor()));
            } else {
                indicator.setVisibility(GONE);
                tabNameView.setTextColor(getTextColor(tabInfo.getDefaultColor()));
            }
        } else if (tabInfo.getTabType() == HiTabTopInfo.TabType.BITMAP) {
            if (init) {
                tabImageView.setVisibility(VISIBLE);
                tabNameView.setVisibility(GONE);
            }
            if (selected) {
                indicator.setVisibility(VISIBLE);
                tabImageView.setImageBitmap(tabInfo.getSelectedBitmap());
            } else {
                indicator.setVisibility(GONE);
                tabImageView.setImageBitmap(tabInfo.getDefaultBitmap());
            }
        }
    }

    @Override
    public void resetHeight(int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = height;
        setLayoutParams(params);
        getTabNameView().setVisibility(GONE);
    }

    @Override
    public void onTabSelectedChange(int index, @Nullable HiTabTopInfo<?> prevInfo, @NonNull HiTabTopInfo<?> nextInfo) {
        if (prevInfo != tabInfo && nextInfo != tabInfo || prevInfo == nextInfo) {
            return;
        }

        if (prevInfo == tabInfo) {
            inflateInfo(false, false);
        } else {
            inflateInfo(true, false);
        }
    }

    public HiTabTopInfo<?> getHiTabInfo() {
        return tabInfo;
    }

    public ImageView getTabImageView() {
        return tabImageView;
    }

    public TextView getTabNameView() {
        return tabNameView;
    }

    @ColorInt
    private int getTextColor(Object color) {
        if (color instanceof String) {
            return Color.parseColor((String) color);
        } else {
            return (int) color;
        }
    }
}
