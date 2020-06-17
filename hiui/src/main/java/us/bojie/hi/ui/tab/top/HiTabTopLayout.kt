package us.bojie.hi.ui.tab.top

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import us.bojie.hi.ui.tab.common.IHiTabLayout
import java.util.*

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