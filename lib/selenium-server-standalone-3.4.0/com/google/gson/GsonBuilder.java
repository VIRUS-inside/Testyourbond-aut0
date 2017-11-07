package com.google.gson;

import com.google.gson.internal..Gson.Preconditions;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;






























































public final class GsonBuilder
{
  private Excluder excluder = Excluder.DEFAULT;
  private LongSerializationPolicy longSerializationPolicy = LongSerializationPolicy.DEFAULT;
  private FieldNamingStrategy fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
  private final Map<Type, InstanceCreator<?>> instanceCreators = new HashMap();
  
  private final List<TypeAdapterFactory> factories = new ArrayList();
  
  private final List<TypeAdapterFactory> hierarchyFactories = new ArrayList();
  private boolean serializeNulls = false;
  private String datePattern;
  private int dateStyle = 2;
  private int timeStyle = 2;
  private boolean complexMapKeySerialization = false;
  private boolean serializeSpecialFloatingPointValues = false;
  private boolean escapeHtmlChars = true;
  private boolean prettyPrinting = false;
  private boolean generateNonExecutableJson = false;
  private boolean lenient = false;
  







  public GsonBuilder() {}
  






  public GsonBuilder setVersion(double ignoreVersionsAfter)
  {
    excluder = excluder.withVersion(ignoreVersionsAfter);
    return this;
  }
  










  public GsonBuilder excludeFieldsWithModifiers(int... modifiers)
  {
    excluder = excluder.withModifiers(modifiers);
    return this;
  }
  








  public GsonBuilder generateNonExecutableJson()
  {
    generateNonExecutableJson = true;
    return this;
  }
  





  public GsonBuilder excludeFieldsWithoutExposeAnnotation()
  {
    excluder = excluder.excludeFieldsWithoutExposeAnnotation();
    return this;
  }
  






  public GsonBuilder serializeNulls()
  {
    serializeNulls = true;
    return this;
  }
  











































































  public GsonBuilder enableComplexMapKeySerialization()
  {
    complexMapKeySerialization = true;
    return this;
  }
  





  public GsonBuilder disableInnerClassSerialization()
  {
    excluder = excluder.disableInnerClassSerialization();
    return this;
  }
  







  public GsonBuilder setLongSerializationPolicy(LongSerializationPolicy serializationPolicy)
  {
    longSerializationPolicy = serializationPolicy;
    return this;
  }
  







  public GsonBuilder setFieldNamingPolicy(FieldNamingPolicy namingConvention)
  {
    fieldNamingPolicy = namingConvention;
    return this;
  }
  







  public GsonBuilder setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy)
  {
    fieldNamingPolicy = fieldNamingStrategy;
    return this;
  }
  









  public GsonBuilder setExclusionStrategies(ExclusionStrategy... strategies)
  {
    for (ExclusionStrategy strategy : strategies) {
      excluder = excluder.withExclusionStrategy(strategy, true, true);
    }
    return this;
  }
  











  public GsonBuilder addSerializationExclusionStrategy(ExclusionStrategy strategy)
  {
    excluder = excluder.withExclusionStrategy(strategy, true, false);
    return this;
  }
  











  public GsonBuilder addDeserializationExclusionStrategy(ExclusionStrategy strategy)
  {
    excluder = excluder.withExclusionStrategy(strategy, false, true);
    return this;
  }
  





  public GsonBuilder setPrettyPrinting()
  {
    prettyPrinting = true;
    return this;
  }
  







  public GsonBuilder setLenient()
  {
    lenient = true;
    return this;
  }
  






  public GsonBuilder disableHtmlEscaping()
  {
    escapeHtmlChars = false;
    return this;
  }
  
















  public GsonBuilder setDateFormat(String pattern)
  {
    datePattern = pattern;
    return this;
  }
  













  public GsonBuilder setDateFormat(int style)
  {
    dateStyle = style;
    datePattern = null;
    return this;
  }
  














  public GsonBuilder setDateFormat(int dateStyle, int timeStyle)
  {
    this.dateStyle = dateStyle;
    this.timeStyle = timeStyle;
    datePattern = null;
    return this;
  }
  
















  public GsonBuilder registerTypeAdapter(Type type, Object typeAdapter)
  {
    .Gson.Preconditions.checkArgument(((typeAdapter instanceof JsonSerializer)) || ((typeAdapter instanceof JsonDeserializer)) || ((typeAdapter instanceof InstanceCreator)) || ((typeAdapter instanceof TypeAdapter)));
    


    if ((typeAdapter instanceof InstanceCreator)) {
      instanceCreators.put(type, (InstanceCreator)typeAdapter);
    }
    if (((typeAdapter instanceof JsonSerializer)) || ((typeAdapter instanceof JsonDeserializer))) {
      TypeToken<?> typeToken = TypeToken.get(type);
      factories.add(TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, typeAdapter));
    }
    if ((typeAdapter instanceof TypeAdapter)) {
      factories.add(TypeAdapters.newFactory(TypeToken.get(type), (TypeAdapter)typeAdapter));
    }
    return this;
  }
  







  public GsonBuilder registerTypeAdapterFactory(TypeAdapterFactory factory)
  {
    factories.add(factory);
    return this;
  }
  














  public GsonBuilder registerTypeHierarchyAdapter(Class<?> baseType, Object typeAdapter)
  {
    .Gson.Preconditions.checkArgument(((typeAdapter instanceof JsonSerializer)) || ((typeAdapter instanceof JsonDeserializer)) || ((typeAdapter instanceof TypeAdapter)));
    

    if (((typeAdapter instanceof JsonDeserializer)) || ((typeAdapter instanceof JsonSerializer))) {
      hierarchyFactories.add(0, 
        TreeTypeAdapter.newTypeHierarchyFactory(baseType, typeAdapter));
    }
    if ((typeAdapter instanceof TypeAdapter)) {
      factories.add(TypeAdapters.newTypeHierarchyFactory(baseType, (TypeAdapter)typeAdapter));
    }
    return this;
  }
  



















  public GsonBuilder serializeSpecialFloatingPointValues()
  {
    serializeSpecialFloatingPointValues = true;
    return this;
  }
  





  public Gson create()
  {
    List<TypeAdapterFactory> factories = new ArrayList();
    factories.addAll(this.factories);
    Collections.reverse(factories);
    factories.addAll(hierarchyFactories);
    addTypeAdaptersForDate(datePattern, dateStyle, timeStyle, factories);
    
    return new Gson(excluder, fieldNamingPolicy, instanceCreators, serializeNulls, complexMapKeySerialization, generateNonExecutableJson, escapeHtmlChars, prettyPrinting, lenient, serializeSpecialFloatingPointValues, longSerializationPolicy, factories);
  }
  


  private void addTypeAdaptersForDate(String datePattern, int dateStyle, int timeStyle, List<TypeAdapterFactory> factories)
  {
    DefaultDateTypeAdapter dateTypeAdapter;
    
    if ((datePattern != null) && (!"".equals(datePattern.trim()))) {
      dateTypeAdapter = new DefaultDateTypeAdapter(datePattern); } else { DefaultDateTypeAdapter dateTypeAdapter;
      if ((dateStyle != 2) && (timeStyle != 2))
        dateTypeAdapter = new DefaultDateTypeAdapter(dateStyle, timeStyle); else {
        return;
      }
    }
    DefaultDateTypeAdapter dateTypeAdapter;
    factories.add(TreeTypeAdapter.newFactory(TypeToken.get(java.util.Date.class), dateTypeAdapter));
    factories.add(TreeTypeAdapter.newFactory(TypeToken.get(Timestamp.class), dateTypeAdapter));
    factories.add(TreeTypeAdapter.newFactory(TypeToken.get(java.sql.Date.class), dateTypeAdapter));
  }
}
