package us.bojie.hi.library.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import us.bojie.hi.library.app.databinding.ActivityMainBinding
import us.bojie.hi.library.app.demo.HiLogDemoActivity
import us.bojie.hi.library.app.demo.refresh.HiRefreshDemoActivity
import us.bojie.hi.library.app.demo.tab.HiTabBottomDemoActivity
import us.bojie.hi.library.app.demo.tab.HiTabTopDemoActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_hilog -> {
                startActivity(Intent(this, HiLogDemoActivity::class.java))
            }
            R.id.tv_tab_bottom -> {
                startActivity(Intent(this, HiTabBottomDemoActivity::class.java))
            }
            R.id.tv_tab_top -> {
                startActivity(Intent(this, HiTabTopDemoActivity::class.java))
            }
            R.id.tv_refresh -> {
                startActivity(Intent(this, HiRefreshDemoActivity::class.java))
            }
        }
    }
}