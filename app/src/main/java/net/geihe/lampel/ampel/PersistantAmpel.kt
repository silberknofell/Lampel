package net.geihe.lampel.ampel

import net.geihe.lampel.Lautstaerkemesser
import net.geihe.lampel.preferences.AppPreferences

class PersistantAmpel(lautstaerkeMesser: Lautstaerkemesser, private val appPrefs: AppPreferences) : Ampel(lautstaerkeMesser) {


    fun saveState() {
        appPrefs.grenzeGruen = p_grenzeGruen
        appPrefs.grenzeGelb = p_grenzeGelb
        appPrefs.zustandGruenString = statistik[Zustand.GRUEN].toString()
        appPrefs.zustandGelbString = statistik[Zustand.GELB].toString()
        appPrefs.zustandRotString = statistik[Zustand.ROT].toString()
    }

    fun readState() {
        p_grenzeGruen = appPrefs.grenzeGruen
        p_grenzeGelb = appPrefs.grenzeGelb

        val stringGruen = appPrefs.zustandGruenString
        val stringGelb = appPrefs.zustandGelbString
        val stringRot = appPrefs.zustandRotString
        statistik[Zustand.GRUEN]?.readFromString(stringGruen)
        statistik[Zustand.GELB]?.readFromString(stringGelb)
        statistik[Zustand.ROT]?.readFromString(stringRot)
    }

    override fun start() {
        super.start()
        readState()
        aktualisiereZustand()
    }

    override fun stop() {
        super.stop()
        saveState()
    }
}
