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

import org.getobjects.appserver.core.WOContext;
import org.getobjects.appserver.publisher.IGoAuthenticator;
import org.getobjects.appserver.publisher.IGoAuthenticatorContainer;
import org.getobjects.appserver.publisher.IGoContext;
import org.getobjects.appserver.publisher.IGoUser;
import org.getobjects.appserver.publisher.annotations.GoMethod;
import org.getobjects.foundation.UString;
import org.getobjects.jetty.WOJettyRunner;
import org.opengroupware.web.OGoApplication;
import org.opengroupware.web.OGoContext;

public class OGoWS extends OGoApplication implements IGoAuthenticatorContainer {

  protected IGoAuthenticator auth;
  
  @Override
  public void init() {
    super.init();

    this.auth = new OGoGoAuthenticator(this);
  }

  public IGoAuthenticator authenticatorInContext(IGoContext _ctx) {
    return this.auth;
  }
    
  /* auth */
  
  @GoMethod(slot = "login", isPublic=true, keyedParameters={"l", "p"})
  public Object loginAction(final String l, final String p, WOContext _ctx) {
    // FIXME: there has to be an easier way just to validate the pwd in OGo
    
    // forge request
    String tok = "Basic " + UString.stringByEncodingBase64(l + ":" + p, null);
    _ctx.request().setHeaderForKey(tok, "authorization");
    
    final IGoAuthenticator auth = this.authenticatorInContext(_ctx);
    final IGoUser user = auth.userInContext(_ctx);
    
    if (user == null || user.getName() == null) // Anonymous!
      return "FAIL";
    
    return "OK";
  }
  
  
  /* controller registry */
  
  public OGoObjectController objectControllerForEntity
    (String _entityName, int _id, OGoContext _ctx)
  {
    if (_entityName.equals("Persons"))
      return new OGoWSPerson(_id, _ctx);
    
    log.error("did not find controller for entity: " + _entityName);
    return null;
  }
  
  public OGoDataSourceController dataSourceControllerForEntity
    (String _entityName, OGoContext _ctx)
  {
    return new OGoDataSourceController(_entityName, _ctx);
  }
  
  
  /* lookup */
  
  @Override
  public Object lookupName
    (final String _name, final IGoContext _ctx, final boolean _acquire)
  {
    // System.err.println("APP LOOKUP: " + _name);
    
    String entityName = UString.capitalizedString(_name);
    if (this.database.model().entityNamed(entityName) != null)
      return this.dataSourceControllerForEntity(entityName, (OGoContext)_ctx);

    return super.lookupName(_name, _ctx, _acquire);
  }
  
  /* main */
  
  public static void main(String[] args) {
    new WOJettyRunner(OGoWS.class, args).run();
  }
}
