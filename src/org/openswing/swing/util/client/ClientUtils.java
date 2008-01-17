package org.openswing.swing.util.client;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.jnlp.*;
import org.openswing.swing.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.domains.java.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.domains.java.*;
import org.openswing.swing.logger.client.Logger;
import org.openswing.swing.form.client.Form;
import java.beans.PropertyDescriptor;
import java.beans.Introspector;
import javax.imageio.ImageIO;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Help singleton class used to comunicate with the external environment,
 * like the server side (via HTTP) or to fetch external resources (XML, images).
 * It is also the main applet to execute: so this class can be derived to access to applet methods (start method...)</p>
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
public class ClientUtils extends JApplet {

  /** server URL */
  private static String serverUrl;

  /** session id of the current client application */
  private static String sessionId = null;

  /** Java Applet */
  private static JApplet applet = null;

  /** list of listener of busy state events */
  private static ArrayList busyListeners = new ArrayList();


  public final void init() {
    applet = this;
    serverUrl = getParameter("SERVERURL");
  }


  public ClientUtils() { }


  /**
   * @param comp graphic component which is contained into a JFrame
   * @return JFrame object which contains the graphic component
   */
  public static JFrame getParentFrame(JComponent comp) {
    Container parentFrame = comp.getParent();
    while(parentFrame!=null) {
      if (parentFrame instanceof JInternalFrame) {
        parentFrame = ((JInternalFrame)parentFrame).getDesktopPane().getParent();
      }
      else if (parentFrame instanceof JFrame) {
        return (JFrame)parentFrame;
      }
      parentFrame = parentFrame.getParent();
    }
    return null;
  }


  /**
   * @param imageName image name; must be stored in the "image" folder accessible by the classpath
   * @return Image object
   */
  public static Image getImage(String imageName) {
    try {
      Image i = null;

      Class jimi = null;
      try {
        jimi = Class.forName("com.sun.jimi.core.Jimi");
      }
      catch (ClassNotFoundException ex1) {
      }
      if (jimi!=null &&
          (imageName.toLowerCase().endsWith(".ico") ||
           imageName.toLowerCase().endsWith(".bmp") ||
           imageName.toLowerCase().endsWith(".png") ||
           imageName.toLowerCase().endsWith(".pic") ||
           imageName.toLowerCase().endsWith(".pcx") ||
           imageName.toLowerCase().endsWith(".tif") ||
           imageName.toLowerCase().endsWith(".tiff"))) {
        i = (Image)jimi.getMethod("getImage",new Class[]{URL.class}).invoke(null,new Object[]{MDIFrame.class.getResource("/images/"+imageName)});
      }
      else if (imageName.toLowerCase().endsWith(".tif") ||
               imageName.toLowerCase().endsWith(".tiff") ||
               imageName.toLowerCase().endsWith(".bmp") ||
               imageName.toLowerCase().endsWith(".png"))
        i = ImageIO.read(MDIFrame.class.getResource("/images/"+imageName));
      else
        i = new ImageIcon(MDIFrame.class.getResource("/images/"+imageName)).getImage();
      if (i!=null)
        return i;
    }
    catch (Throwable ex) {
      ex.printStackTrace();
      return null;
    }
    return null;
  }


  /**
   * Place the dialog window in the middle of parentFrame.
   * @param parentFrame parent frame containing the dialog
   * @param d dialog window to center
   */
  public static void centerDialog(JFrame parentFrame,JDialog d) {
    Dimension dim = parentFrame.getSize();
    d.setLocation(new Point(
        parentFrame.getLocation().x+(dim.width-d.getWidth())/2,
        Math.max(0,parentFrame.getLocation().y+(dim.height-d.getHeight())/2))
    );
  }


  /**
   * Place the window in the middle of parentFrame.
   * @param parentFrame parent frame containing the dialog
   * @param w window to center
   */
  public static void centerWindow(JFrame parentFrame,Window w) {
    Dimension dim = parentFrame.getSize();
    w.setLocation(new Point(
        parentFrame.getLocation().x+(dim.width-w.getWidth())/2,
        Math.max(0,parentFrame.getLocation().y+(dim.height-w.getHeight())/2))
    );
  }


  /**
   * Place the window in the middle of the desktop.
   * @param frame frame to center
   */
  public static void centerFrame(JFrame frame) {
    Dimension dim = frame.getSize();
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(
      (int)screen.getWidth()/2-frame.getWidth()/2,
      Math.max(0,(int)screen.getHeight()/2-frame.getHeight()/2)
    );
  }


  /**
   * @return server URL, retrieved by "SERVERURL" parameter in the HTML/JNLP starting app file.
   * </ul>
   */
  private static String getServerURL() throws Exception {
    if (serverUrl!=null)
      return serverUrl;
    try {
      // trying to retrieve "SERVERURL" parameter from JNLP file...
      serverUrl = System.getProperty("SERVERURL");
      return serverUrl;
    }

    catch (Exception ex) {
      String msg = "An error occours on retrieving the server url";
      Logger.error("org.openswing.util.client.ClientUtils","getServerURL",msg,ex);
      throw ex;
    }
  }


  /**
   * Send a request to the server side via HTTP.
   * @param serverMethodName nome metodo lato server da contattare
   * @param param parametro da passare al metodo
   * @return risposta ritornata dal server
   */
  public static synchronized Response getData(String serverMethodName,Object param) {
    try {
      try {
        for(int i=0;i<busyListeners.size();i++)
          ((BusyListener)busyListeners.get(i)).setBusy(true);
      }
      catch (Exception ex1) {
      }
      String servletURL = getServerURL();
      URLConnection urlC = new URL(servletURL).openConnection();

      // set POST method...
      urlC.setDoOutput(true);
      ObjectOutputStream oos = new ObjectOutputStream(urlC.getOutputStream());
      oos.writeObject(new Command(sessionId,serverMethodName,param));
      oos.close();

      ObjectInputStream ois = new ObjectInputStream(urlC.getInputStream());
      Response response = (Response)ois.readObject();
      ois.close();

      // store the session identifier returned by the server side...
      if (sessionId==null || serverMethodName.equals("login"))
        sessionId = response.getSessionId();

//      if (response.isError()) {
//        JOptionPane.showMessageDialog(
//            ClientSettings.getInstance().getApplet(),
//            response.getErrorMessage(),
//            ClientSettings.getInstance().getResources().getResource("Server Error"),
//            JOptionPane.ERROR_MESSAGE
//        );
//      }

      try {
        for(int i=0;i<busyListeners.size();i++)
          ((BusyListener)busyListeners.get(i)).setBusy(false);
      }
      catch (Exception ex1) {
      }

      return response;
    }
    catch (Exception ex) {
      try {
        for(int i=0;i<busyListeners.size();i++)
          ((BusyListener)busyListeners.get(i)).setBusy(false);
      }
      catch (Exception ex1) {
      }

      ex.printStackTrace();
//      JOptionPane.showMessageDialog(
//          ClientSettings.getInstance().getApplet(),
//          ex.getMessage(),
//          ClientSettings.getInstance().getResources().getResource("Server Comunication Error"),
//          JOptionPane.ERROR_MESSAGE
//      );
      return new ErrorResponse(ex.getMessage());
    }
  }


  /**
   * Set the session id associated to the current client
   * @param id session id associated to the current client
   */
  public static void setSessionId(String id) {
    sessionId = id;
  }


  /**
   * Show the specified document in a browser window.
   * @param docId document identifier
   */
  public static void showDocument(String docId) throws Exception {
    URL url = null;
    try {
        // trying to open the document via JNLP APIs...
        url = new URL(getServerURL()+"?sessionId="+sessionId+"&docId="+docId);
        javax.jnlp.BasicService bs = (javax.jnlp.BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
        bs.showDocument(url);
    }
    catch (Exception ex) {
      try {
        if (applet==null)
//              Process p = Runtime.getRuntime().exec("C:/Programmi/mozilla.org/Mozilla/mozilla.exe "+strUrl);
          Runtime.getRuntime().exec("C:/Programmi/Internet Explorer/IEXPLORE.EXE "+url);
        else
          // trying top open the document via Java Applet...
          applet.getAppletContext().showDocument(url,"_blank");
      }
      catch (Exception ex1) {
        String msg = "An error occours while opening the document";
        Logger.error("org.openswing.util.client.ClientUtils","getServerURL",msg,ex1);
        throw ex1;
      }
    }
  }


  /**
   * Show a document in a specified viewer.
   * @param url local URI
   */
  public static void displayURL(String url) {
    try {
      if (url.toLowerCase().startsWith("file://"))
        url = url.substring(7);
      Object desktop = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
      desktop.getClass().getMethod("open",new Class[]{java.io.File.class}).invoke(desktop,new Object[]{new java.io.File(url)});
    }
    catch (Throwable ex1) {
      if (!url.toLowerCase().startsWith("file://"))
        url = "file://"+url;
      boolean windows = false;
      String os = System.getProperty("os.name");
      if ( os != null && os.startsWith("Windows"))
        windows = true;

      String cmd = null;
      try {
        if (windows) {
          // cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
          cmd = "rundll32 url.dll,FileProtocolHandler " + url;
          Process p = Runtime.getRuntime().exec(cmd);
        }
        else {
          // Under Unix, Netscape has to be running for the "-remote"
          // command to work.  So, we try sending the command and
          // check for an exit value.  If the exit command is 0,
          // it worked, otherwise we need to start the browser.
          // cmd = 'netscape -remote openURL(http://www.javaworld.com)'
          cmd = "netscape -remote openURL(" + url + ")";
          Process p = Runtime.getRuntime().exec(cmd);
          try  {
            // wait for exit code -- if it's 0, command worked,
            // otherwise we need to start the browser up.
            int exitCode = p.waitFor();
            if (exitCode != 0) {
              // Command failed, start up the browser
              // cmd = 'netscape http://www.javaworld.com'
              cmd = "netscape "  + url;
              p = Runtime.getRuntime().exec(cmd);
            }
          }
          catch(InterruptedException ex) {
            Logger.error("org.openswing.swing.util.client.ClientUtils", "displayURL", "Error while showing local document (cmd='"+cmd+"':\n"+ex.getMessage(), ex);
          }
        }
      }
      catch(Exception ex) {
        // couldn't exec viewer
        Logger.error("org.openswing.swing.util.client.ClientUtils", "displayURL", "Error while local document (cmd='"+cmd+"':\n"+ex.getMessage(), ex);
      }
    }
  }



  /**
   * @param comp component linked to a Form object
   * @return Form object that manages the current component; null if no Form is linked to the component
   */
  public static Form getLinkedForm(JComponent comp) {
    Container c = comp.getParent();
    HashSet panels = new HashSet();
    Container root = null;
    while(c!=null && !(c instanceof Form)) {
      panels.add(c);
      root = c;
      c = c.getParent();
    }
    if (c!=null)
      return (Form)c;

    // no Form was found in the ancient hierarchy: trying to find it in some sibling...
    return getLinkedForm(root,panels);
  }


  /**
   * @param c root panel used to find a Form inside it
   * @return Form object that manages a panel stored in "panels" set
   */
  private static Form getLinkedForm(Container c,HashSet panels) {
    Component comp = null;
    Iterator it = null;
    Form form = null;
    if (c==null)
      return null;
    for(int i=0;i<c.getComponentCount();i++) {
      comp = c.getComponent(i);
      if (comp instanceof InputControl)
        continue;
      if (comp instanceof Form) {
        it = panels.iterator();
        while(it.hasNext())
          if ( ((Form)comp).containsLinkedPanel((Container)it.next()) )
            return (Form)comp;
      }
      else if (comp instanceof Container) {
        form = getLinkedForm((Container)comp,panels);
        if (form!=null)
          return form;
      }
    }
    return null;
  }


  /**
   * Add a listener of busy state events.
   * @param busyListener listener of busy state events
   */
  public static final void addBusyListener(BusyListener busyListener) {
    busyListeners.add(busyListener);
  }


  /**
   * Remove a listener of busy state events.
   * @param busyListener listener of busy state events
   */
  public static final void removeBusyListener(BusyListener busyListener) {
    busyListeners.remove(busyListener);
  }


  public static final void fireBusyEvent(boolean busy) {
    try {
      for(int i=0;i<busyListeners.size();i++)
        ((BusyListener)busyListeners.get(i)).setBusy(busy);
    }
    catch (Exception ex1) {
    }
  }


  /**
   * @param clazz Class to analyze
   * @param attributeName attribute name defined within the clazz
   * @return PropertyDescriptor property descriptor associated to the specified attribute name
   */
  public static final PropertyDescriptor getPropertyDescriptor(Class clazz,String attributeName) throws Exception {
    PropertyDescriptor[] props = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
    for(int i=0;i<props.length;i++)
      if (props[i].getName().equals(attributeName))
        return props[i];
    return null;
  }



}
