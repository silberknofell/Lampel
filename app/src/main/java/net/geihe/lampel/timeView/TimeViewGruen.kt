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
class TimeViewGruen(context: AmpelViewActivity, vgSmall: ViewGroup, vgLarge: ViewGroup, ampel: ReadOnlyAmpel) : TimeView(context, vgSmall, vgLarge, ampel) {
    init {
        color =  ContextCompat.getColor(context, R.color.timeview_Green)
    }
    override val tag = "gruen"
    override val text = "Grünzeit"
    override fun time() = TimeView.zeitString(ampel.gruenzeit())
}
