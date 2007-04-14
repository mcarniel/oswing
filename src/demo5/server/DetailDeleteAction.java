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

public class DetailDeleteAction implements Action {
  public DetailDeleteAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "deleteDetail";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = ConnectionManager.getConnection(context);
      stmt = conn.prepareStatement("delete from DEMO5 where TEXT=?");
      TestVO vo = (TestVO)inputPar;
      stmt.setString(1,vo.getStringValue());
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
