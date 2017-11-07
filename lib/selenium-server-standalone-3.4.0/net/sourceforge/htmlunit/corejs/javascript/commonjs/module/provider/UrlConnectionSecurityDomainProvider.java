package net.sourceforge.htmlunit.corejs.javascript.commonjs.module.provider;

import java.net.URLConnection;

public abstract interface UrlConnectionSecurityDomainProvider
{
  public abstract Object getSecurityDomain(URLConnection paramURLConnection);
}
