// YoYoGo

Ext.define('YoYoGo.view.login.Login', {

  extend: 'Ext.window.Window',
  
  xtype: 'app-login',
  
  width:       300,
  height:      200, 
  modal:       true, 
  layout:      'fit',
  closeAction: 'hide',
  closable:    false,
  
  controller: 'login',
  
  // iconCls: 'key', // needs image in CSS
  
  title: "Sign in",
  
  fbar: [
    { text: 'OK',     action: "login"  }
    // no cancel, useless { text: 'Cancel', action: "cancel" }
  ],

  items: {
    xtype:       'form',
    bodyPadding: 10,
    defaultType: 'textfield',
    items: [
      { xtype: 'displayfield', flex: 1,
        value: 'Sign into OpenGroupware.org' },
      { fieldLabel: 'Login',    name: 'login',    value: 'helge' },
      { fieldLabel: 'Password', name: 'password', value: '',
        inputType:  'password',
        listeners: {
          specialKey: function(field, event) {
            if (event.getKey() == event.ENTER)
              this.fireEvent("enter", this);
          }
        }
      }
    ]
  }
});
