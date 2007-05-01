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

public class DetailLoadAction implements Action {
  public DetailLoadAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadDetail";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Statement stmt = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);
      stmt = conn.createStatement();
      String pk = (String)inputPar;
      ResultSet rset = stmt.executeQuery("select DEMO5.TEXT,DEMO5.DECNUM,DEMO5.CURRNUM,DEMO5.THEDATE,DEMO5.COMBO,DEMO5.CHECK_BOX,DEMO5.RADIO,DEMO5.CODE,DEMO5_LOOKUP.DESCRCODE,DEMO5.TA from DEMO5,DEMO5_LOOKUP where TEXT='"+pk+"' and DEMO5.CODE=DEMO5_LOOKUP.CODE");
      if (rset.next()) {
        TestVO vo = new TestVO();
        vo.setCheckValue(rset.getObject(6)==null || !rset.getObject(6).equals("Y") ? Boolean.FALSE:Boolean.TRUE);
        vo.setComboValue(rset.getString(5));
        vo.setCurrencyValue(rset.getBigDecimal(3));
        vo.setDateValue(rset.getDate(4));
        vo.setNumericValue(rset.getBigDecimal(2));
        vo.setRadioButtonValue(rset.getObject(7)==null || !rset.getObject(7).equals("Y") ? Boolean.FALSE:Boolean.TRUE);
        vo.setStringValue(rset.getString(1));
        vo.setLookupValue(rset.getString(8));
        vo.setDescrLookupValue(rset.getString(9));
        vo.setTaValue(rset.getString(10));
        return new VOResponse(vo);
      }
      else
        return new ErrorResponse("No data found.");
    }
    catch (Throwable ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
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
