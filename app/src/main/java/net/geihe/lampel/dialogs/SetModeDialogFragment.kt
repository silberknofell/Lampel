package net.geihe.lampel.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Toast
import net.geihe.lampel.AmpelViewActivity
import net.geihe.lampel.Mode

import net.geihe.lampel.R

class SetModeDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ampelActivity = (activity as AmpelViewActivity)
        val builder: AlertDialog.Builder = AlertDialog.Builder(ampelActivity)

        builder.setTitle("Modus auswählen")
                .setItems(R.array.modi_array) { _, id ->
                    val mode = getModeFromArrayId(id)
                    when (mode) {
                        Mode.SIMPLE_VIEW -> {
                            ampelActivity.onModeSelected(mode)
                        }
                        Mode.SIMPLE_COUNTDOWN -> {
                            ampelActivity.showMinutePickerDialog()
                        }
                        Mode.ALARM -> {
                            ampelActivity.showSecondPickerDialog()
                        }
                    }
                    dismiss()
                }
                .setNegativeButton("Abbrechen")
                { _, _ -> Toast.makeText(activity, "weiter geht's", Toast.LENGTH_SHORT).show() }
                .setNeutralButton("Ampel beenden")
                { _, _ -> activity?.finish() }
        return builder.create()
    }

    private fun getModeFromArrayId(id: Int): Mode {
        return when (id) {
            1 -> Mode.ALARM
            2 -> Mode.SIMPLE_COUNTDOWN
            else -> Mode.SIMPLE_VIEW
        }
    }

    private fun getArrayIdFromMode(mode: Mode): Int {
        return when (mode) {
            Mode.SIMPLE_VIEW -> 0
            Mode.ALARM -> 1
            Mode.SIMPLE_COUNTDOWN -> 2
        }
    }
}