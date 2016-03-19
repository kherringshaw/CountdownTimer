Countdown Timer is an app that counts the number of times the user clicks on a button (+/cancel) and when the maximum is reached (99) the app sounds an 
alarm and begins automatically counting back down to zero when the app will then continue beeping until the user clicks the button to stop.  If the user 
clicks the button again along the way, the count is zeroed out.  If the user hesitates for longer than three seconds while clicking up to the maximum, the 
automatic countdown will again be activated.

Also available to the user is the functionality to manually type in the number and feed that to the count.  The user clicks on the green box and a keypad 
appears that allows the user to choose their count, also up to the maximum of 99.  After clicking ‘enter’, the app will then pause three seconds and begin 
the countdown.

The app was designed to use different states to create and change functionality for the user.  The program has three states: RUNNING, BEEPING, STOPPED.  
Stopwatchstate was created as an interface and each of the states extends from Stopwatchstate. The new states have a variety of methods that are implemented 
throughout the program to change the behavior of the buttons and the output on the screen.  Many classes of code from the StopWatch program were used as 
the foundation of creating Countdown Timer and reused as much as possible in order to keep the code streamlined and able to be reused throughout many areas 
of the program.


"# CountdownTimer" 
