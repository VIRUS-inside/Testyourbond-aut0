package org.openqa.grid.shared;

import java.util.Map;
import javax.servlet.Servlet;
import org.openqa.grid.internal.utils.configuration.StandaloneConfiguration;

public abstract interface GridNodeServer
{
  public abstract void boot()
    throws Exception;
  
  public abstract void stop();
  
  public abstract int getRealPort();
  
  public abstract void setExtraServlets(Map<String, Class<? extends Servlet>> paramMap);
  
  public abstract void setConfiguration(StandaloneConfiguration paramStandaloneConfiguration);
}
