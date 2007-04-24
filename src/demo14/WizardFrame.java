package demo14;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.tree.client.*;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.openswing.swing.wizard.client.WizardPanel;
import org.openswing.swing.util.client.ClientUtils;
import java.io.*;
import java.util.zip.*;
import java.util.ArrayList;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Wizard Frame: it shows three panes, used to zip or unzip file.
 * The ZIPWizardController class is uded to establish which pane to show when pressing "Back" or "Next" button.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class WizardFrame extends JFrame {

  private WizardPanel wizardPanel = new WizardPanel();

  private DefaultMutableTreeNode dragNode = null;


  public WizardFrame() {
    try {
      jbInit();
      setTitle("Zip/Unzip Files Wizard");
      setSize(750,400);
      ClientUtils.centerFrame(this);

      wizardPanel.addPanel(new FirstPanel());
      wizardPanel.addPanel(new SecondPanel());
      wizardPanel.addPanel(new ThirdPanel());
      wizardPanel.setNavigationLogic(new ZIPWizardController());
//      wizardPanel.setImageName("setup.gif");
      wizardPanel.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (e.getSource().equals(wizardPanel.getCancelButton()) && wizardPanel.getCurrentVisiblePanel().getPanelId().equals("FIRST"))
            System.exit(0);
          if (e.getSource().equals(wizardPanel.getCancelButton()) && !wizardPanel.getCurrentVisiblePanel().getPanelId().equals("FIRST")) {
            checkFinish();
          }
        }
      });

      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Check if all input controls are defined.
   */
  private void checkFinish() {
    if (wizardPanel.getCurrentVisiblePanel().getPanelId().equals("SECOND")) {
      SecondPanel p = (SecondPanel)wizardPanel.getCurrentVisiblePanel();
      // file to decompress...
      File zipFile = new File(p.controlFileName.getText());
      if (!zipFile.isFile() || !zipFile.exists() || !zipFile.getName().endsWith(".zip")) {
        OptionPane.showMessageDialog(this,"You must specify a zip file","Attention",JOptionPane.WARNING_MESSAGE);
        return;
      }
      File destDir = new File(p.controlDestPath.getText());
      if (!destDir.isDirectory() || !destDir.exists()) {
        OptionPane.showMessageDialog(this,"You must specify an existing folder","Attention",JOptionPane.WARNING_MESSAGE);
        return;
      }
      try {
        ZipInputStream zip = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry entry = null;
        String name = null;
        byte[] bb = new byte[10000];
        int len = 0;
        FileOutputStream out = null;
        while((entry=zip.getNextEntry())!=null) {
          name = entry.getName();
          if (entry.isDirectory())
            new File(destDir.getAbsolutePath()+"/"+name).mkdirs();
          else {
            out = new FileOutputStream(destDir.getAbsolutePath()+"/"+name);
            while((len = zip.read(bb))>0)
              out.write(bb,0,len);
            out.close();
          }
          zip.closeEntry();
        }
        zip.close();
        System.exit(0);
      }
      catch (Exception ex) {
        OptionPane.showMessageDialog(this,ex.getMessage(),"Attention",JOptionPane.ERROR_MESSAGE);
      }
    }

    else {
      ThirdPanel p = (ThirdPanel)wizardPanel.getCurrentVisiblePanel();
      // folder to compress...
      File folder = new File(p.controlFolder.getText());
      if (!folder.isDirectory() || !folder.exists()) {
        OptionPane.showMessageDialog(this,"You must specify an existing folder","Attention",JOptionPane.WARNING_MESSAGE);
        return;
      }
      File destFile = new File(p.controlZip.getText());
      if (destFile.isDirectory() || !destFile.getName().endsWith(".zip")) {
        OptionPane.showMessageDialog(this,"You must specify a zip file","Attention",JOptionPane.WARNING_MESSAGE);
        return;
      }
      try {
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(destFile));
        ZipEntry entry = null;
        File name = null;
        byte[] bb = new byte[10000];
        int len = 0;
        FileInputStream in = null;

        // find out files and sub-folders...
        ArrayList files = new ArrayList();
        addFiles(files,folder);

        for(int i=0;i<files.size();i++) {
          name = (File)files.get(i);
          entry = new ZipEntry(name.getPath());
          zip.putNextEntry(entry);
          if (name.isFile()) {
            in = new FileInputStream(name);
            while((len = in.read(bb))>0)
              zip.write(bb,0,len);
            in.close();
          }
          zip.closeEntry();
        }
        zip.close();
//        System.exit(0);
      }
      catch (Exception ex) {
        OptionPane.showMessageDialog(this,ex.getMessage(),"Attention",JOptionPane.ERROR_MESSAGE);
      }

    }
  }


  /**
   * Add recursiverly a list of files for the specified directory.
   */
  private void addFiles(ArrayList files,File dir) {
    File[] newfiles = dir.listFiles();
    for(int i=0;i<newfiles.length;i++)
      if (newfiles[i].isDirectory())
        addFiles(files,newfiles[i]);
      else
        files.add(newfiles[i]);
  }


  private void jbInit() throws Exception {
    this.getContentPane().add(wizardPanel, BorderLayout.CENTER);
  }


}

