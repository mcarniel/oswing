package org.openswing.swing.client;

import java.io.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.util.client.ClientUtils;
import javax.swing.JFileChooser;
import org.openswing.swing.form.client.Form;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Input control used for file upload: it allows to select a file from local file system,
 * read it and store it as byte[] within the control.
 * Moreover, it allows to download file starting from byte[] stored within the control.
 * Optionally, another attribute can be binded to this control, in order to store the file name.
 * </p>
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
public class FileControl  extends BaseInputControl implements InputControl {

  /** flag used to define if a file import button must be showed; default value: <code>true</code> */
  private boolean showUploadButton = true;

  /** flag used to define if a file export button must be showed; default value: <code>true</code> */
  private boolean showDownloadButton = true;

  /** optional attribute name used to bind this attribute to the file name */
  private String fileNameAttributeName = null;

  /** file filter used to filter image file selection from select button; default value: jpg and gif files only */
  private javax.swing.filechooser.FileFilter fileFilter = new javax.swing.filechooser.FileFilter() {

    /**
     * Whether the given file is accepted by this filter.
     */
    public boolean accept(File f) {
      return
        f.isFile() ||
        f.isDirectory();
    }

    /**
     * The description of this filter.
     * @see FileView#getName
     */
    public String getDescription() {
      return "All file formats (*.*)";
    }

  };

  /** text field used to show file name */
  private JTextField fileName = new JTextField();

  /** button used to upadload file */
  private JButton uploadButton = new JButton() {
    public void paint(Graphics g) {
      super.paint(g);
      int width = g.getFontMetrics().stringWidth("...");
      if (isEnabled())
        g.setColor(UIManager.getColor("Button.foreground"));
      else
        g.setColor(UIManager.getColor("Button.disabledForeground"));
      g.drawString("...", (this.getWidth()-width+1)/2, this.getHeight()/2+4);
    }
  };


  /** button used to download file */
  private JButton downloadButton = new JButton() {
    public void paint(Graphics g) {
      super.paint(g);
      int width = g.getFontMetrics().stringWidth("...");
      if (isEnabled())
        g.setColor(UIManager.getColor("Button.foreground"));
      else
        g.setColor(UIManager.getColor("Button.disabledForeground"));
      g.drawString("...", (this.getWidth()-width+1)/2, this.getHeight()/2+4);
    }
  };


  /** bytes related to file */
  private byte[] bytes;

  /** used in focus management */
  private String oldFileName = null;


  public FileControl() {
    uploadButton.setPreferredSize(new Dimension(21, fileName.getPreferredSize().height));
    downloadButton.setPreferredSize(new Dimension(21, fileName.getPreferredSize().height));
    this.setLayout(new GridBagLayout());
    fileName.setColumns(10);
    this.add(fileName, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    uploadButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("upload file"));
    downloadButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("download file"));
    this.add(uploadButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(downloadButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    downloadButton.setEnabled(false);

    fileName.addFocusListener(new FocusAdapter() {

      /**
       * Invoked when a component gains the keyboard focus.
       */
      public void focusGained(FocusEvent e) {
        oldFileName = fileName.getText();
      }

      /**
       * Invoked when a component loses the keyboard focus.
       */
      public void focusLost(FocusEvent e) {
        if (fileName.getText()==null || fileName.getText().trim().equals("")) {
          bytes = null;
          oldFileName = null;
          downloadButton.setEnabled(false);
        }
        else if (!fileName.getText().equals(oldFileName)) {
          readFile(fileName.getText());
        }
      }


    });

    uploadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        JFileChooser f = new JFileChooser();
        f.setDialogTitle(ClientSettings.getInstance().getResources().getResource("upload file"));
        f.setDialogType(f.OPEN_DIALOG);
        f.setApproveButtonText(ClientSettings.getInstance().getResources().getResource("upload file"));
        f.setFileSelectionMode(f.FILES_ONLY);
        if (fileFilter!=null)
          f.setFileFilter(fileFilter);
        int res = f.showOpenDialog(ClientUtils.getParentWindow(FileControl.this));
        if (res==f.APPROVE_OPTION) {
          readFile(f.getSelectedFile().getAbsolutePath());
        }
      }

    });
    downloadButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        if (bytes==null)
          return;

        final JFileChooser f = new JFileChooser();
        f.setDialogTitle(ClientSettings.getInstance().getResources().getResource("download file"));
        f.setDialogType(f.SAVE_DIALOG);
        f.setApproveButtonText(ClientSettings.getInstance().getResources().getResource("download file"));

        Form form = ClientUtils.getLinkedForm(FileControl.this);
        if (form != null &&
            fileNameAttributeName!=null &&
           !fileNameAttributeName.equals("")) {
          Object name = form.getVOModel().getValue(fileNameAttributeName);
          if (name!=null)
            f.setSelectedFile(new File(name.toString()));
        }
        int res = f.showSaveDialog(ClientUtils.getParentWindow(FileControl.this));
        if (res==f.APPROVE_OPTION) {
          try {
            File file = f.getSelectedFile();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            out.write(bytes);
            out.close();
          }
          catch (Throwable ex) {
            OptionPane.showMessageDialog(ClientUtils.getParentWindow(FileControl.this),ex.getMessage(),ClientSettings.getInstance().getResources().getResource("Error while saving"),JOptionPane.ERROR_MESSAGE);
          }
        }
      }

    });

    initListeners();
  }


  private void readFile(String file) {
    try {
      File f = new File(file);
      Form form = ClientUtils.getLinkedForm(this);
      if (form != null &&
          fileNameAttributeName!=null &&
         !fileNameAttributeName.equals("")) {
        form.getVOModel().setValue(fileNameAttributeName,f.getName());
        fileName.setText(f.getName());
      }
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
      bytes = new byte[(int)f.length()];
      in.read(bytes);
      in.close();
//    form.getVOModel().setValue(attributeName,bytes);
      downloadButton.setEnabled(true);
    }
    catch (Exception ex) {
      OptionPane.showMessageDialog(ClientUtils.getParentWindow(FileControl.this),"Error",ex.getMessage(),JOptionPane.ERROR_MESSAGE);
      bytes = null;
    }

  }




  /**
   * @return define if a file import button must be showed
   */
  public final boolean isShowUploadButton() {
    return showUploadButton;
  }


  /**
   * Define if a file import button must be showed.
   * @param showUploadButton define if a file import button must be showed, in order to select and upload a file
   */
  public final void setShowUploadButton(boolean showUploadButton) {
    if (showUploadButton && !this.showUploadButton) {
      this.add(uploadButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
      this.revalidate();
    }
    else if (!showUploadButton && this.showUploadButton) {
      this.remove(uploadButton);
      this.revalidate();
    }

    this.showUploadButton = showUploadButton;
  }


  /**
   * @return define if a file export button must be showed
   */
  public final boolean isShowDownloadButton() {
    return showDownloadButton;
  }


  /**
   * Define if a file import button must be showed.
   * @param showDownloadButton define if a file export button must be showed, in order to download the file stored in the cell
   */
  public final void setShowDownloadButton(boolean showDownloadButton) {
    if (showDownloadButton && !this.showDownloadButton) {
      this.add(downloadButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
      this.revalidate();
    }
    else if (!showDownloadButton && this.showDownloadButton) {
      this.remove(downloadButton);
      this.revalidate();
    }

    this.showDownloadButton = showDownloadButton;
  }


  /**
   * @return file filter used to filter file selection from import button
   */
  public final javax.swing.filechooser.FileFilter getFileFilter() {
    return fileFilter;
  }


  /**
   * @return attribute name used to bind this attribute to the file name
   */
  public final String getFileNameAttributeName() {
    return fileNameAttributeName;
  }


  /**
   * Set the attribute name used to bind this attribute to the file name.
   * @param fileNameAttributeName attribute name used to bind this attribute to the file name
   */
  public final void setFileNameAttributeName(String fileNameAttributeName) {
    this.fileNameAttributeName = fileNameAttributeName;
  }


  /**
   * Set the file filter used to filter file selection from import button.
   * Default value: *.*
   * @param fileFilter file filter used to filter file selection from import button
   */
  public final void setFileFilter(javax.swing.filechooser.FileFilter fileFilter) {
    this.fileFilter = fileFilter;
  }


  /**
   * @return file name
   */
  public final String getFileName() {
    return fileName.getText();
  }


  /**
   * @return file content
   */
  public final byte[] getFile() {
    return bytes;
  }


  /**
   * Set file content.
   */
  public final void setFile(byte[] bytes) {
    this.bytes = bytes;
  }


  /**
   * Replace enabled setting with editable setting (this allow tab swithing).
   * @param enabled flag used to set abilitation of control
   */
  public final void setEnabled(boolean enabled) {
    try {
      if (!enabled) {
        fileName.setForeground(UIManager.getColor("TextField.foreground"));
        fileName.setBackground(UIManager.getColor("TextField.inactiveBackground"));
      }
    }
    catch (Exception ex) {
    }
    fileName.setEditable(enabled);
    uploadButton.setEnabled(enabled);
    fileName.setFocusable(enabled || ClientSettings.DISABLED_INPUT_CONTROLS_FOCUSABLE);
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    try {
      return fileName.isEditable();
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * @return value related to the input control
   */
  public final Object getValue() {
    return bytes;
  }


  /**
   * Set value to the input control.
   * @param value value to set into the input control
   */
  public final void setValue(Object value) {
    bytes = (byte[])value;

    downloadButton.setEnabled(bytes!=null && bytes.length>0);

    Form form = ClientUtils.getLinkedForm(this);
    if (form != null &&
        fileNameAttributeName!=null &&
       !fileNameAttributeName.equals("")) {
      fileName.setText( (String)form.getVOModel().getValue(fileNameAttributeName) );
    }
  }


  /**
   * @return component inside this whose contains the value
   */
  public final JComponent getBindingComponent() {
    return fileName;
  }



}
