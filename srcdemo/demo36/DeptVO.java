package demo36;

import demo36.auto._DeptVO;
import org.openswing.swing.message.receive.java.ValueObject;

public class DeptVO extends _DeptVO  implements ValueObject {

  /**
   * Method used by Grid and Form objects to internally duplicate a value object.
   * @return
   * @throws java.lang.CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }


}



