package com.uitester.calendartestapp.test

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import java.io.File


private const val CALENDAR_APP_PACKAGE = "com.google.android.calendar"
private const val LAUNCH_TIMEOUT = 5000L

@RunWith(AndroidJUnit4::class)
class CalendarInstrumentationTest {

    private lateinit var device: UiDevice

    @Before
    fun setupCalendarApp() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            LAUNCH_TIMEOUT
        )

        // Launch the app
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(
            CALENDAR_APP_PACKAGE
        ).apply {
            // Clear out any previous instances
            this?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        // Wait for the app to appear
        device.wait(
            Until.hasObject(By.pkg(CALENDAR_APP_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT
        )
    }

    @Test
    fun testCreateCalendarEvent() {
        val fabButton = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/floating_action_button")
        )

        fabButton.click()

        device.waitForIdle(100)

        // click again on the expanded FAB to create an event
//        fabButton.click()


        val eventButton = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/speed_dial_event_container")
        )

        eventButton.click()


        val moreOptionsSelector = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/more_options")
        )
        moreOptionsSelector.click()


        val doNotRepeat = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/recurrence_spinner")
        )
        val txtSelector = doNotRepeat.getChild(
            UiSelector().resourceId("com.google.android.calendar:id/text")
        )

        val txtRepeat = txtSelector.text


        val addTitleEditText = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/input")
        )
        addTitleEditText.text = "Meeting"


        val startDatePicker = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/start_date")
        )
        startDatePicker.click()


        val dateHeader = device.findObject(
            UiSelector().resourceId("android:id/date_picker_header_date")
        )
        val dateTxt = dateHeader.text;

        println("the header is " + dateTxt)

        /* val saveButton = device.findObject(
             UiSelector().className(Button::class.java)
                 .text("Save")
         )

         saveButton.click()*/

        val saveButton = device.findObject(
            UiSelector().resourceId("android:id/button1")
        )
        saveButton.click()

        val startTime = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/start_time")
        )
        startTime.click()

        val toggleSelector = device.findObject(
            UiSelector().resourceId("android:id/toggle_mode")
        )
        toggleSelector.click()


        val hourSelector = device.findObject(
            UiSelector().resourceId("android:id/input_hour")
        )
        var inputhr = hourSelector.text
        hourSelector.text = inputhr


        val minuteSelector = device.findObject(
            UiSelector().resourceId("android:id/input_minute")
        )
        val inputMin = minuteSelector.text
        minuteSelector.text = inputMin


        val endTime = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/end_time")
        )
        endTime.click()

        toggleSelector.click()


        hourSelector.text = "10"


        minuteSelector.text = "30"

        saveButton.click()


        val finalSaveButton = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/save")
        )
        finalSaveButton.click()

        val hamburgerMenu = device.findObject(
            UiSelector().descriptionContains("Show Calendar List and Settings drawer")
        )
        hamburgerMenu.click()

        val frame = device.findObject(
            UiSelector().resourceId("com.google.android.calendar:id/drawer_list")
        )
        frame.getChild(
            UiSelector().className("android.widget.LinearLayout")
        ).getChild(
            UiSelector().text("Week")
        ).click()


        val createdMeeting = device.findObject(
            UiSelector().descriptionContains("10:10 AM – 10:30 AM: Meeting")
        )
        assertThat(createdMeeting.text, equals("10:10 AM – 10:30 AM: Meeting"))
    }

    @Rule
    var watcher: TestRule = object : TestWatcher() {
        override fun failed(e: Throwable?, description: Description) {
            // Save to external storage (usually /sdcard/screenshots)
            val path = File(
                Environment.getExternalStorageDirectory().getAbsolutePath()
                    .toString() + "/screenshots/" + getTargetContext().packageName
            )
            if (!path.exists()) {
                path.mkdirs()
            }

            // Take advantage of UiAutomator screenshot method
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            val filename: String =
                description.getClassName().toString() + "-" + description.getMethodName() + ".png"
            device.takeScreenshot(File(path, filename))
        }
    }

}