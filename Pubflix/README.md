# README for Pubflix

This little app displays the list of movies matching a search and will,
upon selection display the poster art if available.

## Check-list

(x) Identify needed libraries
- Retrofit for http
- Retrofit2 for serialization
- Coil for image loading/caching
- Compose for navigation

(x) Permissions/Edits in the AndroidManifest.xml
- .Internet

(x) res - resources needed
- App Icon
- Splash Screen Image
 
## Approach

This basic app is very simple with limited error handling (low level http).
The UI is basically to screen (not counting splash):
- displays the search field {with an auto-search feature}
- and the detail page for showing the poster art {if any}

## Goal
To provide a demo of fundamental understanding of Android/Kotlin/Jetpack
using backend services. To show the 'check-list' of taking specs and creating
action items to build the app {both for solo and teams}.

## Addition goals
Set out to show the humble features an app should have:
- Icon resources
- Splash Screen
- Working navigation
- Auto-search when the user pauses typing
- Display keyboard when search begins

## Missing
- There are no tests {Unit, Implementation, Network} to show/support TDD.
- Lower SDK support; this app targets with Android-17 not older versions
- Hard-coded values for API and keys
- Did not do clean-up of any unused .xml or other /res that Android Studio creates

## Special notes:
- Used features of the just released Android-17; this limits devices this could run on
- Reused 'Splash Screen' logic from another project to the black-screen and splash logo
- Used Nano-banana AI to create the 'Pubflix' icons bases on the Publix logo

### Finally:
Be kind to each other.

26-Jun-2026 Steven Smith steven,dev@icodeforyou.com
