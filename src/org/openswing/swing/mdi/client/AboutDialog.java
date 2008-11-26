package org.openswing.swing.mdi.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.util.client.*;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.text.html.HTMLDocument;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: About Dialog.</p>
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
public class AboutDialog extends JDialog {
  BorderLayout borderLayout1 = new BorderLayout();
  JTabbedPane tabbedPane = new JTabbedPane();
  JButton imageButton = new JButton();
  JPanel infoPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JScrollPane scrollPane = new JScrollPane();
  JPanel gcPanel = new JPanel();
  JButton gcButton = new JButton();
  JEditorPane text = new JEditorPane();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel memLabel = new JLabel();


  /**
   * View an about window.
   * @param frame parent frame (MDI Frame)
   * @param aboutText text to view
   */
  public AboutDialog(MDIFrame frame,String aboutText,String aboutImageName) {
    super(frame, ClientSettings.getInstance().getResources().getResource("about")+" "+frame.getTitle(), true);
    try {
      jbInit();
      Image aboutImage = null;
      if (aboutImageName!=null)
        aboutImage = ClientUtils.getImage(aboutImageName);
      if (aboutImage!=null) {
        imageButton.setIcon(new ImageIcon(aboutImage));
        setSize(aboutImage.getWidth(this)+10,aboutImage.getHeight(this)+30);
      }
      else {
        this.getContentPane().remove(tabbedPane);
        this.getContentPane().add(infoPanel, BorderLayout.CENTER);
        setSize(400,300);
      }
      setMem();
      if (aboutText.toLowerCase().trim().startsWith("<html>")) {
        text.setContentType("text/html");
        text.addHyperlinkListener(new Hyperactive());

      }
      text.setText(aboutText);
      ClientUtils.centerDialog(frame,this);
      setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    infoPanel.setLayout(borderLayout2);
    gcButton.setMnemonic(ClientSettings.getInstance().getResources().getResource("forcegcmnemonic").charAt(0));
    gcButton.setText(ClientSettings.getInstance().getResources().getResource("Force GC"));
    gcButton.addActionListener(new AboutDialog_gcButton_actionAdapter(this));
    text.setOpaque(false);
    text.setEditable(false);
    gcPanel.setLayout(gridBagLayout1);
    imageButton.addActionListener(new AboutDialog_imageButton_actionAdapter(this));
    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    tabbedPane.add(imageButton,    ClientSettings.getInstance().getResources().getResource("imageButton"));
    tabbedPane.add(infoPanel,    ClientSettings.getInstance().getResources().getResource("infoPanel"));
    infoPanel.add(scrollPane,  BorderLayout.CENTER);
    infoPanel.add(gcPanel,  BorderLayout.SOUTH);
    gcPanel.add(gcButton,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    gcPanel.add(memLabel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    scrollPane.getViewport().add(text, null);
  }


  /**
   * Set memory occupation.
   */
  private void setMem() {
    long total = Runtime.getRuntime().totalMemory();
    memLabel.setText(
          ClientSettings.getInstance().getResources().getResource("Java Heap")+":     "+
      (total-Runtime.getRuntime().freeMemory())/1024/1024+"MB "+
      ClientSettings.getInstance().getResources().getResource("used")+",     "+
      total/1024/1024+"MB "+
      ClientSettings.getInstance().getResources().getResource("allocated")
    );
  }


  void gcButton_actionPerformed(ActionEvent e) {
    System.gc();
    setMem();
  }


  void imageButton_actionPerformed(ActionEvent e) {
    setVisible(false);
  }


  class Hyperactive implements HyperlinkListener {

         public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
               JEditorPane pane = (JEditorPane) e.getSource();
               if (e instanceof HTMLFrameHyperlinkEvent) {
                   HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
                   HTMLDocument doc = (HTMLDocument)pane.getDocument();
                   doc.processHTMLFrameHyperlinkEvent(evt);
               } else {
                   try {
//                pane.setPage(e.getURL());
                     ClientUtils.displayURL(e.getURL().toExternalForm());
                   } catch (Throwable t) {
                t.printStackTrace();
                   }
               }
            }
        }
     }


}


class AboutDialog_gcButton_actionAdapter implements java.awt.event.ActionListener {
  AboutDialog adaptee;

  AboutDialog_gcButton_actionAdapter(AboutDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.gcButton_actionPerformed(e);
  }
}

class AboutDialog_imageButton_actionAdapter implements java.awt.event.ActionListener {
  AboutDialog adaptee;

  AboutDialog_imageButton_actionAdapter(AboutDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.imageButton_actionPerformed(e);
  }
}
