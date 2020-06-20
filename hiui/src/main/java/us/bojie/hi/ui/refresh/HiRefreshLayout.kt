package us.bojie.hi.ui.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.Scroller
import us.bojie.hi.library.util.HiDisplayUtil
import kotlin.math.abs

class HiRefreshLayout(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes), HiRefresh {

    private lateinit var mState: HiOverView.HiRefreshState
    private val mGestureDetector: GestureDetector
    private var mHiRefreshListener: HiRefresh.HiRefreshListener? = null
    protected var mHiOverView: HiOverView? = null
    private var mLastY = 0
    private val mScroller = AutoScroller()
    private val hiGestureListener = object : HiGestureListener() {
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (abs(distanceX) > abs(distanceY) || mHiRefreshListener != null && !mHiRefreshListener!!.enableRefresh()) {
                //如果列表发生了滚动则不处理
                return false
            }
            if (mDisableRefreshScroll && mState === HiOverView.HiRefreshState.STATE_REFRESH) { //刷新时是否禁止滑动
                return true
            }
            val head = getChildAt(0)
            val child: View = HiScrollUtil.findScrollableChild(this@HiRefreshLayout)
            if (HiScrollUtil.childScrolled(child)) {
                //如果列表发生了滚动则不处理
                return false
            }
            //没有刷新或没有达到可以刷新的距离，且头部已经划出或下拉
            return if ((mState !== HiOverView.HiRefreshState.STATE_REFRESH
                        || head.bottom <= mHiOverView!!.mPullRefreshHeight)
                && (head.bottom > 0 || distanceY <= 0.0f)) {
                //还在滑动中
                if (mState !== HiOverView.HiRefreshState.STATE_OVER_RELEASE) {
                    //阻尼计算
                    val speed: Int = if (child.top < mHiOverView!!.mPullRefreshHeight) {
                        (mLastY / mHiOverView!!.minDamp).toInt()
                    } else {
                        (mLastY / mHiOverView!!.maxDamp).toInt()
                    }
                    //如果是正在刷新状态，则不允许在滑动的时候改变状态
                    val bool: Boolean = moveDown(speed, true)
                    mLastY = (-distanceY).toInt()
                    bool
                } else {
                    false
                }
            } else {
                false
            }
        }
    }

    //刷新时是否禁止滚动
    private var mDisableRefreshScroll = false

    init {
        mGestureDetector = GestureDetector(context, hiGestureListener)
    }

    override fun setDisableRefreshScroll(disable: Boolean) {
        mDisableRefreshScroll = disable
    }

    override fun onRefreshFinished() {
        TODO("Not yet implemented")
    }

    override fun setRefreshListener(listener: HiRefresh.HiRefreshListener) {
        mHiRefreshListener = listener
    }

    override fun setRefreshOverView(hiOverView: HiOverView) {
        mHiOverView = hiOverView
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val head = getChildAt(0)
        if (ev?.action == MotionEvent.ACTION_UP || ev?.action == MotionEvent.ACTION_CANCEL
            || ev?.action == MotionEvent.ACTION_POINTER_INDEX_MASK
        ) {
            //松开手
            if (head.bottom > 0) {
                if (mState !== HiOverView.HiRefreshState.STATE_REFRESH) { //非正在刷新
                    recover(head.bottom)
                    return false
                }
            }
            mLastY = 0
        }
        val consumed = mGestureDetector.onTouchEvent(ev)
        if ((consumed || (mState != HiOverView.HiRefreshState.STATE_INIT && mState != HiOverView.HiRefreshState.STATE_REFRESH)) && head.bottom != 0) {
            ev?.action = MotionEvent.ACTION_CANCEL
            return super.dispatchTouchEvent(ev)
        }

        return if (consumed) {
            true
        } else {
            super.dispatchTouchEvent(ev)
        }
    }

    private fun recover(distance: Int) {
        val pullRefreshHeight = mHiOverView?.mPullRefreshHeight
            ?: HiDisplayUtil.dp2px(66f, resources)
        if (mHiRefreshListener != null && distance > pullRefreshHeight
        ) {
            mScroller.recover(distance - pullRefreshHeight)
            mState = HiOverView.HiRefreshState.STATE_OVER_RELEASE
        } else {
            mScroller.recover(distance)
        }
    }

    /**
     * 借助Scroller实现视图的自动滚动
     * https://juejin.im/post/5c7f4f0351882562ed516ab6
     */
    private inner class AutoScroller : Runnable {
        private val mScroller: Scroller = Scroller(context, LinearInterpolator())
        private var mLastY = 0
        private var mIsFinished = true

        override fun run() {
            if (mScroller.computeScrollOffset()) {//还未滚动完成
                // moveDown
                mLastY = mScroller.currY
                post(this)
            } else {
                removeCallbacks(this)
                mIsFinished = true
            }
        }

        fun recover(dis: Int) {
            if (dis <= 0) {
                return;
            }
            removeCallbacks(this)
            mLastY = 0
            mIsFinished = false
            mScroller.startScroll(0, 0, 0, dis, 300)
            post(this)
        }

        fun isFinished(): Boolean {
            return mIsFinished
        }
    }

}