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