package net.geihe.lampel.timeView

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import net.geihe.lampel.AmpelViewActivity
import net.geihe.lampel.R
import net.geihe.lampel.ampel.ReadOnlyAmpel
import net.geihe.lampel.preferences.AppPreferences

/**
 * Created by test on 04.10.2015.
 */
open class TimeViewAlarm(
        context: AmpelViewActivity,
        vgSmall: ViewGroup,
        vgLarge: ViewGroup,
        ampel: ReadOnlyAmpel
) : TimeView(context, vgSmall, vgLarge, ampel) {
    init {
        color = ContextCompat.getColor(context, R.color.timeview_Red);
    }

    protected val alarmZeit = AppPreferences.alarmZeit

    override val tag = "alarm"
    override val text = "Alarm"
    override fun time() = TimeView.zeitString(alarmZeit)
}
