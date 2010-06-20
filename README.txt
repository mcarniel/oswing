OpenSwing  Project
===================

1. Requirements
---------------
The following files must be available to use OpenSwing framework:
- commonos.jar 
- clientos.jar 
- serveros.jar 
- jcalendar.jar
- poi-2.0-RC2-20040102.jar
- itext-1.4.8.jar
- itext-2.1.7.jar
- itext-rtf-2.1.7.jar
- jnlp.jar
- jnlp-servlet.jar
- log4j-1.2.7.jar
- pooler.jar


Note: hsqldb.jar is only used in demos included with the OpenSwing distribution, so that it is not part of OpenSwing framework.

Note: srccommonos.jar, srcclientos.jar and srcserveros.jar files contain OpenSwing source files.

Note: to run the "demo17" sample you have to include in the classpath Hibernate libraries too (see runDemo17.bat/sh). "demo17" sample app has been tested with Hibernate 3.2.3

Note: to run the "demo18" sample you have to include in the classpath Spring framework libraries too. "demo18" sample app has been tested with release 2.0.4 of Spring, using Tomcat 5.5 and JDK 1.6.
You have to include in web container classpath at least the following Spring files: 
aspectjrt.jar, aspectjweaver.jar, cglib-nodep-2.1_3.jar, commons-dbcp.jar, commons-pool.jar, spring-aspects.jar, spring.jar
Moreover, in last release of this demo, the hessian-3.1.1.jar library has been added; as consequence, this demo can be executed only with Java 1.5 or above.

Note: to run the "demo19" sample you have to include in the classpath iBatis libraries too (see runDemo19.bat/sh). "demo19" sample app has been tested with iBatis 2.3

Note: to run the "demo35" sample you have to use Java 5 EE and include in the classpath an Entity Manager implementation, such as javaee.jar file of GlassFish A.S. (see README.xxt file within demo35 source folder)

Note: to run the "demo36" sample you have to include in the classpath Apache Cayenne library too (see runDemo36.bat/sh). "demo36" sample app has been tested with Cayenne 2.0.4


2. Directory Structure
----------------------
src		source files of OpenSwing framework.

srchibernate    source files of OpenSwing framework specifically related to Hibernate embedding; to compile these classes you have to include in classpath Hibernate jars too (NOT provided in OpenSwing distribution)

srcspring       source files of OpenSwing framework specifically related to Spring framework embedding; to compile these classes you have to include in web container classpath the Spring jars too (NOT provided in OpenSwing distribution)

srchibernate    source files of OpenSwing framework specifically related to iBatis embedding; to compile these classes you have to include in classpath iBatis jars too (NOT provided in OpenSwing distribution)

src14           source files of OpenSwing framework specifically related to export feature using old versions of iText for PDF/RTF export formats

src15           source files of OpenSwing framework specifically related to export feature using recent versions of iText for PDF/RTF export formats; there versions requires java 1.5/1.6

srcejb3         source files of OpenSwing framework specifically related to export feature in a 3 tier application based on EJB3 session beans embedding; to compile these classes you have to use Java 5 EE, such as  GlassFish A.S. (NOT provided in OpenSwing distribution)

srcjpa          source files of OpenSwing framework specifically related to JPA/TopLink embedding; to compile these classes you have to use Java 5 EE and include in classpath an Entity Manager implementation, such as that included in GlassFish A.S. (NOT provided in OpenSwing distribution)

srccayenne      source files of OpenSwing framework specifically related to Apache Cayenne embedding; to compile these classes you have to include in classpath cayenne.jar too (NOT provided in OpenSwing distribution)

srcdemo		source files of demos included in the distribution; 
                "demo17" subfolder requires Hibernate jars too (NOT provided in OpenSwing distribution)
                "demo18" subfolder requires Spring jars too (NOT provided in OpenSwing distribution)
                "demo19" subfolder requires iBatis jars too (NOT provided in OpenSwing distribution)
                "demo35" subfolder requires Java 5 EE and an Entity Manager implementation such as that included in GlassFish A.S. (NOT provided in OpenSwing distribution)
		"demo36" subfolder require cayenne.jar library too (NOT provided in OpenSwing distribution)

build		jar files 

docs/api	javadoc files

docs		documentation files

classes/demo*   demos

srclnf          source files of OpenSwing framework specifically related to Look and Feel issues; currently it contains a UI components used for MAC O.S. to render pagination buttons inside the vertical scrollbar; to correctly compile this source you have to include in the classpath the swingall.jar file too (not included in this distribution)

pub		build.xml file used to build OpenSwing distribution jar files



3. Installation instructions
----------------------------

OpenSwing is based on these files:
- commonos.jar - contains classed needed both in client and server side applications
- clientos.jar - contains client-side OpenSwing components (together with commonos.jar)
- serveros.jar - contains server-side OpenSwing components (together with commonos.jar) 
- jcalendar.jar - an open source library (LGPL) for viewing a calendar, written by Kai Toedter
- poi-2.0-RC2-20040102.jar - an open source library used to generate Excel (XLS) files
- itext-1.4.8.jar - an open source library used to generate PDF or RTF files using java 1.4 or above
- itext-2.1.7.jar - an open source library used to generate PDF files using java 1.5 or above
- itext-rtf-2.1.7.jar - an open source library used to generate RTF files using java 1.5 or above
- jnlp.jar - JNLP API
- jnlp-servlet.jar - JNLP Servlet
- log4j-1.2.7.jar - LOG4J component
- pooler.jar - Java Database Connection Pool
- beaninfo.jar - library that can be installed inside an IDE that supports Java Beans specifications (JBuilder, NetBeans, JDeveloper)
which are available in build directory.

IMPORTANT NOTE: do not include beaninfo.jar as project library: it has only to be included into IDE settings, in order to show the OpenSwing components palette
within the IDE.

IMPORTANT NOTE: in order to use QueryUtil class, an implementation of Servlet API must be provided: for instance servlet-api.jar file
(from tomcat/lib folder) can be included as project library.


Note: hsqldb.jar is only used in demos included with the OpenSwing distribution, so that it is not part of OpenSwing framework.
Note: OfficeLnFs_2.7.jar is only used for demo10 sample application, to show how to include the Office LnF in a OpenSwing based application.
      OfficeLnFs is an open source project available in http://officelnfs.sourceforge.net/ and distributed with a BSD-style license.
Moreover, srccommonos.jar, srcclientos.jar and srcserveros.jar files contains OpenSwing source files, that could be included in your development environment to debug OpenSwing classes.

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
Another plugin for Eclipse 3.x is Jigloo (see http://www.cloudgarden.com/jigloo), that it is distributed in dual licence: free for non commercial use and a commercial use licence. 

3.4.1) After installing "Window Builder - Swing Designer" (see Swing Designer installation instructions), you can create JFrame, JPanel and many other Swing components by: 
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

3.4.2) After installing "Jigloo" (see Jigloo plugin installation instructions), you can create JFrame, JPanel and many other Swing components by: 
- selecting "File" from menu bar
- selecting "New" -> "Other" menu items
- expanding "Designer" -> "Swing" folders
- selecting a Swing components, e.g. JFrame and creating a class

At this point "Jigloo" plugin will open the class with two alternative views: GUI/Java editors.
When GUI editor is visible, it is available a Component Palette.
Inside this Component Palette it is possible to include all OpenSwing components, through the following steps: 
- select "Window" -> "Preferences" from Eclipse menu bar
- expand "Jigloo GUI Builder" node in the menu tree
- select "Component Palette/Custom classes" 
- press "Add (bean or palette)" button to specify a new components folder, such as "OpenSwing": press "Ok" and select it
- press "Add Beans from Archive " button: this will open a jar file selection window
- choose "Beaninfo.jar" file included with OpenSwing distribution and press "Ok" button 
- press "Ok" to confirm new components inclusion: at this point the OpenSwing palette will be visible in the Component Palette



4. How to use the framework
---------------------------

OpenSwing is composed of three jars files: two of them must be used as client-side libraries and 
two must be used as server-side library. You can use only one some of these files or use all of them.
Please see on-line documentation to learn how to use this framework.


