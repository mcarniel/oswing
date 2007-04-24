package demo15;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.form.client.*;
import java.io.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Form controller of image detail frame.
 * It read/write an image gif/jpg file from file system.</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ImageFrameController extends FormController {

  private ImageFrame frame = null;


  public ImageFrameController() {
    frame = new ImageFrame(this);
    ClientUtils.centerFrame(frame);
    frame.setVisible(true);
    frame.getMainPanel().setMode(Consts.READONLY);
    frame.getMainPanel().reload();
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * If the method is not overridden, the current version will return a "demo" value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    try {
      ImageVO vo = new ImageVO();
      File f = new File(".");
      File[] ff = f.listFiles(new FilenameFilter() {

        public boolean accept(File dir, String name) {
          return name.toLowerCase().endsWith(".gif") || name.toLowerCase().endsWith(".jpg");
        }

      });
      if (ff.length==1) {
        FileInputStream in = new FileInputStream(ff[0]);
        byte[] bb = new byte[(int)ff[0].length()];
        in.read(bb);
        in.close();
        vo.setImage(bb);
        vo.setImageName(ff[0].getName());
      }

      VOResponse r = new VOResponse(vo);
      return r;
    }
    catch (Exception ex) {
      return new ErrorResponse("Error on creating the demo value object:\n"+ex.toString());
    }
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    try {
      ImageVO vo = (ImageVO)persistentObject;
      ImageVO oldVO = (ImageVO)oldPersistentObject;

      if (oldVO.getImageName()!=null && oldVO.getImageName().equals(vo.getImageName()))
        new File(oldVO.getImageName()).delete();

      FileOutputStream out = new FileOutputStream(vo.getImageName());
      byte[] bb = vo.getImage();
      out.write(bb);
      out.close();

      VOResponse r = new VOResponse(vo);
      if (oldVO.getImageName()!=null)
        new File(oldVO.getImageName()).delete();

      return r;
    }
    catch (Exception ex) {
      return new ErrorResponse("Error on creating the demo value object:\n"+ex.toString());
    }
  }



}
