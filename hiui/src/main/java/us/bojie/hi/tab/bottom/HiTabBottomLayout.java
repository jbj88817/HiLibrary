package us.bojie.hi.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import us.bojie.hi.R;
import us.bojie.hi.library.util.HiDisplayUtil;
import us.bojie.hi.tab.common.IHiTabLayout;

public class HiTabBottomLayout extends FrameLayout implements IHiTabLayout<HiTabBottom, HiTabBottomInfo<?>> {
    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";
    private List<OnTabSelectedListener<HiTabBottomInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    private HiTabBottomInfo<?> selectedInfo;
    private float bottomAlpha = 1f;
    //TabBottom高度
    private static float tabBottomHeight = 50;
    //TabBottom的头部线条高度
    private float bottomLineHeight = 0.5f;
    //TabBottom的头部线条颜色
    private String bottomLineColor = "#DFE0E1";
    private List<HiTabBottomInfo<?>> infoList;

    public HiTabBottomLayout(@NonNull Context context) {
        this(context, null);
    }

    public HiTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public HiTabBottom findTab(@NonNull HiTabBottomInfo<?> data) {
        ViewGroup ll = findViewWithTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof HiTabBottom) {
                HiTabBottom tab = (HiTabBottom) child;
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<HiTabBottomInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull HiTabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<HiTabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }

        this.infoList = infoList;

        // 移除之前已经添加的View
        for (int i = getChildCount() - 1; i > 0; i--) {
            removeViewAt(i);
        }
        selectedInfo = null;
        addBackground();

        //清除之前添加的HiTabBottom listener，Tips：Java foreach remove问题
        Iterator<OnTabSelectedListener<HiTabBottomInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof HiTabBottom) {
                iterator.remove();
            }
        }

        int height = HiDisplayUtil.dp2px(tabBottomHeight, getResources());
        FrameLayout ll = new FrameLayout(getContext());
        ll.setTag(TAG_TAB_BOTTOM);
        int width = HiDisplayUtil.getDisplayWidthInPx(getContext()) / infoList.size();
        for (int i = 0; i < infoList.size(); i++) {
            final HiTabBottomInfo<?> info = infoList.get(i);
            //Tips：为何不用LinearLayout：当动态改变child大小后Gravity.BOTTOM会失效
            LayoutParams params = new LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM;
            params.leftMargin = i * width;

            HiTabBottom tabBottom = new HiTabBottom(getContext());
            tabSelectedChangeListeners.add(tabBottom);
            tabBottom.setHiTabInfo(info);
            ll.addView(tabBottom, params);
            tabBottom.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(info);
                }
            });
        }

        LayoutParams flParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.BOTTOM;
        addBottomLine();
        addView(ll, flParams);
    }

    public void setTabAlpha(float alpha) {
        this.bottomAlpha = alpha;
    }

    public void setTabHeight(float tabHeight) {
        this.tabBottomHeight = tabHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
    }

    public void setBottomLineColor(String bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }

    private void addBottomLine() {
        View bottomLine = new View(getContext());
        bottomLine.setBackgroundColor(Color.parseColor(bottomLineColor));

        LayoutParams bottomLineParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(bottomLineHeight, getResources()));
        bottomLineParams.gravity = Gravity.BOTTOM;
        bottomLineParams.bottomMargin = HiDisplayUtil.dp2px(tabBottomHeight - bottomLineHeight, getResources());
        addView(bottomLine, bottomLineParams);
        bottomLine.setAlpha(bottomAlpha);
    }

    private void onSelected(@NonNull HiTabBottomInfo<?> nextInfo) {
        for (OnTabSelectedListener<HiTabBottomInfo<?>> listener : tabSelectedChangeListeners) {
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;
    }

    private void addBackground() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hi_bottom_layout_bg, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(tabBottomHeight, getResources()));
        params.gravity = Gravity.BOTTOM;
        addView(view, params);
        view.setAlpha(bottomAlpha);
    }
}