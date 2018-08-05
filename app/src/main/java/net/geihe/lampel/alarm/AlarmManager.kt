package net.geihe.lampel.alarm

import net.geihe.lampel.ampel.ReadOnlyAmpel

/**
 * Created by test on 07.10.2015.
 */
abstract class AlarmManager(protected var ampel: ReadOnlyAmpel, protected var alarmListener: AlarmListener) {

    abstract val isAlarm: Boolean

    fun checkAlarm() {
        if (isAlarm) {
            alarmListener.alarm()
        }
    }
}
