package demo4;

import java.util.ArrayList;
import java.math.BigDecimal;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value object used for the detail Form.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DetailTestVO extends TestVO{


  private ArrayList listValues;

  private BigDecimal year;


  public DetailTestVO() {
  }


  public ArrayList getListValues() {
    return listValues;
  }
  public void setListValues(ArrayList listValues) {
    this.listValues = listValues;
  }
  public BigDecimal getYear() {
    return year;
  }
  public void setYear(BigDecimal year) {
    this.year = year;
  }


}
