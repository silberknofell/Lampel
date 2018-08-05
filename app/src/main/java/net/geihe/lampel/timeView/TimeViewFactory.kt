package net.geihe.lampel.timeView

import android.content.Context
import android.view.ViewGroup
import net.geihe.lampel.AmpelViewActivity
import net.geihe.lampel.Mode
import net.geihe.lampel.ampel.ReadOnlyAmpel
import java.util.*

/**
 * Created by test on 05.10.2015.
 */
class TimeViewFactory(private val context: AmpelViewActivity,
                      private val vgSmall: ViewGroup, private val vgLarge: ViewGroup, private val ampel: ReadOnlyAmpel) {

    fun createTimeViewList(mode: Mode, timeViewManager: TimeViewManager): List<TimeView> {
        vgSmall.removeAllViews()
        vgLarge.removeAllViews()
        val list = ArrayList<TimeView>()
        when (mode) {
            Mode.SIMPLE_VIEW -> {
                list.add(TimeViewGruen(context, vgSmall, vgLarge, ampel))
                list.add(TimeViewGelb(context, vgSmall, vgLarge, ampel))
                list.add(TimeViewRot(context, vgSmall, vgLarge, ampel))
            }
            Mode.ALARM -> {
                list.add(TimeViewAlarm(context, vgSmall, vgLarge, ampel))
                list.add(TimeViewAlarmRestzeit(context, vgSmall, vgLarge, ampel))
            }
            Mode.SIMPLE_COUNTDOWN -> {
                list.add(TimeViewCountDownSimple(context, vgSmall, vgLarge, ampel))
                list.add(TimeViewGruen(context, vgSmall, vgLarge, ampel))
            }
        }
        return list
    }

}
