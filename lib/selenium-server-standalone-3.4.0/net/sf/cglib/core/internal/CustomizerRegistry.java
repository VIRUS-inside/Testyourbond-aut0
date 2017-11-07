package net.sf.cglib.core.internal;

import java.util.List;
import java.util.Map;
import net.sf.cglib.core.Customizer;
import net.sf.cglib.core.KeyFactoryCustomizer;

public class CustomizerRegistry
{
  private final Class[] customizerTypes;
  private Map<Class, List<KeyFactoryCustomizer>> customizers = new java.util.HashMap();
  
  public CustomizerRegistry(Class[] customizerTypes) {
    this.customizerTypes = customizerTypes;
  }
  
  public void add(KeyFactoryCustomizer customizer) {
    Class<? extends KeyFactoryCustomizer> klass = customizer.getClass();
    for (Class type : customizerTypes) {
      if (type.isAssignableFrom(klass)) {
        List<KeyFactoryCustomizer> list = (List)customizers.get(type);
        if (list == null) {
          customizers.put(type, list = new java.util.ArrayList());
        }
        list.add(customizer);
      }
    }
  }
  
  public <T> List<T> get(Class<T> klass) {
    List<KeyFactoryCustomizer> list = (List)customizers.get(klass);
    if (list == null) {
      return java.util.Collections.emptyList();
    }
    return list;
  }
  



  @Deprecated
  public static CustomizerRegistry singleton(Customizer customizer)
  {
    CustomizerRegistry registry = new CustomizerRegistry(new Class[] { Customizer.class });
    registry.add(customizer);
    return registry;
  }
}
