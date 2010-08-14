package org.openswing.swing.mdi.client;

import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Interface called by MDIFrame to:
 * - correctly close the application
 * - logon into the system
 * - view application functions (inside a tree or as menu items in the menubar)
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
public interface MDIController {


  /**
   * Method called after MDI creation.
   */
  public void afterMDIcreation(MDIFrame frame);


  /**
   * @return application title
   */
  public String getMDIFrameTitle();


  /**
   * @see JFrame getExtendedState method
   */
  public int getExtendedState();


  /**
   * @return text to view in the about dialog window
   */
  public String getAboutText();


  /**
   * @return image name to view in the about dialog window (optional); if null then the about window will view only "getAboutText" message
   */
  public String getAboutImage();


  /**
   * Method used to destroy application.
   */
  public void stopApplication();


  /**
   * @return <code>true</code> if the MDI frame must show a change language menu in the menubar, <code>false</code> no change language menu item will be added
   */
  public boolean viewChangeLanguageInMenuBar();


  /**
   * @return list of languages supported by the application (Language objects)
   */
  public ArrayList getLanguages();



  /**
   * @return <code>true</code> if the MDI frame must show a login menu in the menubar, <code>false</code> no login menu item will be added
   */
  public boolean viewLoginInMenuBar();


  /**
   * @param parentFrame parent frame
   * @return a dialog window to logon the application; the method can return null if viewLoginInMenuBar returns false
   */
  public JDialog viewLoginDialog(JFrame parentFrame);


  /**
   * Defines if application functions must be viewed inside a tree panel of MDI Frame.
   * @return <code>true</code> if application functions must be viewed inside a tree panel of MDI Frame, <code>false</code> no tree is viewed
   */
  public boolean viewFunctionsInTreePanel();


  /**
   * Defines if application functions must be viewed in the menubar of MDI Frame.
   * @return <code>true</code> if application functions must be viewed in the menubar of MDI Frame, <code>false</code> otherwise
   */
  public boolean viewFunctionsInMenuBar();


  /**
   * @return client facade, invoked by the MDI Frame tree/menu
   */
  public ClientFacade getClientFacade();


  /**
   * @return application functions (ApplicationFunction objects), organized as a tree
   */
  public DefaultTreeModel getApplicationFunctions();


  /**
   * @return <code>true</code> if the MDI frame must show a panel in the bottom, containing last opened window icons, <code>false</code> no panel is showed
   */
  public boolean viewOpenedWindowIcons();


  /**
   * @return <code>true</code> if the MDI frame must show the "File" menu in the menubar of the frame, <code>false</code> to hide it
   */
  public boolean viewFileMenu();

}
