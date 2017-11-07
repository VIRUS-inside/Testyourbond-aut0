package org.openqa.selenium.remote.server.handler;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.openqa.selenium.remote.server.Session;


















public class DescribeElement
  extends WebElementHandler<Map<String, String>>
{
  public DescribeElement(Session session)
  {
    super(session);
  }
  
  public Map<String, String> call() throws Exception
  {
    return ImmutableMap.of("ELEMENT", getElementId());
  }
  
  public String toString()
  {
    return String.format("[describe: %s", new Object[] { getElementAsString() });
  }
}
