Link to video recording Milestone 2:
https://rmiteduau-my.sharepoint.com/:v:/r/personal/s3912792_student_rmit_edu_au/Documents/Recording-20230513_112503.webm?csf=1&web=1&e=aZdoZe


Link to video recording Milestone 1:

https://rmiteduau-my.sharepoint.com/:v:/r/personal/s3912792_student_rmit_edu_au/Documents/Recording-20230416_135525.webm?csf=1&web=1&e=HlMdme


Assignment 2 MyHealth JavaFX GUI Application
Further Programming A2 Submission
Karsten Beck | s3912792@student.rmit.edu.au


HOW TO COMPILE
-----------------------------------------------------------------------------

The assignment has been created using the IntelliJ IDEA IDE and Maven. To compile the code, , to compile the program
for testing you can simply click the green arrow at the top left hand side of
the program.

Alternatively if you wish to compile the program to be used standalone or by
others then you need to click file -> project structure -> artifacts, from
here you can click plus to add a new artifact -> jar -> from module with
dependencies. As this is a maven project I would recommend for simplicity that
the program be compiled via a IntelliJ artifact instead of a standard Java
commandline compile as was done in A1.

Once you have the artifact added then simply press the build button on the top
ribbon menu, then select artifact, select the one you created then build.


|---------------------------------------------------------------------------|
|                         HOW TO RUN UNIT TESTS                             |
|---------------------------------------------------------------------------|
To make the unit tests easy to run I have placed them all inside the testing
package "com.jackgharris.cosc2288.a2.tests" to run all the testing classes in
that package simply right-click on the package via the project view on the left
and select run "Run tests in com.jackgharris.cosc2288.a2.tests". IntelliJ will
then go ahead and run all the class files and the tests compiled with in.

From there you can export the results as a .html file and place them in the
dedicated "unit testing results" folder.


|---------------------------------------------------------------------------|
|                         JDK Version & Compiler                            |
|---------------------------------------------------------------------------|
For this project I have used the follow:

- open JDK version 19 & Compiler (installed via intelliJ)
- openJavaFX version 17.0.2 (installed via intelliJ with Maven)
- sqlite-jdbc by org.xerial version 3.39.3.0 (installed via intelliJ with Maven)
- commons-validator version 1.7 (installed via intelliJ with Maven)
- Junit version 4.13.2 (installed via intelliJ with Maven)
- hamcrest-core version 1.3 (installed via intelliJ with Maven)