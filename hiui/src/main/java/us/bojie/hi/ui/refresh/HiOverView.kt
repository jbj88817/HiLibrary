package us.bojie.hi.ui.refresh

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import us.bojie.hi.library.util.HiDisplayUtil

/**
 * 下拉刷新的Overlay视图,可以重载这个类来定义自己的Overlay
 */

abstract class HiOverView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    enum class HiRefreshState {
        /**
         * 初始态
         */
        STATE_INIT,

        /**
         * Header展示的状态
         */
        STATE_VISIBLE,

        /**
         * 超出可刷新距离的状态
         */
        STATE_OVER,

        /**
         * 刷新中的状态
         */
        STATE_REFRESH,

        /**
         * 超出刷新位置松开手后的状态
         */
        STATE_OVER_RELEASE
    }

    protected val mState = HiRefreshState.STATE_INIT
    /**
     * 触发下拉刷新 需要的最小高度
     */
    var mPullRefreshHeight = 0

    /**
     * 最小阻尼
     */
    var minDamp = 1.6f

    /**
     * 最大阻尼
     */
    var maxDamp = 2.2f

    init {
        mPullRefreshHeight = HiDisplayUtil.dp2px(66f, resources)
        init()
    }

    /**
     * 初始化
     */
    abstract fun init()

    protected abstract fun onScroll(scrollY: Int, pullRefreshHeight: Int)

    /**
     * 显示Overlay
     */
    protected abstract fun onVisible()

    /**
     * 超过Overlay，释放就会加载
     */
    abstract fun onOver()

    /**
     * 开始加载
     */
    abstract fun onRefresh()

    /**
     * 加载完成
     */
    abstract fun onFinish()
}

