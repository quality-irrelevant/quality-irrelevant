<#-- @ftlvariable name="episodes" type="java.util.Collection<com.qualityirrelevant.web.models.Episode>" -->
<#include "../include/header.ftl">
<div class="posts posts-thumbnails">
<#list episodes as episode>
  <a href="/episodes/${episode.id}">
    <div class="number">${episode.id}</div>
    <img class="thumbnail" src="/img/icon_empty.png" width="250" height="250"/>
    <div class="post-info">
      <h2>${episode.title}</h2>
      <time>${episode.publishedOn}</time>
    </div>
  </a>
</#list>
</div>

<#include "../include/footer.ftl">