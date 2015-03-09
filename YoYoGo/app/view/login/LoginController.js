// YoYoGo

Ext.define('YoYoGo.view.login.LoginController', {
  extend: 'Ext.app.ViewController',

  requires: [
  ],

  alias: 'controller.login',
  
  init: function() {
    var self = this;
    console.log("init login ...");

    self.control({
      'button[action=login]':  { click: self.onLogin  },
      'button[action=cancel]': { click: self.onCancel },
      '*[name=password]':      { enter: self.onLogin  }
    });
    
    self.getView().on("show", function(sender) {
      sender.down("*[name=login]").focus();
    });
  },
  
  onLogin: function(sender) {
    var self   = this;
    var form   = sender.up('window').items.first();
    var values = form.getForm().getValues();
    
    Ext.Ajax.request({
      url: YoYoGo.Current.wsOGoLoginURL,
      params: {
        l: values.login,
        p: values.password
      },
      success: function(r) {
        if (r.responseText != "OK")
          self.onLoginFail(r);
        else
          self.onLoginOK(r, values);
      }
    });
  },
  
  onLoginOK: function(r, values) {
    YoYoGo.Current.authToken =
      "Basic " + btoa(values.login + ":" + values.password);
    this.getView().close();
    
    YoYoGo.Current.fireEvent("didLogin", YoYoGo.Current);
  },
  
  onLoginFail: function(r) {
    var win = Ext.Msg.show({
      title: "Sign in failed", 
      msg: "Failed to sign in ...", 
      icon: Ext.Msg.INFO,
      closable: true
    });
    var self = this;
    win.on("close", function(sender) {
      self.getView().down("*[name=login]").focus();
    });
  },
  
  onCancel: function(sender) {
    sender.up('window').close();
  }
});
