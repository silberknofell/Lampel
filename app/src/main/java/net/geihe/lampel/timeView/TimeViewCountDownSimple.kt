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
class TimeViewCountDownSimple(context: AmpelViewActivity, vgSmall: ViewGroup, vgLarge: ViewGroup, ampel: ReadOnlyAmpel) : TimeView(context, vgSmall, vgLarge, ampel) {
    private val countDownStart: Long = AppPreferences.countdownZeit

    override val tag = "countdown_simple"
    override val text = "Countdown"
    override fun time() = TimeView.zeitString(countDownStart - ampel.gruenzeit())

    init {
        color = ContextCompat.getColor(context, R.color.timeview_Countdown);
    }
}
