package org.openqa.selenium.remote.server.handler.internal;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.KnownElements;



















public class ResultConverter
  implements Function<Object, Object>
{
  private final KnownElements knownElements;
  
  public ResultConverter(KnownElements knownElements)
  {
    this.knownElements = knownElements;
  }
  
  public Object apply(Object result) {
    if ((result instanceof WebElement)) {
      String elementId = knownElements.add((WebElement)result);
      return ImmutableMap.of("ELEMENT", elementId);
    }
    
    if ((result instanceof List))
    {
      List<Object> resultAsList = (List)result;
      return Lists.newArrayList(Iterables.transform(resultAsList, this));
    }
    
    if ((result instanceof Map)) {
      Map<?, ?> resultAsMap = (Map)result;
      Map<Object, Object> converted = Maps.newHashMapWithExpectedSize(resultAsMap.size());
      for (Map.Entry<?, ?> entry : resultAsMap.entrySet()) {
        converted.put(entry.getKey(), apply(entry.getValue()));
      }
      return converted;
    }
    
    return result;
  }
}
