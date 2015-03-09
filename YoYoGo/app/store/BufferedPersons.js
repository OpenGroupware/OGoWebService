// YoYoGo

Ext.define('YoYoGo.store.BufferedPersons', {
  extend:   'YoYoGo.store.Base',
  
  model:    'YoYoGo.model.Person',
  
  autoLoad:     true,

  leadingBufferZone: 500,
  pageSize:          200,
  
  proxy: {
    url:    '$WSBASEURL/persons'
  }
});
