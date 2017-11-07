package net.sourceforge.htmlunit.corejs.javascript.commonjs.module;

import java.io.Serializable;
import java.net.URI;
import net.sourceforge.htmlunit.corejs.javascript.Script;























public class ModuleScript
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final Script script;
  private final URI uri;
  private final URI base;
  
  public ModuleScript(Script script, URI uri, URI base)
  {
    this.script = script;
    this.uri = uri;
    this.base = base;
  }
  




  public Script getScript()
  {
    return script;
  }
  




  public URI getUri()
  {
    return uri;
  }
  





  public URI getBase()
  {
    return base;
  }
  





  public boolean isSandboxed()
  {
    return (base != null) && (uri != null) && 
      (!base.relativize(uri).isAbsolute());
  }
}
