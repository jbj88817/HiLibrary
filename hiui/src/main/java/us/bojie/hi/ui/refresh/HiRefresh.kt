package us.bojie.hi.ui.refresh

interface HiRefresh {
    fun setDisableRefreshScroll(disable: Boolean)
    fun refreshFinished()
    fun setRefreshListener(listener: HiRefreshListener)
    fun setRefreshOverView(hiOverView: HiOverView)
    interface HiRefreshListener {
        fun onRefresh()
        fun enableRefresh(): Boolean
    }
}