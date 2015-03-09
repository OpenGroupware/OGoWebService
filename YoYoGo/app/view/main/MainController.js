/**
 * This class is the main view for the application. It is specified in app.js as the
 * "autoCreateViewport" property. That setting automatically applies the "viewport"
 * plugin to promote that instance of this class to the body element.
 *
 * TODO - Replace this content of this view to suite the needs of your application.
 */
Ext.define('YoYoGo.view.main.MainController', {
  extend: 'Ext.app.ViewController',

  requires: [
    'Ext.MessageBox'
  ],
  
  alias: 'controller.main',
  
  init: function() {
    this.control({
      'button[action=YoYoGoyo]': {
        click: this.onYoYoGoYo
      }
    });
  },
  
  // hh this is explicitly mapped above
  onYoYoGoYo: function() {
    Ext.Msg.confirm('Confirm', 'YoYoGoyo?', 'onConfirm', this);
  },
  
  // hh: those are referred to by 'handler' keys in the view spec
  onClickButton: function () {
    Ext.Msg.confirm('Confirm', 'Are you sure?', 'onConfirm', this);
  },

  onConfirm: function (choice) {
    console.log("Choice: " + choice);
      if (choice === 'yes') {
          //
      }
  }
});
