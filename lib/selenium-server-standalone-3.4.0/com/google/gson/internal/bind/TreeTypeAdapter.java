package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal..Gson.Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;




















public final class TreeTypeAdapter<T>
  extends TypeAdapter<T>
{
  private final JsonSerializer<T> serializer;
  private final JsonDeserializer<T> deserializer;
  private final Gson gson;
  private final TypeToken<T> typeToken;
  private final TypeAdapterFactory skipPast;
  private final TreeTypeAdapter<T>.GsonContextImpl context = new GsonContextImpl(null);
  
  private TypeAdapter<T> delegate;
  

  public TreeTypeAdapter(JsonSerializer<T> serializer, JsonDeserializer<T> deserializer, Gson gson, TypeToken<T> typeToken, TypeAdapterFactory skipPast)
  {
    this.serializer = serializer;
    this.deserializer = deserializer;
    this.gson = gson;
    this.typeToken = typeToken;
    this.skipPast = skipPast;
  }
  
  public T read(JsonReader in) throws IOException {
    if (deserializer == null) {
      return delegate().read(in);
    }
    JsonElement value = Streams.parse(in);
    if (value.isJsonNull()) {
      return null;
    }
    return deserializer.deserialize(value, typeToken.getType(), context);
  }
  
  public void write(JsonWriter out, T value) throws IOException {
    if (serializer == null) {
      delegate().write(out, value);
      return;
    }
    if (value == null) {
      out.nullValue();
      return;
    }
    JsonElement tree = serializer.serialize(value, typeToken.getType(), context);
    Streams.write(tree, out);
  }
  
  private TypeAdapter<T> delegate() {
    TypeAdapter<T> d = delegate;
    return this.delegate = gson
    
      .getDelegateAdapter(skipPast, typeToken);
  }
  


  public static TypeAdapterFactory newFactory(TypeToken<?> exactType, Object typeAdapter)
  {
    return new SingleTypeFactory(typeAdapter, exactType, false, null);
  }
  





  public static TypeAdapterFactory newFactoryWithMatchRawType(TypeToken<?> exactType, Object typeAdapter)
  {
    boolean matchRawType = exactType.getType() == exactType.getRawType();
    return new SingleTypeFactory(typeAdapter, exactType, matchRawType, null);
  }
  




  public static TypeAdapterFactory newTypeHierarchyFactory(Class<?> hierarchyType, Object typeAdapter)
  {
    return new SingleTypeFactory(typeAdapter, null, false, hierarchyType);
  }
  
  private static final class SingleTypeFactory implements TypeAdapterFactory
  {
    private final TypeToken<?> exactType;
    private final boolean matchRawType;
    private final Class<?> hierarchyType;
    private final JsonSerializer<?> serializer;
    private final JsonDeserializer<?> deserializer;
    
    SingleTypeFactory(Object typeAdapter, TypeToken<?> exactType, boolean matchRawType, Class<?> hierarchyType) {
      serializer = ((typeAdapter instanceof JsonSerializer) ? (JsonSerializer)typeAdapter : null);
      

      deserializer = ((typeAdapter instanceof JsonDeserializer) ? (JsonDeserializer)typeAdapter : null);
      

      .Gson.Preconditions.checkArgument((serializer != null) || (deserializer != null));
      this.exactType = exactType;
      this.matchRawType = matchRawType;
      this.hierarchyType = hierarchyType;
    }
    



    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
    {
      boolean matches = exactType != null ? false : (exactType.equals(type)) || ((matchRawType) && (exactType.getType() == type.getRawType())) ? true : hierarchyType.isAssignableFrom(type.getRawType());
      return matches ? new TreeTypeAdapter(serializer, deserializer, gson, type, this) : null;
    }
  }
  
  private final class GsonContextImpl implements JsonSerializationContext, JsonDeserializationContext
  {
    private GsonContextImpl() {}
    
    public JsonElement serialize(Object src) {
      return gson.toJsonTree(src);
    }
    
    public JsonElement serialize(Object src, Type typeOfSrc) { return gson.toJsonTree(src, typeOfSrc); }
    
    public <R> R deserialize(JsonElement json, Type typeOfT) throws JsonParseException
    {
      return gson.fromJson(json, typeOfT);
    }
  }
}
