# Pre-work - The ToDo App

The ToDo App is an android app that allows building a todo list and basic todo item management functionality including adding new item, editing and deleting an existing item.

Submitted by: Sahil Agarwal

Time spent: 6 hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove item** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo item** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo item [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [ ] Improve style of the todo item in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [ ] Add support for completion due dates for todo item (and display within listview item)
* [ ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing item
* [ ] Add support for selecting the priority of each todo item (and display in listview item)
* [ ] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [ ] List anything else that you can get done to improve the app functionality!

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/ylS30Lv.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** It felt super familiar to me actually - I code reguarly in Java using Gradle, and took the iOS bootcamp - and so the pickup was fairly smooth. That being said, there's a lot of _magic_ that's occuring and which I don't understand. For example, how classes are tied to the layout xml, how transitions between screens work, or why there needs to be an array adapter as opposed to using an arraylist directly. I do like it was so simple to create an app and geting going!

In regards to the relative layout, I do wish we had more control over elements. It seems that elements can be linked to other elements, but I'm not able to control all four sides of it. I'm sure the transition away from relative layout will address that.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** The ArrayAdapter seems to be a class that allows for the linking between a java List object and the ListView UI object. Having it allows for a clean separation of concerns such as a notification to the UI element when to update its view and with what information. Maybe in a sense it acts as the controller in a MVC context. The getView method seems to be what the ListView calls to populate its rows of data, and the convertView object allows for view recycling and thus improve performance.

## Notes

## License

    Copyright 2017 Sahil Agarwal

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.