package net.geihe.lampel.alarm

import net.geihe.lampel.Mode
import net.geihe.lampel.ampel.ReadOnlyAmpel

/**
 * Created by test on 07.10.2015.
 */
class AlarmManagerFactory(private val ampel: ReadOnlyAmpel, private val alarmListener: AlarmListener) {

    fun create(mode: Mode): AlarmManager {
        when (mode) {
            Mode.ALARM -> return AlarmManagerAlarm(ampel, alarmListener)
            Mode.SIMPLE_COUNTDOWN -> return AlarmManagerCountdown(ampel, alarmListener)
            else -> return AlarmManagerSimpleView(ampel, alarmListener)
        }
    }
}