package org.apache.http.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.http.util.Args;

































public final class RegistryBuilder<I>
{
  private final Map<String, I> items;
  
  public static <I> RegistryBuilder<I> create()
  {
    return new RegistryBuilder();
  }
  
  RegistryBuilder()
  {
    items = new HashMap();
  }
  
  public RegistryBuilder<I> register(String id, I item) {
    Args.notEmpty(id, "ID");
    Args.notNull(item, "Item");
    items.put(id.toLowerCase(Locale.ROOT), item);
    return this;
  }
  
  public Registry<I> build() {
    return new Registry(items);
  }
  
  public String toString()
  {
    return items.toString();
  }
}
