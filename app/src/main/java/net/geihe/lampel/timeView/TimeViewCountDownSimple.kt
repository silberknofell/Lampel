package net.geihe.lampel.timeView

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import net.geihe.lampel.R
import net.geihe.lampel.ampel.ReadOnlyAmpel
import net.geihe.lampel.ampel.Zustand
import net.geihe.lampel.preferences.PrefHelper

/**
 * Created by test on 04.10.2015.
 */
class TimeViewCountDownSimple(context: Context, vgSmall: ViewGroup, vgLarge: ViewGroup, ampel: ReadOnlyAmpel) : TimeView(context, vgSmall, vgLarge, ampel) {
    private val countDownStart: Long = PrefHelper.countdownStart

    override val tag = "countdown_simple"
    override val text = "Countdown"
    override fun time() = TimeView.zeitString(countDownStart - ampel.gruenzeit())

    init {
        color = ContextCompat.getColor(context, R.color.timeview_Countdown);
    }
}
