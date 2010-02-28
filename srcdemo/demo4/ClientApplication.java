package demo4;

import java.util.*;

import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.domains.java.*;
import java.sql.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.permissions.java.ButtonsAuthorizations;
import org.openswing.swing.internationalization.java.*;
import javax.swing.UIManager;
import javax.swing.*;
import java.awt.event.KeyEvent;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates a grid frame and a detail frame accessed by double click on the grid.
 * Detail form shows also how to restore last valid code in a lookup control when code validation returns an error.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication {


  Connection conn = null;



  public ClientApplication() {

    Hashtable domains = new Hashtable();
    Properties props = new Properties();
    props.setProperty("uri","URI");
    props.setProperty("linkLabel","Link");
    props.setProperty("this text will\nbe translated","This text will\nbe translated");
    props.setProperty("date","Date");
    props.setProperty("combobox","Combobox");
    props.setProperty("opened","Opened");
    props.setProperty("suspended","Suspended");
    props.setProperty("delivered","Delivered");
    props.setProperty("closed","Closed");
    props.setProperty("radio button","Radio Button");
    props.setProperty("stringValue","Text");
    props.setProperty("dateValue","Date");
    props.setProperty("checkValue","CheckBox");
    props.setProperty("radioButtonValue","RadioButton");
    props.setProperty("comboValue","ComboBox");
    props.setProperty("currencyValue","Currency");
    props.setProperty("numericValue","Number");
    props.setProperty("lookupValue","Lookup Code");
    props.setProperty("descrLookupValue","Lookup Description");
    props.setProperty("taValue","Text Area");
    props.setProperty("formattedTextValue","Formatted Text");
    props.setProperty("combo","Combo");
    props.setProperty("listValues","List Values");
    props.setProperty("item0","Item0");
    props.setProperty("item1","Item1");
    props.setProperty("item2","Item2");
    props.setProperty("item3","Item3");
    props.setProperty("item4","Item4");

    ButtonsAuthorizations auth = new ButtonsAuthorizations();
    auth.addButtonAuthorization("F1",true,false,true);

    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("$",props,false),
        domains,
        auth
    );
    ClientSettings.VIEW_MANDATORY_SYMBOL = true;
    ClientSettings.FILTER_PANEL_ON_GRID = true;
    ClientSettings.VIEW_BACKGROUND_SEL_COLOR = true;
    ClientSettings.SHOW_SORTING_ORDER = true;
    ClientSettings.SHOW_FOCUS_BORDER_ON_FORM = false;
    ClientSettings.SHOW_NAVIGATOR_BAR_IN_LOOKUP = true;
    ClientSettings.AS_TAB = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);

//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "com.jgoodies.looks.plastic.PlasticXPLookAndFeel";
//com.jgoodies.looks.plastic.PlasticLookAndFeel
//com.jgoodies.looks.plastic.Plastic3DLookAndFeel
    ClientSettings.LOOKUP_AUTO_COMPLETITION_WAIT_TIME = 1000;
    ClientSettings.AUTO_FIT_COLUMNS = true;

    Domain orderStateDomain = new Domain("ORDERSTATE");
    ComboVO comboVO = null;
    comboVO = new ComboVO(); comboVO.setCode("O"); comboVO.setDescription("opened"); orderStateDomain.addDomainPair(comboVO,comboVO.getDescription());
    comboVO = new ComboVO(); comboVO.setCode("S"); comboVO.setDescription("suspended"); orderStateDomain.addDomainPair(comboVO,comboVO.getDescription());
    comboVO = new ComboVO(); comboVO.setCode("D"); comboVO.setDescription("delivered"); orderStateDomain.addDomainPair(comboVO,comboVO.getDescription());
    comboVO = new ComboVO(); comboVO.setCode("C"); comboVO.setDescription("closed"); orderStateDomain.addDomainPair(comboVO,comboVO.getDescription());
    domains.put(
      orderStateDomain.getDomainId(),
      orderStateDomain
    );

    Domain listValuesDomain = new Domain("LISTVALUES");
    listValuesDomain.addDomainPair("I0","item0");
    listValuesDomain.addDomainPair("I1","item1");
    listValuesDomain.addDomainPair("I2","item2");
    listValuesDomain.addDomainPair("I3","item3");
    listValuesDomain.addDomainPair("I4","item4");
    domains.put(
      listValuesDomain.getDomainId(),
      listValuesDomain
    );



    createConnection();


    new GridFrameController(conn);
  }


  public static void main(String[] argv) {
    new ClientApplication();
  }


  /**
   * Create the database connection (using Hypersonic DB - in memory) and initialize tables...
   */
  private void createConnection() {
    try {
      Class.forName("org.hsqldb.jdbcDriver");
      conn = DriverManager.getConnection("jdbc:hsqldb:mem:"+"a"+Math.random(),"sa","");
      PreparedStatement stmt = null;
      try {
        stmt = conn.prepareStatement("create table DEMO4(TEXT VARCHAR,FORMATTED_TEXT VARCHAR,DECNUM DECIMAL(10,2),CURRNUM DECIMAL(10,2),THEDATE DATE,COMBO VARCHAR,CHECK_BOX CHAR(1),RADIO CHAR(1),CODE VARCHAR,TA VARCHAR,URI VARCHAR,LINK_LABEL VARCHAR,PRIMARY KEY(TEXT))");
        stmt.execute();
        stmt.close();

        stmt = conn.prepareStatement("create table DEMO4_LOOKUP(CODE VARCHAR,DESCRCODE VARCHAR,PRIMARY KEY(CODE))");
        stmt.execute();
        stmt.close();

        stmt = conn.prepareStatement("create table DEMO4_LIST_VALUES(TEXT VARCHAR,CODE VARCHAR,PRIMARY KEY(TEXT,CODE))");
        stmt.execute();
        stmt.close();

        for(int i=0;i<200;i++) {
          stmt = conn.prepareStatement("insert into DEMO4 values('ABC"+getCode(3,i+1)+"',null,"+12+i+0.333+","+1234+i+0.56+",?,'C','Y','Y','A"+i+"','AAAAAA"+i+"','https://sourceforge.net/forum/message.php?msg_id="+(5612560+i)+"','Link "+(i+1)+"')");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()+86400000*i));
          stmt.execute();
          stmt.close();

          stmt = conn.prepareStatement("insert into DEMO4_LIST_VALUES values('ABC"+getCode(3,i+1)+"','I0')");
          stmt.execute();
          stmt.close();
          stmt = conn.prepareStatement("insert into DEMO4_LIST_VALUES values('ABC"+getCode(3,i+1)+"','I1')");
          stmt.execute();
          stmt.close();
        }

        for(int i=0;i<200;i++) {
          stmt = conn.prepareStatement("insert into DEMO4_LOOKUP values('A"+i+"','ABCDEF"+i+"')");
          stmt.execute();
          stmt.close();
        }


      }
      catch (SQLException ex1) {
        ex1.printStackTrace();
      }
      finally {
        try {
          stmt.close();
        }
        catch (Exception ex2) {
        }
      }

      conn.commit();

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  private String getCode(int len,int num) {
    String code = String.valueOf(num);
    for(int i=code.length();i<len;i++)
      code = "0"+code;
    return code;
  }


}
