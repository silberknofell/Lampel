package net.geihe.lampel.alarm

import net.geihe.lampel.ampel.ReadOnlyAmpel
import net.geihe.lampel.preferences.AppPreferences

/**
 * Created by test on 07.10.2015.
 */
class AlarmManagerCountdown(ampel: ReadOnlyAmpel, alarmListener: AlarmListener) : AlarmManager(ampel, alarmListener) {
    private val countDownZeit: Long

    override val isAlarm: Boolean
        get() = countDownZeit <= ampel.gruenzeit()

    init {
        this.countDownZeit = AppPreferences.countdownZeit
    }
}
