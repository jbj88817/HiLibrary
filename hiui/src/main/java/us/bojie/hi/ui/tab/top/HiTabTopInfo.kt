package us.bojie.hi.ui.tab.top

import android.graphics.Bitmap
import androidx.fragment.app.Fragment

class HiTabTopInfo<Color> {
    enum class TabType {
        BITMAP, TEXT
    }

    var fragment: Class<out Fragment?>? = null
    var name: String
    var defaultBitmap: Bitmap? = null
    var selectedBitmap: Bitmap? = null
    var defaultColor: Color? = null
    var tintColor: Color? = null
    var tabType: TabType

    constructor(name: String, defaultBitmap: Bitmap?, selectedBitmap: Bitmap?) {
        this.name = name
        this.defaultBitmap = defaultBitmap
        this.selectedBitmap = selectedBitmap
        tabType = TabType.BITMAP
    }

    constructor(name: String, defaultColor: Color, tintColor: Color) {
        this.name = name
        this.defaultColor = defaultColor
        this.tintColor = tintColor
        tabType = TabType.TEXT
    }
}