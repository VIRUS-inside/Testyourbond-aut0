package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal..Gson.Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;















































































public final class MapTypeAdapterFactory
  implements TypeAdapterFactory
{
  private final ConstructorConstructor constructorConstructor;
  final boolean complexMapKeySerialization;
  
  public MapTypeAdapterFactory(ConstructorConstructor constructorConstructor, boolean complexMapKeySerialization)
  {
    this.constructorConstructor = constructorConstructor;
    this.complexMapKeySerialization = complexMapKeySerialization;
  }
  
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    Type type = typeToken.getType();
    
    Class<? super T> rawType = typeToken.getRawType();
    if (!Map.class.isAssignableFrom(rawType)) {
      return null;
    }
    
    Class<?> rawTypeOfSrc = .Gson.Types.getRawType(type);
    Type[] keyAndValueTypes = .Gson.Types.getMapKeyAndValueTypes(type, rawTypeOfSrc);
    TypeAdapter<?> keyAdapter = getKeyAdapter(gson, keyAndValueTypes[0]);
    TypeAdapter<?> valueAdapter = gson.getAdapter(TypeToken.get(keyAndValueTypes[1]));
    ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);
    


    TypeAdapter<T> result = new Adapter(gson, keyAndValueTypes[0], keyAdapter, keyAndValueTypes[1], valueAdapter, constructor);
    
    return result;
  }
  


  private TypeAdapter<?> getKeyAdapter(Gson context, Type keyType)
  {
    return (keyType == Boolean.TYPE) || (keyType == Boolean.class) ? TypeAdapters.BOOLEAN_AS_STRING : context
    
      .getAdapter(TypeToken.get(keyType));
  }
  
  private final class Adapter<K, V> extends TypeAdapter<Map<K, V>>
  {
    private final TypeAdapter<K> keyTypeAdapter;
    private final TypeAdapter<V> valueTypeAdapter;
    private final ObjectConstructor<? extends Map<K, V>> constructor;
    
    public Adapter(Type context, TypeAdapter<K> keyType, Type keyTypeAdapter, TypeAdapter<V> valueType, ObjectConstructor<? extends Map<K, V>> valueTypeAdapter)
    {
      this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper(context, keyTypeAdapter, keyType);
      
      this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper(context, valueTypeAdapter, valueType);
      
      this.constructor = constructor;
    }
    
    public Map<K, V> read(JsonReader in) throws IOException {
      JsonToken peek = in.peek();
      if (peek == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      
      Map<K, V> map = (Map)constructor.construct();
      
      if (peek == JsonToken.BEGIN_ARRAY) {
        in.beginArray();
        while (in.hasNext()) {
          in.beginArray();
          K key = keyTypeAdapter.read(in);
          V value = valueTypeAdapter.read(in);
          V replaced = map.put(key, value);
          if (replaced != null) {
            throw new JsonSyntaxException("duplicate key: " + key);
          }
          in.endArray();
        }
        in.endArray();
      } else {
        in.beginObject();
        while (in.hasNext()) {
          JsonReaderInternalAccess.INSTANCE.promoteNameToValue(in);
          K key = keyTypeAdapter.read(in);
          V value = valueTypeAdapter.read(in);
          V replaced = map.put(key, value);
          if (replaced != null) {
            throw new JsonSyntaxException("duplicate key: " + key);
          }
        }
        in.endObject();
      }
      return map;
    }
    
    public void write(JsonWriter out, Map<K, V> map) throws IOException {
      if (map == null) {
        out.nullValue();
        return;
      }
      
      if (!complexMapKeySerialization) {
        out.beginObject();
        for (Map.Entry<K, V> entry : map.entrySet()) {
          out.name(String.valueOf(entry.getKey()));
          valueTypeAdapter.write(out, entry.getValue());
        }
        out.endObject();
        return;
      }
      
      boolean hasComplexKeys = false;
      List<JsonElement> keys = new ArrayList(map.size());
      
      List<V> values = new ArrayList(map.size());
      for (Map.Entry<K, V> entry : map.entrySet()) {
        JsonElement keyElement = keyTypeAdapter.toJsonTree(entry.getKey());
        keys.add(keyElement);
        values.add(entry.getValue());
        hasComplexKeys |= ((keyElement.isJsonArray()) || (keyElement.isJsonObject()));
      }
      
      if (hasComplexKeys) {
        out.beginArray();
        for (int i = 0; i < keys.size(); i++) {
          out.beginArray();
          Streams.write((JsonElement)keys.get(i), out);
          valueTypeAdapter.write(out, values.get(i));
          out.endArray();
        }
        out.endArray();
      } else {
        out.beginObject();
        for (int i = 0; i < keys.size(); i++) {
          JsonElement keyElement = (JsonElement)keys.get(i);
          out.name(keyToString(keyElement));
          valueTypeAdapter.write(out, values.get(i));
        }
        out.endObject();
      }
    }
    
    private String keyToString(JsonElement keyElement) {
      if (keyElement.isJsonPrimitive()) {
        JsonPrimitive primitive = keyElement.getAsJsonPrimitive();
        if (primitive.isNumber())
          return String.valueOf(primitive.getAsNumber());
        if (primitive.isBoolean())
          return Boolean.toString(primitive.getAsBoolean());
        if (primitive.isString()) {
          return primitive.getAsString();
        }
        throw new AssertionError();
      }
      if (keyElement.isJsonNull()) {
        return "null";
      }
      throw new AssertionError();
    }
  }
}
