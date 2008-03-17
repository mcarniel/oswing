To run this web application, tou have to:
- deploy demo18.war file in "webapps" subfolder of Tomcat installation (for example); this demo was tested using Tomcat 5.5.23 and JDK 1.6

- include the following Spring framework files in the web container classpath, (for example "shared/lib" subfolder of Tomcat installation):

  aspectjrt.jar, aspectjweaver.jar, cglib-nodep-2.1_3.jar, commons-dbcp.jar, commons-pool.jar, spring-aspects.jar, spring.jar

- prepare a database schema having 3 tables: TASKS, DEPT and EMP, that can be created through the following SQL scripts:

  create table EMP(EMP_CODE VARCHAR(20),FIRST_NAME VARCHAR(20),LAST_NAME VARCHAR(20),SALARY DECIMAL(10,2),HIRE_DATE DATE,SEX CHAR(1),DEPT_CODE VARCHAR(20),TASK_CODE VARCHAR(20),NOTE VARCHAR(2000),PRIMARY KEY(EMP_CODE))
  create table TASKS(TASK_CODE VARCHAR(20),DESCRIPTION VARCHAR(255),STATUS CHAR(1),PRIMARY KEY(TASK_CODE))
  create table DEPT(DEPT_CODE VARCHAR(20),DESCRIPTION VARCHAR(255),ADDRESS VARCHAR(100),STATUS CHAR(1),PRIMARY KEY(DEPT_CODE))

- modify "demo18/WEB-INF/jdbc.properties" files by specifying database connection settings, according to the database schema provided.

- launch client-side application through a web browser by clicking on the "demo18.jnlp" file; this starting version does not require a login.

- launch client-side application through a web browser by clicking on the "demo18login.jnlp" file; this stating version does require a login: use account: "admin" / "guest".
  Before use this file you have to (i) uncomment the "sessionCheckInterceptor" id bean from "controller-servlet.xml" file and (ii) uncomment "interceptors" property from the same file.

- in last release of this application, the hessian-3.1.1.jar library has been added; as consequence, this demo can be executed only with Java 1.5 or above.