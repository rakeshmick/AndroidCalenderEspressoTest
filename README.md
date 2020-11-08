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
