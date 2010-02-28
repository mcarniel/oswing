package org.openswing.swing.util.client;

import java.util.regex.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Search window used in combo control or list control or grid control.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * Licen1se as published by the Free Software Foundation;
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
public class SearchWindowManager {

  /** input control that will use this search window */
  private SearchControl inputControl;

  /** component included in the input control that will use this search window */
  private JComponent comp;

  /** text in the search window */
  private String textToSearch = "";

  /** search window window */
  private SearchWindow searchWindow;

  /** last element found*/
  private int lastElementFound = -1;


  public SearchWindowManager(SearchControl inputControl) {
    this.inputControl = inputControl;

    inputControl.getComponent().addKeyListener(new KeyAdapter() {

        public void keyTyped(KeyEvent e) {
          processKey(e);
        }

        public void keyPressed(KeyEvent e) {
          // for ESCAPE/BACKSPACE events...
          processKey(e);
        }

    });

    inputControl.getComponent().addFocusListener(new FocusAdapter() {

        public void focusLost(FocusEvent e) {
          hideSearchWindow();
          textToSearch = "";
        }


    });


    // add a component listener to synchronize input control component events with search window events...
    Container c = inputControl.getComponent().getParent();
    while(c!=null && !(c instanceof JScrollPane) )
      c = c.getParent();
    if (c instanceof JScrollPane)
      this.comp = (JScrollPane)c;
    else
      this.comp = inputControl.getComponent();

    this.comp.addComponentListener(new ComponentAdapter() {

        public void componentHidden(ComponentEvent e) {
          super.componentHidden(e);
          hideSearchWindow();
        }

        public void componentResized(ComponentEvent e) {
          super.componentResized(e);
          Point location = getSearchWindowLocation();
          if (location!=null && searchWindow!=null)
            searchWindow.setLocation(location.x, location.y);
        }

        public void componentMoved(ComponentEvent e) {
          super.componentMoved(e);
          Point location = getSearchWindowLocation();
          if (location!=null && searchWindow!=null)
            searchWindow.setLocation(location.x, location.y);
        }

    });


  }


  /**
   * Process key pressed/typed events.
   */
  private void processKey(KeyEvent e) {
    if (!inputControl.isReadOnly() || inputControl.disableListener()) {
      hideSearchWindow();
      return;
    }

    char c = e.getKeyChar();
    boolean ok = e.getID() == KeyEvent.KEY_TYPED && (Character.isLetterOrDigit(c) || c==' ' || c == '*' || c == '?' || c == '\b' || c == '/' || c =='.' || c==',' || c=='-');
    if (ok) {
      if (e.getID() == KeyEvent.KEY_TYPED) {
          if (((e.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0)) {
              return;
          }
          if (e.isAltDown()) {
              return;
          }

          if (c == '\b')
            // backspace pressed...
            textToSearch = textToSearch.length()>0 ? textToSearch.substring(0,textToSearch.length()-1) : "";
          else
            textToSearch += String.valueOf(e.getKeyChar());
      }

      // show search window...
      if (searchWindow!=null)
       hideSearchWindow();

      // recreate search window...
      searchWindow = new SearchWindow(textToSearch);

      // retrieve window location...
      Point location = getSearchWindowLocation();
      if (location != null)
        searchWindow.setLocation(location.x, location.y);
      searchWindow.setVisible(true);

      if (e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != e.VK_ESCAPE) {
          e.consume();
      }
    }
    else if (e.getKeyCode()==e.VK_ESCAPE) {
      if (searchWindow!=null) {
        hideSearchWindow();
        textToSearch = "";
        e.consume();
      }
    }
  }


  /**
   * @return <code>true</code> if search window is currently visible, <code>false</code> otherwise
   */
  public final boolean isSearchWindowVisible() {
    return searchWindow!=null;
  }


  /**
   * @return location of the search window
   */
  private Point getSearchWindowLocation() {
      Point searchWindowLocation;
      try {
        searchWindowLocation = comp.getLocationOnScreen();
      }
      catch (IllegalComponentStateException e) {
        return comp.getLocation();
      }
      if(searchWindowLocation==null)
        return null;

      if (searchWindow==null)
        return null;

      searchWindowLocation.y  = searchWindowLocation.y - searchWindow.getPreferredSize().height;
      if ((searchWindowLocation.y < 0))
        searchWindowLocation.y = 0;
      if ((searchWindowLocation.x < 0))
        searchWindowLocation.x = 0;

      packSearchWindow();
      return searchWindowLocation;
  }


  /**
   * Method used to pack the search window.
   */
  private void packSearchWindow() {
    if (searchWindow == null)
      return;
    searchWindow.pack();
  }



  /**
   * Hide the search window.
   */
  public void hideSearchWindow() {
    if (searchWindow != null) {
      searchWindow.setVisible(false);
      searchWindow.dispose();
      searchWindow = null;
      //textToSearch = "";
    }
  }






  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class that defines the real search window.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * <p> </p>
   * @author Mauro Carniel
   * @version 1.0
   */
  private class SearchWindow extends JWindow {

    /** "search" label */
    private JLabel searchLabel = new JLabel(ClientSettings.getInstance().getResources().getResource("search")+": ");

    /** "pattern not found" label */
    private JLabel notFoundLabel = new JLabel();

    /** search text field */
    private SearchField searchField;

    private Color defaultForeground;


    public SearchWindow(String text) {
      searchLabel.setVerticalAlignment(JLabel.BOTTOM);
      searchLabel.setFont(new Font(searchLabel.getFont().getName(),Font.BOLD,searchLabel.getFont().getSize()));
      notFoundLabel.setVerticalAlignment(JLabel.BOTTOM);
      notFoundLabel.setForeground(Color.red);

      //setup search field
      searchField = new SearchField();
      searchField.setCursor(getCursor());
      defaultForeground = searchField.getForeground();

      // add listener to search field...
      searchField.getDocument().addDocumentListener(new DocumentListener() {

          private Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            applyText();
            }

          });

          public void insertUpdate(DocumentEvent e) {
            startTimer();
          }

          public void removeUpdate(DocumentEvent e) {
            startTimer();
          }

          public void changedUpdate(DocumentEvent e) {
            startTimer();
          }

          protected void applyText() {
            String text = searchField.getText().trim();
            if (text.length() != 0) {
              int found = searchFromCurrentIndex(text);
              if (found == -1) {
                 found = inputControl.search(text);
                 if (found==-1)
                   searchField.setForeground(Color.red);
                 else
                   searchField.setForeground(defaultForeground);
              }
              else
                  searchField.setForeground(defaultForeground);

              select(found, null, text);
            }
            else
              hideSearchWindow();
          }

          void startTimer() {
            applyText();
          }

      });
      searchField.setText(text);

      try {
        ( (JPanel) getContentPane()).setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.lightGray, 1),
            BorderFactory.createEmptyBorder(2, 6, 2, 8))
        );
        getContentPane().setBackground(new Color(250,250,200));
      }
      catch (Exception ex) {
      }

      // set dimensions...
      Dimension size = searchLabel.getPreferredSize();
      size.height = searchField.getPreferredSize().height;
      searchLabel.setPreferredSize(size);

      // add labels and search field to window...
      getContentPane().setLayout(new GridBagLayout());

      getContentPane().add(searchLabel,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
           ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0));
      getContentPane().add(searchField,      new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
           ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,0), 0, 0));
      getContentPane().add(notFoundLabel,      new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
           ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,0), 0, 0));



    }


    /**
     * Search the next matching index from the current index.
     * If it reaches the end, it will restart from the beginning.
     * @param text pattern to search
     * @return the next index that the element matches the searching pattern
     */
    private int searchFromCurrentIndex(String textToSearch) {
      // search is always case insensitive...
      textToSearch = textToSearch.toUpperCase();

      // retrieve starting search index...
      int selectedIndex =
        lastElementFound != -1 ?
        lastElementFound :
        inputControl.getSelectedIndex();
      if (selectedIndex < 0)
        selectedIndex = 0;

      if (inputControl.getRowCount() == 0)
        return -1; // no items to search...

      // search from starting index...
      String element = null;
      for(int i = selectedIndex; i < inputControl.getRowCount(); i++) {
        element = inputControl.getValueAt(i);
        if (compare(element, textToSearch))
          return i;
      }

      // if not found, start again from the beginning
      for (int i = 0; i < selectedIndex; i++) {
        element = inputControl.getValueAt(i);
        if (compare(element, textToSearch))
          return i;
      }

      // no item matches the pattern...
      return -1;
    }


    /**
     * @param item current item to compare
     * @param textToSearch text to search
     * @return <code>true</code> if matches; <code>false</code> otherwise
     */
    private boolean compare(String item, String textToSearch) {
      if (item == null)
        return false;
      if (textToSearch == null ||
          textToSearch.trim().length() == 0)
        return true;

      if (textToSearch.indexOf('*')==-1 && textToSearch.indexOf('?')==-1) {
        // no jolly symbols found...
        return item.toUpperCase().startsWith(textToSearch);
      }
      try {
        if (!textToSearch.startsWith("*"))
          return Pattern.compile(".*"+textToSearch.toUpperCase(),Pattern.CASE_INSENSITIVE).matcher(item.toUpperCase()).find();
        else
          return Pattern.compile("."+textToSearch.toUpperCase(),Pattern.CASE_INSENSITIVE).matcher(item.toUpperCase()).find();
      }
      catch (PatternSyntaxException e) {
        return false;
      }
    }



    /**
     * Listen window events and forward them to search field.
     */
    public void processKeyEvent(KeyEvent e) {
      searchField.processKeyEvent(e);
      if (e.isConsumed()) {
        if (searchField.getText()==null ||
          searchField.getText().trim().length() == 0) {
          return;
        }
      }
      if (e.getKeyCode() != KeyEvent.VK_ENTER) {
        e.consume();
      }
    }


    /**
     * Method invoked when an item does (not) match the search pattern.
     * @param index index to select
     * @param e key event fired
     * @param textToSearch text just searched
     */
    private void select(int index, KeyEvent e, String textToSearch) {
      if (index != -1) {
        // item found...
        inputControl.setSelectedIndex(index);
        lastElementFound = index;
        searchField.setForeground(defaultForeground);
        notFoundLabel.setText("");
      }
      else {
        // item not found...
        searchField.setForeground(Color.red);
        notFoundLabel.setText(ClientSettings.getInstance().getResources().getResource("not found"));
      }
    }



  } // end inner class





  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to define a search input field.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * <p> </p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class SearchField extends JTextField {

    SearchField() {
      setBorder(BorderFactory.createEmptyBorder());
      setFocusable(false);
      setOpaque(false);
    }


    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width = getFontMetrics(getFont()).stringWidth(getText()) + 4;
        return size;
    }


    public void processKeyEvent(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_BACK_SPACE && getDocument().getLength() == 0) {
            e.consume();
            return;
        }
        if (isDeactivateKey(e)) {
            hideSearchWindow();
            if (keyCode == KeyEvent.VK_ESCAPE)
                e.consume();
            return;
        }
        super.processKeyEvent(e);
        if (keyCode == KeyEvent.VK_BACK_SPACE)
          e.consume();
    }


    private boolean isDeactivateKey(KeyEvent e) {
        int keyCode = e.getKeyCode();
        return keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_ESCAPE
                || keyCode == KeyEvent.VK_PAGE_UP || keyCode == KeyEvent.VK_PAGE_DOWN
                || keyCode == KeyEvent.VK_HOME || keyCode == KeyEvent.VK_END
                || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT
                || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN;
    }


  } // end inner class









}
