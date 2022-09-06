package `in`.singhangad.compose_ui.utils

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.util.*

class DatePickerDialogFactory {

    companion object {
        fun create(context: Context, block: (mYear: Int, mMonth: Int, mDayOfMonth: Int) -> Unit): DatePickerDialog {
            val mYear: Int
            val mMonth: Int
            val mDay: Int

            val mCalendar = Calendar.getInstance()

            // Fetching current year, month and day
            mYear = mCalendar.get(Calendar.YEAR)
            mMonth = mCalendar.get(Calendar.MONTH)
            mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

            mCalendar.time = Date()

            val mDatePickerDialog = DatePickerDialog(
                context,
                { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                    block(mYear, mMonth, mDayOfMonth)
                }, mYear, mMonth, mDay
            )

            return mDatePickerDialog
        }
    }
}