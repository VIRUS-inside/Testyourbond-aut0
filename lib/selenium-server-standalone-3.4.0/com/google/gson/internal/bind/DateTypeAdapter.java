package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;





















public final class DateTypeAdapter
  extends TypeAdapter<Date>
{
  public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory()
  {
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      return typeToken.getRawType() == Date.class ? new DateTypeAdapter() : null;
    }
  };
  private final DateFormat enUsFormat;
  
  public DateTypeAdapter() { enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US); }
  
  private final DateFormat localFormat = DateFormat.getDateTimeInstance(2, 2);
  
  public Date read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    return deserializeToDate(in.nextString());
  }
  
  private synchronized Date deserializeToDate(String json) {
    try {
      return localFormat.parse(json);
    }
    catch (ParseException localParseException1) {
      try {
        return enUsFormat.parse(json);
      }
      catch (ParseException localParseException2) {
        try {
          return ISO8601Utils.parse(json, new ParsePosition(0));
        } catch (ParseException e) {
          throw new JsonSyntaxException(json, e);
        }
      }
    } }
  
  public synchronized void write(JsonWriter out, Date value) throws IOException { if (value == null) {
      out.nullValue();
      return;
    }
    String dateFormatAsString = enUsFormat.format(value);
    out.value(dateFormatAsString);
  }
}
