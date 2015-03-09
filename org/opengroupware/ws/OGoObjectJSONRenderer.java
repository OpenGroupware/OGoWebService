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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.getobjects.appserver.core.WOContext;
import org.getobjects.appserver.publisher.GoSimpleJSONRenderer;
import org.getobjects.eoaccess.EOActiveRecord;
import org.getobjects.eoaccess.EOAttribute;
import org.getobjects.foundation.UMap;
import org.opengroupware.logic.authz.OGoAccessDeniedException;
import org.opengroupware.logic.db.OGoObject;
import org.opengroupware.logic.db.OGoResultSet;

public class OGoObjectJSONRenderer extends GoSimpleJSONRenderer {
  protected static final Log log = LogFactory.getLog("OGoWebService");

  @Override
  public boolean canRenderObjectInContext(Object _object, WOContext _ctx) {
    //System.err.println("CAN RENDER?: " + _object);
    
    if (!this.isJSONRequest(_ctx.request()) || _object == null)
      return false;
    
    if (_object instanceof OGoObject)
      return true;
    
    if (_object instanceof OGoResultSet) {
      OGoResultSet rs = (OGoResultSet)_object;
      if (rs.size() == 0)
        return true;
      
      if (rs.get(0) instanceof OGoObject)
        return true;
    }
    
    // this could also emit meta-data, like the fields of the records and the
    // columns of tableviews, see:
    // file:///Users/helge/Desktop/extjs-docs-5.0.0/index.html#!/api/Ext.data.reader.Json
    
    // also take over JSON ...
    return super.canRenderObjectInContext(_object, _ctx);
  }

  /* actual imp */
  
  @Override
  public Exception appendCustomObjectToString(Object _obj, StringBuilder _sb) {
    if (_obj instanceof OGoObject)
      return this.appendOGoObject((OGoObject)_obj, _sb);
    if (_obj instanceof OGoResultSet)
      return this.appendResultSet((OGoResultSet)_obj, _sb);
    
    return super.appendCustomObjectToString(_obj, _sb);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Exception appendOGoObject(OGoObject _p, StringBuilder _sb) {
    Map json = this.jsonForEO(_p);
    json.put("permissions", _p.appliedPermissions());
    return this.appendMapToString(json, _sb);
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Map jsonForEO(final EOActiveRecord _eo) {
    if (_eo == null)
      return null;
    
    final EOAttribute[] attrs = _eo.entity().attributes();
    final Map keyToValue = new HashMap(attrs.length);
    for (final EOAttribute a: attrs) {
      final String key = a.name();
      final Object value = _eo.valueForKey(key);
      if (value != null) // only non-null
        keyToValue.put(key, value);
    }

    return keyToValue;
  }
  
  public Exception appendResultSet(OGoResultSet _rs, StringBuilder _sb) {
    if (_rs == null) {
      _sb.append("null");
      return null;
    }
    
    _sb.append("(");
    {
      for (Object o: _rs) {
        Exception e = this.appendObjectToString(o, _sb);
        if (e != null)
          return e;
      }
    }
    _sb.append(")");
    
    return null;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Exception appendExceptionToString(Throwable _ex, StringBuilder _sb) {
    if (_ex instanceof OGoAccessDeniedException) {
      OGoAccessDeniedException oe = (OGoAccessDeniedException)_ex;
      
      Map<String, Object> json = UMap.create(
        "message",     oe.getMessage(),
        "permissions", oe.availablePermissions(),
        "required",    oe.requestedPermissions(),
        "missing",     oe.missingPermissions(),
        "gid",         oe.globalID().toString()
      );
      
      return this.appendMapToString(json, _sb);
    }
    
    return super.appendExceptionToString(_ex, _sb);
  }

}
