package net.geihe.lampel

import android.media.MediaRecorder

import java.io.IOException

class Lautstaerkemesser {
    private var stumm = false
    private var mr: MediaRecorder? = null
    private val p_EMAFaktor = 0.3
    private var ema = 0.0
    private val MAXAMPLITUDE = 32768.0

    private val amplitude: Double
        get() = if (mr != null && stumm == false)
            mr!!.maxAmplitude.toDouble()
        else
            0.0

    val amplitudeEMA: Double
        get() {
            ema = p_EMAFaktor * amplitude + (1.0 - p_EMAFaktor) * ema
            return ema / MAXAMPLITUDE
        }

    val sqrtAmplitudeEMA: Double
        get() = Math.sqrt(amplitudeEMA)

    @Throws(IllegalStateException::class, IOException::class)
    fun start() {
        if (mr == null) {
            mr = MediaRecorder()
            mr!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mr!!.setOutputFile("/dev/null")
            mr!!.prepare()
            mr!!.start()
            ema = 0.0
        }
    }

    fun stop() {
        if (mr != null) {
            mr!!.stop()
            mr!!.reset()
            mr!!.release()
            mr = null
        }
    }

    fun setStumm() {
        stumm = true
    }

    fun setAktiv() {
        stumm = false
    }

    fun toggleStumm() {
        stumm = !stumm
    }
}
