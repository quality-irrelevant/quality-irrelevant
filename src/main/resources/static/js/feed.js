var posts = function ($element) {
  var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
  if ($element.length > 0) {
    var templateName = "posts/" + $element.data("view");
    var id = $element.data("id");
    $element.addClass("posts-" + $element.data("view"));
    $.ajax({
      url: "/rss.xml",
      success: function (xml) {
        var context = {posts: []};
        $(xml).find("item").each(function () {
          var currentId = $(this).find("id").text();
          if (id === undefined || id === currentId) {
            var pubDate = new Date($(this).find("pubDate").text());
            var post = {
              title: $(this).find("title").text(),
              publishedOn: pubDate.getDate() + " " + months[pubDate.getMonth()] + " " + pubDate.getFullYear(),
              url: $(this).find("link").text(),
              description: $(this).find("description").text(),
              id: currentId
            };
            context.posts.push(post);
          }
          if (id === currentId) {
            return false;
          }
        });
        Handlebars.getTemplate(templateName).done(function (template) {
          var html = template(context);
          $element.append(html);
        });

      },
      error: function () {
        $element.append($('<li>Failed to get the damn feed!</li>'));
      }
    });
  }
};
$(document).ready(function () {
  posts($('.posts'));
});