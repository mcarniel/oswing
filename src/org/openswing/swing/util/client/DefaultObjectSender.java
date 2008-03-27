package org.openswing.swing.util.client;

import java.io.*;
import java.net.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Help class used to comunicate with the the server-side layer, via HTTP.
 * it provides a utility method to create an ObjectOutputStream to send serialized object and retrieve the response through an ObjectInputStream.</p>
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
public class DefaultObjectSender extends ObjectSender {


  /**
   * Send a serialized object through the provided connection
   * @param connection URLConnection used to create an ObjectOutputStream to use to send the specified object and an ObjectInputStream to retrieve the response
   * @param objectToSend serialized object to send
   * @return response object
   */
  public final Response sendRequest(URLConnection connection,Command objectToSend) throws Throwable {
    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(connection.getOutputStream());
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

    ObjectInputStream ois = null;
    try {
      ois = new ObjectInputStream(connection.getInputStream());
      return (Response) ois.readObject();
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



}
