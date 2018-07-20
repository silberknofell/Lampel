package net.geihe.lampel.ampel

import net.geihe.lampel.Lautstaerkemesser
import java.io.IOException

open class Ampel(private val lautstaerkeMesser: Lautstaerkemesser) : ReadOnlyAmpel() {
    var isMute: Boolean = false

    init {
        isMute = false
        zustand = Zustand.GRUEN
        setzeStatistikZurueck()
    }


    fun update() {
        if (istAktiv) {
            if (isMute) {
                lautstaerke = 0.0
            } else {
                lautstaerke = lautstaerkeMesser.sqrtAmplitudeEMA
            }
            aktualisiereZustand()
        }
    }

    fun setzeStatistikZurueck() {
        statistik.values.forEach { stat -> stat.init() }
    }

    fun toggleMute() {
        isMute = !isMute
    }

    open fun start() {
        istAktiv = true
        letzterZustand = Zustand.START
        lautstaerke = 0.0
        try {
            lautstaerkeMesser.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    open fun stop() {
        statistik[zustand]!!.verlasseZustand(System.currentTimeMillis())
        lautstaerkeMesser.stop()
        istAktiv = false
    }
}
