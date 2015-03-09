Ext.define("YoYoGo.view.persons.FilterBuilder", {

  smartQualifierForSearchString: function(s) {
    s = s || null;
    var parts = this.trimmedStringParts(s);
    if (parts.length == 0)
      return null;
    
    var filters = [];
    for (var i = 0; i < parts.length; i++) {
      var sf;
      
      console.log("PART:" + parts[i])
      sf = this.filtersForName(parts[i]);
      
      Ext.Array.push(filters, sf);
    }
    
    console.log("FILTERS:", filters);
    return { filters: filters, op: 'OR' }
  },
  
  filtersForName: function(s) {
    var properties = [ 'firstname', 'lastname' ];
    var filters = this.filtersForProperties(properties, s + "*");
    return filters;
  },
  
  trimmedStringParts: function(s) {
    if (s == null || s.length == 0)
      return [];
    
    var sparts = s.split(/\s+/g);
    var parts  = [];
    for (var i = 0; i < sparts.length; i++) {
      var sp = sparts[i];
      if (sp && sp.length > 0)
        parts.push(sp);
    }
    return parts;
  },
  
  filtersForProperties: function(properties, value, operator, caseSensitive) {
    operator      = operator || 'caseInsensitiveLike';
    caseSensitive = caseSensitive || false;
    
    var filters = Ext.Array.map(properties, function(prop) {
      return { property: prop,
               value:    value,
               caseSensitive: caseSensitive, // not sent to server
               operator: operator };
    });
    return filters;
  }
  
});
