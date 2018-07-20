package net.geihe.lampel.preferences

import android.content.SharedPreferences
import net.geihe.lampel.Mode
import net.geihe.lampel.preferences.PrefHelper.prefs


/**
 * Created by test on 09.10.2015.
 */
object PrefHelper {

    val ABFRAGE_INTERVALL = "abfrageIntervall"
    val ALARM_BLINKER = "alarmBlinker"
    val ALARM_TON = "alarmTon"
    val RINGTONE = "ringtone"
    val SKALA_ANZEIGEN = "skalaAnzeigen"
    val PADDING_LEFT_RIGHT = "padding_left_right"

    val TIMEVIEW_PREFIX = "tv_"

    val COUNTDOWN_ZEIT = "countdownZeit"
    val ENDZEIT = "endzeit"
    val EXTRAZEIT = "extrazeit"

    var prefs: SharedPreferences? = null

    val editor: SharedPreferences.Editor?
        get() = prefs?.edit()

    val abfrageIntervall: Int
        get() {
            val ai = prefs!!.getString(ABFRAGE_INTERVALL, "100")
            return Integer.parseInt(ai)
        }

    val isAlarmBlinker: Boolean
        get() = prefs!!.getBoolean(ALARM_BLINKER, true)

    val isAlarmSound: Boolean
        get() = prefs!!.getBoolean(
                ALARM_TON, true)

    val ringTone: String?
        get() = prefs!!.getString(RINGTONE, null)

    val isSkalaVisible: Boolean
        get() = prefs!!.getBoolean(SKALA_ANZEIGEN, true)

    val countdownStart: Long
        get() {
            val cds = prefs!!.getString(COUNTDOWN_ZEIT, "300000")
            return str2ms(cds)
        }

    val endzeit: Long
        get() {
            val cds = prefs!!.getString(ENDZEIT, "47400000")
            return str2ms(cds)
        }

    val extrazeit: Long
        get() {
            val cds = prefs!!.getString(EXTRAZEIT, "30000")
            return str2ms(cds)
        }
    val alarmzeit: Long
        get() {
            val cds = prefs!!.getString("alarmZeit", "20000")
            return str2ms(cds)
        }

    var mode: Mode=Mode.SIMPLE_VIEW
    get() = Mode.SIMPLE_VIEW
/*        get() = prefs!!.getInt(Mode.KEY, Mode.SIMPLE_VIEW)
        set(modeIndex) {
            val editor = editor
            if (editor != null) {
                editor.putInt(Mode.KEY, modeIndex)
                editor.apply()
            }
        }*/

    fun hasPrefs(): Boolean {
        return prefs != null
    }

    fun getPaddingLeftRight(vorgabe: Int): Int {
        return prefs!!.getInt(
                PADDING_LEFT_RIGHT, vorgabe)
    }

    fun isTimeViewLarge(tag: String): Boolean {
        val key = TIMEVIEW_PREFIX + tag
        return prefs!!.getBoolean(key, false)
    }

    fun setTimeViewLarge(tag: String, isLarge: Boolean) {
        val key = TIMEVIEW_PREFIX + tag
        val editor = editor

        if (editor != null) {
            editor.putBoolean(key, isLarge)
            editor.apply()
        }
    }

    fun ms2min(ms: Long): Long {
        return ms / 1000 / 60
    }

    fun min2ms(min: Long): Long {
        return min * 60 * 1000
    }

    fun minstr2ms(cds: String): Long {
        return min2ms(java.lang.Long.parseLong(cds))
    }

    fun str2ms(cds: String): Long {
        return java.lang.Long.parseLong(cds)
    }

    fun min2str(min: Long): String {
        return ms2str(min2ms(min))
    }

    fun ms2str(ms: Long): String {
        return java.lang.Long.toString(ms)
    }

    fun ms2minstr(ms: Long): String {
        return java.lang.Long.toString(ms2min(ms))
    }

    fun readMinStr(key: String, defaultMin: Long): String {
        val defString = min2str(defaultMin)
        val str = prefs!!.getString(key, defString)
        val ms = str2ms(str)
        return ms2minstr(ms)
    }
}
