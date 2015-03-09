Ext.define('YoYoGo.view.common.Controller', {
  extend: 'Ext.app.ViewController',

  init: function() {
    var self = this;
    
    self.logALot = false;
  },

  configureColumnsFromModel: function(model) {
    var newColumns = Ext.Array.map(model.getFields(), function(field) {
      return {
        text:      Ext.String.capitalize(field.name),
        dataIndex: field.name,
        flex: 1,
        editor: {
            xtype: 'textfield',
            allowBlank: false
        }
      }
    });
    this.getView().reconfigure(undefined /* store */, newColumns);
  }
});
