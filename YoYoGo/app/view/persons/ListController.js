// YoYoGo

Ext.define('YoYoGo.view.persons.ListController', {
  extend: 'YoYoGo.view.common.Controller',

  requires: [
    'Ext.MessageBox',
    'YoYoGo.view.persons.FilterBuilder'
  ],
  
  alias: 'controller.persons-list',
  
  models: [ 'Person'  ], // require & provide accessors
  stores: [ 'Persons' ],
  
  init: function() {
    var self = this;
    if (self.logALot) {
      console.log("init persons ...");
      console.log("app", YoYoGo.Current);
    }
    
    self.filterBuilder = Ext.create('YoYoGo.view.persons.FilterBuilder');
    
    this.control({
      'tool[action=help]':    { click: this.onHelp   },
      'searchfield[yoyo=searchfield]': {
        change: this.onSearch,
        enter:  this.onSearch
      }
    });
    
    self.getView().on('beforeedit', this.onBeforeEdit, self);    
    self.getView().on("edit",       this.onEdit,       self);
    
    var store = self.getStore();
    if (false) {
      var model = store.getModel();
      //this.configureColumnsFromModel(model);
    }
    
    store.reload(); // this triggers the login panel if it's missing
    
    // FIXME: we need to .un() ourselves!
    YoYoGo.Current.on("didLogin", function() {
      self.getStore().reload();
    }, self);
  },
  
  onSearch: Ext.Function.createBuffered(function(sender) {
    var store = this.getStore();
    var q     = this.filterBuilder.smartQualifierForSearchString(sender.value);
    
    if (q == null) {
      if (this.logALot) console.log("clear search:", sender.value);
      store.clearFilter();
      return;
    }
    
    if (this.logALot) console.log("do search:", sender.value);
    console.log("setting " + q.filters.length + " filters:", q.filters);
    store.setFilters(q.filters);
    console.log("  got:", store.getFilters());
  }, 200),
  
  onHelp: function(sender) {
    alert("Ha ha, har har! ;-)");
  },
  
  getStore: function() {
    return this.getView().getStore();
  },
  
  onBeforeEdit: function(plugin, edit) {
    // must have 'w' / write permission
    var perms = edit.record.get("permissions");
    if (perms.indexOf("w") < 0)
      return false;
  },
  
  onEdit: function(sender, grid) {
    this.getStore().sync({ failure: self.onSaveError, scope: this });
    // single commit doesn't work, presumably because the Model isn't properly
    // setup?
    // grid.record.commit();
  },
  
  onSaveError: function(batch, options) {
    var exceptions = batch.exceptions;
    for (var i = 0; i < exceptions.length; i++) {
      var ex    = exceptions[i];
      var error = ex.getError();
      Ext.Msg.alert('Save Error', JSON.stringify(error));
      
      var recs = ex.getRecords();
      for (var j = 0; j < recs.length; j++) {
        recs[j].reject();
      }
    }
  }
});
