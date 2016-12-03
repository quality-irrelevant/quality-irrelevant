jQuery(document).ready(function ($) {
  var $contactForm = $(".contact-form");
  if ($contactForm.length > 0) {
    $contactForm.validate({
      rules: {
        name: {
          required: true,
          minlength: 7
        },
        email: {
          required: true,
          email: true
        },
        message: {
          required: true,
          minlength: 10
        }
      },
      messages: {
        name: {
          required: "Enter your name, or any goddamn name.",
          minlength: "Woah, that's bit short. You trying to spam us boy?"
        },
        email: {
          required: "Gonna need an email address here or no cigar.",
          email: "That email is not valid. What you trying to pull?"
        },
        message: {
          required: "Say something!",
          minlength: "Come on, you can do better than that."
        }
      }
    });
  }
});