package org.openswing.swing.util.client;

import java.beans.*;
import java.net.*;
import java.util.*;
import javax.imageio.*;
import javax.jnlp.*;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.form.client.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.tree.client.TreePanel;
import org.openswing.swing.tree.client.TreeGridPanel;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.lang.reflect.Method;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


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

  /** sender used inside "getData" method to comunicate with a remote site via HTTP; default value: "DefaultObjectSender" */
  private static ObjectSender defaultSender = new DefaultObjectSender();


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
    if (comp==null)
      return null;
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
   * @param comp graphic component which is contained into a JDialog/JFrame
   * @return JDialog/JFrame object which contains the graphic component
   */
  public static Window getParentWindow(JComponent comp) {
    if (comp==null)
      return null;
    Container parentFrame = comp.getParent();
    while(parentFrame!=null) {
      if (parentFrame instanceof JInternalFrame) {
        parentFrame = ((JInternalFrame)parentFrame).getDesktopPane().getParent();
      }
      else if (parentFrame instanceof JDialog) {
        return (Window)parentFrame;
      }
      else if (parentFrame instanceof JFrame) {
        return (Window)parentFrame;
      }
      parentFrame = parentFrame.getParent();
    }
    return null;
  }


  /**
   * @param comp graphic component which is contained into a JInternalFrame
   * @return JInternalFrame object which contains the graphic component
   */
  public static JInternalFrame getParentInternalFrame(JComponent comp) {
    Container parentFrame = comp.getParent();
    while(parentFrame!=null) {
      if (parentFrame instanceof JInternalFrame)
        return (JInternalFrame)parentFrame;
      parentFrame = parentFrame.getParent();
    }
    return null;
  }


  /**
   * @param imageName image name; must be stored in the "image" folder accessible by the classpath
   * @return Image object
   */
  public static Image getImage(String imageName) {
    return getImage(imageName,MDIFrame.class);
  }


  /**
   * @param imageName image name; must be stored in the "image" folder accessible by the classpath
   * @return class already instantiated
   * @return Image object
   */
  public static Image getImage(String imageName,Class clazz) {
    if (Beans.isDesignTime())
      return new ImageIcon(MDIFrame.class.getResource("/images/"+imageName)).getImage();

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
        i = ImageIO.read(clazz.getResource("/images/"+imageName));
      else
        i = new ImageIcon(clazz.getResource("/images/"+imageName)).getImage();
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
  public static void centerDialog(Window parentFrame,JDialog d) {
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
  public static void centerWindow(Window parentFrame,Window w) {
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
  public static void centerFrame(Window frame) {
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
  public static String getServerURL() throws Exception {
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
  public static final Response getData(String serverMethodName,Object param) {
    try {
      synchronized(busyListeners) {
        try {
          for(int i=0;i<busyListeners.size();i++)
            ((BusyListener)busyListeners.get(i)).setBusy(true);
        }
        catch (Exception ex1) {
        }
      }
      String servletURL = getServerURL();
      URLConnection urlC = new URL(servletURL).openConnection();

      urlC.setRequestProperty ("Content-Type","application/octet-stream");

      // set POST method...
      urlC.setDoOutput(true);

      Response response = defaultSender.sendRequest(urlC,new Command(sessionId,serverMethodName,param));

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

      synchronized(busyListeners) {
        try {
          for(int i=0;i<busyListeners.size();i++)
            ((BusyListener)busyListeners.get(i)).setBusy(false);
        }
        catch (Exception ex1) {
        }
      }

      return response;
    }
    catch (Throwable ex) {
      synchronized(busyListeners) {
        try {
          for(int i=0;i<busyListeners.size();i++)
            ((BusyListener)busyListeners.get(i)).setBusy(false);
        }
        catch (Exception ex1) {
        }
      }

//      ex.printStackTrace();
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
      if (!url.toLowerCase().startsWith("file://") &&
          !url.toLowerCase().startsWith("http://") &&
          !url.toLowerCase().startsWith("https://"))
        url = "file://"+url;
      boolean windows = false;
      boolean mac = false;
      String os = System.getProperty("os.name");
      if ( os != null && os.startsWith("Windows"))
        windows = true;
      if ( os != null && os.toLowerCase().indexOf("mac")!=-1)
        mac = true;

      String cmd = null;
      try {
        if (windows) {
          // cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
          cmd = "rundll32 url.dll,FileProtocolHandler " + url;
          Process p = Runtime.getRuntime().exec(cmd);
        }
        else if (mac) {
          try {
            Class clazz = Class.forName("com.apple.mrj.MRJFileUtils");
            clazz.getMethod("openURL",new Class[]{String.class}).invoke(null,new Object[]{url});
          }
          catch (Throwable ex) {
            String[] commandLine = {"netscape", url};
            Process process = Runtime.getRuntime().exec(commandLine);
          }
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
      catch(Throwable ex) {
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
    synchronized(busyListeners) {
      busyListeners.add(busyListener);
    }
  }


  /**
   * Remove a listener of busy state events.
   * @param busyListener listener of busy state events
   */
  public static final void removeBusyListener(BusyListener busyListener) {
    synchronized(busyListeners) {
      busyListeners.remove(busyListener);
    }
  }


  public static final void fireBusyEvent(boolean busy) {
    synchronized(busyListeners) {
      try {
        for(int i=0;i<busyListeners.size();i++)
          ((BusyListener)busyListeners.get(i)).setBusy(busy);
      }
      catch (Exception ex1) {
      }
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


  /**
   * Set the sender to use inside "getData" method to comunicate with a remote site via HTTP.
   * @param objectSender ObjectSender to set
   */
  public static final void setObjectSender(ObjectSender objectSender) {
    defaultSender = objectSender;
  }


  /**
   * Remove components no more used.
   * @param components to analyze in order to remove existing bindings and avoid memory leaks
   */
  public static final void disposeComponents(Component[] c) {
    for(int i=0;i<c.length;i++) {
      if (c[i] instanceof Form) {
        ((Form)c[i]).finalize();
      }
      else if (c[i] instanceof GridControl) {
        ((GridControl)c[i]).finalize();
      }
      else if (c[i] instanceof InputControl) {
        InputControl ic = (InputControl)c[i];
        if (ic instanceof JComponent) {
          JComponent cc = (JComponent)ic;
          FocusListener[] fl = cc.getFocusListeners();
          for(int j=0;j<fl.length;j++)
            cc.removeFocusListener(fl[j]);
          MouseListener[] ml = cc.getMouseListeners();
          for(int j=0;j<ml.length;j++)
            cc.removeMouseListener(ml[j]);
          KeyListener[] ll = cc.getKeyListeners();
          for(int j=0;j<ll.length;j++)
            cc.removeKeyListener(ll[j]);
        }
        if (ic instanceof BaseInputControl) {
          JComponent cc = ((BaseInputControl)ic).getBindingComponent();
          if (cc!=null) {
            FocusListener[] fl = cc.getFocusListeners();
            for(int j=0;j<fl.length;j++)
              cc.removeFocusListener(fl[j]);
            MouseListener[] ml = cc.getMouseListeners();
            for(int j=0;j<ml.length;j++)
              cc.removeMouseListener(ml[j]);
            KeyListener[] ll = cc.getKeyListeners();
            for(int j=0;j<ll.length;j++)
              cc.removeKeyListener(ll[j]);
          }
        }
        if (ic instanceof  DateControl) {
          ((DateControl)ic).finalize();
        }

      }
      else if (c[i] instanceof TreePanel) {
      }
      else if (c[i] instanceof TreeGridPanel) {
      }
      else if (c[i] instanceof JComponent) {
        disposeComponents(((JComponent)c[i]).getComponents());

        JComponent cc = (JComponent)c[i];
        FocusListener[] fl = cc.getFocusListeners();
        for(int j=0;j<fl.length;j++)
          cc.removeFocusListener(fl[j]);
        MouseListener[] ml = cc.getMouseListeners();
        for(int j=0;j<ml.length;j++)
          cc.removeMouseListener(ml[j]);
        KeyListener[] ll = cc.getKeyListeners();
        for(int j=0;j<ll.length;j++)
          cc.removeKeyListener(ll[j]);
      }
    }


    for(int i=0;i<c.length;i++) {
      try {
        if (c[i].getParent() != null) {
          c[i].getParent().removeAll();
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }

  }


  /**
   * @param value data to check
   * @param targetType type of data
   * @return object to return, i.e. the orginal data, if it has the type specified by "targetType" (or some subtype) otherwise its convertion
   */
  public static final Object convertObject(Object value,Class targetType) {
    if (value==null)
      return value;
    if (value!=null && !targetType.isAssignableFrom(value.getClass())) {
      try {
        if (value instanceof Number) {
          if (targetType==Integer.TYPE)
            value = new Integer(((Number)value).intValue());
          else if (targetType==Long.TYPE)
            value = new Long(((Number)value).longValue());
          else if (targetType==Float.TYPE)
            value = new Float(((Number)value).floatValue());
          else if (targetType==Double.TYPE)
            value = new Double(((Number)value).doubleValue());
          else if (targetType==Short.TYPE)
            value = new Short(((Number)value).shortValue());
          else
            value = new Long(((Number)value).longValue());
          value = targetType.getConstructor(new Class[] {String.class}).newInstance(new Object[] {value.toString()});
        }
        else if (value instanceof java.util.Date) {
          if (targetType.equals(java.sql.Date.class))
            value = new java.sql.Date(((java.util.Date)value).getTime());
          else if (targetType.equals(java.sql.Timestamp.class))
            value = new java.sql.Timestamp(((java.util.Date)value).getTime());
        }
      }
      catch (Throwable ex1) {
      }
    }
    return value;
  }


  /**
   * @param vo ValueObject to analyze
   * @param attributeName v.o. attribute name
   * @return value stored in v.o. related to the specified attribute name
   */
  public static Object getValue(ValueObject vo,String attributeName) throws Exception {
    String aName = attributeName;
    Method getter = null;
    Class clazz = vo.getClass();
    Object obj = vo;
    while(aName.indexOf(".")!=-1) {
      try {
        getter = clazz.getMethod(
          "get" +
          aName.substring(0, 1).
          toUpperCase() +
          aName.substring(1,aName.indexOf(".")),
          new Class[0]
        );
      }
      catch (NoSuchMethodException ex2) {
        getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1,aName.indexOf(".")),new Class[0]);
      }
      obj = getter.invoke(obj,new Object[0]);
      if (obj==null)
        break;
      aName = aName.substring(aName.indexOf(".")+1);
      clazz = getter.getReturnType();
    }

    if (obj!=null) {
      try {
        getter = clazz.getMethod(
          "get" +
          aName.substring(0, 1).
          toUpperCase() +
          aName.substring(1),
          new Class[0]
        );
      }
      catch (NoSuchMethodException ex2) {
        getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1),new Class[0]);
      }
      obj = getter.invoke(obj,new Object[0]);
    }
    return obj;
  }


  /**
   * Utility method invoked by input controls in order to listen for a AS_TAB key event to translate in a TAB key event,
   * to transfer focus to next component.
   * @param c input control to whose add a KeyListener
   */
  public static void addTabListener(final JComponent c) {
    if (ClientSettings.AS_TAB!=null &&
       (ClientSettings.USE_AS_TAB_IN_TEXTAREA ||
       !(c instanceof org.openswing.swing.client.TextAreaControl || c instanceof javax.swing.JTextArea)
       )) {
      c.addKeyListener(new KeyAdapter() {

        public void keyReleased(KeyEvent e) {
          if (e.getKeyCode()==ClientSettings.AS_TAB.getKeyCode() &&
              e.getModifiers()+e.getModifiersEx()==ClientSettings.AS_TAB.getModifiers()) {
            c.transferFocus();
          } // end if
        } // end keyReleased method

      });
    } // end if
  }

}
