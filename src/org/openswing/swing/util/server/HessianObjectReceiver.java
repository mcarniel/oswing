package org.openswing.swing.util.server;

import java.io.*;
import javax.servlet.http.*;

import org.openswing.swing.message.send.java.*;
import com.caucho.hessian.io.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Base class used to comunicate with the the client-side layer, via HTTP.
 * it provides two utility methods: one to receive a service request through an ObjectInputStream and
 * the other to send a serialized object as response to that service request.
 * IO streams are wrapped through Hessian library.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class HessianObjectReceiver extends ObjectReceiver {


  /**
   * Receive a serialized object, through the provided HTTP request.
   * Object is deserialized using Hessian library.
   * @param request HTTP request that containes the serialized object
   * @return service request
   */
  public final Command getObjectFromRequest(HttpServletRequest request) throws Exception {
    InputStream is = null;
      HessianInput in = null;
    try {
      is = request.getInputStream();
      in = new HessianInput(is);
      try {
        return (Command)in.readObject();
      }
      catch (Throwable ex) {
        throw new Exception(ex.getMessage());
      }
    }
    finally {
      try {
        if (is!=null)
          is.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * Send a serialized object as response to a service request, through the provided HTTP response.
   * Object is serialized using Hessian library.
   * @param response HTTP response used to send the response object
   * @param objectToSend serialized object to send as response to the service request
   */
  public final void setObjectToResponse(HttpServletResponse response,Object objectToSend) throws Exception {
    OutputStream os = null;
    try {
      os = response.getOutputStream();
      HessianOutput out = new HessianOutput(os);
      out.writeObject(objectToSend);
    }
    finally {
      try {
        if (os!=null)
          os.close();
      }
      catch (Exception ex) {
      }
    }
  }



}
