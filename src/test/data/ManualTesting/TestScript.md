## 2Do Test Script

This is a test script for 2Do that contains sample commands for all available operations in 2Do. Please note that it is written based on the assumption that a tester will run the commands in the order listed below with our provided sample data.

#### Pre-requisites:
<pre>
> Java version 1.8.0_60
> The latest (v0.5) 2Do.jar
</pre>

#### Setup:
1. Run 2Do.jar.
2. Locate /data/taskboss.xml that has been created in the directory containing 2Do.jar.
3. Replace contents of this file with our sample data found [here](https://github.com/CS2103JUN2017-T3/main/blob/master/src/test/data/ManualTesting/SampleData.xml)

#### Commands:

**1. Check current options**
<pre>
help option
</pre>
Expected behavior: Usage mesage for the Option command and current settings will be printed.

**2. Change options**
<pre>
option a/60 minutes m/true
</pre>
Expected behavior: Default alarm is set to "60 minutes" before deadline.
                  Tasks with deadline past current date is automatically marked as complete.

**3. Get help**
<pre>
help
</pre>
Expected behavior: 2Do will pop up a help window of UserGuide.
<pre>
help add
</pre>
Expected behavior: 2Do will show the usage message for the Add command.

**4. Add floating tasks**
<pre>
add n/Dinner Meetup d/JC friends t/Personal
a n/Dinner Meetup d/JC friends t/Personal
+ n/Dinner Meetup d/JC friends t/Personal
</pre>
Expected behavior: Add a task with name "Dinner Meetup", description "JC friends", and tag "Personal"
<pre>
add n/Toilet paper t/Groceries t/Personal
</pre>
Expected behavior: Add a task with name "Toilet Paper", tags "Groceries" and "Personal"

**5. Add events tasks**
<pre>
add n/IT fair s/19 July 2017 10am e/last mon of July 12am
</pre>
Expected behavior: Add a task with name "IT fair", start date "19/07/17 10am", and end date "31/07/17 12am".

**6. Add tasks with deadline**
<pre>
add n/CS2103 Tutorial d/Need to present tutorial e/24 July 2017 4pm a/2 days
</pre>
Expected behavior: Add a task with name "CS2103 Tutorial", description "Need to present tutorial", deadline "24/07/2017 4pm", and alarm "2 days" before deadline.

**7. Edit a task**
<pre>
edit 1 n/Project
e 1 n/Project
</pre>
Expected behavior: Edit the task with index 1; change its name to "Project".
<pre>
edit 2 t/Personal a/3 days
</pre>
Expected behavior: Edit the task with index 2; change its tags to "Personal", and alarm "3 days" before deadline.
<pre>
edit 3 s/yesterday 8am e/today 6pm
</pre>
Expected behavior: Edit the task with index 3; change its start date to "yesterday 8am", and end date to "today 6pm".


**8. Delete a task**
<pre>
delete 23
d 23
</pre>
Expected behavior: Delete the task with index 23.
<pre>
delete 22
</pre>
Expected behavior: Delete the tasks with index 22.
<pre>
delete 1
</pre>
Expected behavior: Delete the task with index 1.

[//]: # (Need test cases)

**9. Mark a task as done**
<pre>
mark 1
</pre>
Expected behavior: Mark the task with index 1 as done. The task will only appear in Done category.
<pre>
mark 5
</pre>
Expected behavior: Mark the task with index 5 as done. The task will only appear in Done category.


**10. Find task by keywords**
<pre>
find CS2103 personal
f CS2103 personal
</pre>
Expected behavior: Find all the tasks whose name and description containing the keywords "CS2103" or "personal.

**11. List tasks**
<pre>
list
l
</pre>
Expected behavior: List all incomplete tasks.
<pre>
list /f
</pre>
Expected behavior: List all incomplete floating tasks.
<pre>
list t/Personal
</pre>
Expected behavior: List all incomplete tasks with tag "Personal".
<pre>
list s/last week 10am
</pre>
Expected behavior: List all incomplete tasks after "last week 10am".
<pre>
list s/today 1am e/tomorrow 11pm
</pre>
Expected behavior: List all incomplete tasks between "today 1am" and "tomorrow 11pm".
<pre>
list /h
</pre>
Expected behavior: List all complete tasks.

**12. Unmark done task(s)**
<pre>
unmark 2
</pre>
Expected behavior: Unmark the tasks with index 2.
                  The tasks will be removed from Done category and added back to the unfinished task list.
<pre>
unmark 1
</pre>
Expected behavior: Unmark the task with index 1.
                   The task will be removed from Done category and added back to the unfinished task list.

**13. Clear all tasks**
<pre>
clear
c
</pre>
Expected behavior: Deletes all tasks from 2Do.

**14. Undo previous command**
<pre>
undo
u
</pre>
Expected behavior: Undo the previous operation: clear.
<pre>
undo
</pre>
Expected behavior: Undo the previous operation: unmark 1.

**15. Redo previous undo**
<pre>
redo
r
</pre>
Expected behavior: Redo the previous undo operation: unmark 1.
<pre>
redo
</pre>
Expected behavior: Redo the previous undo operation: clear.

**16. Save 2Do data to a specified filepath (OS-dependent, filepath may differ under different environment)**
<pre>
save C://Users/Desktop/2Do.xml    // Windows
s /Users/<Username>/Desktop/2Do.xml    // Mac
</pre>
Expected behavior: Create the 2Do.xml file in Desktop.

**17. Show command history**
<pre>
history
</pre>
Expected behavior: Deletes all tasks from 2Do.

**18. Repeat past commands**
<pre>
**up arrow**
</pre>
Expected behavior: Previous command, history, is shown in the command box.
<pre>
**up arrow**
</pre>
Expected behavior: Previous command, save C://Users/Desktop/2Do.xml, is shown in the command box.
<pre>
**down arrow**
</pre>
Expected behavior: Next command, history, is shown in the command box.

**19. Load 2Do data from a specified filepath (OS-dependent, filepath may differ under different environment)**
<pre>
load C://Users/Desktop/data/2Do.xml    // Windows
s /Users/<Username>/Desktop/data/2Do.xml    // Mac
</pre>
Expected behavior: Load the specified 2Do.xml file.

**20. Exit 2Do**
<pre>
exit
quit
x
q
</pre>
Expected behavior: Exit 2Do.

<h4 align="center">- End of document -</h4>
