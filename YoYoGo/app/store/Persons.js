// YoYoGo

Ext.define('YoYoGo.store.Persons', {
  extend:   'YoYoGo.store.Base',
  
  model: 'YoYoGo.model.Person',
  
  proxy: {
    url: '$WSBASEURL/persons'
  }
});
