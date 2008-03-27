package org.openswing.swing.items.client;

import java.util.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Mapper class to link items v.o. attributes to items container v.o. attributes.</p>
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
public class ItemsMapper {

  /** items v.o. attributes -> items container v.o. attributes */
  private Hashtable items2ParentLinks = new Hashtable();

  /** items container v.o. attributes -> items v.o. attributes */
  private Hashtable parent2itemsLinks = new Hashtable();


  public ItemsMapper() { }


  /**
   * @return items container v.o. attributes
   */
  public final Collection getParentChangedAttributes() {
    return items2ParentLinks.values();
  }

  /**
   * @return items v.o. attributes
   */
  public final Enumeration getItemsChangedAttributes() {
    return(items2ParentLinks.keys());
  }


  /**
   * Add the mapping: [ items v.o. attribute , items container v.o. attribute ]
   * @param itemsAttributeName items v.o. attribute
   * @param parentAttributeName items container v.o. attribute
   */
  public final void addItem2ParentLink(String itemsAttributeName,String parentAttributeName) {
    items2ParentLinks.put(itemsAttributeName,parentAttributeName);
    parent2itemsLinks.put(parentAttributeName, itemsAttributeName);
  }


  /**
   * Set items container v.o. attribute with the argument value.
   * @param itemsParent items container
   * @param itemsAttributeName items v.o. attribute
   * @param itemsAttributeType items v.o. attribute type
   * @param itemsAttributeValue items v.o. attribute value
   * @return <code>true</code> if there exists the mapping between items and container items attributes
   */
  public final boolean setParentAttribute(
      ItemsParent itemsParent,
      String itemsAttributeName,
      Class itemsAttributeType,
      Object itemsAttributeValue) {
    String attrName = (String)items2ParentLinks.get(itemsAttributeName);
    if (attrName==null)
      return false;
//    if (attrName.length()==1)
//      attrName = "set"+attrName.toUpperCase();
//    else
//      attrName = "set"+attrName.substring(0,1).toUpperCase()+attrName.substring(1);
    try {
      itemsParent.setValue(attrName,itemsAttributeValue);
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
   * @param parentAttributeName items container v.o. attribute
   * @return items v.o. attribute related to items container v.o. attribute
   */
  public final String getItemsAttributeName(String parentAttributeName) {
    return((String) this.parent2itemsLinks.get(parentAttributeName));
  }


  /**
   * @param itemsAttributeName items container v.o. attribute
   * @return v.o. attribute related to parent container v.o. attribute
   */
  public final String getParentAttributeName(String itemsAttributeName) {
    return((String) this.items2ParentLinks.get(itemsAttributeName));
  }


}
