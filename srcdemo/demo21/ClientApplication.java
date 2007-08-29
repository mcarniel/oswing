package demo21;

import java.util.*;

import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.domains.java.*;
import java.sql.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates an editable grid frame for properties definition.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication {


  public ClientApplication() {

    Hashtable domains = new Hashtable();
    Properties props = new Properties();
    props.setProperty("date","Date");
    props.setProperty("combobox","Combobox");
    props.setProperty("male","Male");
    props.setProperty("female","Female");
    props.setProperty("stringValue","Text");
    props.setProperty("dateValue","Date");
    props.setProperty("checkValue","CheckBox");
    props.setProperty("comboValue","ComboBox");
    props.setProperty("currencyValue","Currency");
    props.setProperty("numericValue","Number");
    props.setProperty("formattedTextValue","Formatted Text");
    props.setProperty("intValue","Integer Value");
    props.setProperty("...","...");
    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("£",props,true),
        domains
    );

    Domain orderStateDomain = new Domain("SEX");
    orderStateDomain.addDomainPair("M","male");
    orderStateDomain.addDomainPair("F","female");
    domains.put(
      orderStateDomain.getDomainId(),
      orderStateDomain
    );

    new GridFrameController();
  }


  public static void main(String[] argv) {
    new ClientApplication();
  }




}
