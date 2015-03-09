// hh: extract the sidebar view into an own component

Ext.define('YoYoGo.view.sidebar.Sidebar', { // keep the container as a wrapper
    extend: 'Ext.panel.Panel',

    xtype: 'app-sidebar',
    
    title: 'YoYoGo Sidebar',

    html: '<ul><li>This area is commonly used for navigation, for example, using a "tree" component.</li></ul>',
    
    tbar: [{
            text: 'Button',
            handler: 'onClickButton'
    }]
});
