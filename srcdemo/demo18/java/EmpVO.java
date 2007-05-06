package demo18.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value Object used to store employee info.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpVO extends GridEmpVO {

  private String sex;
  private BigDecimal salary;
  private Date hireDate;
  private String note;
  private String taskCode;
  private String taskDescription;

  public EmpVO() {
  }


  public BigDecimal getSalary() {
    return salary;
  }
  public Date getHireDate() {
    return hireDate;
  }
  public String getNote() {
    return note;
  }
  public String getSex() {
    return sex;
  }
  public void setSex(String sex) {
    this.sex = sex;
  }
  public void setNote(String note) {
    this.note = note;
  }
  public void setHireDate(Date hireDate) {
    this.hireDate = hireDate;
  }
  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }
  public String getTaskCode() {
    return taskCode;
  }
  public void setTaskCode(String taskCode) {
    this.taskCode = taskCode;
  }
  public String getTaskDescription() {
    return taskDescription;
  }
  public void setTaskDescription(String taskDescription) {
    this.taskDescription = taskDescription;
  }




}
