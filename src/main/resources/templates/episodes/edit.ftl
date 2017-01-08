<#include "../include/header.ftl">
<section class="site-form">

  <form method="post" action="/episodes/${episode.id}">
    <input type="text" name="name" value="${episode.name}" placeholder="Title">
    <label for="description">Description</label>
    <textarea id="description" name="description">${episode.description}</textarea>
    <input class="button" type="submit" value="Update">
  </form>

</section>

<#include "../include/footer.ftl">