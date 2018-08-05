package net.geihe.lampel.timeView

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.geihe.lampel.AmpelViewActivity
import net.geihe.lampel.ampel.ReadOnlyAmpel
import net.geihe.lampel.ampel.Zustand
import net.geihe.lampel.ampel.ZustandsStatistik
import net.geihe.lampel.preferences.AppPreferences

/**
 * Created by test on 19.09.2015.
 */
abstract class TimeView(
        protected val ampelViewActivity: AmpelViewActivity,
        private val vgSmall: ViewGroup,
        private val vgLarge: ViewGroup,
        protected val ampel: ReadOnlyAmpel
) : View.OnClickListener {
    private lateinit var tvKlein: TextView
    private lateinit var tvGross: TextView
    private var isLarge: Boolean = false


    var color: Int = Color.GRAY
        set(color) {
            tvKlein.setTextColor(color)
            tvGross.setTextColor(color)
        }

    protected val content
        get() = " $text \n " + time() + " "

    abstract val tag: String
    abstract val text: String
    abstract fun time(): String

    protected val statistik: Map<Zustand, ZustandsStatistik>
        get() = ampel.statistik

    val textViewSmall: View?
        get() = tvKlein

    init {
        createTextViewSmall(ampelViewActivity)
        createTextViewLarge(ampelViewActivity)
        color = color
    }

    private fun createTextViewLarge(context: Context) {
        tvGross = TextView(context)
        tvGross.textSize = TEXT_SIZE_LARGE.toFloat()
        tvGross.gravity = Gravity.CENTER_HORIZONTAL

        tvGross.setOnClickListener(this)
    }

    private fun createTextViewSmall(context: Context) {
        tvKlein = TextView(context)
        tvKlein.textSize = TEXT_SIZE_SMALL.toFloat()
        tvKlein.gravity = Gravity.CENTER_HORIZONTAL
        tvKlein.setOnClickListener(this)
    }

    fun show() {
        vgSmall.addView(tvKlein)
        isLarge = AppPreferences.isTimeViewLarge(tag)
        if (isLarge) {
            showLarge()
        }
    }

    override fun onClick(v: View) {
        toggleLarge()
    }

    fun update() {
        tvKlein.text = content
        tvGross.text = time()
    }

    private fun showLarge() {
        ampelViewActivity.timeViewManager.hideAllLarge()
        vgLarge.removeAllViews()
        vgLarge.addView(tvGross)
        isLarge = true
        setPref()
    }

    fun hideLarge() {
        vgLarge.removeView(tvGross)
        isLarge = false
        setPref()
    }

    private fun setPref() {
        AppPreferences.setTimeViewLarge(tag, isLarge)
    }

    fun remove() {
        vgLarge.removeAllViews()
        isLarge = false
        vgSmall.removeView(tvKlein)
    }

    fun toggleLarge() {
        if (isLarge) {
            hideLarge()
        } else {
            showLarge()
        }
    }

    private fun largeIsVisible(): Boolean {
        return vgLarge.indexOfChild(tvGross) >= 0
    }

    companion object {

        val TEXT_SIZE_SMALL = 22
        val TEXT_SIZE_LARGE = 111

        fun zeitString(zeitInMS: Long): String {
            val vorzeichen = java.lang.Long.signum(zeitInMS)
            val zeitInS = Math.round(zeitInMS / 1000.0 * vorzeichen) //damit auf jeden Fall positiv
            val minutenString = java.lang.Long.toString(zeitInS / 60)
            val sekunden = zeitInS % 60
            val sekundenString = if (sekunden < 10)
                "0" + java.lang.Long.toString(sekunden)
            else
                java.lang.Long.toString(sekunden)
            val vorzeichenString = if (vorzeichen < 0) "-" else ""
            return "$vorzeichenString$minutenString:$sekundenString"
        }
    }
}
