package com.google.gson;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SqlDateTypeAdapter;
import com.google.gson.internal.bind.TimeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;






























































public final class Gson
{
  static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
  static final boolean DEFAULT_LENIENT = false;
  static final boolean DEFAULT_PRETTY_PRINT = false;
  static final boolean DEFAULT_ESCAPE_HTML = true;
  static final boolean DEFAULT_SERIALIZE_NULLS = false;
  static final boolean DEFAULT_COMPLEX_MAP_KEYS = false;
  static final boolean DEFAULT_SPECIALIZE_FLOAT_VALUES = false;
  private static final TypeToken<?> NULL_KEY_SURROGATE = new TypeToken() {};
  



  private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
  



  private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls = new ThreadLocal();
  

  private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache = new ConcurrentHashMap();
  



  private final List<TypeAdapterFactory> factories;
  



  private final ConstructorConstructor constructorConstructor;
  


  private final Excluder excluder;
  


  private final FieldNamingStrategy fieldNamingStrategy;
  


  private final boolean serializeNulls;
  


  private final boolean htmlSafe;
  


  private final boolean generateNonExecutableJson;
  


  private final boolean prettyPrinting;
  


  private final boolean lenient;
  


  private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  



  public Gson()
  {
    this(Excluder.DEFAULT, FieldNamingPolicy.IDENTITY, 
      Collections.emptyMap(), false, false, false, true, false, false, false, LongSerializationPolicy.DEFAULT, 
      

      Collections.emptyList());
  }
  




  Gson(Excluder excluder, FieldNamingStrategy fieldNamingStrategy, Map<Type, InstanceCreator<?>> instanceCreators, boolean serializeNulls, boolean complexMapKeySerialization, boolean generateNonExecutableGson, boolean htmlSafe, boolean prettyPrinting, boolean lenient, boolean serializeSpecialFloatingPointValues, LongSerializationPolicy longSerializationPolicy, List<TypeAdapterFactory> typeAdapterFactories)
  {
    constructorConstructor = new ConstructorConstructor(instanceCreators);
    this.excluder = excluder;
    this.fieldNamingStrategy = fieldNamingStrategy;
    this.serializeNulls = serializeNulls;
    generateNonExecutableJson = generateNonExecutableGson;
    this.htmlSafe = htmlSafe;
    this.prettyPrinting = prettyPrinting;
    this.lenient = lenient;
    
    List<TypeAdapterFactory> factories = new ArrayList();
    

    factories.add(TypeAdapters.JSON_ELEMENT_FACTORY);
    factories.add(ObjectTypeAdapter.FACTORY);
    

    factories.add(excluder);
    

    factories.addAll(typeAdapterFactories);
    

    factories.add(TypeAdapters.STRING_FACTORY);
    factories.add(TypeAdapters.INTEGER_FACTORY);
    factories.add(TypeAdapters.BOOLEAN_FACTORY);
    factories.add(TypeAdapters.BYTE_FACTORY);
    factories.add(TypeAdapters.SHORT_FACTORY);
    TypeAdapter<Number> longAdapter = longAdapter(longSerializationPolicy);
    factories.add(TypeAdapters.newFactory(Long.TYPE, Long.class, longAdapter));
    factories.add(TypeAdapters.newFactory(Double.TYPE, Double.class, 
      doubleAdapter(serializeSpecialFloatingPointValues)));
    factories.add(TypeAdapters.newFactory(Float.TYPE, Float.class, 
      floatAdapter(serializeSpecialFloatingPointValues)));
    factories.add(TypeAdapters.NUMBER_FACTORY);
    factories.add(TypeAdapters.ATOMIC_INTEGER_FACTORY);
    factories.add(TypeAdapters.ATOMIC_BOOLEAN_FACTORY);
    factories.add(TypeAdapters.newFactory(AtomicLong.class, atomicLongAdapter(longAdapter)));
    factories.add(TypeAdapters.newFactory(AtomicLongArray.class, atomicLongArrayAdapter(longAdapter)));
    factories.add(TypeAdapters.ATOMIC_INTEGER_ARRAY_FACTORY);
    factories.add(TypeAdapters.CHARACTER_FACTORY);
    factories.add(TypeAdapters.STRING_BUILDER_FACTORY);
    factories.add(TypeAdapters.STRING_BUFFER_FACTORY);
    factories.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
    factories.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
    factories.add(TypeAdapters.URL_FACTORY);
    factories.add(TypeAdapters.URI_FACTORY);
    factories.add(TypeAdapters.UUID_FACTORY);
    factories.add(TypeAdapters.CURRENCY_FACTORY);
    factories.add(TypeAdapters.LOCALE_FACTORY);
    factories.add(TypeAdapters.INET_ADDRESS_FACTORY);
    factories.add(TypeAdapters.BIT_SET_FACTORY);
    factories.add(DateTypeAdapter.FACTORY);
    factories.add(TypeAdapters.CALENDAR_FACTORY);
    factories.add(TimeTypeAdapter.FACTORY);
    factories.add(SqlDateTypeAdapter.FACTORY);
    factories.add(TypeAdapters.TIMESTAMP_FACTORY);
    factories.add(ArrayTypeAdapter.FACTORY);
    factories.add(TypeAdapters.CLASS_FACTORY);
    

    factories.add(new CollectionTypeAdapterFactory(constructorConstructor));
    factories.add(new MapTypeAdapterFactory(constructorConstructor, complexMapKeySerialization));
    jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    factories.add(jsonAdapterFactory);
    factories.add(TypeAdapters.ENUM_FACTORY);
    factories.add(new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder, jsonAdapterFactory));
    

    this.factories = Collections.unmodifiableList(factories);
  }
  
  public Excluder excluder() {
    return excluder;
  }
  
  public FieldNamingStrategy fieldNamingStrategy() {
    return fieldNamingStrategy;
  }
  
  public boolean serializeNulls() {
    return serializeNulls;
  }
  
  public boolean htmlSafe() {
    return htmlSafe;
  }
  
  private TypeAdapter<Number> doubleAdapter(boolean serializeSpecialFloatingPointValues) {
    if (serializeSpecialFloatingPointValues) {
      return TypeAdapters.DOUBLE;
    }
    new TypeAdapter() {
      public Double read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        }
        return Double.valueOf(in.nextDouble());
      }
      
      public void write(JsonWriter out, Number value) throws IOException { if (value == null) {
          out.nullValue();
          return;
        }
        double doubleValue = value.doubleValue();
        Gson.checkValidFloatingPoint(doubleValue);
        out.value(value);
      }
    };
  }
  
  private TypeAdapter<Number> floatAdapter(boolean serializeSpecialFloatingPointValues) {
    if (serializeSpecialFloatingPointValues) {
      return TypeAdapters.FLOAT;
    }
    new TypeAdapter() {
      public Float read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        }
        return Float.valueOf((float)in.nextDouble());
      }
      
      public void write(JsonWriter out, Number value) throws IOException { if (value == null) {
          out.nullValue();
          return;
        }
        float floatValue = value.floatValue();
        Gson.checkValidFloatingPoint(floatValue);
        out.value(value);
      }
    };
  }
  
  static void checkValidFloatingPoint(double value) {
    if ((Double.isNaN(value)) || (Double.isInfinite(value))) {
      throw new IllegalArgumentException(value + " is not a valid double value as per JSON specification. To override this behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
    }
  }
  

  private static TypeAdapter<Number> longAdapter(LongSerializationPolicy longSerializationPolicy)
  {
    if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
      return TypeAdapters.LONG;
    }
    new TypeAdapter() {
      public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        }
        return Long.valueOf(in.nextLong());
      }
      
      public void write(JsonWriter out, Number value) throws IOException { if (value == null) {
          out.nullValue();
          return;
        }
        out.value(value.toString());
      }
    };
  }
  
  private static TypeAdapter<AtomicLong> atomicLongAdapter(TypeAdapter<Number> longAdapter) {
    
    






      new TypeAdapter()
      {
        public void write(JsonWriter out, AtomicLong value)
          throws IOException { val$longAdapter.write(out, Long.valueOf(value.get())); }
        
        public AtomicLong read(JsonReader in) throws IOException {
          Number value = (Number)val$longAdapter.read(in);
          return new AtomicLong(value.longValue());
        }
      }.nullSafe();
  }
  
  private static TypeAdapter<AtomicLongArray> atomicLongArrayAdapter(TypeAdapter<Number> longAdapter) {
    
    





















      new TypeAdapter()
      {
        public void write(JsonWriter out, AtomicLongArray value)
          throws IOException
        {
          out.beginArray();
          int i = 0; for (int length = value.length(); i < length; i++) {
            val$longAdapter.write(out, Long.valueOf(value.get(i)));
          }
          out.endArray();
        }
        
        public AtomicLongArray read(JsonReader in) throws IOException { List<Long> list = new ArrayList();
          in.beginArray();
          while (in.hasNext()) {
            long value = ((Number)val$longAdapter.read(in)).longValue();
            list.add(Long.valueOf(value));
          }
          in.endArray();
          int length = list.size();
          AtomicLongArray array = new AtomicLongArray(length);
          for (int i = 0; i < length; i++) {
            array.set(i, ((Long)list.get(i)).longValue());
          }
          return array;
        }
      }.nullSafe();
  }
  






  public <T> TypeAdapter<T> getAdapter(TypeToken<T> type)
  {
    TypeAdapter<?> cached = (TypeAdapter)typeTokenCache.get(type == null ? NULL_KEY_SURROGATE : type);
    if (cached != null) {
      return cached;
    }
    
    Map<TypeToken<?>, FutureTypeAdapter<?>> threadCalls = (Map)calls.get();
    boolean requiresThreadLocalCleanup = false;
    if (threadCalls == null) {
      threadCalls = new HashMap();
      calls.set(threadCalls);
      requiresThreadLocalCleanup = true;
    }
    

    FutureTypeAdapter<T> ongoingCall = (FutureTypeAdapter)threadCalls.get(type);
    if (ongoingCall != null) {
      return ongoingCall;
    }
    try
    {
      FutureTypeAdapter<T> call = new FutureTypeAdapter();
      threadCalls.put(type, call);
      
      for (TypeAdapterFactory factory : factories) {
        TypeAdapter<T> candidate = factory.create(this, type);
        if (candidate != null) {
          call.setDelegate(candidate);
          typeTokenCache.put(type, candidate);
          return candidate;
        }
      }
      throw new IllegalArgumentException("GSON cannot handle " + type);
    } finally {
      threadCalls.remove(type);
      
      if (requiresThreadLocalCleanup) {
        calls.remove();
      }
    }
  }
  



















































  public <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory skipPast, TypeToken<T> type)
  {
    if (!factories.contains(skipPast)) {
      skipPast = jsonAdapterFactory;
    }
    
    boolean skipPastFound = false;
    for (TypeAdapterFactory factory : factories) {
      if (!skipPastFound) {
        if (factory == skipPast) {
          skipPastFound = true;
        }
      }
      else
      {
        TypeAdapter<T> candidate = factory.create(this, type);
        if (candidate != null)
          return candidate;
      }
    }
    throw new IllegalArgumentException("GSON cannot serialize " + type);
  }
  





  public <T> TypeAdapter<T> getAdapter(Class<T> type)
  {
    return getAdapter(TypeToken.get(type));
  }
  












  public JsonElement toJsonTree(Object src)
  {
    if (src == null) {
      return JsonNull.INSTANCE;
    }
    return toJsonTree(src, src.getClass());
  }
  















  public JsonElement toJsonTree(Object src, Type typeOfSrc)
  {
    JsonTreeWriter writer = new JsonTreeWriter();
    toJson(src, typeOfSrc, writer);
    return writer.get();
  }
  












  public String toJson(Object src)
  {
    if (src == null) {
      return toJson(JsonNull.INSTANCE);
    }
    return toJson(src, src.getClass());
  }
  














  public String toJson(Object src, Type typeOfSrc)
  {
    StringWriter writer = new StringWriter();
    toJson(src, typeOfSrc, writer);
    return writer.toString();
  }
  












  public void toJson(Object src, Appendable writer)
    throws JsonIOException
  {
    if (src != null) {
      toJson(src, src.getClass(), writer);
    } else {
      toJson(JsonNull.INSTANCE, writer);
    }
  }
  













  public void toJson(Object src, Type typeOfSrc, Appendable writer)
    throws JsonIOException
  {
    try
    {
      JsonWriter jsonWriter = newJsonWriter(Streams.writerForAppendable(writer));
      toJson(src, typeOfSrc, jsonWriter);
    } catch (IOException e) {
      throw new JsonIOException(e);
    }
  }
  




  public void toJson(Object src, Type typeOfSrc, JsonWriter writer)
    throws JsonIOException
  {
    TypeAdapter<?> adapter = getAdapter(TypeToken.get(typeOfSrc));
    boolean oldLenient = writer.isLenient();
    writer.setLenient(true);
    boolean oldHtmlSafe = writer.isHtmlSafe();
    writer.setHtmlSafe(htmlSafe);
    boolean oldSerializeNulls = writer.getSerializeNulls();
    writer.setSerializeNulls(serializeNulls);
    try {
      adapter.write(writer, src);
    } catch (IOException e) {
      throw new JsonIOException(e);
    } finally {
      writer.setLenient(oldLenient);
      writer.setHtmlSafe(oldHtmlSafe);
      writer.setSerializeNulls(oldSerializeNulls);
    }
  }
  






  public String toJson(JsonElement jsonElement)
  {
    StringWriter writer = new StringWriter();
    toJson(jsonElement, writer);
    return writer.toString();
  }
  





  public void toJson(JsonElement jsonElement, Appendable writer)
    throws JsonIOException
  {
    try
    {
      JsonWriter jsonWriter = newJsonWriter(Streams.writerForAppendable(writer));
      toJson(jsonElement, jsonWriter);
    } catch (IOException e) {
      throw new JsonIOException(e);
    }
  }
  

  public JsonWriter newJsonWriter(Writer writer)
    throws IOException
  {
    if (generateNonExecutableJson) {
      writer.write(")]}'\n");
    }
    JsonWriter jsonWriter = new JsonWriter(writer);
    if (prettyPrinting) {
      jsonWriter.setIndent("  ");
    }
    jsonWriter.setSerializeNulls(serializeNulls);
    return jsonWriter;
  }
  


  public JsonReader newJsonReader(Reader reader)
  {
    JsonReader jsonReader = new JsonReader(reader);
    jsonReader.setLenient(lenient);
    return jsonReader;
  }
  


  public void toJson(JsonElement jsonElement, JsonWriter writer)
    throws JsonIOException
  {
    boolean oldLenient = writer.isLenient();
    writer.setLenient(true);
    boolean oldHtmlSafe = writer.isHtmlSafe();
    writer.setHtmlSafe(htmlSafe);
    boolean oldSerializeNulls = writer.getSerializeNulls();
    writer.setSerializeNulls(serializeNulls);
    try {
      Streams.write(jsonElement, writer);
    } catch (IOException e) {
      throw new JsonIOException(e);
    } finally {
      writer.setLenient(oldLenient);
      writer.setHtmlSafe(oldHtmlSafe);
      writer.setSerializeNulls(oldSerializeNulls);
    }
  }
  















  public <T> T fromJson(String json, Class<T> classOfT)
    throws JsonSyntaxException
  {
    Object object = fromJson(json, classOfT);
    return Primitives.wrap(classOfT).cast(object);
  }
  

















  public <T> T fromJson(String json, Type typeOfT)
    throws JsonSyntaxException
  {
    if (json == null) {
      return null;
    }
    StringReader reader = new StringReader(json);
    T target = fromJson(reader, typeOfT);
    return target;
  }
  
















  public <T> T fromJson(Reader json, Class<T> classOfT)
    throws JsonSyntaxException, JsonIOException
  {
    JsonReader jsonReader = newJsonReader(json);
    Object object = fromJson(jsonReader, classOfT);
    assertFullConsumption(object, jsonReader);
    return Primitives.wrap(classOfT).cast(object);
  }
  


















  public <T> T fromJson(Reader json, Type typeOfT)
    throws JsonIOException, JsonSyntaxException
  {
    JsonReader jsonReader = newJsonReader(json);
    T object = fromJson(jsonReader, typeOfT);
    assertFullConsumption(object, jsonReader);
    return object;
  }
  
  private static void assertFullConsumption(Object obj, JsonReader reader) {
    try {
      if ((obj != null) && (reader.peek() != JsonToken.END_DOCUMENT)) {
        throw new JsonIOException("JSON document was not fully consumed.");
      }
    } catch (MalformedJsonException e) {
      throw new JsonSyntaxException(e);
    } catch (IOException e) {
      throw new JsonIOException(e);
    }
  }
  







  public <T> T fromJson(JsonReader reader, Type typeOfT)
    throws JsonIOException, JsonSyntaxException
  {
    boolean isEmpty = true;
    boolean oldLenient = reader.isLenient();
    reader.setLenient(true);
    try {
      reader.peek();
      isEmpty = false;
      TypeToken<T> typeToken = TypeToken.get(typeOfT);
      typeAdapter = getAdapter(typeToken);
      T object = typeAdapter.read(reader);
      return object;
    }
    catch (EOFException e)
    {
      TypeAdapter<T> typeAdapter;
      
      if (isEmpty) {
        return null;
      }
      throw new JsonSyntaxException(e);
    } catch (IllegalStateException e) {
      throw new JsonSyntaxException(e);
    }
    catch (IOException e) {
      throw new JsonSyntaxException(e);
    } finally {
      reader.setLenient(oldLenient);
    }
  }
  














  public <T> T fromJson(JsonElement json, Class<T> classOfT)
    throws JsonSyntaxException
  {
    Object object = fromJson(json, classOfT);
    return Primitives.wrap(classOfT).cast(object);
  }
  

















  public <T> T fromJson(JsonElement json, Type typeOfT)
    throws JsonSyntaxException
  {
    if (json == null) {
      return null;
    }
    return fromJson(new JsonTreeReader(json), typeOfT);
  }
  
  static class FutureTypeAdapter<T> extends TypeAdapter<T> { private TypeAdapter<T> delegate;
    
    FutureTypeAdapter() {}
    
    public void setDelegate(TypeAdapter<T> typeAdapter) { if (delegate != null) {
        throw new AssertionError();
      }
      delegate = typeAdapter;
    }
    
    public T read(JsonReader in) throws IOException {
      if (delegate == null) {
        throw new IllegalStateException();
      }
      return delegate.read(in);
    }
    
    public void write(JsonWriter out, T value) throws IOException {
      if (delegate == null) {
        throw new IllegalStateException();
      }
      delegate.write(out, value);
    }
  }
  
  public String toString()
  {
    return 
    


      "{serializeNulls:" + serializeNulls + "factories:" + factories + ",instanceCreators:" + constructorConstructor + "}";
  }
}
