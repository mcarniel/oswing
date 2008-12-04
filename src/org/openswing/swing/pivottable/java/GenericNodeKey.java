package org.openswing.swing.pivottable.java;

import java.io.Serializable;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Key of path of nodes.</p>
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
public class GenericNodeKey implements Serializable {

  private Object[] path = null;
  private int hashCode;


  public GenericNodeKey(Object[] path,int hashCode) {
    this.path = path;
    this.hashCode = hashCode;
  }


  public GenericNodeKey() {
    this.path = new Object[0];
    this.hashCode = 1;
  }


  public final Object[] getPath() {
    return path;
  }


  public final Object getLastNode() {
    if (path.length>0)
      return path[path.length-1];
    return null;
  }


  public final GenericNodeKey appendKey(Object node) {
    Object[] newpath = new Object[path.length+1];
    System.arraycopy(path,0,newpath,0,path.length);
    newpath[newpath.length-1] = node;
    return new GenericNodeKey(newpath,hashCode*node.hashCode());
  }


  public final boolean equals(Object obj) {
    if (obj==null || !(obj instanceof GenericNodeKey))
      return false;
    Object[] path2 = ((GenericNodeKey)obj).getPath();
    if (path2.length!=path.length)
      return false;
    for(int i=0;i<path2.length;i++)
      if (!path2[i].equals(path[i]))
          return false;
    return true;
  }


  public final int hashCode() {
    return hashCode;
  }



}
