package demo26;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value Object used for the ListVOControl.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class TestListVO extends ValueObjectImpl {

  private String pricelistCode;
  private String description;
  private String note;
  private java.sql.Date startDate;
  private java.sql.Date endDate;


  public TestListVO() {
  }


  public String getDescription() {
    return description;
  }
  public String getNote() {
    return note;
  }
  public String getPricelistCode() {
    return pricelistCode;
  }
  public java.sql.Date getStartDate() {
    return startDate;
  }
  public java.sql.Date getEndDate() {
    return endDate;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public void setEndDate(java.sql.Date endDate) {
    this.endDate = endDate;
  }
  public void setNote(String note) {
    this.note = note;
  }
  public void setPricelistCode(String pricelistCode) {
    this.pricelistCode = pricelistCode;
  }
  public void setStartDate(java.sql.Date startDate) {
    this.startDate = startDate;
  }





}
