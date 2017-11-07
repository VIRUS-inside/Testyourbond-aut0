package net.sourceforge.htmlunit.corejs.javascript.commonjs.module.provider;

import java.io.Reader;
import java.io.Serializable;
import java.net.URI;









































public class ModuleSource
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final Reader reader;
  private final Object securityDomain;
  private final URI uri;
  private final URI base;
  private final Object validator;
  
  public ModuleSource(Reader reader, Object securityDomain, URI uri, URI base, Object validator)
  {
    this.reader = reader;
    this.securityDomain = securityDomain;
    this.uri = uri;
    this.base = base;
    this.validator = validator;
  }
  






  public Reader getReader()
  {
    return reader;
  }
  






  public Object getSecurityDomain()
  {
    return securityDomain;
  }
  




  public URI getUri()
  {
    return uri;
  }
  





  public URI getBase()
  {
    return base;
  }
  






  public Object getValidator()
  {
    return validator;
  }
}
