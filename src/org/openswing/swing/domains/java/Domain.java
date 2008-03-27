package org.openswing.swing.domains.java;

import java.io.*;
import java.util.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class that represents a domain of values (enumeration of couples code,description).</p>
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
public class Domain implements Serializable {

  /** domain identifier */
  private String domainId = null;

  /** collection of associations (code, DomainPair object) */
  private Hashtable pairs = new Hashtable();

  /** collection of associations (code, DomainPair object) */
  private ArrayList pairsList = new ArrayList();


  /**
   * Costructor.
   * @param domainId domain identifier
   */
  public Domain(String domainId) {
    this.domainId = domainId;
  }


  /**
   * @return domain identifier
   */
  public final String getDomainId() {
    return this.domainId;
  }


  /**
   * @param code code associated to a DomainPair object
   * @return DomainPair object related to the specified code
   */
  public final DomainPair getDomainPair(Object code) {
    if (code!=null)
      return (DomainPair)pairs.get(code);
    else
      for(int i=0;i<pairsList.size();i++)
        if (((DomainPair)pairsList.get(i)).getCode()==null)
          return (DomainPair)pairsList.get(i);
    return null;
  }


  /**
   * @return list of DomainPair objects that compose this domain
   */
  public final DomainPair[] getDomainPairList() {
    return (DomainPair[])pairsList.toArray(new DomainPair[pairsList.size()]);
  }


  /**
   * Add a new couple (code, description).
   * @param code code to add
   * @param description description related to the code (this description will be translated)
   * @return <code>false</code> if the code already exists in the domain, <code>true</code> otherwise
   */
  public final boolean addDomainPair(Object code,String description) {
    if (code!=null && pairs.containsKey(code))
      return false;
    if (code==null)
      for(int i=0;i<pairsList.size();i++)
        if (((DomainPair)pairsList.get(i)).getCode()==null)
          return false;
    DomainPair pair = new DomainPair(code,description);
    if (code!=null)
      pairs.put(code,pair);
    pairsList.add(pair);
    return true;
  }

}
