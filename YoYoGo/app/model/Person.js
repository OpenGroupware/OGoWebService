// YoYoGo

Ext.define('YoYoGo.model.Person', {
  extend: 'YoYoGo.model.Base',
  
  proxy: {
    // me wants this to be dynamic
    //url: 'http://localhost:8181/OGoWS/persons'
  },
  
  fields: [
    { name: 'login',  type: 'string' },
    { name: 'number', type: 'string' },
    'lastname', 'firstname'
  ],
  
  validators: {
    number: 'presence'
  }
});
