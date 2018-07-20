package net.geihe.lampel.timeView
import net.geihe.lampel.Mode
import net.geihe.lampel.preferences.PrefHelper
import java.util.*

class TimeViewManager(private val timeViewFactory: TimeViewFactory) {
    protected var viewsMap: HashMap<Mode, List<TimeView>>


    init {
        viewsMap = HashMap()
        for (mode in Mode.values()) {
            createViews(mode)
        }
    }

    fun createViews() {
        val mode = PrefHelper.mode;
        createViews(mode)
    }

    private fun createViews(mode: Mode) {
        removeViews(mode)
        viewsMap[mode] = timeViewFactory.createTimeViewList(mode)
    }

    fun showViews(mode: Mode) {
        showViews(viewsMap[mode])
    }

    private fun showViews(views: List<TimeView>?) {
        if (views != null && views.size > 0) {
            for (tv in views) {
                tv.show()
            }
        }
    }

    fun getTimeViewList(mode: Mode): List<TimeView> {
        return viewsMap[mode]!!
    }

    fun removeViews(mode: Mode) {
        val views = viewsMap[mode]
        if (views != null && views.size > 0) {
            for (tv in views) {
                tv.remove()
            }
        }
    }

    fun update(mode: Mode) {
        viewsMap[mode]?.forEach{ tv -> tv.update()}

    }
}
