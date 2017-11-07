package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;














final class TypeAdapterRuntimeTypeWrapper<T>
  extends TypeAdapter<T>
{
  private final Gson context;
  private final TypeAdapter<T> delegate;
  private final Type type;
  
  TypeAdapterRuntimeTypeWrapper(Gson context, TypeAdapter<T> delegate, Type type)
  {
    this.context = context;
    this.delegate = delegate;
    this.type = type;
  }
  
  public T read(JsonReader in) throws IOException
  {
    return delegate.read(in);
  }
  






  public void write(JsonWriter out, T value)
    throws IOException
  {
    TypeAdapter chosen = delegate;
    Type runtimeType = getRuntimeTypeIfMoreSpecific(type, value);
    if (runtimeType != type) {
      TypeAdapter runtimeTypeAdapter = context.getAdapter(TypeToken.get(runtimeType));
      if (!(runtimeTypeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter))
      {
        chosen = runtimeTypeAdapter;
      } else if (!(delegate instanceof ReflectiveTypeAdapterFactory.Adapter))
      {

        chosen = delegate;
      }
      else {
        chosen = runtimeTypeAdapter;
      }
    }
    chosen.write(out, value);
  }
  


  private Type getRuntimeTypeIfMoreSpecific(Type type, Object value)
  {
    if ((value != null) && ((type == Object.class) || ((type instanceof TypeVariable)) || ((type instanceof Class))))
    {
      type = value.getClass();
    }
    return type;
  }
}
