package demo5.server;

import org.openswing.swing.server.Action;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.server.UserSessionParameters;
import org.openswing.swing.server.ConnectionManager;
import demo5.java.TestVO;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class DetailInsertAction implements Action {
  public DetailInsertAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertDetail";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement stmt = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);
      stmt = conn.prepareStatement("insert into DEMO5(TEXT,DECNUM,CURRNUM,THEDATE,COMBO,CHECK,RADIO,CODE,TA) values(?,?,?,?,?,?,?,?,?)");
      TestVO vo = (TestVO)inputPar;
      stmt.setObject(6,vo.getCheckValue()==null || !vo.getCheckValue().booleanValue() ? "N":"Y");
      stmt.setString(5,vo.getComboValue());
      stmt.setBigDecimal(3,vo.getCurrencyValue());
      stmt.setDate(4,vo.getDateValue());
      stmt.setBigDecimal(2,vo.getNumericValue());
      stmt.setObject(7,vo.getRadioButtonValue()==null || !vo.getRadioButtonValue().booleanValue() ? "N":"Y");
      stmt.setString(1,vo.getStringValue());
      stmt.setString(8,vo.getLookupValue());
      stmt.setString(9,vo.getTaValue());
      stmt.execute();
      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
        conn.commit();
      }
      catch (SQLException ex1) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex2) {
      }
    }


  }


}
