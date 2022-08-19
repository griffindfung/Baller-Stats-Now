# Baller Stats Now <img src="https://user-images.githubusercontent.com/111543654/185542128-ef838bc4-b2ea-4144-adc5-6f3ebb6cb3db.png" align="right" width="50" height="50">
## Purpose
The app was born out of my incessant checking of when NBA games occurred on the same day, thus the app allows users to quickly preview today's games.
Furthermore, users are also able to search for any games to see their scores, and players to see their seasonal averages.

## Features

<table>
  <tr>
    <td>Preview Today's Games</td>
    <td>Search for Players</td>
    <td>Search for Player's Seasonal Averages</td>
  </tr>
  <tr>
    <td><img src="https://user-images.githubusercontent.com/111543654/185708045-eb1a4b55-f2ee-412d-be83-bb5a60056211.jpg" width=225 height=500></td>
    <td><img src="https://user-images.githubusercontent.com/111543654/185709186-349a5ffd-5821-4a26-8c3b-cef2b6cfc445.jpg" width=225 height=500></td>
    <td><img src="https://user-images.githubusercontent.com/111543654/185709796-5f91bdf7-f916-4434-98be-20bca6c72c3c.jpg" width=225 height=500></td>
  </tr>
 </table>

<table>
  <tr>
    <td>Search for Games by Date</td>
    <td>Get Game Score Details</td>
  </tr>
  <tr>
    <td><img src="https://user-images.githubusercontent.com/111543654/185709995-7a206682-870f-4e6a-9591-0e995ed348be.jpg" width=225 height=500></td>
    <td><img src="https://user-images.githubusercontent.com/111543654/185710137-2823e841-e297-4ffe-abf9-de225ee90c70.jpg" width=225 height=500></td>
  </tr>
 </table>


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
