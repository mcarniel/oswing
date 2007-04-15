OpenSwing  Project
===================

1. Requirements
---------------
The following files must be available to use OpenSwing framework:
- clientos.jar 
- serveros.jar 
- jcalendar.jar
- poi-2.0-RC2-20040102.jar
- jnlp.jar
- jnlp-servlet.jar
- log4j-1.2.7.jar
- pooler.jar
- srcclientos.jar
- srcserveros.jar

Note: hsqldb.jar is only used in demos included with the OpenSwing distribution, so that it is not part of OpenSwing framework.



2. Directory Structure
----------------------
src		source file of OpwnSwing project.

build		jar files 

docs/api	javadoc files

docs		documentation files.

classes/demo*   demos



3. Installation instructions
----------------------------

OpenSwing is based on these files:
- clientos.jar - contains client-side OpenSwing components
- serveros.jar - contains server-side OpenSwing components 
- jcalendar.jar - an open source library (LGPL) for viewing a calendar, written by Kai Toedter
- poi-2.0-RC2-20040102.jar - an open source library used to generate Excel (XLS) files
- jnlp.jar - JNLP API
- jnlp-servlet.jar - JNLP Servlet
- log4j-1.2.7.jar - LOG4J component
- pooler.jar - Java Database Connection Pool
- beaninfo.jar - library that can be installed inside an IDE that supports Java Beans specifications (JBuilder, NetBeans, JDeveloper)

which are available in build directory.

Note: hsqldb.jar is only used in demos included with the OpenSwing distribution, so that it is not part of OpenSwing framework.
Moreover, srcclientos.jar and srcserveros.jar files contains OpenSwing source files, that could be included in your development environment to debug OpenSwing classes.

JDK 1.4 is required.
I can be used with java 1.5 or 1.6 too.


3.1 Installing OpenSwing components palette in JBuilder
-------------------------------------------------------

To create a palette of components in JBuilder: 
- select "Tools" from menu bar
- select "Configure Palette" and press "Add" button to define a new palette
- select new defined palette and go to the "Add Components" folder
- press "Select Library" button and configure a new library by pressing "New" button
- in "New library" wizard add the beaninfo.jar and close that window
- press "Add from selected library" and close all windows
Now the OpenSwing palette will be visible.
Note: sometimes in JBuilder this procedure must be repeated twice because of a bug in some versions of JBuilder.


3.2 Installing OpenSwing components palette in NetBeans
-------------------------------------------------------

To create a palette of components in NetBeans: 
- select "Tools" from menu bar
- select "Palette Manager" and press "New Category" button
- define a new category on which inserting OpenSwing components
- select the new category just defined and press "Add from jar" button
- add beaninfo.jar file and select all classes that will be proposed
- press "next" button
- select the new category just defined
- press "next" until the end of wizard
Now the OpenSwing palette will be visible.


3.3 Installing OpenSwing components palette in JDeveloper 10.1.2
----------------------------------------------------------------

To create a palette of components in JDeveloper you must first define a library: 
- select "Tools" from menu bar
- select "Manage Libraries" and select "User libraries" node and press "New" button to define a new library
- set a name for the new library
- define the classpath by pressing "Edit" button to select the beaninfo.jar file
- press ok to close all windows

To define the palette:
- select "Tools" from menu bar
- select "Configure Palette" and press "Add" button on the left to define a new palette
- select new defined palette and press "Add" button on the right to add a library
- select the library just created and select the filter "Java Beans with BeanInfo only"
- select all filters classes and add them
Now the OpenSwing palette will be visible.
Note: maybe component images could be not visible; in that case you have to select images from beaninfo.jar, for each image not viewed.




4. How to use the framework
---------------------------

OpenSwing is composed of a client-side library and a serverside library. You can use only one of these
files or use both libraries.
Please see on-line documentation to learn how to use this framework.


