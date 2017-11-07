package com.gargoylesoftware.htmlunit.javascript.configuration;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Repeatable(JsxClasses.class)
public @interface JsxClass
{
  Class<?> domClass() default Object.class;
  
  boolean isJSObject() default true;
  
  String className() default "";
  
  WebBrowser[] browsers() default {@WebBrowser(BrowserName.CHROME), @WebBrowser(BrowserName.FF), @WebBrowser(BrowserName.IE), @WebBrowser(BrowserName.EDGE)};
}
