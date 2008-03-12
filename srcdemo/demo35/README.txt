Demo35 shows how to combine JPA with OpenSwing.
In this application an editable grid frame is showed. Data showed within the grid is loaded and stored to a database through JPA.
Java project has been created using NetBeans 6.0, GlassFish and TopLink Essentials as ORM layer to provide object persistence.
You have to use java 1.5 or 1.6 and Java EE 5.

To execute demo35 in NetBeans 6 you could follow these steps:
- in NetBeans choose File -> Open Project, select "srcdemo/demo35" directory and click Open Project
- right click the project's Libraries node and chhose Add JAR/Folder
- add ${GLASSFISH_HOME}/lib/javaee.jar
- add the driver to your database, for instance the Java DB database bundled with the Sun Java System A.S. (GlassFish), add ${GLASSFISH_HOME}/javadb/lib/derbyclient.jar
- create a META-INF/persistence.xml file such as the one

<?xml version="1.0" encoding="UTF-8"?>
<persistence version="1.0" xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd">
	<persistence-unit name="demo35PU" transaction-type="RESOURCE_LOCAL">
		<provider>oracle.toplink.essentials.PersistenceProvider</provider>
		<class>demo35.Address</class>
		<class>demo35.Person</class>
		<properties>
			<property name="toplink.jdbc.user" value="app"/>
			<property name="toplink.jdbc.password" value="app"/>
			<property name="toplink.jdbc.url" value="jdbc:derby://localhost:1527/sample"/>
			<property name="toplink.jdbc.driver" value="org.apache.derby.jdbc.ClientDriver"/>
			<property name="toplink.ddl-generation" value="drop-and-create-tables"/>
		</properties>
	</persistence-unit>
</persistence>

- you can create that file inside NetBeans, by right clicking the projerct and choosing New -> File/Folder
- from Persistence category, select Persistence Unit and click next
- select the database connection for your application from the drop down list
- select Drop and Create as the Table Generation Strategy
- click Finish. The IDE creates persistence.xml
- be sure that the property "toplink.jdbc.password" in persistence.xml has been correctly defined
- be sure that the persistent unit name is as the one defined in demo35.ClientApplication source file

Finally, before starting application you have to start the Java DB database (choose Tools -> Java DB database -> Start Java DB Server).

