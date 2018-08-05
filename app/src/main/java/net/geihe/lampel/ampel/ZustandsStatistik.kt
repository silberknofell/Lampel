package net.geihe.lampel.ampel

class ZustandsStatistik {

    var anzahlImZustand: Int = 0
        private set
    private var gesamtZeitImZustand: Long = 0
    //in Millisekunden
    var aktuelleZeitImZustand: Long = 0
        private set
    //in Millisekunden
    var laengsteZeitImZustand: Long = 0
        private set
    private var betretenZurZeit: Long = 0

    init {
        init()
    }

    fun getGesamtZeitImZustand(): Long { //in Millisekunden
        return gesamtZeitImZustand + aktuelleZeitImZustand
    }

    fun imZustand(): Boolean {
        return betretenZurZeit >= 0
    }

    fun init() {
        anzahlImZustand = 0
        gesamtZeitImZustand = 0
        aktuelleZeitImZustand = 0
        laengsteZeitImZustand = 0
        betretenZurZeit = -1 // negativ - Zustand nicht aktiv
    }

    fun betreteZustand(zeit: Long) {
        if (!imZustand()) {
            anzahlImZustand++
            betretenZurZeit = zeit
            aktuelleZeitImZustand = 0
        } else {    // Fehler, Zustand schon aktiv
            bleibeInZustand(zeit)
        }
    }

    fun bleibeInZustand(zeit: Long) {
        if (imZustand()) {
            aktuelleZeitImZustand = zeit - betretenZurZeit
            if (aktuelleZeitImZustand > laengsteZeitImZustand) {
                laengsteZeitImZustand = aktuelleZeitImZustand
            }
        } else {
            // Fehler, Zustand wird betreten
            betreteZustand(zeit)
        }

    }

    fun verlasseZustand(zeit: Long) {
        if (imZustand()) {
            bleibeInZustand(zeit)
            gesamtZeitImZustand += aktuelleZeitImZustand
            aktuelleZeitImZustand = 0
            betretenZurZeit = -1
        } else {
            // Fehler, Zustand gar nicht aktiv, tue nichts
        }
    }

    override fun toString(): String {
        return (Integer.toString(anzahlImZustand)
                + ";" + java.lang.Long.toString(gesamtZeitImZustand)
                + ";" + java.lang.Long.toString(aktuelleZeitImZustand)
                + ";" + java.lang.Long.toString(laengsteZeitImZustand)
                + ";" + java.lang.Long.toString(betretenZurZeit))
    }

    fun readFromString(string: String) {
        val values = string.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (values.size == 5) {
            anzahlImZustand = Integer.parseInt(values[0])
            gesamtZeitImZustand = java.lang.Long.parseLong(values[1])
            aktuelleZeitImZustand = java.lang.Long.parseLong(values[2])
            laengsteZeitImZustand = java.lang.Long.parseLong(values[3])
            betretenZurZeit = java.lang.Long.parseLong(values[4])
        }
    }

    companion object {
        val LEER_STRING = "0;0;0;0;0"

    }
}
