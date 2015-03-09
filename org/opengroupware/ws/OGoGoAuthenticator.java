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

import org.getobjects.appserver.core.WOSession;
import org.getobjects.appserver.publisher.IGoUser;
import org.getobjects.appserver.publisher.GoHTTPAuthenticator;
import org.opengroupware.logic.auth.OGoDefaultLoginConfig;
import org.opengroupware.web.IOGoDatabaseProvider;

/**
 * OGoJoAuthenticator
 * <p>
 * Authenticator for the Go authentication infrastructure. Do not mix that up
 * with the JAAS OGoLoginModule which is 'da real thing, doing the real
 * authentication checks (and provides the OGoLoginContext [principal]).
 * <br>
 * This object is just for fitting the OGo authentication into the Go
 * framework.
 * <p>
 * @author helge
 */
public class OGoGoAuthenticator extends GoHTTPAuthenticator {
  
  public OGoGoAuthenticator(IOGoDatabaseProvider _app) {
    super("OpenGroupware.org",
        new OGoDefaultLoginConfig(_app.databaseForContext(null)));
  }
  
  /* derive user object from context */

  @Override
  public IGoUser extractUserFromSession(final WOSession _sn) {
    // TBD: eliminate that
    return _sn != null ? (IGoUser)_sn.valueForKey("activeUser") : null;
  }

}
