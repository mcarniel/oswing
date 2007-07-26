package demo4;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value object for the combo box.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ComboVO extends ValueObjectImpl {

  private String code;
  private String description;

  public ComboVO() {
  }
  public String getCode() {
    return code;
  }
  public String getDescription() {
    return description;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public void setDescription(String description) {
    this.description = description;
  }


  /**
   * This method must be overrided to guarantee the objects identity
   */
  public boolean equals(Object o) {
    if (!(o instanceof ComboVO))
     return false;
    ComboVO c = (ComboVO)o;
    if (c.getCode()==null && code!=null || c.getCode()!=null && code==null ||
        c.getDescription()==null && code!=null || c.getDescription()!=null && description==null)
      return false;
    if (c.getCode()!=null && !c.getCode().equals(code) ||
        c.getDescription()!=null && !c.getDescription().equals(description))
      return false;
    return true;
  }


  /**
   * This method must be overrided to guarantee the objects identity, when used with hastable.
   */
  public int hashCode() {
    if (code==null || description==null)
      return super.hashCode();
    return code.hashCode()+description.hashCode();
  }



}