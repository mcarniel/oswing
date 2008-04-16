package demo36;

import demo36.auto._EmpVO;
import org.openswing.swing.message.receive.java.ValueObject;

public class EmpVO extends _EmpVO implements ValueObject {

  private demo36.DeptVO dept;
  private demo36.TasksVO task;

  public void setDept(demo36.DeptVO dept) {
      this.dept = dept;
  }

  public demo36.DeptVO getDept() {
      return dept;
  }


  public void setTask(demo36.TasksVO task) {
      this.task = task;
  }

  public demo36.TasksVO getTask() {
      return task;
  }


  /**
   * Method used by Grid and Form objects to internally duplicate a value object.
   * @return
   * @throws java.lang.CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }


}



