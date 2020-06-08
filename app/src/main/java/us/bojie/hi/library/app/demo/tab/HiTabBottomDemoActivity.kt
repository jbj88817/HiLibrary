package us.bojie.hi.library.app.demo.tab

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import us.bojie.hi.library.app.R
import us.bojie.hi.library.app.databinding.ActivityHiTabBottomDemoBinding
import us.bojie.hi.library.util.HiDisplayUtil
import us.bojie.hi.ui.tab.bottom.HiTabBottomInfo

class HiTabBottomDemoActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityHiTabBottomDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHiTabBottomDemoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initTabBottom()
    }

    private fun initTabBottom() {
        mBinding.hitablayout.setTabAlpha(0.85f)
        val bottomInfoList: MutableList<HiTabBottomInfo<*>> = ArrayList()
        val homeInfo = HiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val infoRecommend = HiTabBottomInfo(
            "收藏",
            "fonts/iconfont.ttf",
            getString(R.string.if_favorite),
            null,
            "#ff656667",
            "#ffd44949"
        )
//        val infoCategory = HiTabBottomInfo(
//            "分类",
//            "fonts/iconfont.ttf",
//            getString(R.string.if_category),
//            null,
//            "#ff656667",
//            "#ffd44949"
//        )

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire, null)

        val infoCategory = HiTabBottomInfo<String>(
            "分类",
            bitmap,
            bitmap
        )
        val infoChat = HiTabBottomInfo(
            "推荐",
            "fonts/iconfont.ttf",
            getString(R.string.if_recommend),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val infoProfile = HiTabBottomInfo(
            "我的",
            "fonts/iconfont.ttf",
            getString(R.string.if_profile),
            null,
            "#ff656667",
            "#ffd44949"
        )
        bottomInfoList.add(homeInfo)
        bottomInfoList.add(infoRecommend)
        bottomInfoList.add(infoCategory)
        bottomInfoList.add(infoChat)
        bottomInfoList.add(infoProfile)
        mBinding.hitablayout.inflateInfo(bottomInfoList)
        mBinding.hitablayout.addTabSelectedChangeListener { _, _, nextInfo ->
            Toast.makeText(this@HiTabBottomDemoActivity, nextInfo.name, Toast.LENGTH_SHORT).show()
        }
        mBinding.hitablayout.defaultSelected(homeInfo)
        val tabBottom = mBinding.hitablayout.findTab(bottomInfoList[2])
        tabBottom?.apply { resetHeight(HiDisplayUtil.dp2px(66f, resources)) }
    }
}