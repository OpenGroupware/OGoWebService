// YoYoGo

Ext.define('YoYoGo.store.Base', {
  extend:   'Ext.data.Store',
  
  autoLoad:     false,
  
  remoteSort:   true,
  remoteFilter: true,

  proxy: {
    type:                'rest',
    withCredentials:     true,
    useDefaultXhrHeader: false,
    
    headers:    { "accept": "application/json" },
    
    startParam: 'idx',
    limitParam: 'limit',
    pageParam:  'page',
    extraParams: { "filterop": "OR" },
    
    reader: {
      type: 'json',
      
      rootProperty:    'results',
      totalProperty:   'count',
      messageProperty: 'message'
    }
  }
});
