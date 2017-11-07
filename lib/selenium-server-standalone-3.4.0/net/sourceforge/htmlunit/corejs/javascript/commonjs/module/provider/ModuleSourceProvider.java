package net.sourceforge.htmlunit.corejs.javascript.commonjs.module.provider;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;



















public abstract interface ModuleSourceProvider
{
  public static final ModuleSource NOT_MODIFIED = new ModuleSource(null, null, null, null, null);
  
  public abstract ModuleSource loadSource(String paramString, Scriptable paramScriptable, Object paramObject)
    throws IOException, URISyntaxException;
  
  public abstract ModuleSource loadSource(URI paramURI1, URI paramURI2, Object paramObject)
    throws IOException, URISyntaxException;
}
