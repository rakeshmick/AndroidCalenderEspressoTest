package pageObjects

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector


class MeetingConfigurationPage constructor(var device: UiDevice) {

    val moreOptionsSelector = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/more_options")
    )

    val doNotRepeat = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/recurrence_spinner")
    )
    val txtSelector = doNotRepeat.getChild(
            UiSelector().resourceId("com.google.android.calendar:id/text")
    )

    val addTitleEditText = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/input")
    )



    fun getTextFromRepeatOption(): String {
        moreOptionsSelector.click()
        return txtSelector.text
    }

    fun enterMeetingName(meetingName:String){
        addTitleEditText.text=meetingName
    }

}


