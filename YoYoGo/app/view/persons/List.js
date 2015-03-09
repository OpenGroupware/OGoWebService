// YoYoGo

Ext.define('Ext.ux.form.SearchField', {
  extend: 'Ext.form.field.Text',    
  alias: 'widget.searchfield',    
  listeners: {
    specialKey: function(field, event) {
      if (event.getKey() == event.ENTER)
        this.fireEvent("enter", this);
    }
  },
  triggers: {
    s: { cls: Ext.baseCSSPrefix + 'form-search-trigger' }
  }
});

Ext.define('YoYoGo.view.persons.List', {
  extend: 'Ext.grid.Panel',
  
  xtype: 'app-persons-list',
  
  controller: 'persons-list',
  
  store: 'Persons',
  
  title: 'Persons',
  
  dockedItems: [
    { dock:        'bottom', 
      xtype:       'pagingtoolbar',
      store:       'Persons',
      displayInfo: true,
      displayMsg:  'Persons {0} - {1} of {2}',
      emptyMsg:    "No persons to display"
    }
  ],
  
  tools: [
    { xtype: 'searchfield', yoyo: 'searchfield' },
    { type: 'help',   action: 'help', tooltip: 'Get Help' }
  ],
  
  columns: [
    { text: 'Firstname', dataIndex: 'firstname',
      editor: { xtype: 'textfield', allowBlank: false }
    },
    { text: 'Lastname',  dataIndex: 'lastname', flex: 1,
      editor: { xtype: 'textfield', allowBlank: false }
    },
    { text: 'Number',    dataIndex: 'number',
      editor: { xtype: 'textfield', allowBlank: false }
    },
    { text: 'ID',        dataIndex: 'id'          },
    { text: 'Perms',     dataIndex: 'permissions' }
  ],

  selType: 'rowmodel',
  plugins: [
    Ext.create('Ext.grid.plugin.CellEditing', { clicksToEdit: 2 })
    //Ext.create('Ext.grid.plugin.RowEditing', { clicksToEdit: 1 })
  ]
});
