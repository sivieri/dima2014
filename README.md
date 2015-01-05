dima2014
========

Code for TA lessons of "Design and Implementation of Mobile Applications" @ Politecnico di Milano, 2014/2015

# Lesson 1
* IDE overview
* DimaTodos v1 overview
* DimaTodos main activity
* Introduction to fragments

This version includes the Android Support Library v4, which is automatically added by Eclipse ADT when a new project is created, and it also includes the JodaTime library, used to handle timestamps for the notes in the database.

# Lesson 2
* Fragments (cont'd)
* Internationalization
* Tablet view
* Content provider

Known bug: the list of all titles of the notes is not able to handle changes to the titles themselves from the editor. As specified at lesson, these first two lessons were mostly about handling the graphical part on different devices; we are going to solve the list management as soon as we start using the *content provider* that we started last time.

# Lesson 3
* Use the content provider to handle the notes list
* Add the *add new* and *delete* actions
* Support sharing of text to and from the app

# Lesson 4
* Multitasking (see the [dedicated project](https://github.com/sivieri/BlockingTests))
* Using Android content providers
* Integrate calendar actions
* Properly handle canceling the creation of a new note

# Lesson 5
* Interface with Wikitionary API to search for terms
* Get the current location and add it to new notes
* Open a map application with the current location
* Minor changes

This version requires the Google Play Services library: it has to be set up according to the local Android SDK location, [here](http://developer.android.com/google/play-services/setup.html) you can find the instructions.

# Lesson 6
* Ported the project to AndroidStudio (now the official IDE)
* Ported the project to API 21
* Created a new style for API 21+, using Material Design
* Ported all ListViews to RecyclerViews
* Ported the Location Client API to Google Play Services 6.5+

This version requires AndroidStudio. It does not require the Google Play Services library to be added by hand, the Gradle building system takes care of it (as well as the JodaTime library and the support library v.4 and v.7).
