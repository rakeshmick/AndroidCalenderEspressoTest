package com.uitester.calendartestapp.test

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pageObjects.FixMeetingTimePage
import pageObjects.HamburgerMenu
import pageObjects.MainPage
import pageObjects.MeetingConfigurationPage

private const val CALENDAR_APP_PACKAGE = "com.google.android.calendar"
private const val LAUNCH_TIMEOUT = 5000L

@RunWith(AndroidJUnit4::class)
class CalendarInstrumentationTestPageObject {

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
                CALENDAR_APP_PACKAGE).apply {
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

        //Navigate to new meeting config
        MainPage(device).navigateToEvent();

        val meetingConfigurationPage = MeetingConfigurationPage(device)
        //Get text from do not repeat to confrm that its not repeating
        val txtRepeat = meetingConfigurationPage.getTextFromRepeatOption()
        //set the new meeting name
        meetingConfigurationPage.enterMeetingName("Recurring-Team Catch Up")

        val fixMeetingTimePage = FixMeetingTimePage(device)
        //verify that it is not a non working day based on user input
        fixMeetingTimePage.picADayWhichIsNot("Friday")
        //get the current start time to calculate end time
        val startDate = fixMeetingTimePage.startTime.text
        // format and get the hrs and min to calculate end time
        val times = startDate.split(" ").indexOf(0).toString().split(":")

        val dateString =
                fixMeetingTimePage.manageTimeinHHMMaaFormat(times.indexOf(0), times.indexOf(1), 10, 5)


        fixMeetingTimePage.endTime.text = dateString

        val finalTime = fixMeetingTimePage.endTime.text

        fixMeetingTimePage.saveAllConfigurations()

        HamburgerMenu(device).selectWeekView()

        val createdMeeting = device.findObject(
                UiSelector().descriptionContains(startDate + " – " + finalTime + ": Recurring-Team Catch Up")
        )
        assertThat(createdMeeting.contentDescription, containsString(startDate + " – " + finalTime + ": Recurring-Team Catch Up"))


    }


}