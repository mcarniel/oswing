package org.openswing.swing.lookup.client;

import java.util.*;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Mapper class to link lookup v.o. attributes to lookup container v.o. attributes.</p>
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
public class LookupMapper {

  /** lookup v.o. attributes -> lookup container v.o. attributes */
  private Hashtable lookup2ParentLinks = new Hashtable();

  /** lookup container v.o. attributes -> lookup v.o. attributes */
  private Hashtable parent2lookupLinks = new Hashtable();


  public LookupMapper() { }


  /**
   * @return lookup container v.o. attributes
   */
  public final Collection getParentChangedAttributes() {
    return lookup2ParentLinks.values();
  }

  /**
   * @return lookup v.o. attributes
   */
  public final Enumeration getLookupChangedAttributes() {
    return(lookup2ParentLinks.keys());
  }


  /**
   * Add the mapping: [ lookup v.o. attribute , lookup container v.o. attribute ]
   * @param lookupAttributeName lookup v.o. attribute
   * @param parentAttributeName lookup container v.o. attribute
   */
  public final void addLookup2ParentLink(String lookupAttributeName,String parentAttributeName) {
    lookup2ParentLinks.put(lookupAttributeName,parentAttributeName);
    parent2lookupLinks.put(parentAttributeName, lookupAttributeName);
  }


  /**
   * Set lookup container v.o. attribute with the argument value.
   * @param lookupParent lookup container
   * @param lookupAttributeName lookup v.o. attribute
   * @param lookupAttributeType lookup v.o. attribute type
   * @param lookupAttributeValue lookup v.o. attribute value
   * @return <code>true</code> if there exists the mapping between lookup and container lookup attributes
   */
  public final boolean setParentAttribute(
      LookupParent lookupParent,
      String lookupAttributeName,
      Class lookupAttributeType,
      Object lookupAttributeValue) {
    String attrName = (String)lookup2ParentLinks.get(lookupAttributeName);
    if (attrName==null)
      return false;
//    if (attrName.length()==1)
//      attrName = "set"+attrName.toUpperCase();
//    else
//      attrName = "set"+attrName.substring(0,1).toUpperCase()+attrName.substring(1);
    try {
      lookupParent.setValue(attrName,lookupAttributeValue);
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    catch (Error er) {
      er.printStackTrace();
      return false;
    }
  }

  /**
   * @param parentAttributeName lookup container v.o. attribute
   * @return lookup v.o. attribute related to lookup container v.o. attribute
   */
  public final String getLookupAttributeName(String parentAttributeName) {
    return((String) this.parent2lookupLinks.get(parentAttributeName));
  }


  /**
   * @param lookupAttributeName lookup container v.o. attribute
   * @return v.o. attribute related to parent container v.o. attribute
   */
  public final String getParentAttributeName(String lookupAttributeName) {
    return((String) this.lookup2ParentLinks.get(lookupAttributeName));
  }


}
