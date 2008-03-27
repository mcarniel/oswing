package org.openswing.swing.util.client;

import java.io.*;
import java.net.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import com.caucho.hessian.io.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Base class used to comunicate with the the server-side layer, via HTTP.
 * it provides a utility method to wrap an ObjectOutputStream to send serialized object and retrieve the response through a wrapped ObjectInputStream.</p>
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
public class HessianObjectSender extends ObjectSender {


  /**
   * Send a serialized object through the provided connection; object is serialized using Hessian library
   * @param connection URLConnection used to create a wrapped ObjectOutputStream to use to send the specified object and a wrapped ObjectInputStream to retrieve the response
   * @param objectToSend serialized object to send
   * @return response object
   */
  public final Response sendRequest(URLConnection connection,Command objectToSend) throws Throwable {
    OutputStream os = null;
    try {
      os = connection.getOutputStream();
      HessianOutput hout = new HessianOutput(os);
      hout.writeObject(objectToSend);
    }
    finally {
      try {
        if (os!=null)
          os.close();
      }
      catch (Exception ex) {
      }
    }

    InputStream is = null;
    try {
      is = connection.getInputStream();
      HessianInput hin = new HessianInput(is);
      return (Response) hin.readObject();
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


}
