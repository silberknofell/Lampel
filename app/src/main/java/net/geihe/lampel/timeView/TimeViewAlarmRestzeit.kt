package net.geihe.lampel.timeView

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import net.geihe.lampel.R
import net.geihe.lampel.ampel.Ampel
import net.geihe.lampel.ampel.ReadOnlyAmpel
import net.geihe.lampel.ampel.Zustand
import net.geihe.lampel.preferences.PrefHelper

/**
 * Created by test on 04.10.2015.
 */
class TimeViewAlarmRestzeit(context: Context, vgSmall: ViewGroup, vgLarge: ViewGroup, ampel: ReadOnlyAmpel)
    : TimeViewAlarm(context, vgSmall, vgLarge, ampel) {
    init {
        color = ContextCompat.getColor(context, R.color.timeview_Red);
    }

    override val tag = "alarm_restzeit"
    override val text = "Rest"
    override fun time() = TimeView.zeitString(ampel.rotzeit() - alarmZeit)
}
