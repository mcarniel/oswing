package demo36;

import demo36.auto._EmpVO;
import org.openswing.swing.message.receive.java.ValueObject;

public class EmpVO extends _EmpVO  implements ValueObject {

  /**
   * Method used by Grid and Form objects to internally duplicate a value object.
   * @return
   * @throws java.lang.CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }


}



