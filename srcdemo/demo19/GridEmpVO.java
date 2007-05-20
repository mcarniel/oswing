package demo19;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value Object used to store employee summary info.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridEmpVO extends ValueObjectImpl {

  private String empCode;
  private String lastName;
  private String firstName;
  private TaskVO taskVO;


  public GridEmpVO() {
  }


  public String getEmpCode() {
    return empCode;
  }
  public void setEmpCode(String empCode) {
    this.empCode = empCode;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public TaskVO getTaskVO() {
    if (taskVO==null)
      taskVO = new TaskVO();
    return taskVO;
  }
  public void setTaskVO(TaskVO taskVO) {
    this.taskVO = taskVO;
  }


//  public String getTaskCode() {
//    return getTaskVO().getTaskCode();
//  }
//  public void setTaskCode(String taskCode) {
//    this.getTaskVO().setTaskCode(taskCode);
//  }
//  public String getDescription() {
//    return getTaskVO().getDescription();
//  }
//  public void setDescription(String description) {
//    this.getTaskVO().setDescription(description);
//  }
//  public String getStatus() {
//    return getTaskVO().getStatus();
//  }
//  public void setStatus(String status) {
//    this.getTaskVO().setStatus(status);
//  }




  /**
   * Method used by Grid and Form objects to internally duplicate a value object.
   * @return
   * @throws java.lang.CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException {
    GridEmpVO vo = (GridEmpVO)super.clone();
    if (taskVO!=null)
      vo.setTaskVO((TaskVO)taskVO.clone());
    return vo;
  }





}
