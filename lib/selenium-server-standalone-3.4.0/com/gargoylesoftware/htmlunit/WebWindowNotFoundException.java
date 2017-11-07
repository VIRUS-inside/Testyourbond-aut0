package com.gargoylesoftware.htmlunit;












public class WebWindowNotFoundException
  extends RuntimeException
{
  private final String name_;
  










  public WebWindowNotFoundException(String name)
  {
    super("Searching for [" + name + "]");
    name_ = name;
  }
  



  public String getName()
  {
    return name_;
  }
}
