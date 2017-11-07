package com.google.gson;

import com.google.gson.internal.bind.util.ISO8601Utils;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;


























final class DefaultDateTypeAdapter
  implements JsonSerializer<java.util.Date>, JsonDeserializer<java.util.Date>
{
  private final DateFormat enUsFormat;
  private final DateFormat localFormat;
  
  DefaultDateTypeAdapter()
  {
    this(DateFormat.getDateTimeInstance(2, 2, Locale.US), 
      DateFormat.getDateTimeInstance(2, 2));
  }
  
  DefaultDateTypeAdapter(String datePattern) {
    this(new SimpleDateFormat(datePattern, Locale.US), new SimpleDateFormat(datePattern));
  }
  
  DefaultDateTypeAdapter(int style) {
    this(DateFormat.getDateInstance(style, Locale.US), DateFormat.getDateInstance(style));
  }
  
  public DefaultDateTypeAdapter(int dateStyle, int timeStyle) {
    this(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US), 
      DateFormat.getDateTimeInstance(dateStyle, timeStyle));
  }
  
  DefaultDateTypeAdapter(DateFormat enUsFormat, DateFormat localFormat) {
    this.enUsFormat = enUsFormat;
    this.localFormat = localFormat;
  }
  


  public JsonElement serialize(java.util.Date src, Type typeOfSrc, JsonSerializationContext context)
  {
    synchronized (localFormat) {
      String dateFormatAsString = enUsFormat.format(src);
      return new JsonPrimitive(dateFormatAsString);
    }
  }
  
  public java.util.Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    throws JsonParseException
  {
    if (!(json instanceof JsonPrimitive)) {
      throw new JsonParseException("The date should be a string value");
    }
    java.util.Date date = deserializeToDate(json);
    if (typeOfT == java.util.Date.class)
      return date;
    if (typeOfT == Timestamp.class)
      return new Timestamp(date.getTime());
    if (typeOfT == java.sql.Date.class) {
      return new java.sql.Date(date.getTime());
    }
    throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
  }
  
  private java.util.Date deserializeToDate(JsonElement json)
  {
    synchronized (localFormat) {
      try {
        return localFormat.parse(json.getAsString());
      } catch (ParseException localParseException1) {
        try {
          return enUsFormat.parse(json.getAsString());
        } catch (ParseException localParseException2) {
          try {
            return ISO8601Utils.parse(json.getAsString(), new ParsePosition(0));
          } catch (ParseException e) {
            throw new JsonSyntaxException(json.getAsString(), e);
          }
        }
      }
    }
  }
  
  public String toString() { StringBuilder sb = new StringBuilder();
    sb.append(DefaultDateTypeAdapter.class.getSimpleName());
    sb.append('(').append(localFormat.getClass().getSimpleName()).append(')');
    return sb.toString();
  }
}
