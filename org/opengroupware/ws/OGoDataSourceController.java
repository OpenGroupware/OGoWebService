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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.getobjects.appserver.publisher.GoInternalErrorException;
import org.getobjects.appserver.publisher.GoPermission;
import org.getobjects.appserver.publisher.IGoContext;
import org.getobjects.appserver.publisher.IGoObject;
import org.getobjects.appserver.publisher.annotations.DefaultAccess;
import org.getobjects.appserver.publisher.annotations.DefaultRoles;
import org.getobjects.appserver.publisher.annotations.GoMethod;
import org.getobjects.appserver.publisher.annotations.ProtectedBy;
import org.getobjects.eocontrol.EOAndQualifier;
import org.getobjects.eocontrol.EOKeyValueQualifier;
import org.getobjects.eocontrol.EOOrQualifier;
import org.getobjects.eocontrol.EOQualifier;
import org.getobjects.eocontrol.EOSortOrdering;
import org.getobjects.foundation.NSObject;
import org.getobjects.foundation.UMap;
import org.getobjects.foundation.UObject;
import org.mortbay.util.ajax.JSON;
import org.opengroupware.logic.core.OGoObjectContext;
import org.opengroupware.logic.db.OGoResultSet;
import org.opengroupware.web.OGoContext;

@ProtectedBy(GoPermission.View)
@DefaultAccess("allow")
@DefaultRoles( anonymous = { }, authenticated = { GoPermission.View })
public class OGoDataSourceController extends NSObject implements IGoObject {
  protected static final Log log = LogFactory.getLog("OGoWebService");
  
  transient final OGoContext       ctx;
  transient final OGoObjectContext oc;
  final String entityName;

  public OGoDataSourceController(String _entityName, final OGoContext _ctx) {
    this.ctx = _ctx;
    this.oc  = this.ctx.objectContext();
    this.entityName = _entityName;
  }
  
  /* accessors */
  
  public String entityName() {
    return this.entityName;
  }

  /* child object lookup */
  
  @Override
  public Object lookupName(String _name, IGoContext _ctx, boolean _acquire) {
    // System.err.println("LOOKUP: " + _name);
    
    if (_name != null && _name.length() > 1) {
      if (Character.isDigit(_name.charAt(0))) {
        return this.createChildController(UObject.intValue(_name), 
                                          (OGoContext)_ctx);
      }
    }
    
    return IGoObject.DefaultImplementation
             .lookupName(this, _name, _ctx, _acquire);
  }
  
  public Object createChildController(int _id, OGoContext _ctx) {
    return ((OGoWS)_ctx.application())
        .objectControllerForEntity(this.entityName(), _id, _ctx);
  }
  
  
  /* methods */
  
  @GoMethod(slot = "POST", protectedBy="Create") 
  public Object postAction() {
    System.err.println("IMPLEMENT POST");
    return new GoInternalErrorException("POST not implemented!");
  }
  
  
  /* default method */
  
  @GoMethod(slot = "default", protectedBy="View", 
            keyedParameters={ "limit", "idx", "sort", "filter", "filterop" })
  public Object defaultAction
    (int limit, int idx, String s, String filter, String filterOp)
  {
    // this is a little bit lame. Should defaultMethod first check for POST?
    if (this.ctx.request().method().equals("POST"))
      return this.postAction();
    
    /* sort (not really required? doFetch also supports strings) */
    EOSortOrdering[] sortOrderings = this.sortOrderingsFromFormValue(s);
    EOQualifier      q = this.qualifierFromFormValue(filter, filterOp);
    
    /* fetch total count */
    
    final Number rsCount = this.oc.doFetchTotal(this.entityName(),
        "qualifier", q,
        "distinct",  true
    );
    System.err.println("COUNT: " + rsCount);
    
    /* fetch batch */
    
    final OGoResultSet rs = this.oc.doFetch(this.entityName(),
        "limit",     limit,
        "offset",    idx > 0 ? new Integer(idx) : null,
        "qualifier", q,
        "orderings", sortOrderings,
        "distinct",  true
    );
    
    // System.err.println("RES: " + rs);
    return UMap.create("count", rsCount, "results", rs);
  }
  
  
  /* sort orderings */
  
  @SuppressWarnings("unchecked")
  protected EOQualifier qualifierFromFormValue(final String s, String fop) {
    if (UObject.isEmpty(s))
      return null;
    
    final Object[] jsonObject = (Object[]) JSON.parse(s);
    
    List<EOQualifier> filters = new ArrayList<EOQualifier>(jsonObject.length);
    for (final Object jsonEntry: jsonObject) {
      Map<String, Object> filterInfo = (Map<String, Object>)jsonEntry;
      System.err.println("F: " + filterInfo);
      
      String prop  = (String)filterInfo.get("property");
      Object value =         filterInfo.get("value");
      String op    = (String)filterInfo.get("operator");
      if (op == null) op = "=";
      
      final EOQualifier q = new EOKeyValueQualifier(prop, op, value);
      filters.add(q);
    }
    System.err.println("FILTERS:" + filters);
    
    if (fop != null) {
      if ("OR".equalsIgnoreCase(fop))
        return new EOOrQualifier(filters);
      else if ("AND".equalsIgnoreCase(fop))
        return new EOAndQualifier(filters);
      else
        log.error("unexpected filterop: " + fop);
    }
    return new EOAndQualifier(filters);
  }
  
  @SuppressWarnings("unchecked")
  protected EOSortOrdering[] sortOrderingsFromFormValue(String s) {
    if (UObject.isEmpty(s))
      return null;

    // Jetty specific
    // eg [{"property":"lastname","direction":"ASC"}]
    Object[] o = (Object[]) JSON.parse(s);
    List<EOSortOrdering> sos = new ArrayList<EOSortOrdering>(o.length);
    
    for (final Object item: o) {
      Map<String, String> k = (Map<String, String>)item;
      final String tk = k.get("direction");
      Object sel = null;
      
      if (tk.equalsIgnoreCase("ASC"))
        sel = EOSortOrdering.EOCompareAscending;
      else if (tk.equalsIgnoreCase("DESC"))
        sel = EOSortOrdering.EOCompareDescending;
      
      final EOSortOrdering so = new EOSortOrdering(k.get("property"), sel);
      sos.add(so);
    }
    
    EOSortOrdering[] a = new EOSortOrdering[sos.size()];
    return sos.toArray(a);
  }
  
}
