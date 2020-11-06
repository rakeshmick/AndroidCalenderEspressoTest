package com.uitester.calendartestapp.test

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import pageObjects.FixMeetingTimePage
import pageObjects.HamburgerMenu
import pageObjects.MainPage
import pageObjects.MeetingConfigurationPage


private const val CALENDAR_APP_PACKAGE = "com.google.android.calendar"
private const val LAUNCH_TIMEOUT = 5000L

class StepDefs{

    private lateinit var device: UiDevice
    lateinit var meetingConfigurationPage : MeetingConfigurationPage
    lateinit var fixMeetingTimePage : FixMeetingTimePage
    lateinit var startDate:String
    lateinit var  finalTime:String
    @io.cucumber.java.Before
    fun setupCalendarApp() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        MatcherAssert.assertThat(launcherPackage, CoreMatchers.notNullValue())
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

    @Given("I have launched the Calendar App")
    fun i_have_launched_the_app(){
        //Navigate to new meeting config
        MainPage(device).navigateToEvent();


    }
    @When("^It is not a (\\S+)$")
    fun when_its_not_working_day(day : String){
        fixMeetingTimePage = FixMeetingTimePage((device))
        //verify that it is not a non working day based on user input
        fixMeetingTimePage.picADayWhichIsNot(day)
    }
    @And("Meeting is not repeated on successive days")
    fun meeting_not_created_repeatedly(){
        meetingConfigurationPage = MeetingConfigurationPage(device)
        //Get text from do not repeat to confrm that its not repeating
        val txtRepeat = meetingConfigurationPage.getTextFromRepeatOption()
    }


    @Then ("I want to book a meeting with the title “Recurring-Team Catch Up”")
    fun i_want_to_book_meeting_with_title(){
        //set the new meeting name
        meetingConfigurationPage.enterMeetingName("Recurring-Team Catch Up")
    }
    @And("Set Meeting duration as {int} in the evening")
    fun set_meeting_duration_as( hrsMinutes :Int){
        //get the current start time to calculate end time
        startDate = fixMeetingTimePage.startTime.text
        // format and get the hrs and min to calculate end time
        val firstSplit = startDate.split(" ")[0]

           val times= firstSplit.toString().split(":")

        val dateString =
            fixMeetingTimePage.manageTimeinHHMMaaFormat(times[0].toInt(), times[1].toInt(), hrsMinutes.toInt(), 5)

        val lastTimefirstSplit = dateString.split(" ")[0]

        val lastTimetimes= lastTimefirstSplit.toString().split(".")
        //fixMeetingTimePage.endTime.text = dateString
        fixMeetingTimePage.setEndTime(lastTimetimes[0].toInt(),lastTimetimes[1].toInt())
        finalTime = fixMeetingTimePage.endTime.text

    }
    @And("I save the meeting")
    fun i_save_theMeeting(){
        fixMeetingTimePage.saveAllConfigurations()
    }
    @Then("I Check if the meeting is created as expected")
    fun I_Check_if_the_meeting_is_created(){
        HamburgerMenu(device).selectWeekView()

        val createdMeeting = device.findObject(
            UiSelector().descriptionContains(startDate + " – " + finalTime + ": Recurring-Team Catch Up")
        )
        MatcherAssert.assertThat(
            createdMeeting.contentDescription,
            Matchers.containsString(startDate + " – " + finalTime + ": Recurring-Team Catch Up")
        )
        println( createdMeeting.contentDescription)

    }





}