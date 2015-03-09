/**
 * This class is the main view for the application. It is specified in app.js as the
 * "autoCreateViewport" property. That setting automatically applies the "viewport"
 * plugin to promote that instance of this class to the body element.
 *
 * TODO - Replace this content of this view to suite the needs of your application.
 */

// hh: this is the autocreateViewport in app.js
Ext.define('YoYoGo.view.main.Main', {
  extend: 'Ext.container.Container', // hh: should this be a viewport?

  xtype: 'app-main',
  
  requires: [
    "YoYoGo.view.sidebar.Sidebar",
    "YoYoGo.view.persons.List"
  ],
  
  controller: 'main', // hh: this is the view ctrlr? (/controller/Main.js)
  
  viewModel: {
      type: 'main' // hh: hm, what does this do? MainModel.js I guess
  },

  layout: {
      type: 'border' // hh: what does this say, west/north/etc capability?
  },

  items: [
    { xtype:       'app-sidebar',
      region:      'west', // hh: left side
      split:       true,
      collapsible: true,
      collapsed:   true,
      width:       50
    },
    { region:  'center',
      xtype:   'tabpanel',
      padding: 10,
      items:[
        { title:  'Persons',
          padding: 10,
          layout:  'fit',
          items:   [ { xtype: 'app-persons-list' } ]
        },
        { title: 'Inline HTML',
          html: '<h2>Some HTML content.</h2>'
        }
      ]
    },
    { region:  'north',
      xtype:   'component',
      padding: 10,
      height:  40,
      html:    'Always Right Institute',
      cls:     'appBanner' // hh: CSS class
    },
    {
      region: 'north', // hh: added a toolbar
      xtype: 'toolbar',
      items: [
        { xtype: 'button', text: 'YoYoGoYo', action: 'YoYoGoyo' },
        { xtype: 'button', text: 'MeMeMe',   action: 'mememe' }
      ]
    }
  ]
});
