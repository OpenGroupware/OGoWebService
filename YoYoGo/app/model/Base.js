// YoYoGo

Ext.define('YoYoGo.model.Base', {
  extend: 'Ext.data.Model',

  schema: {
    namespace: 'YoYoGo.model'
    // Note: 'urlPrefix'
  },

  proxy: {
    type:       'rest',
    reader:     'json',
    
    withCredentials:     true,
    useDefaultXhrHeader: false,
    
    headers:    { "accept": "application/json" }
  },

  fields: [
    { name: 'id',          type: 'int'    },
    { name: 'permissions', type: 'string' }
  ],

  idProperty: 'id'
});
