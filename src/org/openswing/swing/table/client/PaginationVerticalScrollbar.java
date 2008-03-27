package org.openswing.swing.table.client;

import javax.swing.*;
import javax.swing.plaf.*;

import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Vertical scrollbar, used inside the grid control to show pagination buttons.</p>
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
public class PaginationVerticalScrollbar extends JScrollBar {


  public PaginationVerticalScrollbar() {
    super(VERTICAL);
  }


  public void updateUI() {
    if (!ClientSettings.SHOW_PAGINATION_BUTTONS_ON_GRID) {
      super.updateUI();
      return;
    }
    try {
      ScrollBarUI ui = (ScrollBarUI) UIManager.getUI(new JScrollBar());
      if (ui.getClass().getName().endsWith("MetalScrollBarUI")) {
        setUI(MetalPaginationVerticalScrollbarUI.createUI(this));
      }
      else if (ui.getClass().getName().endsWith("MacScrollBarUI")) {
        ComponentUI cui = (ComponentUI)Class.forName("org.openswing.swing.table.client.MacPaginationVerticalScrollBarUI").getMethod("createUI",new Class[]{JComponent.class}).invoke(null,new Object[]{this});
        setUI(cui);
      }
      else {
        setUI(WindowsPaginationVerticalScrollbarUI.createUI(this));
      }
    }
    catch (Throwable ex) {
      setUI(WindowsPaginationVerticalScrollbarUI.createUI(this));
    }
  }


  public JButton getNextPgButton() {
    if (ui instanceof PaginationVerticalScrollbarUI)
      return ((PaginationVerticalScrollbarUI)ui).getNextPgButton();
    else
      return null;
  }


  public JButton getDecrButton() {
    if (ui instanceof PaginationVerticalScrollbarUI)
      return ((PaginationVerticalScrollbarUI)ui).getDecrButton();
    return (JButton)getComponents()[0];
  }


  public JButton getIncrButton() {
    if (ui instanceof PaginationVerticalScrollbarUI)
      return ((PaginationVerticalScrollbarUI)ui).getIncrButton();
    else
      return (JButton)getComponents()[0];
  }


  public JButton getPrevPgButton() {
    if (ui instanceof PaginationVerticalScrollbarUI)
      return ((PaginationVerticalScrollbarUI)ui).getPrevPgButton();
    else
      return null;
  }


}
