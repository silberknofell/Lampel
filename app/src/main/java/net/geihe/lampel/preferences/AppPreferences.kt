package net.geihe.lampel.preferences


import android.content.Context
import android.content.SharedPreferences
import net.geihe.lampel.Mode
import net.geihe.lampel.ampel.Zustand
import net.geihe.lampel.ampel.ZustandsStatistik

/**
 * @author Antonina
 */
object AppPreferences {
    private const val NAME = "Lampel"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    // list of app specific preferences
    private val IS_FIRST_RUN_PREF = Pair("is_first_run", false)
    private val ABFRAGE_INTERVALL = Pair("abfrageIntervall", "200")
    private val ALARM_TON = Pair("ringtone", null)
    private val SKALA_ANZEIGEN = Pair("skala_anzeigen", false)

    private val PADDING_LEFT_RIGHT = Pair("padding_left_right", 100)

    private val ALARM_ZEIT = Pair("alarm_zeit", 30 * 1000L)
    private val COUNTDOWN_ZEIT = Pair("countdown_zeit", 15 * 60 * 1000L)

    private val TIMEVIEW_PREFIX = "tv_"


    private val GRENZE_GRUEN = Pair("grenzeGruen", 0.25)
    private val GRENZE_GELB = Pair("grenzeGelb", 0.5)
    private val ZUSTAND_GRUEN_STRING = Pair("zustand_gruen", ZustandsStatistik.LEER_STRING)
    private val ZUSTAND_GELB_STRING = Pair("zustand_gelb", ZustandsStatistik.LEER_STRING)
    private val ZUSTAND_ROT_STRING = Pair("zustand_rot", ZustandsStatistik.LEER_STRING)

    private val VIEW_MODE = Pair("view_mode", Mode.SIMPLE_VIEW.toString())

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    private fun SharedPreferences.Editor.putDouble(key: String, double: Double) =
            putLong(key, java.lang.Double.doubleToRawLongBits(double))

    private fun SharedPreferences.getDouble(key: String, default: Double) =
            java.lang.Double.longBitsToDouble(getLong(key, java.lang.Double.doubleToRawLongBits(default)))

    var firstRun: Boolean
    // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getBoolean(IS_FIRST_RUN_PREF.first, IS_FIRST_RUN_PREF.second)

    // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putBoolean(IS_FIRST_RUN_PREF.first, value)
        }

    var abfrageIntervall: Int
        get() = preferences.getString(ABFRAGE_INTERVALL.first, ABFRAGE_INTERVALL.second).toInt()
        set(value) = preferences.edit {
            it.putString(ABFRAGE_INTERVALL.first, value.toString())
        }
    var alarmTon: String
        get() = preferences.getString(ALARM_TON.first, ALARM_TON.second)
        set(value) = preferences.edit {
            it.putString(ALARM_TON.first, value)
        }
    var skalaAnzeigen: Boolean
        get() = preferences.getBoolean(SKALA_ANZEIGEN.first, SKALA_ANZEIGEN.second)
        set(value) = preferences.edit {
            it.putBoolean(SKALA_ANZEIGEN.first, value)
        }
    var paddingLeftRight: Int
        get() = preferences.getInt(PADDING_LEFT_RIGHT.first, PADDING_LEFT_RIGHT.second)
        set(value) = preferences.edit {
            it.putInt(PADDING_LEFT_RIGHT.first, value)
        }
    var alarmZeit: Long
        get() = preferences.getLong(ALARM_ZEIT.first, ALARM_ZEIT.second)
        set(value) = preferences.edit {
            it.putLong(ALARM_ZEIT.first, value)
        }
    var countdownZeit: Long
        get() = preferences.getLong(COUNTDOWN_ZEIT.first, COUNTDOWN_ZEIT.second)
        set(value) = preferences.edit {
            it.putLong(COUNTDOWN_ZEIT.first, value)
        }
    var grenzeGruen: Double
        get() = preferences.getDouble(GRENZE_GRUEN.first, GRENZE_GRUEN.second)
        set(value) = preferences.edit {
            it.putDouble(GRENZE_GRUEN.first, value)
        }
    var grenzeGelb: Double
        get() = preferences.getDouble(GRENZE_GELB.first, GRENZE_GELB.second)
        set(value) = preferences.edit {
            it.putDouble(GRENZE_GELB.first, value)
        }
    var zustandGruenString: String
        get() = preferences.getString(ZUSTAND_GRUEN_STRING.first, ZUSTAND_GRUEN_STRING.second)
        set(value) = preferences.edit {
            it.putString(ZUSTAND_GRUEN_STRING.first, value)
        }
    var zustandGelbString: String
        get() = preferences.getString(ZUSTAND_GELB_STRING.first, ZUSTAND_GELB_STRING.second)
        set(value) = preferences.edit {
            it.putString(ZUSTAND_GELB_STRING.first, value)
        }
    var zustandRotString: String
        get() = preferences.getString(ZUSTAND_ROT_STRING.first, ZUSTAND_ROT_STRING.second)
        set(value) = preferences.edit {
            it.putString(ZUSTAND_ROT_STRING.first, value)
        }
    var viewMode: Mode
        get() = Mode.valueOf(preferences.getString(VIEW_MODE.first, VIEW_MODE.second))
        set(value) = preferences.edit {
            it.putString(VIEW_MODE.first, value.toString())
        }

    fun isTimeViewLarge(tag: String):Boolean {
        return preferences.getBoolean(TIMEVIEW_PREFIX + tag, false)
    }

    fun setTimeViewLarge(tag: String, isLarge: Boolean) {
        preferences.edit { it.putBoolean(TIMEVIEW_PREFIX + tag, isLarge) }
    }

}