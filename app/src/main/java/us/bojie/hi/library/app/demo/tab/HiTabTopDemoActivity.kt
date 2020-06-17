package us.bojie.hi.library.app.demo.tab

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import us.bojie.hi.library.app.R
import us.bojie.hi.library.app.databinding.ActivityHiTabTopDemoBinding
import us.bojie.hi.ui.tab.top.HiTabTopInfo
import us.bojie.hi.ui.tab.top.HiTabTopLayout
import java.util.*

class HiTabTopDemoActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityHiTabTopDemoBinding
    var tabsStr = arrayOf(
        "热门",
        "服装",
        "数码",
        "鞋子",
        "零食",
        "家电",
        "汽车",
        "百货",
        "家居",
        "装修",
        "运动"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHiTabTopDemoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initTabTop()
    }

    private fun initTabTop() {
        val hiTabTopLayout: HiTabTopLayout = mBinding.tabTopLayout
        val infoList: MutableList<HiTabTopInfo<*>> = ArrayList()
        val defaultColor = resources.getColor(R.color.tabBottomDefaultColor)
        val tintColor = resources.getColor(R.color.tabBottomTintColor)
        for (s in tabsStr) {
            val info: HiTabTopInfo<*> = HiTabTopInfo(s, defaultColor, tintColor)
            infoList.add(info)
        }
        hiTabTopLayout.apply {
            inflateInfo(infoList)
            addTabSelectedChangeListener { _, _, nextInfo ->
                Toast.makeText(context, nextInfo.name, Toast.LENGTH_SHORT).show()
            }
            defaultSelected(infoList[0])
        }
    }
}