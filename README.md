# My TV Tracker

This is an Android app to help you keep track of the TV shows you are watching. The app will let you add and remove shows from you "Watch List". You will be reminded of upcoming episodes on your Watch List. You can also search for TV shows and find out more infomation about them, including a description, the time it airs and information about the seasons and episodes.

## Requirements Summary

Here is a brief list of the feature requirements:

- Search for a TV show
- List TV shows that the user is tracking, in a ‘watch list’
- Notify the user when a new episode is about to begin, or a new season is starting (this feature will be implemented in the future)
- View information about a TV show and their episodes

More features will be added in the future.

## Screen Flow

Prototype on FluidUI: https://www.fluidui.com/editor/live/preview/p_OuPfL5qOHz5IaVCi9RnphnsykCvAzF5s.1414372986270

The final screens do not look like the prototype, but the information on each screen should be the same. The FluidUI prototype has notes.

## Issues

- As agreed, notifications will be added at a later date.
- Sometimes the app lags a little, due to pulling information from the web. This is the main issue that needs to be resolved either through caching or loading screens.
- Other issues are noted in the GitHub issues page/section.

## Demonstration

Coming soon...

## Platforms/Devices covered

The app is designed to work on Android phones and tablets running API 14 (Android 4.01/Ice Cream Sandwich) or higher. There may be support for more devices in the future.

## Test Plan

The app has been tested on various emulators for phones and tablets running different screen sizes and Android versions. The lowest version/screen size tested was a Nexus S, API 16, 480x800. No major issues with layout noted.

Tested physically with an LG G3 and Nexus 7 (1st gen).

## Major Concepts Covered

1. Multiple activities - the app has more than 1 different screen.
2. Custom launcher - the app has a custom launcher icon.
3. Customized views - the app does use a responsive design. When you view information about a show and it's episodes, that screen uses a Master/Detail flow layout and shows items in a 'two pane' mode on tablets.
4. Consume RESTful web services - the app uses Trakt.tv API to pull information.
5. ListView and ExpandableListView - layouts not covered in class, used for search results and Watch List items respectively.
6. NavigationDrawer - used on the main screen as the main menu, to access search and the Watch List screens.
7. Multimedia - images are used for TV show posters and screen caps from episodes.
8. Data Persistence - the users' Watch List is stored in a local database.

## Expansion Topic

I was going to use notifications, but did not implement this in time. However, I used a lot of Fragments in my app, which was not fully covered in class.

## Source Code/Branch Set up

The 'master' branch contains the latest development edition.

When the app is released, the 'production' branch will contain the latest version added to the Play Store.

## Notes/Instructions

As of writing, the app is still in development and the main feature missing is notifications.

## References

- Certain parts of the code were used from online tutorials, those classes have a license statement as a comment in them.
