# ScreenshotUI
This is a UI that will appear after a user takes a screenshot to give them various options.
## Intended Functionality
- Screenshot keycombo launches the service through a broadcast intent
- Server takes a screenshot, passes it to the UI when it's created
- ScreenshotUI shows the screenshot, along with options
- Crop button will discard the screenshot and initiate a partial screenshot (does not return to UI)
- Scroll button will discard the screenshot and initiate a scrolling screenshot (does not return to UI)
- Edit button will save the screenshot and send a photo edit intent
- Cancel button will discard the screenshot and close the UI
- Share button will save the screenshot and send a file share intent
- Save button will save the screenshot and close the UI
## TODO
- Decide if back button should function as cancel or save (probably save)
- Impliment broadcast receiver
- Add button functionality (requires some system APIs, so it will be done last)
- UI close animation?
- Add hardware accelleration flag to window params
