package demo32;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Test Value Object.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ContactsVO extends ValueObjectImpl {

  private String name;
  private String emailAddress;
  private String surname;


  public ContactsVO() {
  }


  public String getName() {
    return name;
  }
  public String getSurname() {
    return surname;
  }
  public void setName(String name) {
    this.name = name;
  }
  public void setSurname(String surname) {
    this.surname = surname;
  }
  public String getEmailAddress() {
    return emailAddress;
  }
  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }



}
