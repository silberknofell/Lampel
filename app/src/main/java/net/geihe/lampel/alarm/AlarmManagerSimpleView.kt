package net.geihe.lampel.alarm

import net.geihe.lampel.ampel.ReadOnlyAmpel

/**
 * Created by test on 07.10.2015.
 */
class AlarmManagerSimpleView(ampel: ReadOnlyAmpel, alarmListener: AlarmListener) : AlarmManager(ampel, alarmListener) {

    override val isAlarm: Boolean
        get() = false

}
