<#-- @ftlvariable name="episode" type="com.qualityirrelevant.web.models.Episode" -->
<#include "../include/header.ftl">

<img class="thumbnail" src="/img/icon.png" width="250" height="250"/>

<div class="post-info">
  <h2>${episode.title}</h2>
  <time><img class="icon" src="/img/calendar.png" width="20"
             height="20"/>${episode.publishedOn}</time>
</div>

<audio controls preload="none">
  <source src="${episode.url}" type="audio/mp3">
</audio>

<p class="description">${episode.description}</p>

<#if authenticated>
  <a href="/episodes/${episode.id}/edit" class="button">Edit</a>
  <form method="post" action="/episodes/${episode.id}/delete">
    <input class="button" type="submit" value="Delete">
  </form>
</#if>


<#include "../include/footer.ftl">