package com.gargoylesoftware.htmlunit.gae;














public final class GAEUtils
{
  private GAEUtils() {}
  












  public static boolean isGaeMode()
  {
    return System.getProperty("com.google.appengine.runtime.environment") != null;
  }
}
