# Baking App

This app gathers recipe data from a specified json resource on the web. Once the data is received, gson parses it and creates POJOs which are later utilised to show the recipes in a list. Each recipe contains steps and ingredients that are displayed on another activity once the recipe card is clicked. Once on that activity, you can click on the step/ingredients card to go to an activity that shows details on that. However, if the app is run on a tablet, the details will be shown on the right side by side. If a video is present with that specific step, it will be displayed above the instructions.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the app

```
1. Install Android Studio on your machine. Versions for Linux, Windows and macOS are available from here https://developer.android.com/studio/
2. Create a new emulator for a Google Pixel 2 with SDK version 27 or have a physical device on hand
3. If you are using a physical device, enable ADB debugging on your device. Refer to official documentation on how to install do it
```

### Installing

A step by step series of examples that tell you how to get a development env running

Cloning the project

```
1. Navigate to a location on your machine's file system, where you want to clone the project using a terminal or command line
2. Run this command to clone the master branch https://github.com/TomMichalkevic/Baking-App.git
3. Open your Android Studio and open the project from the project root you have just created by cloning
```

Setting up the project

```
1. Run gradle sync on the build.gradle in the app folder
2. When either an emulator or a physical device is connected, run installDebug task
3. Now you have a working instance the app
```

## Running the tests

To run the tests, you need to go to app/src/androidTest/com.tomasmichalkevic.bakingapp/MainActivityTest.java and run the main class.

### Test Breakdown

These test were created to test basic app functionality such as instantiating the activity, getting the app context and making sure that data was pulled. Please note that for the tests that test actions that potentially can take a long time have to use the IdlingResource as otherwise they will not work.

Test breakdown

```
1. useAppContext - Checks using the context of an application to get the package name
2. instantiateMainActivity - Checks whether it is possible to instantiate the activity
3. checkLoadedRecipes - Checks that the correct recipies are loaded
4. opensDetailsActivity - Checks opening the Details activity for the recipe, which contains the details such as ingredients and steps
```

## Built With

* [Gradle](https://gradle.org) - Dependency management and building

## Contributing

Please read [CONTRIBUTING.md](https://github.com/TomMichalkevic/Baking-App/blob/master/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/TomMichalkevic/Baking-App/tags). 

## Authors

* **Tomas Michalkevic** - *Initial work* - [Baking App](https://github.com/TomMichalkevic/Baking-App)

See also the list of [contributors](https://github.com/TomMichalkevic/Baking-App/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [official resource](https://opensource.org/licenses/MIT) for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
