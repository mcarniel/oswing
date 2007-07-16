OpenSwing  Project
===================

1. Requirements
---------------
The following files must be available to use OpenSwing framework:
- clientos.jar 
- serveros.jar 
- jcalendar.jar
- poi-2.0-RC2-20040102.jar
- itext-1.4.8.jar
- jnlp.jar
- jnlp-servlet.jar
- log4j-1.2.7.jar
- pooler.jar
- srcclientos.jar
- srcserveros.jar

Note: hsqldb.jar is only used in demos included with the OpenSwing distribution, so that it is not part of OpenSwing framework.

Note: to run the "demo17" sample you have to include in the classpath Hibernate libraries too (see runDemo17.bat/sh). "demo17" sample app has been tested with Hibernate 3.2.3

Note: to run the "demo18" sample you have to include in the classpath Spring framework libraries too. "demo18" sample app has been tested with release 2.0.4 of Spring, using Tomcat 5.5 and JDK 1.6.
You have to include in web container classpath at least the following Spring files: 
aspectjrt.jar, aspectjweaver.jar, cglib-nodep-2.1_3.jar, commons-dbcp.jar, commons-pool.jar, spring-aspects.jar, spring.jar

Note: to run the "demo19" sample you have to include in the classpath iBatis libraries too (see runDemo19.bat/sh). "demo19" sample app has been tested with iBatis 2.3



2. Directory Structure
----------------------
src		source files of OpenSwing framework.

srchibernate    source files of OpenSwing framework specifically related to Hibernate embedding; to compile these classes you have to include in classpath Hibernate jars too (NOT provided in OpenSwing distribution)

srcspring       source files of OpenSwing framework specifically related to Spring framework embedding; to compile these classes you have to include in web container classpath the Spring jars too (NOT provided in OpenSwing distribution)

srchibernate    source files of OpenSwing framework specifically related to iBatis embedding; to compile these classes you have to include in classpath iBatis jars too (NOT provided in OpenSwing distribution)

srcdemo		source files of demos included in the distribution; 
                "demo17" subfolder requires Hibernate jars too (NOT provided in OpenSwing distribution)
                "demo18" subfolder requires Spring jars too (NOT provided in OpenSwing distribution)
                "demo19" subfolder requires iBatis jars too (NOT provided in OpenSwing distribution)

build		jar files 

docs/api	javadoc files

docs		documentation files.

classes/demo*   demos

srclnf          source files of OpenSwing framework specifically related to Look and Feel issues; currently it contains a UI components used for MAC O.S. to render pagination buttons inside the vertical scrollbar; to correctly compile this source you have to include in the classpath the swingall.jar file too (not included in this distribution).



3. Installation instructions
----------------------------

OpenSwing is based on these files:
- clientos.jar - contains client-side OpenSwing components
- serveros.jar - contains server-side OpenSwing components 
- jcalendar.jar - an open source library (LGPL) for viewing a calendar, written by Kai Toedter
- poi-2.0-RC2-20040102.jar - an open source library used to generate Excel (XLS) files
- itext-1.4.8.jar - an open source library used to generate PDF or RTF files
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


3.4 Installing OpenSwing components palette in Eclipse 3.x/WebSphere Studio IDEs
--------------------------------------------------------------------------------

Eclipse's Visual Editor is not able to correctly render all OpenSwing graphics controls, because of it does not fully support Java Beans specifications.
You can use Eclipse and OpenSwing only if you include an Eclipse plugin that fully support Java Beans specifications.
A good (non free) plugin for Eclipse 3.x is "Window Builder - Swing Designer" (see http://www.swing-designer.com/)

After installing "Window Builder - Swing Designer" (see Swing Designer installation instructions), you can create JFrame, JPanel and many other Swing components by: 
- selecting "File" from menu bar
- selecting "New" -> "Other" menu items
- expanding "Designer" -> "Swing" folders
- selecting a Swing components, e.g. JFrame and creating a class

At this point "Swing Designer" plugin will open the class with two alternative views: "Source" and "Design".
When switching to "Design" view, it is available a Component Palette.
Inside this Component Palette it is possible to include all OpenSwing components, through the following steps: 
- click with the right mouse button inside the Palette
- select "Palette Manager" in the popup menu just Viewed
- press "Add Category" button and specify a category name, such as "OpenSwing" and press "Ok" button
- select the category just created and press "Add from Archive" button
- in the "Archive" input field specify the absolute path to the "lib/BeanInfo.jar" file included in the OpenSwing distribution and press ENTER
- when the "Select All" button becomes enabled, press it and press "Ok" button to confirm all OpenSwing components
- finally press "Ok": at this point the OpenSwing palette will be visible in the Component Palette

Note: if OpenSwing components are not visible inside the new category, close Eclipse and restart it.


4. How to use the framework
---------------------------

OpenSwing is composed of a client-side library and a serverside library. You can use only one of these
files or use both libraries.
Please see on-line documentation to learn how to use this framework.


