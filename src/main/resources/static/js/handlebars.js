Handlebars.getTemplate = function (name) {
  var promise = $.Deferred();
  if (Handlebars.templates === undefined || Handlebars.templates[name] === undefined) {
    $.ajax({
      url: '/handlebars/' + name + '.hbs',
      success: function (data) {
        if (Handlebars.templates === undefined) {
          Handlebars.templates = {};
        }
        Handlebars.templates[name] = Handlebars.compile(data);
        promise.resolve(Handlebars.templates[name]);
      }
    });
  } else {
    promise.resolve(Handlebars.templates[name]);
  }
  return promise;
};

Handlebars.registerHelper("limit", function (items, limit) {
  return items.slice(0, limit);
});
