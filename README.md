# PassChord: Musical Authentication with Multi-Key Support

## Authors:
- Areeb Babar
- Diego Drago
- Matthew Malek-Podjaski
- Patryk Kaczmarczyk
- Rhodri Rees

## Installation Instructions:
This is an Android Studio project, as such it should be installed using Android Studio.
- Open the project directory in Android Studio
- Connect a compatible Android device (or use the built-in emulator)
- Select the device in the top bar (USB debugging must be enabled on the device)
- Build and run the project using the ▶ (play) symbol
- Confirm the installation on the connected device

## Operation:
### Creating a New Passchord:
To create a new passchord, tap the "CREATE" button in the top-left of the screen
and then play your sequence of notes and chords (precise timing is not necessary).
Once finished, tap the "SAVE" button (which replaced the "CREATE" button in the
top-left).

### Logging In
To log in using a saved passchord, tap the "RECORD" button in the top-right of the
screen and then play a sequence of notes and chords. Once done, tap the "SUBMIT"
button (again, in the top-right of the screen). The app will then check whether the
passchord entered matches the one that was previously saved and displays an appropriate
message.

### Manually Setting a Passchord
Chords are internally represented by a list of booleans corresponding to which keys are
being pressed. This can be visualised as an integer corresponding to the binary
representation of the boolean list. A sequence of chords is represented by a string
consisting of these integers, separated by `|` characters. To hard-code a passchord,
find the integer representations of the chords (either manually or by using the app
with debugging turned on) and replace line 99 of `PianoActivity.java` as such:

```java
String password = EventsToString(events);
// to
String password = "8|16|32|64|" // replace this with your string
```
