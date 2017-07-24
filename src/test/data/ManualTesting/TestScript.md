## 2Do Test Script

Thisx is a test script for 2Do that contains sample commands for all available operations in 2Do. Please note that it is written based on the assumption that a tester will run the commands in the order listed below with our provided sample data.

#### Pre-requisites:
<pre>
> Java version 1.8.0_60
> The latest (v0.5) 2Do.jar
</pre>

#### Setup:
1. Run 2Do.jar.
2. Locate `/data/2Do.xml` that has been created in the directory containing 2Do.jar.
3. Replace contents of this file with our sample data found [here](https://github.com/CS2103JUN2017-T3/main/blob/master/src/test/data/ManualTesting/SampleData.xml).

#### Commands:
**1. Help**
<pre>
help
</pre>
Expected behavior: 2Do will pop up a help window of UserGuide.
<pre>
help add
</pre>
Expected behavior: 2Do will show the usage message for the Add command.

**2. Add floating tasks**
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

**3. Add events tasks**
<pre>
add n/IT fair s/19 July 2017 10am e/last mon of July 12am
</pre>
Expected behavior: Add a task with name "IT fair", start date "19/07/17 10am", and end date "31/07/17 12am".

**4. Add deadline tasks**
<pre>
add n/CS2103 Tutorial d/Need to present tutorial e/24 July 2017 4pm a/2 days
</pre>
Expected behavior: Add a task with name "CS2103 Tutorial", description "Need to present tutorial", end date "24/07/2017 4pm", and alarm "2 days" before end date.

[//]: # (Need test cases)

**5. Edit a task**
<pre>
edit 1 t/Project p/no
</pre>
Expected behavior: Edit the task with index 9; change it to Project category, with no priority.
<pre>
e 10 c/Project p/no
</pre>
Expected behavior: Edit the task with index 10; change it to Project category, with no priority.
<pre>
edit 10 ed/
</pre>
Expected behavior: Edit the task with index 10; Remove its end date.

[//]: # (Need test cases)

**6. Delete task(s)**
<pre>
delete 23
</pre>
Expected behavior: Delete the task with index 23.
<pre>
delete 22 24 25 26
</pre>
Expected behavior: Delete the tasks with index 22, 24, 25, 26.
<pre>
d 1
</pre>
Expected behavior: Delete the task with index 1.

[//]: # (Need test cases)

**7. Mark task(s) as done**
<pre>
mark 1
</pre>
Expected behavior: Mark the task with index 1 as done. The task will only appear in Done category.
<pre>
mark 2 5 6
</pre>
Expected behavior: Mark the tasks with index 2, 5, 6 as done. The tasks will only appear in Done category.
<pre>
m 5
</pre>
Expected behavior: Mark the task with index 5 as done. The task will only appear in Done category.

[//]: # (Need test cases)

**10. Find task by keywords**
<pre>
find pay
</pre>
Expected behavior: Find all the tasks whose name and information contains the keyword "pay".
<pre>
f pay
</pre>
Expected behavior: Find all the tasks whose name and information contains the keyword "pay".

**12. List**
<pre>
list
l
</pre>
Expected behavior: List all incomplete tasks.
<pre>
list /f
</pre>
Expected behavior: List all complete tasks.
<pre>
list t/Personal
</pre>
Expected behavior: List all incomplete tasks with tag "Personal".
<pre>
l s/last week 10am
</pre>
Expected behavior: List all incomplete tasks after "last week 10am".

**13. Unmark done task(s)**
<pre>
unmark 2 3
</pre>
Expected behavior: Unmark the tasks with index 2 and 3.
                  The tasks will be removed from Done category and added back to the unfinished task list.
<pre>
um 1
</pre>
Expected behavior: Unmark the task with index 1.
                   The task will be removed from Done category and added back to the unfinished task list.

**14. Rename a category**
<pre>
name personal life
</pre>
Expected behavior: The category Personal will be renamed as Life.
<pre>
n work company
</pre>
Expected behavior: The category Work will be renamed as Company.

**16. Clear all tasks**
<pre>
c c/Meetings
</pre>
Expected behavior: Clear all the tasks under Meetings Category.
                   The Meetings category will be removed from the category panel.
<pre>
clear
</pre>
Expected behavior: Clear all the tasks in TaskBoss.
                   The task list panel and category list panel will be blank.

**17. Undo**
<pre>
undo
</pre>
Expected behavior: Undo the previous operation: clear.
<pre>
u
</pre>
Expected behavior: Undo the previous operation: c c/Meetings.

**18. Redo**
<pre>
redo
</pre>
Expected behavior: Redo the previous undo operation: c c/Meetings.
<pre>
r
</pre>
Expected behavior: Redo the previous undo operation: clear.

**19. Save 2Do data to a specified filepath (OS-dependent, filepath may differ under different environment)**
<pre>
save C://Users/Desktop/2Do.xml    // Windows
sv /Users/<Username>/Desktop/2Do.xml    // Mac
</pre>
Expected behavior: Create the 2Do.xml file in Desktop.

**20. Exit 2Do**
<pre>
exit
quit
x
q
</pre>
Expected behavior: Exit 2Do.

<h4 align="center">- End of document -</h4>
