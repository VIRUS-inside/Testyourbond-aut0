package org.openqa.selenium.remote.internal;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.remote.Dialect;
import org.openqa.selenium.remote.RemoteWebElement;























public class WebElementToJsonConverter
  implements Function<Object, Object>
{
  public WebElementToJsonConverter() {}
  
  public Object apply(Object arg)
  {
    if ((arg == null) || ((arg instanceof String)) || ((arg instanceof Boolean)) || ((arg instanceof Number)))
    {
      return arg;
    }
    
    while ((arg instanceof WrapsElement)) {
      arg = ((WrapsElement)arg).getWrappedElement();
    }
    
    if ((arg instanceof RemoteWebElement)) {
      return ImmutableMap.of(Dialect.OSS
        .getEncodedElementKey(), ((RemoteWebElement)arg).getId(), Dialect.W3C
        .getEncodedElementKey(), ((RemoteWebElement)arg).getId());
    }
    
    if (arg.getClass().isArray()) {
      arg = Lists.newArrayList((Object[])arg);
    }
    
    if ((arg instanceof Collection)) {
      Collection<?> args = (Collection)arg;
      return Collections2.transform(args, this);
    }
    
    if ((arg instanceof Map)) {
      Map<?, ?> args = (Map)arg;
      Map<String, Object> converted = Maps.newHashMapWithExpectedSize(args.size());
      for (Map.Entry<?, ?> entry : args.entrySet()) {
        Object key = entry.getKey();
        if (!(key instanceof String))
        {
          throw new IllegalArgumentException("All keys in Map script arguments must be strings: " + key.getClass().getName());
        }
        converted.put((String)key, apply(entry.getValue()));
      }
      return converted;
    }
    

    throw new IllegalArgumentException("Argument is of an illegal type: " + arg.getClass().getName());
  }
}
