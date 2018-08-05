package net.geihe.lampel.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.view.ContextThemeWrapper
import android.widget.*
import net.geihe.lampel.AmpelViewActivity
import net.geihe.lampel.Mode

import net.geihe.lampel.R
import net.geihe.lampel.preferences.AppPreferences

class SelectSecondsDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ampelActivity = (activity as AmpelViewActivity)
        val builder: AlertDialog.Builder = AlertDialog.Builder(ampelActivity)
        val inflater = activity?.layoutInflater
        val mView = inflater?.inflate(R.layout.my_time_picker_seconds, null)!!

        val linLayout = mView.findViewById(R.id.layoutChooseSeconds) as LinearLayout
        val cw = ContextThemeWrapper(activity, R.style.NumberPickerText)
        val numberPicker = NumberPicker(cw)
        linLayout.addView(numberPicker)
        numberPicker.minValue = 0
        numberPicker.maxValue = 60
        numberPicker.value = 5
        numberPicker.wrapSelectorWheel = false

        builder.setView(mView)
        builder.setPositiveButton("starten")
        { _, _ ->
            val time = numberPicker.value  * 1000
            AppPreferences.alarmZeit = time.toLong()
            ampelActivity.onModeSelected(Mode.ALARM)
            dismiss()
        }
        return builder.create()
    }


}


