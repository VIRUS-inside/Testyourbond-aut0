package net.sourceforge.htmlunit.corejs.javascript.commonjs.module.provider;

import java.net.URLConnection;

public abstract interface UrlConnectionExpiryCalculator
{
  public abstract long calculateExpiry(URLConnection paramURLConnection);
}
