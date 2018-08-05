package net.geihe.lampel.timeView

import net.geihe.lampel.Mode
import java.util.*

class TimeViewManager(private val timeViewFactory: TimeViewFactory) {
    private var viewsList: List<TimeView> = listOf()

    fun createViews(mode: Mode) {
        viewsList.forEach { v -> v.remove() }
        viewsList = timeViewFactory.createTimeViewList(mode, this)
        viewsList.forEach { v -> v.show() }
    }

    fun update() {
        viewsList.forEach { v -> v.update() }
    }

    fun hideAllLarge() {
        viewsList.forEach { v -> v.hideLarge() }
    }
}
