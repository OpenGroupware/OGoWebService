/**
 * The main application class. An instance of this class is created by app.js
 * when it calls Ext.application().
 * This is the ideal place to handle application launch and initialization
 * details.
 */

// This is the OGo/J WebService Entry Point
var WSOGoURL = "http://localhost:8181/OGoWS";

Ext.define('YoYoGo.Application', {
    extend: 'Ext.app.Application',
    
    name:        'YoYoGo',
    appProperty: 'Current', // access from anywhere via 'YoYoGo.Current'
    
    requires: [
      "YoYoGo.view.login.Login"
    ],
    
    views: [
    ],
    
    controllers: [
        'Root'
    ],
    
    stores: [
      'Persons'
    ],
    
    wsOGoURL:      WSOGoURL,
    wsOGoLoginURL: WSOGoURL + "/login",
    
    constructor: function(config) {
      console.log("YoYoGo is being constructed ...", this);
      
      this.logALot = false;
      
      Ext.data.StoreManager.on("add", this.onStoreAdd, this);
      
      this.callParent(arguments);
      
      console.log("YoYoGo is up&running.", this);
    },
     
    init: function() {
      // this runs before the viewport is created
      var self = this;
      console.log("APP: init", this);
      
      if (false)
        self.authToken = "Basic " + btoa('helge:rxroot');
      
      Ext.Ajax.on('beforerequest',    this.onAJAXRequest,   self);      
      Ext.Ajax.on('requestexception', this.onAJAXException, self);
    },
    
    launch: function () {
      // Note: this runs AFTER the main views got initialized, too late to setup
      //       shared state
      console.log("did launch ...", this);      
    },
    
    onStoreAdd: function(idx, item) {
      var proxy;
      if (item) proxy = item.getProxy();
      if (proxy) {
        var url = proxy.getUrl();
        var newURL = url.replace("$WSBASEURL", WSOGoURL);
        if (url !== newURL) {
          proxy.setUrl(newURL);
          // console.log("patched store URL", url, newURL);
        }
      }      
    },
    
    onSignIn: function() {
      var self = this;
      if (this.logALot)
        console.log("onSignIn: ", self);
      self.signinWin = self.signinWin || Ext.create('widget.app-login', {});
      self.signinWin.show();
    },
    
    onAJAXRequest: function(con, opts, eopts) {
      var self = this;
      
      // careful, this runs for *ANY* ExtJS request, we only want to push the auth
      // to the actual webservice.
      if (opts.url.indexOf(self.wsOGoLoginURL) == 0) {
        console.log("WebService login starts ...", opts);
      }
      else if (opts.url.indexOf(self.wsOGoURL) == 0) {
        if (this.logALot) {
          console.log("WebService request starts ...", opts);
          console.log("  Con:", con);
        }
      
        opts.headers = opts.headers || {}
        if (self.authToken)
          opts.headers['authorization'] = self.authToken;
        else {
          // at this point there are no requests in the queue, can't abort yet.
          // this function can return false, but then Server.js runs into an
          // NPE.
          self.onSignIn();
          return false;
        }
      }
      else
        console.log("Other AJAX request starts ...", opts, this);
    },
    
    onAJAXException: function(connection, response, rqOpts, listenerOptions) {
      var self = this;
      console.log("APP: exception");
      console.log("  Con", connection);
      console.log("  Res", response);
      console.log("  Req", rqOpts);
      
      if (response.status == 401) {
        console.log("  GOT 401!");
      }
    }
});
