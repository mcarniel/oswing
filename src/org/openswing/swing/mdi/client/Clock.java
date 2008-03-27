package org.openswing.swing.mdi.client;

import java.text.*;
import java.util.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Status Panel containing a clock (date + time).</p>
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
public class Clock extends JTextField implements ActionListener {

  private Timer timer = null;
  private SimpleDateFormat dateTimeFormat = null;


  /**
   * Constructor.
   */
  public Clock() {
    super.setOpaque(false);
    super.setEditable(false);
    super.setColumns(14);
    dateTimeFormat = new SimpleDateFormat(ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE_TIME));
    super.setText(dateTimeFormat.format(new Date()));
    timer = new Timer(60000,(ActionListener)this);
    timer.setInitialDelay(0);
    timer.setCoalesce(true);
    timer.start();
  }

  public void actionPerformed(ActionEvent e) {
    super.setText(dateTimeFormat.format(new Date()));
  }

}
