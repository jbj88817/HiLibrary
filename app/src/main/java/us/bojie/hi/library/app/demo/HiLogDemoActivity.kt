package us.bojie.hi.library.app.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import us.bojie.hi.library.app.R
import us.bojie.hi.library.log.HiLog
import us.bojie.hi.library.log.HiLogConfig
import us.bojie.hi.library.log.HiLogType

class HiLogDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_log_demo)
        findViewById<View>(R.id.btn_log).setOnClickListener {
            printLog()
        }
    }

    private fun printLog() {
        HiLog.log(object :HiLogConfig() {
            override fun includeThread(): Boolean {
                return true
            }

            override fun stackTraceDepth(): Int {
                return 0;
            }
        }, HiLogType.E, "-----", "5566");
        HiLog.a("9900")
    }
}