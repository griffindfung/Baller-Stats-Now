# Baller Stats Now <img src="https://user-images.githubusercontent.com/111543654/185542128-ef838bc4-b2ea-4144-adc5-6f3ebb6cb3db.png" align="right" width="50" height="50">
## Purpose
The app was born out of my incessant checking of when NBA games occurred on the same day, thus the app allows users to quickly preview today's games.
Furthermore, users are also able to search for any games to see their scores, and players to see their seasonal averages.

## Technologies
The app also doubled as a way to practice more recent Android technologies at the time of writing:
* Kotlin as it has become the supported language for Android development by Google
* Coroutines as it has become a more efficient and safe method of working asynchronously
* LiveData as a way to propagate data changes to the view
* ViewModel as a method to utilize LiveData in MVVM architecture
* Retrofit as it has still been a popular method of creating API calls

Overall, the app employs the technologies by combining Retrofit and Coroutines to asynchonously send API calls. With the asynchronous responses, the ViewModel can update its LiveData
and corresponding observers in the Activities and Fragments will update their views to display the basketball stats.

## Installation
If you want to try out the app, users can clone the repo and simply run in Android Studio after gradle syncing.
Alternatively, you can install the apk located in the repo in the directory /app/build/outputs/apk/debug/ with adb.

## Credits
This is a personal project, but additional resources were used in the making of this app.\
The app's data is powered by the [balldontlie API](http://www.balldontlie.io/).\
Basketball icon used is from [freesvg.org](https://freesvg.org/basketball-ball-vector-drawing-with-thick-border) under the public domain license.
