<#include "../include/header.ftl">
<section class="site-form">

  <form method="post" action="/episodes" enctype="multipart/form-data">
    <input type="number" name="number" value="${episode.number?c}" placeholder="#">
    <input type="text" name="name" value="${episode.name}" placeholder="Title">
    <label for="description">Description</label>
    <textarea id="description" name="description">${episode.description}</textarea>
    <input type="file" name="file" accept="audio/*">
    <input class="button" type="submit" value="Create">
  </form>

</section>

<#include "../include/footer.ftl">