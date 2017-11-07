package org.eclipse.jetty.websocket.common.events;

import java.util.ArrayList;




















public class ParamList
  extends ArrayList<Class<?>[]>
{
  public ParamList() {}
  
  public void addParams(Class<?>... paramTypes)
  {
    add(paramTypes);
  }
}
