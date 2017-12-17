package id.ac.amikom.avent.picker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by dhiyaulhaqza on 12/17/17.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimePickerListener mTimePickerListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mTimePickerListener = (TimePickerListener) getActivity();
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTimePickerListener.onTimeSetListener(getTag(), hourOfDay, minute);
    }
}
