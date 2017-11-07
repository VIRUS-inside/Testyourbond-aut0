package org.eclipse.jetty.websocket.common.extensions;

import org.eclipse.jetty.util.DecoratedObjectFactory;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.extensions.Extension;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;


















public class WebSocketExtensionFactory
  extends ExtensionFactory
{
  private WebSocketContainerScope container;
  
  public WebSocketExtensionFactory(WebSocketContainerScope container)
  {
    this.container = container;
  }
  

  public Extension newInstance(ExtensionConfig config)
  {
    if (config == null)
    {
      return null;
    }
    
    String name = config.getName();
    if (StringUtil.isBlank(name))
    {
      return null;
    }
    
    Class<? extends Extension> extClass = getExtension(name);
    if (extClass == null)
    {
      return null;
    }
    
    try
    {
      Extension ext = (Extension)container.getObjectFactory().createInstance(extClass);
      if ((ext instanceof AbstractExtension))
      {
        AbstractExtension aext = (AbstractExtension)ext;
        aext.init(container);
        aext.setConfig(config);
      }
      return ext;
    }
    catch (InstantiationException|IllegalAccessException e)
    {
      throw new WebSocketException("Cannot instantiate extension: " + extClass, e);
    }
  }
}
