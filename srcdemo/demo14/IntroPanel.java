package demo14;

import org.openswing.swing.wizard.client.WizardInnerPanel;
import java.awt.*;
import org.openswing.swing.miscellaneous.client.*;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.awt.event.ItemListener;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Licence panel.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @version 1.0
 */
public class IntroPanel extends WizardInnerPanel {
  BorderLayout borderLayout1 = new BorderLayout();
  LicencePanel licencePanel = new LicencePanel();


  public IntroPanel() {
    try {
      jbInit();

      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("../licence.txt")));
      StringBuffer sb = new StringBuffer("");
      String line = null;
      while((line=br.readLine())!=null)
        sb.append(line);
      br.close();

      licencePanel.setTitle("Zip/unzip utility");
      licencePanel.setSubTitle("A free utility to compress or decompress files based on java.util.zip package");
      licencePanel.setLicence("<html><body>"+sb.toString()+"</body></html>");
      licencePanel.setShowBackButton(false);
      licencePanel.setShowCancelButton(false);
      licencePanel.setShowOkButton(false);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Add an ItemListener to the "ok" radio button.
   * @param listener ItemListener added to the "ok" radio button
   */
  public final void addOkRadioButtonItemListener(ItemListener listener) {
    licencePanel.addOkRadioButtonItemListener(listener);
  }


  public String getPanelId() {
    return "INTRO";
  }


  /**
   * This method is automatically called by WizardPanel when the panel is showed:
   * it can be overrided to add custom logic that must be executed when the panel is showed.
   */
  public void init() {}


  /**
   * This method could be overrided.
   * @return image name; null if no image is required
   */
  public String getImageName() {
    return "setup.gif";
  }


  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    this.add(licencePanel, BorderLayout.CENTER);
  }


}
