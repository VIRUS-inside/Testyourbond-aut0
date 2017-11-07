package com.gargoylesoftware.htmlunit.javascript.configuration;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface BrowserFeature
{
  WebBrowser[] value() default {@WebBrowser(BrowserName.IE), @WebBrowser(BrowserName.FF), @WebBrowser(BrowserName.CHROME), @WebBrowser(BrowserName.EDGE)};
}
