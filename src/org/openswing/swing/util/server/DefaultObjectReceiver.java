package org.openswing.swing.util.server;

import java.io.*;
import javax.servlet.http.*;

import org.openswing.swing.message.send.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Base class used to comunicate with the the client-side layer, via HTTP.
 * it provides two utility methods: one to receive a service request through an ObjectInputStream and
 * the other to send a serialized object as response to that service request.</p>
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
public class DefaultObjectReceiver extends ObjectReceiver {


  /**
   * Receive a serialized object, through the provided HTTP request.
   * @param request HTTP request that containes the serialized object
   * @return service request
   */
  public final Command getObjectFromRequest(HttpServletRequest request) throws Exception {
    ObjectInputStream ois = null;
    try {
      ois = new ObjectInputStream(request.getInputStream());
      return (Command) ois.readObject();
    }
    finally {
      try {
        if (ois!=null)
          ois.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * Send a serialized object as response to a service request, through the provided HTTP response.
   * @param response HTTP response used to send the response object
   * @param objectToSend serialized object to send as response to the service request
   */
  public final void setObjectToResponse(HttpServletResponse response,Object objectToSend) throws Exception {
    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(response.getOutputStream());
      oos.writeObject(objectToSend);
    }
    finally {
      try {
        if (oos!=null)
          oos.close();
      }
      catch (Exception ex) {
      }
    }
  }





}
