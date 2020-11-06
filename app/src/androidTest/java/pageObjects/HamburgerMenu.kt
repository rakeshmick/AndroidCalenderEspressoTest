package pageObjects

import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

class HamburgerMenu (var device:UiDevice){
    val hamburgerMenu = device.findObject(
            UiSelector().descriptionContains("Show Calendar List and Settings drawer")
    )


    val frame = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/drawer_list")
    )


    fun selectWeekView(){
        hamburgerMenu.click()
        frame.getChild(
                UiSelector().className("android.widget.LinearLayout")
        ).getChild(
                UiSelector().text("Week")
        ).click()
    }
}