package pageObjects

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*

class FixMeetingTimePage(var device: UiDevice) {


    val startDatePicker = device.findObject(
        UiSelector().resourceId("com.google.android.calendar:id/start_date")
    )


    val dateHeader = device.findObject(
        UiSelector().resourceId("android:id/date_picker_header_date")
    )

    val saveButton = device.findObject(
        UiSelector().resourceId("android:id/button1")
    )

    val startTime = device.findObject(
        UiSelector().resourceId("com.google.android.calendar:id/start_time")
    )


    val toggleSelector = device.findObject(
        UiSelector().resourceId("android:id/toggle_mode")
    )

    val hourSelector = device.findObject(
        UiSelector().resourceId("android:id/input_hour")
    )


    val minuteSelector = device.findObject(
        UiSelector().resourceId("android:id/input_minute")
    )

    val amSelector = device.findObject(
        UiSelector().resourceId("android:id/am_label")
    )
    val pmSelector = device.findObject(
        UiSelector().resourceId("android:id/pm_label")
    )

    val endTime = device.findObject(
        UiSelector().resourceId("com.google.android.calendar:id/end_time")
    )

    val finalSaveButton = device.findObject(
        UiSelector().resourceId("com.google.android.calendar:id/save")
    )


    fun getStartDate(): String {
        startDatePicker.click()
        val dateTxt = dateHeader.text;
        saveButton.click()
        return dateTxt
    }

    fun picADayWhichIsNot(day: String) {
        val d = day.subSequence(0, 3)
        val startStringFull = getStartDate()
        val startDate = startStringFull.subSequence(0, 3)
        startDatePicker.click()
        if (!startDate.equals(d)) {
            saveButton.click()
            return
        } else {
            val da = startStringFull.substring(startStringFull.length - 2, startStringFull.length)
            val newDate = da.trim().toInt() + 1
            device.findObject(
                UiSelector().descriptionContains(newDate.toString() + " November 2020")
            ).click()
        }
        saveButton.click()

    }

    fun setStartTime(inputHr: Int, inputMin: Int) {
        startTime.click()
        toggleSelector.click()
        hourSelector.text = inputHr.toString()
        minuteSelector.text = inputMin.toString()
        saveButton.click()
    }


    fun setEndTime(inputHr: Int, inputMin: Int, am_pm: String) {


        endTime.click()
        toggleSelector.click()
        hourSelector.text = inputHr.toString()
        minuteSelector.text = inputMin.toString()
        getAM_PMselector(am_pm)?.click()
        saveButton.click()
    }

    fun manageTimeinHHMMaaFormat(hr: Int, min: Int,aa: String, hrsToAdd: Int, minToAdd: Int): String {
        val dateFormat: DateFormat = SimpleDateFormat("hh.mm aa")
        val cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, hr)
        cal.set(Calendar.MINUTE, min)
        when(aa){
            "am","AM"-> cal[Calendar.AM_PM] = Calendar.AM
            "pm","PM"-> cal[Calendar.AM_PM] = Calendar.PM
        }
        cal.add(Calendar.HOUR, hrsToAdd)
        cal.add(Calendar.MINUTE, minToAdd)
        val dateString = dateFormat.format(cal.time).toString()
        println("Current time in AM/PM: $dateString")
        return dateString
    }


    fun saveAllConfigurations() {
        finalSaveButton.click()
    }

    fun getAM_PMselector(am_pm: String): UiObject? {
        if(am_pm.equals("am"))
            return amSelector
        else if (am_pm.equals("pm"))
            return pmSelector
        else return null
    }
}