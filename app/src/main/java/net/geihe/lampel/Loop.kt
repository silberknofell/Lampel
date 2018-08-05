package net.geihe.lampel
import android.os.Handler
import net.geihe.lampel.preferences.AppPreferences

interface Updater {
    fun update()
}

/**
 * Created by test on 27.09.2015.
 */
class Loop(private val updater: Updater) : Runnable {


    private val handler: Handler = Handler()
    private var abfrageIntervall = 200

    override fun run() {
        updater.update()
        handler.postDelayed(this, abfrageIntervall.toLong())
    }

    fun start() {
        abfrageIntervall = AppPreferences.abfrageIntervall
        handler.post(this)
    }

    fun stop() {
        handler.removeCallbacks(this)
    }

}
