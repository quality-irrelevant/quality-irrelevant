<#include "include/header.ftl">
<section class="site-form">
  <h1>Contact Quality Irrelevant</h1>

  <p>I mean, you could just send a fucking email to qualityirrelevant@gmail.com</p>

  <form class="contact-form" method="post" novalidate>
    <input type="text" name="name" value="${name}" placeholder="Your Name">
    <#if (nameError?has_content)>
      <p>${nameError}</p>
    </#if>
    <input type="email" name="email" value="${email}" placeholder="Your Email">
    <#if (emailError?has_content)>
      <p>${emailError}</p>
    </#if>
    <textarea name="message" placeholder="Your Message">${message}</textarea>
    <#if (messageError?has_content)>
      <p>${messageError}</p>
    </#if>
    <input class="button" type="submit" name="submitform" value="Submit">
  </form>

</section>

<#include "include/footer.ftl">