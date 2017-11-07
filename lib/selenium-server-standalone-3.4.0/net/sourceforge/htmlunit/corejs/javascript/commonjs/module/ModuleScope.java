package net.sourceforge.htmlunit.corejs.javascript.commonjs.module;

import java.net.URI;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.TopLevel;










public class ModuleScope
  extends TopLevel
{
  private static final long serialVersionUID = 1L;
  private final URI uri;
  private final URI base;
  
  public ModuleScope(Scriptable prototype, URI uri, URI base)
  {
    this.uri = uri;
    this.base = base;
    setPrototype(prototype);
    cacheBuiltins();
  }
  
  public URI getUri() {
    return uri;
  }
  
  public URI getBase() {
    return base;
  }
}
