package org.opengroupware.ws;

import org.getobjects.appserver.core.WOApplication;
import org.getobjects.appserver.core.WORequest;
import org.opengroupware.web.OGoContext;

public class Context extends OGoContext {

  public Context(WOApplication _app, WORequest _rq) {
    super(_app, _rq);
  }

}
