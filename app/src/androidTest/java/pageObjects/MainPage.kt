package pageObjects

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

class MainPage(var device: UiDevice) {


    val fabButton = device.findObject(
        UiSelector().resourceId("com.google.android.calendar:id/floating_action_button")
    )

    val eventButton = device.findObject(
        UiSelector().resourceId("com.google.android.calendar:id/speed_dial_event_container")
    )


    fun navigateToEvent() {

        fabButton.click()

        device.waitForIdle(100)

        eventButton.click()

    }


}