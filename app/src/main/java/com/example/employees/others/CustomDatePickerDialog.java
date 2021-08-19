package com.example.employees.others;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;



public class CustomDatePickerDialog extends DatePickerDialog{

        private boolean mIgnoreEvent = false, mignorsunday = true;

        public static int mYear, mMonth, mDate, maxYear, maxMonth, maxDay, minYear,
                minMonth, minDay;
        public static int dateflag = 0;
        public static int dateflag2 = 0;

    /**
     *
     * @param context
     * @param callBack
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @param maxYear
     * @param maxMonth
     * @param maxDay
     */
        public CustomDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener callBack,
                                      int year, int monthOfYear, int dayOfMonth, int maxYear,
                                      int maxMonth, int maxDay) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            // TODO Auto-generated constructor stub
            dateflag = 0;
            dateflag2 = 1;
            mYear = year;
            mMonth = monthOfYear;
            mDate = dayOfMonth;

            //Log.i("Hello World ::", "Please Its Updating At Every time");

            CustomDatePickerDialog.maxYear = maxYear;
            CustomDatePickerDialog.maxMonth = maxMonth;
            CustomDatePickerDialog.maxDay = maxDay;
            minYear = year;
            minMonth = monthOfYear;
            minDay = dayOfMonth;

            setTitle("Select Birth Date");

        }

    /**
     *
     * @param view
     * @param year
     * @param month
     * @param day
     */
        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            // TODO Auto-generated method stub
            super.onDateChanged(view, year, month, day);
            dateflag = 1;
            dateflag2 = 1;
            if (!mIgnoreEvent) {
                mIgnoreEvent = true;
                if (year > maxYear || month > maxMonth && year == maxYear
                        || day > maxDay && year == maxYear && month == maxMonth) {
                    mYear = maxYear;
                    mMonth = maxMonth;
                    mDate = maxDay;
                    view.updateDate(maxYear, maxMonth, maxDay);
                } else if (year < minYear || month < minMonth && year == minYear
                        || day < minDay && year == minYear && month == minMonth) {
                    mYear = minYear;
                    mMonth = minMonth;
                    mDate = minDay;
                    view.updateDate(minYear, minMonth, minDay);
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DATE, day);

                    mYear = year;
                    mMonth = month;
                    mDate = day;

                    view.updateDate(mYear, mMonth, mDate);
                }
                setTitle("Select Birth Date");
                mIgnoreEvent = false;
            }
        }

        public int getSelectedYear() {
            return mYear;
        }

        public int getSelectedMonth() {
            return mMonth;
        }

        public int getSelectedDate() {
            return mDate;
        }

}
