package us.bojie.hi.library.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import us.bojie.hi.library.app.databinding.ActivityMainBinding
import us.bojie.hi.library.app.demo.HiLogDemoActivity
import us.bojie.hi.tab.bottom.HiTabBottomInfo

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val homeInfo = HiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#FF656667",
            "#FFD44949"
        )
        mBinding.tabBottom.setHiTabInfo(homeInfo)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_hilog -> {
                startActivity(Intent(this, HiLogDemoActivity::class.java))
            }
        }
    }
}