package org.openswing.swing.table.profiles.client;

import java.io.*;
import java.math.*;
import java.text.*;
import java.util.*;

import org.openswing.swing.message.send.java.*;
import org.openswing.swing.table.profiles.java.*;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid profile manager: it manages grid profile storing and fetching.
 * This implementation is based on the file system: it stores and retrieves user profiles from the user's local file system.
 * User profile files are stored in the default user home.</p>
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
public class FileGridProfileManager extends GridProfileManager {

  private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

  public FileGridProfileManager() {
  }


  /**
   * @param functionId identifier (functionId) associated to the grid
   * @return list of GridProfileDescription objects
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public ArrayList getUserProfiles(final String functionId) throws Throwable {
    String userHome = System.getProperty("user.home").replace('\\','/');
    if (!userHome.endsWith("/"))
      userHome += "/";
    userHome += "profiles";
    File dir = new File(userHome);
    File[] files = dir.listFiles(new FileFilter() {

      public boolean accept(File pathname) {
        return
            pathname.getName().startsWith(functionId.replace(' ','_')+"_"+getUsername().replace(' ','_')+"_") &&
            pathname.getName().endsWith(".cfg");
      }

    });

    ArrayList profiles = new ArrayList();
    GridProfile profile = null;
    if (files!=null)
      for(int i=0;i<files.length;i++) {
        try {
          profile = getUserProfile(functionId, files[i].getName());
        }
        catch (Throwable ex) {
        }
        if (profile!=null)
          profiles.add(new GridProfileDescription(files[i].getName(),profile.getDescription(),profile.isDefaultProfile()));
      }
    return profiles;
  }


  /**
   * @return default user profile; null if the default profile has not been yet stored
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public final GridProfile getDefaultProfile(final String functionId) throws Throwable {
    String userHome = System.getProperty("user.home").replace('\\','/');
    if (!userHome.endsWith("/"))
      userHome += "/";
    userHome += "profiles";
    File dir = new File(userHome);
    File[] files = dir.listFiles(new FileFilter() {

      public boolean accept(File pathname) {
        return
            pathname.getName().startsWith(functionId.replace(' ','_')+"_"+getUsername().replace(' ','_')+"_") &&
            pathname.getName().endsWith(".cfg");
      }

    });

    GridProfile profile = null;
    if (files!=null)
      for(int i=0;i<files.length;i++) {
        profile = getUserProfile(functionId,files[i].getName());
        if (profile.isDefaultProfile())
          return profile;
      }
    return null;
  }


  /**
   * @param id grid profile identifier
   * @return user profile
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   */
  public GridProfile getUserProfile(String functionId,Object id) throws Throwable {
    String userHome = System.getProperty("user.home").replace('\\','/');
    if (!userHome.endsWith("/"))
      userHome += "/";
    userHome += "profiles";
    File dir = new File(userHome);
    File f = new File(userHome+"/"+id);
    if (!f.exists())
      throw new IOException("File not found: "+id);

    BufferedReader br = null;
    try {

      br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

      String description = br.readLine(); // description
      functionId = br.readLine(); // functionId
      String username = br.readLine(); // username

      String aux = br.readLine(); // currentSortedColumns
      ArrayList currentSortedColumns = new ArrayList();
      StringTokenizer st = new StringTokenizer(aux,",");
      while(st.hasMoreTokens())
        currentSortedColumns.add( st.nextToken() );

      aux = br.readLine(); // currentSortedVersusColumns
      ArrayList currentSortedVersusColumns = new ArrayList();
      st = new StringTokenizer(aux,",");
      while(st.hasMoreTokens())
        currentSortedVersusColumns.add( st.nextToken() );

      aux = br.readLine(); // columnsAttribute
      st = new StringTokenizer(aux,",");
      int cols = st.countTokens();
      String[] columnsAttribute = new String[cols];
      int i = 0;
      while(st.hasMoreTokens())
        columnsAttribute[i++] = st.nextToken();

      aux = br.readLine(); // columnsVisibility
      boolean[] columnsVisibility = new boolean[cols];
      st = new StringTokenizer(aux,",");
      i = 0;
      while(st.hasMoreTokens())
        columnsVisibility[i++] = st.nextToken().toLowerCase().equals("true");

      aux = br.readLine(); // columnsWidth
      int[] columnsWidth = new int[cols];
      st = new StringTokenizer(aux,",");
      i = 0;
      while(st.hasMoreTokens())
        columnsWidth[i++] = Integer.parseInt( st.nextToken().trim() );

      aux = br.readLine(); // defaultProfile
      boolean defaultProfile = aux.toLowerCase().endsWith("true");

      // quickFilterValues
      HashMap quickFilterValues = new HashMap(); // hashtable which contains the associations: attribute name, new FilterWhereClause[2] {FilterWhereClause,FilterWhereClause})
      String attributeName = null;
      String operator1,typevalue1,operator2,typevalue2;
      Object value1,value2;
      FilterWhereClause[] filter = new FilterWhereClause[2];
      ArrayList values = null;
      String[] tokens = null;
      while((aux = br.readLine())!=null) { // aux = attribute name,operator1,typevalue1,value1a\tvalue1b\tvalue1c[,operator2,typevalue2,value2...]
        filter = new FilterWhereClause[2];
        st = new StringTokenizer(aux,",");
        attributeName = st.nextToken();
        operator1 = st.nextToken();
        typevalue1 = st.nextToken();
        value1 = st.nextToken();
        tokens = value1.toString().split("\t");
        if (tokens.length==1 && !operator1.equals(Consts.IN)) {
          if (typevalue1.equals("D"))
            value1 = sdf.parse(value1.toString());
          else if (typevalue1.equals("N"))
            value1 = new BigDecimal(value1.toString());
        }
        else {
          values = new ArrayList();
          for(int j=0;j<tokens.length;j++)
            if (typevalue1.equals("D"))
              values.add(sdf.parse(tokens[j]));
            else if (typevalue1.equals("N"))
              values.add(new BigDecimal(tokens[j]));
            else
              values.add(tokens[j]);
          value1 = values;
        }
        filter[0] = new FilterWhereClause(attributeName,operator1,value1);

        if (st.hasMoreTokens()) {
          operator2 = st.nextToken();
          typevalue2 = st.nextToken();
          value2 = st.nextToken();

          tokens = value2.toString().split("\t");
          if (tokens.length==1 && !operator2.equals(Consts.IN)) {
            if (typevalue1.equals("D"))
              value2 = sdf.parse(value2.toString());
            else if (typevalue1.equals("N"))
              value2 = new BigDecimal(value2.toString());
          }
          else {
            values = new ArrayList();
            for(int j=0;j<tokens.length;j++)
              if (typevalue2.equals("D"))
                values.add(sdf.parse(tokens[j]));
              else if (typevalue2.equals("N"))
                values.add(new BigDecimal(tokens[j]));
              else
                values.add(tokens[j]);
            value2 = values;
          }
          filter[1] = new FilterWhereClause(attributeName,operator2,value2);
        }
        else
          filter[1] = null;

        quickFilterValues.put( attributeName,filter );
      }

      return new GridProfile(
        id,
        description, // description
        functionId, // functionId
        username, // username
        currentSortedColumns, // currentSortedColumns
        currentSortedVersusColumns, // currentSortedVersusColumns
        quickFilterValues, // quickFilterValues
        columnsAttribute, // columnsAttribute
        columnsVisibility, // columnsVisibility
        columnsWidth, // columnsWidth
        defaultProfile // defaultProfile
      );

    }
    catch (Throwable t) {
      throw new IOException(t.getMessage());
    }
    finally {
      try {
        br.close();
      }
      catch (Exception ex) {
      }
    }
  }


  /**
   * Store the specified grid profile.
   * @param profile profile to store
   * @return profile id
   * @throws Throwable throwed if storing operation does not correctly accomplished
   * Note: if profile.getId() is null then this method must define id property.
   */
  public Object storeUserProfile(GridProfile profile) throws Throwable {
    if (profile.getId()==null)
      profile.setId(
        profile.getFunctionId().replace(' ','_')+"_"+getUsername().replace(' ','_')+"_"+System.currentTimeMillis()+".cfg"
      );

    String userHome = System.getProperty("user.home").replace('\\','/');
    if (!userHome.endsWith("/"))
      userHome += "/";
    userHome += "profiles";
    File dir = new File(userHome);
    dir.mkdirs();
    File f = new File(userHome+"/"+profile.getId());
    if (f.exists())
      f.delete();

    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new FileOutputStream(f));
      pw.println(profile.getDescription());
      pw.println(profile.getFunctionId());
      pw.println(profile.getUsername());

      String currentSortedColumns = "";
      for(int i=0;i<profile.getCurrentSortedColumns().size();i++)
        currentSortedColumns += profile.getCurrentSortedColumns().get(i)+",";
      if (currentSortedColumns.length()>0)
        currentSortedColumns = currentSortedColumns.substring(0,currentSortedColumns.length()-1);
      pw.println(currentSortedColumns);

      String currentSortedVersusColumns = "";
      for(int i=0;i<profile.getCurrentSortedVersusColumns().size();i++)
        currentSortedVersusColumns += profile.getCurrentSortedVersusColumns().get(i)+",";
      if (currentSortedVersusColumns.length()>0)
        currentSortedVersusColumns = currentSortedVersusColumns.substring(0,currentSortedVersusColumns.length()-1);
      pw.println(currentSortedVersusColumns);

      String columnsAttribute = "";
      for(int i=0;i<profile.getColumnsAttribute().length;i++)
        columnsAttribute += profile.getColumnsAttribute()[i]+",";
      if (columnsAttribute.length()>0)
        columnsAttribute = columnsAttribute.substring(0,columnsAttribute.length()-1);
      pw.println(columnsAttribute);

      String columnsVisibility = "";
      for(int i=0;i<profile.getColumnsVisibility().length;i++)
        columnsVisibility += profile.getColumnsVisibility()[i]+",";
      if (columnsVisibility.length()>0)
        columnsVisibility = columnsVisibility.substring(0,columnsVisibility.length()-1);
      pw.println(columnsVisibility);

      String columnsWidth = "";
      for(int i=0;i<profile.getColumnsWidth().length;i++)
        columnsWidth += profile.getColumnsWidth()[i]+",";
      if (columnsWidth.length()>0)
        columnsWidth = columnsWidth.substring(0,columnsWidth.length()-1);
      pw.println(columnsWidth);

      pw.println(profile.isDefaultProfile());

      Iterator it = profile.getQuickFilterValues().keySet().iterator();
      String aux = "";
      FilterWhereClause[] filter = null;
      String attributeName = null;
      while(it.hasNext()) {
        attributeName = it.next().toString();
        filter = (FilterWhereClause[])profile.getQuickFilterValues().get(attributeName);
        aux = attributeName+","+filter[0].getOperator()+",";
        if (filter[0].getValue()!=null) {
          if (filter[0].getOperator().equals(Consts.IN) || filter[0].getValue() instanceof ArrayList) {
            ArrayList values = (ArrayList)filter[0].getValue();
            if (values.size()>0)
              if (values.get(0) instanceof Date)
                aux += "D,";
              else if (values.get(0) instanceof Number)
                aux += "N,";
              else
                aux += "T,";

            for(int j=0;j<values.size();j++)
              if (values.get(j) instanceof Date)
                aux += sdf.format( values.get(j) )+"\t";
              else if (values.get(j) instanceof Number)
                aux += values.get(j)+"\t";
              else
                aux += values.get(j)+"\t";
          }
          else {
            if (filter[0].getValue() instanceof Date)
              aux += "D,"+sdf.format( filter[0].getValue() );
            else if (filter[0].getValue() instanceof Number)
              aux += "N,"+filter[0].getValue();
            else
              aux += "T,"+filter[0].getValue();
          }
        }

        if (filter[1]!=null) {
          aux += ","+filter[1].getOperator()+",";
          if (filter[1].getValue()!=null) {
            if (filter[1].getOperator().equals(Consts.IN) || filter[1].getValue() instanceof ArrayList) {
              ArrayList values = (ArrayList)filter[1].getValue();
              if (values.size()>0)
                if (values.get(0) instanceof Date)
                  aux += "D,";
                else if (values.get(0) instanceof Number)
                  aux += "N,";
                else
                  aux += "T,";

              for(int j=0;j<values.size();j++)
                if (values.get(j) instanceof Date)
                  aux += sdf.format( values.get(j) )+"\t";
                else if (values.get(j) instanceof Number)
                  aux += values.get(j)+"\t";
                else
                  aux += values.get(j)+"\t";
            }
            else {
              if (filter[1].getValue() instanceof Date)
                aux += "D,"+sdf.format( filter[1].getValue() );
              else if (filter[1].getValue() instanceof Number)
                aux += "N,"+filter[1].getValue();
              else
                aux += "T,"+filter[1].getValue();
            }
          }
        }
        pw.println(aux);
      }
      return profile.getId();
    }
    catch (Throwable t) {
      throw new IOException(t.getMessage());
    }
    finally {
      try {
        pw.close();
      }
      catch (Exception ex) {
      }
    }
  }


  /**
   * Delete the specified grid profile.
   * @param id grid profile identifier
   * @throws Throwable throwed if deleting operation does not correctly accomplished
   */
  public void deleteUserProfile(String functionId,Object id) throws Throwable {
    String userHome = System.getProperty("user.home").replace('\\','/');
    if (!userHome.endsWith("/"))
      userHome += "/";
    userHome += "profiles";
    File dir = new File(userHome);
    dir.mkdirs();
    File f = new File(userHome+"/"+id);
    if (f.exists())
      f.delete();
    else
      throw new IOException("File not found: "+id);
  }


  /**
   * Delete all grid profiles, independently from the current user.
   * This method is automatically invoked if "grid digest" comparison lead to discover a grid change: in this case all grid profiles must be removed.
   * @param functionId identifier (functionId) associated to the grid
   * @throws Throwable throwed if deleting operation does not correctly accomplished
   */
  public void deleteAllGridProfiles(final String functionId) throws Throwable {
    String userHome = System.getProperty("user.home").replace('\\','/');
    if (!userHome.endsWith("/"))
      userHome += "/";
    userHome += "profiles";
    File dir = new File(userHome);
    File[] files = dir.listFiles(new FileFilter() {

      public boolean accept(File pathname) {
        return
            pathname.getName().indexOf(functionId.replace(' ','_')+"_")!=-1 &&
            pathname.getName().endsWith(".cfg");
      }

    });
    if (files!=null)
      for(int i=0;i<files.length;i++) {
        files[i].delete();
      }
  }


  /**
   * Store the "grid digest", i.e. a value that globally identify the current grid configuration.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public void storeGridDigest(String functionId,String gridDigest) throws Throwable {
    String userHome = System.getProperty("user.home").replace('\\','/');
    if (!userHome.endsWith("/"))
      userHome += "/";
    userHome += "profiles";
    File dir = new File(userHome);
    dir.mkdirs();
    File f = new File(userHome+"/"+functionId.replace(' ','_')+".dig");
    if (f.exists())
      f.delete();

    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new FileOutputStream(f));
      pw.println(gridDigest);

    } catch (Throwable ex){
      throw new IOException(ex.getMessage());
    }
    finally {
      try {
        pw.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * @return retrieve the "grid digest", i.e. a value that globally identify the current grid configuration; this digest is used to check if grid columns have been changed from last grid execution: in this case all grid profiles will be deleted
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   * Note: this method returns null if no digest has been yet stored (i.e. this is the first time the grid is being viewed)
   */
  public String getLastGridDigest(String functionId) throws Throwable {
    String userHome = System.getProperty("user.home").replace('\\','/');
    if (!userHome.endsWith("/"))
      userHome += "/";
    userHome += "profiles";
    File dir = new File(userHome);
    dir.mkdirs();
    File f = new File(userHome+"/"+functionId.replace(' ','_')+".dig");
    if (!f.exists())
      return null;

    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      return br.readLine();

    } catch (Throwable ex){
      throw new IOException(ex.getMessage());
    }
    finally {
      try {
        br.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * @return retrieve the last profile identifier in action
   * @throws Throwable throwed if fetching operation does not correctly accomplished
   * Note: this method returns null if no profile identifier has been yet stored (i.e. this is the first time the grid is being viewed)
   */
  public Object getLastGridProfileId(String functionId) throws Throwable {
    String userHome = System.getProperty("user.home").replace('\\','/');
    if (!userHome.endsWith("/"))
      userHome += "/";
    userHome += "profiles";
    File dir = new File(userHome);
    dir.mkdirs();
    File f = new File(userHome+"/"+functionId.replace(' ','_')+"_"+getUsername().replace(' ','_')+".ini");
    if (!f.exists())
      return null;

    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      return br.readLine();

    } catch (Throwable ex){
      throw new IOException(ex.getMessage());
    }
    finally {
      try {
        br.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * Store the current profile identifier in action.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public void storeGridProfileId(String functionId,Object id) throws Throwable {
    String userHome = System.getProperty("user.home").replace('\\','/');
    if (!userHome.endsWith("/"))
      userHome += "/";
    userHome += "profiles";
    File dir = new File(userHome);
    dir.mkdirs();
    File f = new File(userHome+"/"+functionId.replace(' ','_')+"_"+getUsername().replace(' ','_')+".ini");
    if (f.exists())
      f.delete();

    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new FileOutputStream(f));
      pw.println(id);

    } catch (Throwable ex){
      throw new IOException(ex.getMessage());
    }
    finally {
      try {
        pw.close();
      }
      catch (Exception ex1) {
      }
    }

  }


  /**
   * Delete all grid profiles identifiers for the current user.
   * This method is automatically invoked if "grid digest" comparison lead to discover a grid change.
   * @throws Throwable throwed if storing operation does not correctly accomplished
   */
  public void deleteAllGridProfileIds(String functionId) throws Throwable {
    String userHome = System.getProperty("user.home").replace('\\','/');
    if (!userHome.endsWith("/"))
      userHome += "/";
    userHome += "profiles";
    File dir = new File(userHome);
    dir.mkdirs();
    File f = new File(userHome+"/"+functionId.replace(' ','_')+"_"+getUsername().replace(' ','_')+".ini");
    if (f.exists())
      f.delete();
  }


}
