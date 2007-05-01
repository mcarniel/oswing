package demo5.server;

import org.openswing.swing.server.Action;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.UserSessionParameters;
import demo5.java.TestVO;
import org.openswing.swing.server.QueryUtil;
import java.sql.Connection;
import org.openswing.swing.server.ConnectionManager;
import java.sql.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class DetailUpdateAction implements Action {
  public DetailUpdateAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateDetail";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);
      HashMap map = new HashMap();
      map.put("stringValue","TEXT");
      map.put("numericValue","DECNUM");
      map.put("currencyValue","CURRNUM");
      map.put("dateValue","THEDATE");
      map.put("comboValue","COMBO");
      map.put("checkValue","CHECK_BOX");
      map.put("radioButtonValue","RADIO");
      map.put("lookupValue","CODE");
      map.put("taValue","TA");

      HashSet pk = new HashSet();
      pk.add("stringValue");

      ValueObject oldVO = ((ValueObject[])inputPar)[0];
      ValueObject newVO = ((ValueObject[])inputPar)[1];
      return QueryUtil.updateTable(
          conn,
          userSessionPars,
          pk,
          oldVO,
          newVO,
          "DEMO5",
          map,
          "Y",
          "N",
          context,
          true
      );
    }
    catch (Throwable ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        conn.commit();
      }
      catch (SQLException ex2) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }


}
