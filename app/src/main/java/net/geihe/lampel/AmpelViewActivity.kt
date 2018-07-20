package net.geihe.lampel

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_ampel_view.*
import net.geihe.lampel.ampel.Ampel


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class AmpelViewActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback, Updater {
    val loop = Loop(this)
    lateinit var ampel: Ampel
    val REQUEST_RECORD_AUDIO_PERMISSION = 200

    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION && grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init()
        } else {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Requesting permission to RECORD_AUDIO
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            init()
        }
    }

    private fun init() {
        setContentView(R.layout.activity_ampel_view)
        ampel = Ampel(Lautstaerkemesser())
        ampel_view.ampel = ampel
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ampel.start()
        loop.start()
    }

    override fun update() {
        ampel.update()
        ampel_view.invalidate();
    }


}
