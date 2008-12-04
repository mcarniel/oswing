package demo49;

import java.util.*;

import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.domains.java.*;
import java.sql.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.table.profiles.client.FileGridProfileManager;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.io.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates an editable grid frame.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication {


  public ClientApplication() {

    Hashtable domains = new Hashtable();
    Properties props = new Properties();
    props.setProperty("category","Category");
    props.setProperty("subCategory","Sub Category");
    props.setProperty("item","Item");
    props.setProperty("sellQty","Qty");
    props.setProperty("sellAmount","Sell");
    props.setProperty("orderDate","Order Date");
    props.setProperty("year","Year");
    props.setProperty("quarter","Quarter");
    props.setProperty("sells","Sells");
    props.setProperty("agent","Agent");
    props.setProperty("country","Country");
    props.setProperty("region","Region");

    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("£",props,true),
        domains
    );
    ClientSettings.FILTER_PANEL_ON_GRID = true;

    Domain orderStateDomain = new Domain("ORDERSTATE");
    orderStateDomain.addDomainPair(new Integer(0),"opened");
    orderStateDomain.addDomainPair(new Integer(1),"suspended");
    orderStateDomain.addDomainPair(new Integer(2),"delivered");
    orderStateDomain.addDomainPair(new Integer(3),"closed");
    domains.put(
      orderStateDomain.getDomainId(),
      orderStateDomain
    );
    ClientSettings.ALLOW_OR_OPERATOR = false;
    ClientSettings.INCLUDE_IN_OPERATOR = false;
    ClientSettings.GRID_PROFILE_MANAGER = new FileGridProfileManager();

    //createData();
    long t = System.currentTimeMillis();
//    ArrayList vos = loadData();
//    System.out.println("Loading data in "+(System.currentTimeMillis()-t)+"ms");
//    ArrayList vos = null;
//    new GridFrame(vos);

    new GridFrame();
  }


  public static void main(String[] argv) {
    new ClientApplication();
  }


  /**
   * Create data.
   */
  private void createData() {
    try {
      String[] categories = new String[]{"Hardware","Software"};
      String[][] subCategories = new String[][]{{"PC","Monitor","Printer"},{"O.S.","IDEs","Office Appl.","Database","Games","Other SW"}};
      String[] agents = new String[]{"Agent 1","Agent 2","Agent 3"};
      String[] countries = new String[]{"Italy","France","Germany","Other"};
      String[][] zones = new String[][]{{"North Italy","Center Italy","South Italy"},{"Paris","Outside Paris"},{"East","West"},{"Iberica Peninsula","U.K. & Ireland","Be.Ne.Lux.","East Europe"}};
      HashMap items = new HashMap();
      items.put("PC",new String[]{"Dell","HP"});
      items.put("Monitor",new String[]{"LG","Sony","Philips"});
      items.put("Printer",new String[]{"HP","Epson"});
      items.put("O.S.",new String[]{"Windows","Linux","Mac"});
      items.put("IDEs",new String[]{"JBuilder","IBM RAD","MS Visual Studio .NET"});
      items.put("Office Appl.",new String[]{"MS Office 2007","Open Office"});
      items.put("Database",new String[]{"Oracle","MS SQLServer","Sybase","IBM DB2"});
      items.put("Games",new String[]{"Need4Speed","Fifa"});
      items.put("Other SW",new String[]{"Norton AV","Photoshop"});


      FileOutputStream out = new FileOutputStream("orders.txt");
      String line = null;
      int i=0;
      Calendar cal = Calendar.getInstance();
      cal.set(cal.YEAR,2007);
      cal.set(cal.MONTH,0);
      cal.set(cal.DAY_OF_MONTH,1);
      cal.set(cal.HOUR_OF_DAY,0);
      cal.set(cal.MINUTE,0);
      cal.set(cal.SECOND,0);
      cal.set(cal.MILLISECOND,0);
      long t = cal.getTimeInMillis();
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      String date = null;
      int maxRows = 2000000;
      String[] ii = null;
      String[] sub = null;
      String[] z = null;
      do {
        date = sdf.format(new java.util.Date(t));
        for(int k1=0;k1<categories.length;k1++) {
          sub = subCategories[k1];
          for(int k2=0;k2<sub.length;k2++) {
            if (i>=maxRows)
              break;
            for(int k3=0;k3<countries.length;k3++) {
              z = zones[k3];
              for(int k4=0;k4<z.length;k4++)
                for(int k5=0;k5<agents.length;k5++) {
                  if (i>=maxRows)
                    break;
                  ii = (String[])items.get(sub[k2]);
                  for(int k6=0;k6<ii.length;k6++) {
                    if (i>=maxRows)
                      break;

                    line =
                        date+";"+
                        categories[k1]+";"+
                        sub[k2]+";"+
                        countries[k3]+";"+
                        z[k4]+";"+
                        agents[k5]+";"+
                        ii[k6]+";"+
                        new BigDecimal(Math.random()*10).setScale(0,BigDecimal.ROUND_HALF_UP)+";"+
                        new BigDecimal(Math.random()*1000).setScale(2,BigDecimal.ROUND_HALF_UP)+";"+
                        "\n";
                    out.write(line.getBytes());
                    out.flush();
                    i++;
                  }
                }
            }
          }
        }
        t += 86400000;
      }
      while(i<maxRows);
      out.close();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Load data.
   */
  private ArrayList loadData() {
    try {
      ArrayList vos = new ArrayList();
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("orders.txt")));
      String line = null;
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      OrdersVO vo = null;
      String[] t = null;
      while((line=br.readLine())!=null) {
        t = line.split(";");
        vo = new OrdersVO();
        vos.add(vo);
        vo.setOrderDate(sdf.parse(t[0]));
        vo.setCategory(t[1]);
        vo.setSubCategory(t[2]);
        vo.setCountry(t[3]);
        vo.setZone(t[4]);
        vo.setAgent(t[5]);
        vo.setItem(t[6]);
        vo.setSellQty(new BigDecimal(t[7]));
        vo.setSellAmount(new BigDecimal(t[8]));
      }
      br.close();
      return vos;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ArrayList();
    }
  }




}
