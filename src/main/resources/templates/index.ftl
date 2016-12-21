<#-- @ftlvariable name="episodes" type="java.util.Collection<com.qualityirrelevant.web.models.Episode>" -->
<#include "include/header.ftl">

<ul class="posts posts-players">
  <#list episodes as episode>
    <li class="post">
      <a href="/episodes/${episode.id}"><img class="icon" src="/img/icon.png" width="50" height="50"/></a>

      <a href="/episodes/${episode.id}"><h2 class="episode-title">${episode.title}</h2></a>

      <time><img class="icon" src="/img/calendar.png" width="20"
                 height="20"/>${episode.publishedOn}
      </time>

      <audio controls>
        <source src="${episode.url}" type='audio/mp3'>
      </audio>

      <p class="description">${episode.description}</p>
    </li>
  </#list>
</ul>

<a href="/episodes" class="button">All Episodes</a>

<#include "include/footer.ftl">
