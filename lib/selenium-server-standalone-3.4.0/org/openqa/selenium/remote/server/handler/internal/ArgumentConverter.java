package org.openqa.selenium.remote.server.handler.internal;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.openqa.selenium.remote.server.KnownElements;
import org.openqa.selenium.remote.server.KnownElements.ProxiedElement;















public class ArgumentConverter
  implements Function<Object, Object>
{
  private final KnownElements knownElements;
  
  public ArgumentConverter(KnownElements knownElements)
  {
    this.knownElements = knownElements;
  }
  
  public Object apply(Object arg) {
    if ((arg instanceof Map))
    {
      Map<String, Object> paramAsMap = (Map)arg;
      if (paramAsMap.containsKey("ELEMENT"))
      {
        KnownElements.ProxiedElement element = (KnownElements.ProxiedElement)knownElements.get((String)paramAsMap.get("ELEMENT"));
        return element.getWrappedElement();
      }
      
      Map<String, Object> converted = Maps.newHashMapWithExpectedSize(paramAsMap.size());
      for (Map.Entry<String, Object> entry : paramAsMap.entrySet()) {
        converted.put(entry.getKey(), apply(entry.getValue()));
      }
      return converted;
    }
    
    if ((arg instanceof List)) {
      return Lists.newArrayList(Iterables.transform((List)arg, this));
    }
    
    return arg;
  }
}
