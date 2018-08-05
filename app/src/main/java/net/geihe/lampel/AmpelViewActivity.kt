package net.geihe.lampel

import android.Manifest
import android.content.pm.PackageManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_ampel_view.*
import net.geihe.lampel.alarm.AlarmListener
import net.geihe.lampel.alarm.AlarmManager
import net.geihe.lampel.alarm.AlarmManagerFactory
import net.geihe.lampel.ampel.PersistantAmpel
import net.geihe.lampel.dialogs.SelectMinutesDialogFragment
import net.geihe.lampel.dialogs.SelectSecondsDialogFragment
import net.geihe.lampel.dialogs.SetModeDialogFragment
import net.geihe.lampel.preferences.AppPreferences
import net.geihe.lampel.timeView.TimeViewFactory
import net.geihe.lampel.timeView.TimeViewManager


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class AmpelViewActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback, Updater, AlarmListener {
    val loop = Loop(this)

    var countdownPause = false
    var isAlarm = false
    lateinit var ringtone: Ringtone

    lateinit var ampel: PersistantAmpel
    lateinit var timeViewManager: TimeViewManager
    lateinit var alarmManagerFactory: AlarmManagerFactory
    lateinit var alarmManager: AlarmManager
    val REQUEST_RECORD_AUDIO_PERMISSION = 200
    var mode = Mode.SIMPLE_COUNTDOWN
        set(value) {
            field = value
            countdownPause = false
            isAlarm = false
            ringtone.stop()
            ampel.setzeStatistikZurueck()
            timeViewManager.createViews(mode)
            alarmManager = alarmManagerFactory.create(field)
        }

    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)

    lateinit var menuActionbar: Menu

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION && grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            myInit()
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
            myInit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        menuActionbar = menu!!

        setIcons()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        ringtone.stop()

        return when (item.itemId) {
            R.id.mic_off, R.id.mic_on -> {
                ampel.toggleMute()
                setIcons()
                true
            }
            R.id.countdown_pause -> {
                ampel.stop()
                countdownPause = true
                setIcons()
                true
            }
            R.id.countdown_resume -> {
                ampel.start()
                countdownPause = false
                setIcons()
                true
            }
            R.id.settings -> {
                val newFragment = SetModeDialogFragment()
                newFragment.show(supportFragmentManager, "mode-select")
                true
            }

            else -> {
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                super.onOptionsItemSelected(item)
            }

        }
    }
    fun onModeSelected(mode: Mode) {
        this.mode = mode
        setIcons()
    }

    public override fun onPause() {
        super.onPause()
        AppPreferences.viewMode = mode
        ampel.stop()
        loop.stop()
        ringtone?.stop()

    }

    public override fun onResume() {
        super.onResume()
        mode = AppPreferences.viewMode
        ampel.start()
        loop.start()
    }

    fun setIcons() {
        when (ampel.isMute) {
            false -> {
                menuActionbar.findItem(R.id.mic_on).setVisible(true)
                menuActionbar.findItem(R.id.mic_off).setVisible(false)
                ampel_view.setBackgroundColor(ContextCompat.getColor(this, R.color.background))
            }
            true -> {
                menuActionbar.findItem(R.id.mic_on).setVisible(false)
                menuActionbar.findItem(R.id.mic_off).setVisible(true)
            }
        }
        if (mode == Mode.SIMPLE_COUNTDOWN) {
            when (countdownPause) {
                true -> {
                    menuActionbar.findItem(R.id.countdown_pause).setVisible(false)
                    menuActionbar.findItem(R.id.countdown_resume).setVisible(true)
                    ampel_view.setBackgroundColor(ContextCompat.getColor(this, R.color.background_mute))
                }
                false -> {
                    menuActionbar.findItem(R.id.countdown_pause).setVisible(true)
                    menuActionbar.findItem(R.id.countdown_resume).setVisible(false)
                }
            }

        } else {
            menuActionbar.findItem(R.id.countdown_pause).setVisible(false)
            menuActionbar.findItem(R.id.countdown_resume).setVisible(false)
        }

        val bgColorRessource = if (isAlarm) R.color.background_alarm
        else if (ampel.isMute || countdownPause) R.color.background_mute
        else R.color.background

        ampel_view.setBackgroundColor(ContextCompat.getColor(this, bgColorRessource))
    }

    private fun myInit() {
        val Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this,Uri)

        AppPreferences.init(this)
        setContentView(R.layout.activity_ampel_view)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        textViewPause.visibility = View.INVISIBLE

        ampel = PersistantAmpel(Lautstaerkemesser(), AppPreferences)
        ampel_view.ampel = ampel

        alarmManagerFactory = AlarmManagerFactory(ampel, this)

        val vgLarge = anzeigeGross as ViewGroup
        val vgSmall = fussZeile as ViewGroup
        val timeViewFactory = TimeViewFactory(this, vgSmall, vgLarge, ampel)
        timeViewManager = TimeViewManager(timeViewFactory)

        mode = AppPreferences.viewMode
        ampel.start()
        loop.start()
    }

    override fun alarm() {
        if (!isAlarm) {
            isAlarm = true
            ampel_view.setBackgroundColor(ContextCompat.getColor(this, R.color.background_alarm))

            ringtone.play()
        }
    }

    fun showMinutePickerDialog() {
        val newFragment = SelectMinutesDialogFragment()
        newFragment.show(supportFragmentManager, "minute-select")
    }

    fun showSecondPickerDialog() {
        val newFragment = SelectSecondsDialogFragment()
        newFragment.show(supportFragmentManager, "second-select")
    }

    override fun update() {
        ampel.update()
        timeViewManager.update()
        alarmManager.checkAlarm()
        ampel_view.invalidate()

    }
}
