Update
------
Effective December 8, 2014, the ESPN API is no longer active.

Fantasy Feed
------------
A native-Android mobile app to get current Fantasy Football news from ESPN experts.
![Screenshot](http://es0329.com/images/fantasy_feed_framed.png)

Technical
---------
- Written in Java with the Android SDK and optimized for handheld devices.
- Uses Fragments, native ActionBar, and is backwards-compatible to Android API level 10.
- Communication handled off the main thread by extension of AsyncTask.
- Values (Strings, colors, and dimensions) are referenced in respective files, reducing future efforts for localization and alternate UIs.
- The main ListView consists of custom list items and selector states.

Credits
-------
Developed by [Eric Sepulvado][1] with data provided by ESPN's "[Headlines API][2]."
Thanks to [James Smith][3] for Smart-ImageView, which allows images to be loaded and cached via URL.

[1]: https://twitter.com/es0329 "@es0329"
[2]: http://developer.espn.com/ "ESPN API"
[3]: https://twitter.com/loopj "James Smith"
