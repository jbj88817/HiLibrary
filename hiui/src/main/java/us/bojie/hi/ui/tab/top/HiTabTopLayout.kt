package us.bojie.hi.ui.tab.top

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import us.bojie.hi.library.util.HiDisplayUtil
import us.bojie.hi.ui.tab.common.IHiTabLayout
import java.util.*
import kotlin.math.abs

class HiTabTopLayout(context: Context?, attrs: AttributeSet?) : HorizontalScrollView(
    context,
    attrs,
    0,
    0
), IHiTabLayout<HiTabTop, HiTabTopInfo<*>> {

    private val tabSelectedChangeListeners: MutableList<IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<*>>> =
        ArrayList()
    private var selectedInfo: HiTabTopInfo<*>? = null
    private lateinit var infoList: List<HiTabTopInfo<*>>
    private var tabWith: Int? = 0

    init {
        isVerticalScrollBarEnabled = false
    }

    override fun findTab(info: HiTabTopInfo<*>): HiTabTop? {
        val ll: ViewGroup = getRootLayout(false)
        for (i in 0 until ll.childCount) {
            val child = ll.getChildAt(i)
            if (child is HiTabTop) {
                if (child.hiTabInfo === info) {
                    return child
                }
            }
        }
        return null
    }

    override fun addTabSelectedChangeListener(listener: IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<*>>) {
        tabSelectedChangeListeners.add(listener)
    }

    override fun defaultSelected(defaultInfo: HiTabTopInfo<*>) {
        onSelected(defaultInfo)
    }

    override fun inflateInfo(infoList: MutableList<HiTabTopInfo<*>>) {
        if (infoList.isEmpty()) {
            return
        }
        this.infoList = infoList
        val linearLayout = getRootLayout(true)
        //清除之前添加的HiTabTop listener，Tips：Java foreach remove问题
        val iterator: MutableIterator<IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<*>>> =
            tabSelectedChangeListeners.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() is HiTabTop) {
                iterator.remove()
            }
        }
        for (i in infoList.indices) {
            val info = infoList[i]
            val tab = HiTabTop(context)
            tabSelectedChangeListeners.add(tab)
            tab.hiTabInfo = info
            linearLayout.addView(tab)
            tab.setOnClickListener {
                onSelected(info)
            }
        }
    }

    private fun onSelected(nextInfo: HiTabTopInfo<*>) {
        for (listener in tabSelectedChangeListeners) {
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo)
        }
        selectedInfo = nextInfo
        autoScroll(nextInfo)
    }

    private fun autoScroll(nextInfo: HiTabTopInfo<*>) {
        val tabTop = findTab(nextInfo) ?: return
        val index = infoList.indexOf(nextInfo)
        val loc = IntArray(2)
        //获取点击的控件在屏幕的位置
        tabTop.getLocationInWindow(loc)
        val scrollWidth: Int
        if (tabWith == 0) {
            tabWith = tabTop.width
        }

        //判断点击了屏幕左侧还是右侧
        scrollWidth =
            if ((loc[0] + tabWith!! / 2) > HiDisplayUtil.getDisplayWidthInPx(context) / 2) {
                rangeScrollWidth(index, 2)
            } else {
                rangeScrollWidth(index, -2)
            }
        scrollTo(scrollX + scrollWidth, 0)
    }

    /**
     * 获取可滚动的范围
     *
     * @param index 从第几个开始
     * @param range 向前向后的范围
     * @return 可滚动的范围
     */
    private fun rangeScrollWidth(index: Int, range: Int): Int {
        var scrollWidth = 0
        for (i in 0..abs(range)) {
            val next =
                if (range < 0) {
                    range + i + index
                } else {
                    range - i + index
                }
            if (next >= 0 && next < infoList.size) {
                if (range < 0) {
                    scrollWidth -= scrollWidth(next, false)
                } else {
                    scrollWidth += scrollWidth(next, true)
                }
            }
        }
        return scrollWidth
    }

    /**
     * 指定位置的控件可滚动的距离
     *
     * @param index   指定位置的控件
     * @param toRight 是否是点击了屏幕右侧
     * @return 可滚动的距离
     */
    private fun scrollWidth(index: Int, toRight: Boolean): Int {
        val target = findTab(infoList[index]) ?: return 0
        val rect = Rect()
        target.getLocalVisibleRect(rect)
        return if (toRight) { //点击屏幕右侧
            if (rect.right > tabWith!!) { //right坐标大于控件的宽度时，说明完全没有显示
                tabWith!!
            } else { //显示部分，减去已显示的宽度
                tabWith!! - rect.right
            }
        } else {
            if (rect.left <= -tabWith!!) { //left坐标小于等于-控件的宽度，说明完全没有显示
                return tabWith!!
            } else if (rect.left > 0) { //显示部分
                return rect.left
            }
            0
        }
    }

    private fun getRootLayout(clear: Boolean): LinearLayout {
        var rootView = getChildAt(0) as LinearLayout?
        if (rootView == null) {
            rootView = LinearLayout(context)
            rootView.orientation = LinearLayout.HORIZONTAL
            val layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            addView(rootView, layoutParams)
        } else if (clear) {
            rootView.removeAllViews()
        }
        return rootView
    }
}