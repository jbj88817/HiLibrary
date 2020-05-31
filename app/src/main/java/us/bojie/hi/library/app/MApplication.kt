package us.bojie.hi.library.app

import android.app.Application
import us.bojie.hi.library.log.HiLogConfig
import us.bojie.hi.library.log.HiLogManager

class MApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        HiLogManager.init(object : HiLogConfig() {
            override fun getGlobalTag(): String {
                return "MApplication"
            }

            override fun enable(): Boolean {
                return true
            }
        })
    }
}