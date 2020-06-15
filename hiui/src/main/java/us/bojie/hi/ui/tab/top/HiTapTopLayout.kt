package us.bojie.hi.ui.tab.top

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import us.bojie.hi.ui.tab.common.IHiTabLayout
import java.util.*

class HiTapTopLayout(context: Context?, attrs: AttributeSet?) : HorizontalScrollView(
    context,
    attrs
), IHiTabLayout<HiTabTop, HiTabTopInfo<*>> {

    private val tabSelectedChangeListeners: MutableList<IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<*>>> =
        ArrayList()
    private lateinit var selectedInfo: HiTabTopInfo<*>
    private lateinit var infoList: List<HiTabTopInfo<*>>

    override fun findTab(data: HiTabTopInfo<*>): HiTabTop {
        TODO("Not yet implemented")
    }

    override fun addTabSelectedChangeListener(listener: IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<*>>?) {
        TODO("Not yet implemented")
    }

    override fun defaultSelected(defaultInfo: HiTabTopInfo<*>) {
        TODO("Not yet implemented")
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