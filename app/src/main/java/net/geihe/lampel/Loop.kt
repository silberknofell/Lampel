package net.geihe.lampel
import android.os.Handler

interface Updater {
    fun update()
}

/**
 * Created by test on 27.09.2015.
 */
class Loop(private val updater: Updater) : Runnable {


    private val handler: Handler = Handler()
    private var abfrageIntervall = 100

    override fun run() {
        updater.update()
        handler.postDelayed(this, abfrageIntervall.toLong())
    }

    fun start() {
//        abfrageIntervall = PrefHelper.abfrageIntervall
        handler.post(this)
    }

    fun stop() {
        handler.removeCallbacks(this)
    }

}
