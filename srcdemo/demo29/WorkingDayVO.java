package demo29;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value object that stores a working day.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @version 1.0
 */
public class WorkingDayVO extends ValueObjectImpl {

  private java.math.BigDecimal day;
  private String empCode;
  private java.sql.Timestamp startMorningHour;
  private java.sql.Timestamp endMorningHour;
  private java.sql.Timestamp startAfternoonHour;
  private java.sql.Timestamp endAfternoonHour;
  public WorkingDayVO() {
  }
  public java.math.BigDecimal getDay() {
    return day;
  }
  public void setDay(java.math.BigDecimal day) {
    this.day = day;
  }
  public String getEmpCode() {
    return empCode;
  }
  public void setEmpCode(String empCode) {
    this.empCode = empCode;
  }
  public java.sql.Timestamp getStartMorningHour() {
    return startMorningHour;
  }
  public void setStartMorningHour(java.sql.Timestamp startMorningHour) {
    this.startMorningHour = startMorningHour;
  }
  public java.sql.Timestamp getEndMorningHour() {
    return endMorningHour;
  }
  public void setEndMorningHour(java.sql.Timestamp endMorningHour) {
    this.endMorningHour = endMorningHour;
  }
  public java.sql.Timestamp getStartAfternoonHour() {
    return startAfternoonHour;
  }
  public void setStartAfternoonHour(java.sql.Timestamp startAfternoonHour) {
    this.startAfternoonHour = startAfternoonHour;
  }
  public java.sql.Timestamp getEndAfternoonHour() {
    return endAfternoonHour;
  }
  public void setEndAfternoonHour(java.sql.Timestamp endAfternoonHour) {
    this.endAfternoonHour = endAfternoonHour;
  }



}
