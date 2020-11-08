# AndroidCalenderEspressoTest


Command to run from cli
./gradlew connectedCheck


The scenario is tested and is working for Pixel 2 API 26
Android 8  X86 Oreo Apple Play API.
Before running the tests create a compatible emulator with the above mentioned details.
Also need to be logged in to the google account in the calendar.
The test will open the app and create the event and verify it.

Since the App under test is a google application and have no app source, I used
context to fetch the launch activity intent and launching the app with it.
"context.packageManager.getLaunchIntentForPackage"

in ./adb shell   to get the apps package and main activity
./adb logcat -b events    

I am_on_resume_called: [0,com.android.calendar.AllInOneActivity,LAUNCH_ACTIVITY]

The test report is the one cucumber creates by default

Missing details.

* I was unable to complete every step of the scenario, due to time constraint.
* One main thing missing is adding the emails to sent.
* Another important thing is currently the final verification is done by finding
  the newly created event based on the information gathered while creating the event.
  and comparing the furnished text with the accessibility id of the event.
  This fails when the start and end date is on two separate days.
  This was found late and I did not cater to it when i created the test.
* Also the screen shot when failure is not working.
* When validating the final created event, if the even is down and not visible,
  then the test will fail. I need to add a work around like swiping to reach the
  element and verify it.
