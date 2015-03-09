/*
  Copyright (C) 2014 Helge Hess

  This file is part of OpenGroupware.org (OGo)

  OGo is free software; you can redistribute it and/or modify it under
  the terms of the GNU General Public License as published by the
  Free Software Foundation; either version 2, or (at your option) any
  later version.

  OGo is distributed in the hope that it will be useful, but WITHOUT ANY
  WARRANTY; without even the implied warranty of MERCHANTABILITY or
  FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public
  License for more details.

  You should have received a copy of the GNU General Public
  License along with OGo; see the file COPYING.  If not, write to the
  Free Software Foundation, 59 Temple Place - Suite 330, Boston, MA
  02111-1307, USA.
*/
package org.opengroupware.ws;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.getobjects.appserver.publisher.GoInternalErrorException;
import org.getobjects.appserver.publisher.GoNotFoundException;
import org.getobjects.appserver.publisher.GoPermission;
import org.getobjects.appserver.publisher.annotations.DefaultAccess;
import org.getobjects.appserver.publisher.annotations.DefaultRoles;
import org.getobjects.appserver.publisher.annotations.GoMethod;
import org.getobjects.appserver.publisher.annotations.ProtectedBy;
import org.getobjects.foundation.NSObject;
import org.getobjects.foundation.UList;
import org.getobjects.foundation.UMap;
import org.mortbay.util.ajax.JSON;
import org.opengroupware.logic.core.IOGoOperation;
import org.opengroupware.logic.core.OGoObjectContext;
import org.opengroupware.logic.db.OGoObject;
import org.opengroupware.web.OGoContext;

@ProtectedBy(GoPermission.View)
@DefaultAccess("allow")
@DefaultRoles( anonymous = { },
               authenticated = { GoPermission.View, "Edit" })
public class OGoObjectController extends NSObject {
  protected static final Log log = LogFactory.getLog("OGoWebService");

  transient final OGoContext       ctx;
  transient final OGoObjectContext oc;
  final String entityName;
  final int id;

  public OGoObjectController
    (final String _entityName, final int _id, final OGoContext _ctx)
  {
    this.ctx        = _ctx;
    this.oc         = this.ctx.objectContext();
    this.entityName = _entityName;
    this.id         = _id;
  }
  
  /* accessors */
  
  public String entityName() {
    return this.entityName;
  }
  public int id() {
    return this.id;
  }
  
  /* fetch operation */
  
  @GoMethod(slot = "default", protectedBy=GoPermission.View) 
  public Object defaultAction() {
    /* fetch batch */
    
    final OGoObject object = (OGoObject)this.oc.find(this.entityName(),this.id);
    
    // System.err.println("RES: " + object);
    
    if (object == null)
      return new GoNotFoundException();
    
    return object;
  }
  
  /* save operation */
  
  @SuppressWarnings("unchecked")
  @GoMethod(slot = "PUT", protectedBy="Edit")
  public Object PUTAction() {
    // Note: cannot call this 'PUT' - this will return the value due to
    //       KVC!
    
    System.err.println("OC: " + this.oc);
    
    Map<String, Object> changes = (Map<String, Object>)
        JSON.parse(this.ctx.request().contentString());
    System.err.println("PUT: " + changes);
    changes.remove("id"); // nope
    // assert id==this.id?
    
    IOGoOperation[] ops = this.operationsForChanges(changes);
    if (ops == null)
      return new GoInternalErrorException("got no save operations for object!");
    
    Exception e = this.oc.performOperations(ops);
    if (e != null) // rather do a succes=false?
      return e;
    
    // Note: this works just right, the value is reset in the grid as we dont
    //       actually change anything.
    Object o = this.defaultAction();
    return UMap.create("success", true, "results", UList.create(o));
  }
  
  public IOGoOperation[] operationsForChanges(Map<String, Object> changes) {
    log.error("subclass needs to override operationsForChanges!");
    return null;
  }
  
  /* description */
  
  @Override
  public void appendAttributesToDescription(StringBuilder _d) {
    super.appendAttributesToDescription(_d);
    
    _d.append(" gid=");
    _d.append(this.entityName());
    _d.append(":");
    _d.append(this.id());
  }
}
