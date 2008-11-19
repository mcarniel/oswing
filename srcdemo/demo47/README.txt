Intro
-----

This sample application is a three tier rich client application, with:
- OpenSwing front-end
- business logic realized using EJB 3.0 Stateless Session Beans (Container managed) and JPA
- client-server comunication based on RMI

This demo can be compiled using Eclipse 3.4 and Java 1.6 and deployed into JBoss 5.0_CR2.

Folders organization
--------------------

There are 5 projects, linked together:
- demo47JPA, contains persistence.xml descriptor and all Entity beans used by JPA layer
- demo47EJB, contains all business logic, realized using Stateless Session Beans
- demo47EJBClient, contains al classes needed by an EJB client application, i.e. EJB remote interfaces
- demo47EJBEAR, creates an .ear file to deploy into JBoss, that contains classes belonging to the three projects above
- demo47Client, the client-side application, based on OpenSwing components, that remotelly invokes server-side application contained in .ear

Note: demo does not contains a .jnlp launch file to start client-sude application, so you should start it from Eclipse or from the shell,
by launching "ClientApplication" main class. 
To create a complete application (i.e. running outside the Eclipse), demo47Client should be included in .ear file and downloaded via HTTP from JBoss,
moreover; client-side classes should be signed when creating .jar file used by .jnlp file.

Before starting application
---------------------------

Be sure to copy the correct JDBC driver within JBoss and to include inside JBoss a xxx-ds.xml descriptor related to the datasource specified in persistence.xml.
A database schema must be created in some database. Demo47 uses Oracle db, but you can simply specify another db type,
by changing persistence.xml file content.
You have to create some tables within that schema:

CREATE TABLE DOC01_ORDERS(
  DOC_YEAR NUMERIC,
  DOC_NUMBER NUMERIC,
  DOC_DATE DATETIME,
  TOTAL DECIMAL(12,2),
  NOTE VARCHAR(255),
  STATUS CHAR(1),
  COD_UTENTE_INS VARHAR(20),
  COD_UTENTE_AGG VARCHAR(20),
  DATA_INS DATETIME,
  DATA_AGG DATETIME,
  TIMESTAMP NUMERIC,
  STATE CHAR(1),
  CUSTOMER_ID_DOC02 VARCHAR(20),
  PRICELIST_ID VARCHAR(20),
  PRIMARY KEY (DOC_YEAR,DOC_NUMBER)
);

CREATE TABLE DOC02_CUSTOMERS(
  CUSTOMER_ID VARCHAR(20),
  DESCRIPTION VARCHAR(255),
  STATUS CHAR(1),
  ADDRESS VARCHAR(255),
  COD_UTENTE_INS VARHAR(20),
  COD_UTENTE_AGG VARCHAR(20),
  DATA_INS DATETIME,
  DATA_AGG DATETIME,
  TIMESTAMP NUMERIC,
  PRIMARY KEY (CUSTOMER_ID)  
);

CREATE TABLE DOC03_PRICELISTS(
  CUSTOMER_ID_DOC02 VARCHAR(20),
  PRICELIST_ID VARCHAR(20),
  DESCRIPTION VARCHAR(255),
  STATUS CHAR(1),
  COD_UTENTE_INS VARHAR(20),
  COD_UTENTE_AGG VARCHAR(20),
  DATA_INS DATETIME,
  DATA_AGG DATETIME,
  TIMESTAMP NUMERIC,
  PRIMARY KEY (CUSTOMER_ID_DOC02,PRICELIST_ID)  
);

CREATE TABLE DOC04_ORDER_ROWS(
  DOC_YEAR NUMERIC,
  DOC_NUMBER NUMERIC,
  ROW_NUMBER NUMERIC,
  ITEM_ID VARCHAR(20),
  QTY DECIMAL(12,2),
  UNIT_PRICE DECIMAL(12,2),
  TOTAL DECIMAL(12,2),
  NOTE VARCHAR(255),
  STATUS CHAR(1),
  COD_UTENTE_INS VARHAR(20),
  COD_UTENTE_AGG VARCHAR(20),
  DATA_INS DATETIME,
  DATA_AGG DATETIME,
  TIMESTAMP NUMERIC,
  PRIMARY KEY (DOC_YEAR,DOC_NUMBER,ROW_NUMBER)  
);

CREATE TABLE DOC04_ORDER_ROWS(
  ITEM_ID VARCHAR(20),
  DEFAULT_UNIT_PRICE DECIMAL(12,2),
  STATUS CHAR(1),
  COD_UTENTE_INS VARHAR(20),
  COD_UTENTE_AGG VARCHAR(20),
  DATA_INS DATETIME,
  DATA_AGG DATETIME,
  TIMESTAMP NUMERIC,
  PRIMARY KEY (ITEM_ID)  
);

