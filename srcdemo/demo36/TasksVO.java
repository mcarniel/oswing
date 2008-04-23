package demo36;

import demo36.auto._TasksVO;
import org.openswing.swing.message.receive.java.ValueObject;

public class TasksVO extends _TasksVO  implements ValueObject {

  /**
   * Method used by Grid and Form objects to internally duplicate a value object.
   * @return
   * @throws java.lang.CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }


}



