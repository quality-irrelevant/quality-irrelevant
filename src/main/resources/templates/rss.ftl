<#-- @ftlvariable name="episodes" type="java.util.Collection<com.qualityirrelevant.web.models.Episode>" -->
<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0" xmlns:itunes="http://www.itunes.com/dtds/podcast-1.0.dtd">
  <channel>
    <title>Quality Irrelevant</title>
    <link>https://qualityirrelevant.com</link>
    <ttl>60</ttl>
    <language>en</language>
    <copyright>All rights reserved</copyright>
    <description>Quality Irrelevant is an infrequent and unfunny show where Theo forces his friend and hostage, Phil, to record a podcast. What could go wrong?</description>
    <itunes:subtitle>Quality Irrelevant is an infrequent and unfunny show where Theo forces his friend and hostage, Phil, to record a podcast. What could go wrong?</itunes:subtitle>
    <itunes:owner>
      <itunes:name>Quality Irrelevant</itunes:name>
      <itunes:email>qualityirrelevant@gmail.com</itunes:email>
    </itunes:owner>
    <itunes:author>Quality Irrelevant</itunes:author>
    <itunes:keywords>quality, irrelevant, funny</itunes:keywords>
    <itunes:explicit>yes</itunes:explicit>
    <itunes:image href="https://qualityirrelevant.com/media/images/01.png"/>
    <image>
      <url>https://qualityirrelevant.com/media/images/01.png</url>
      <title>Quality Irrelevant</title>
      <link>https://qualityirrelevant.com</link>
    </image>
    <itunes:category text="Comedy"/>
    <#list episodes as episode>
      <item>
        <guid isPermaLink="false">${episode.absoluteUrl}</guid>
        <title>${episode.title}</title>
        <pubDate>${episode.fullPublishedOn}</pubDate>
        <link>${episode.absoluteUrl}</link>
        <id>${episode.id}</id>
        <itunes:duration>${episode.formattedDuration}</itunes:duration>
        <itunes:author>Quality Irrelevant</itunes:author>
        <itunes:explicit>yes</itunes:explicit>
        <itunes:subtitle>${episode.description}</itunes:subtitle>
        <description>${episode.description}</description>
        <enclosure type="audio/mpeg" url="${episode.absoluteUrl}" length="${episode.size?c}"/>
        <itunes:image href="https://qualityirrelevant.com/media/images/02.png"/>
      </item>
    </#list>
  </channel>
</rss>