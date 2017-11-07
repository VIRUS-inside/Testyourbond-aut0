package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal..Gson.Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



















public final class ReflectiveTypeAdapterFactory
  implements TypeAdapterFactory
{
  private final ConstructorConstructor constructorConstructor;
  private final FieldNamingStrategy fieldNamingPolicy;
  private final Excluder excluder;
  private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  
  public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingPolicy, Excluder excluder, JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory)
  {
    this.constructorConstructor = constructorConstructor;
    this.fieldNamingPolicy = fieldNamingPolicy;
    this.excluder = excluder;
    this.jsonAdapterFactory = jsonAdapterFactory;
  }
  
  public boolean excludeField(Field f, boolean serialize) {
    return excludeField(f, serialize, excluder);
  }
  
  static boolean excludeField(Field f, boolean serialize, Excluder excluder) {
    return (!excluder.excludeClass(f.getType(), serialize)) && (!excluder.excludeField(f, serialize));
  }
  
  private List<String> getFieldNames(Field f)
  {
    SerializedName annotation = (SerializedName)f.getAnnotation(SerializedName.class);
    if (annotation == null) {
      String name = fieldNamingPolicy.translateName(f);
      return Collections.singletonList(name);
    }
    
    String serializedName = annotation.value();
    String[] alternates = annotation.alternate();
    if (alternates.length == 0) {
      return Collections.singletonList(serializedName);
    }
    
    List<String> fieldNames = new ArrayList(alternates.length + 1);
    fieldNames.add(serializedName);
    for (String alternate : alternates) {
      fieldNames.add(alternate);
    }
    return fieldNames;
  }
  
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    Class<? super T> raw = type.getRawType();
    
    if (!Object.class.isAssignableFrom(raw)) {
      return null;
    }
    
    ObjectConstructor<T> constructor = constructorConstructor.get(type);
    return new Adapter(constructor, getBoundFields(gson, type, raw));
  }
  

  private BoundField createBoundField(final Gson context, final Field field, String name, final TypeToken<?> fieldType, boolean serialize, boolean deserialize)
  {
    final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
    
    JsonAdapter annotation = (JsonAdapter)field.getAnnotation(JsonAdapter.class);
    TypeAdapter<?> mapped = null;
    if (annotation != null) {
      mapped = jsonAdapterFactory.getTypeAdapter(constructorConstructor, context, fieldType, annotation);
    }
    
    final boolean jsonAdapterPresent = mapped != null;
    if (mapped == null) { mapped = context.getAdapter(fieldType);
    }
    final TypeAdapter<?> typeAdapter = mapped;
    new BoundField(name, serialize, deserialize)
    {
      void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException
      {
        Object fieldValue = field.get(value);
        
        TypeAdapter t = jsonAdapterPresent ? typeAdapter : new TypeAdapterRuntimeTypeWrapper(context, typeAdapter, fieldType.getType());
        t.write(writer, fieldValue);
      }
      
      void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
        Object fieldValue = typeAdapter.read(reader);
        if ((fieldValue != null) || (!isPrimitive))
          field.set(value, fieldValue);
      }
      
      public boolean writeField(Object value) throws IOException, IllegalAccessException {
        if (!serialized) return false;
        Object fieldValue = field.get(value);
        return fieldValue != value;
      }
    };
  }
  
  private Map<String, BoundField> getBoundFields(Gson context, TypeToken<?> type, Class<?> raw) {
    Map<String, BoundField> result = new LinkedHashMap();
    if (raw.isInterface()) {
      return result;
    }
    
    Type declaredType = type.getType();
    while (raw != Object.class) {
      Field[] fields = raw.getDeclaredFields();
      for (Field field : fields) {
        boolean serialize = excludeField(field, true);
        boolean deserialize = excludeField(field, false);
        if ((serialize) || (deserialize))
        {

          field.setAccessible(true);
          Type fieldType = .Gson.Types.resolve(type.getType(), raw, field.getGenericType());
          List<String> fieldNames = getFieldNames(field);
          BoundField previous = null;
          for (int i = 0; i < fieldNames.size(); i++) {
            String name = (String)fieldNames.get(i);
            if (i != 0) serialize = false;
            BoundField boundField = createBoundField(context, field, name, 
              TypeToken.get(fieldType), serialize, deserialize);
            BoundField replaced = (BoundField)result.put(name, boundField);
            if (previous == null) previous = replaced;
          }
          if (previous != null) {
            throw new IllegalArgumentException(declaredType + " declares multiple JSON fields named " + name);
          }
        }
      }
      type = TypeToken.get(.Gson.Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
      raw = type.getRawType();
    }
    return result;
  }
  
  static abstract class BoundField {
    final String name;
    final boolean serialized;
    final boolean deserialized;
    
    protected BoundField(String name, boolean serialized, boolean deserialized) {
      this.name = name;
      this.serialized = serialized;
      this.deserialized = deserialized;
    }
    
    abstract boolean writeField(Object paramObject) throws IOException, IllegalAccessException;
    
    abstract void write(JsonWriter paramJsonWriter, Object paramObject) throws IOException, IllegalAccessException;
    
    abstract void read(JsonReader paramJsonReader, Object paramObject) throws IOException, IllegalAccessException; }
  
  public static final class Adapter<T> extends TypeAdapter<T> { private final ObjectConstructor<T> constructor;
    private final Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields;
    
    Adapter(ObjectConstructor<T> constructor, Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields) { this.constructor = constructor;
      this.boundFields = boundFields;
    }
    
    public T read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      
      T instance = constructor.construct();
      try
      {
        in.beginObject();
        while (in.hasNext()) {
          String name = in.nextName();
          ReflectiveTypeAdapterFactory.BoundField field = (ReflectiveTypeAdapterFactory.BoundField)boundFields.get(name);
          if ((field == null) || (!deserialized)) {
            in.skipValue();
          } else {
            field.read(in, instance);
          }
        }
      } catch (IllegalStateException e) {
        throw new JsonSyntaxException(e);
      } catch (IllegalAccessException e) {
        throw new AssertionError(e);
      }
      in.endObject();
      return instance;
    }
    
    public void write(JsonWriter out, T value) throws IOException {
      if (value == null) {
        out.nullValue();
        return;
      }
      
      out.beginObject();
      try {
        for (ReflectiveTypeAdapterFactory.BoundField boundField : boundFields.values()) {
          if (boundField.writeField(value)) {
            out.name(name);
            boundField.write(out, value);
          }
        }
      } catch (IllegalAccessException e) {
        throw new AssertionError(e);
      }
      out.endObject();
    }
  }
}
