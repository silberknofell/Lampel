package net.geihe.lampel.timeView

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import net.geihe.lampel.AmpelViewActivity
import net.geihe.lampel.R
import net.geihe.lampel.ampel.ReadOnlyAmpel

/**
 * Created by test on 04.10.2015.
 */
class TimeViewAlarmRestzeit(context: AmpelViewActivity, vgSmall: ViewGroup, vgLarge: ViewGroup, ampel: ReadOnlyAmpel)
    : TimeViewAlarm(context, vgSmall, vgLarge, ampel) {
    init {
        color = ContextCompat.getColor(context, R.color.timeview_Countdown);
    }

    override val tag = "alarm_restzeit"
    override val text = "Rest"
    override fun time() = TimeView.zeitString(alarmZeit - ampel.rotzeit())
}
