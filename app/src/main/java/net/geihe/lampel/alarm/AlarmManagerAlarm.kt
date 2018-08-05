package net.geihe.lampel.alarm

import net.geihe.lampel.ampel.ReadOnlyAmpel
import net.geihe.lampel.preferences.AppPreferences

/**
 * Created by test on 07.10.2015.
 */
class AlarmManagerAlarm(ampel: ReadOnlyAmpel, alarmListener: AlarmListener) : AlarmManager(ampel, alarmListener) {

    var alarmZeitInMS: Long = 0

    override val isAlarm: Boolean
        get() = ampel.rotzeit() >= alarmZeitInMS

    init {
        alarmZeitInMS = AppPreferences.alarmZeit
    }
}
