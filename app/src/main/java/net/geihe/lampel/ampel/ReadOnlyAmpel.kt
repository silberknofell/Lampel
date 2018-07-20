package net.geihe.lampel.ampel

/**
 * Created by test on 03.10.2015.
 */
enum class Zustand{
    START, ROT, GELB, GRUEN
}

abstract class ReadOnlyAmpel {

    protected var p_grenzeGelb = 0.6
    protected var p_grenzeGruen = 0.1

    protected var istAktiv: Boolean = false

    var lautstaerke = 0.0

    protected var letzterZustand = Zustand.START

    var statistik = mapOf(
            Zustand.ROT to ZustandsStatistik(),
            Zustand.GELB to ZustandsStatistik(),
            Zustand.GRUEN to ZustandsStatistik()
    )
    var zustand = Zustand.START

    var grenzeGelb
        get() = p_grenzeGelb
        set(grenzeGelb) {
            if (p_grenzeGruen < grenzeGelb) {
                this.p_grenzeGelb = grenzeGelb
                aktualisiereZustand()
            }
        }

    var grenzeGruen
        get() = p_grenzeGruen
        set(grenzeGruen) {
            if (grenzeGruen < p_grenzeGelb) {
                this.p_grenzeGruen = grenzeGruen
                aktualisiereZustand()
            }
        }

    fun gruenzeit() = statistik[Zustand.GRUEN]!!.getGesamtZeitImZustand()
    fun gelbzeit() = statistik[Zustand.GELB]!!.getGesamtZeitImZustand()
    fun rotzeit() = statistik[Zustand.ROT]!!.getGesamtZeitImZustand()
    fun lautzeit() = gelbzeit() + rotzeit()

    protected fun aktualisiereZustand() {

        if (istAktiv) {
            val zeit = System.currentTimeMillis()
            if (lautstaerke < grenzeGruen) {
                zustand = Zustand.GRUEN
            } else if (lautstaerke < grenzeGelb) {
                zustand = Zustand.GELB
            } else {
                zustand = Zustand.ROT
            }
            if (letzterZustand == zustand) {
                statistik[zustand]!!.bleibeInZustand(zeit)
            } else { // Zustand gewechselt

                statistik[zustand]!!.betreteZustand(zeit)
                if (letzterZustand  != Zustand.START) {
                    statistik[letzterZustand]!!.verlasseZustand(zeit)
                }
                letzterZustand = zustand
            }
        }

    }

    fun istAktiv(): Boolean {
        return istAktiv
    }
}

