The app allows a user to set a timer in seconds and start/stop/restart it.

There is an APK in the root directory that is built and ready to be installed.


Assumptions
----------------------
Timer does not keep state across restarts.

Timer continues from where it was stopped. To restart the timer, you need to close out of the app. Or you can change the duration of the timer.

Max time is 9999 seconds. The app counts down in pure seconds and does not format to HH:MM:SS.

Uses the following Libraries:
----------------------
Butterknife
JodaTime






