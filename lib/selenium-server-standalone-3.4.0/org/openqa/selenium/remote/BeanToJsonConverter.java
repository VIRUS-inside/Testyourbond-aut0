package org.openqa.selenium.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogLevelMapping;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.SessionLogs;

























public class BeanToJsonConverter
{
  private static final int MAX_DEPTH = 5;
  
  public BeanToJsonConverter() {}
  
  public String convert(Object object)
  {
    if (object == null) {
      return null;
    }
    try
    {
      JsonElement json = convertObject(object);
      return new GsonBuilder().disableHtmlEscaping().serializeNulls().create().toJson(json);
    } catch (Exception e) {
      throw new WebDriverException("Unable to convert: " + object, e);
    }
  }
  






  public JsonElement convertObject(Object object)
  {
    if (object == null) {
      return JsonNull.INSTANCE;
    }
    try
    {
      return convertObject(object, 5);
    } catch (Exception e) {
      throw new WebDriverException("Unable to convert: " + object, e);
    }
  }
  
  private JsonElement convertObject(Object toConvert, int maxDepth) throws Exception
  {
    if (toConvert == null) {
      return JsonNull.INSTANCE;
    }
    
    if ((toConvert instanceof Boolean)) {
      return new JsonPrimitive((Boolean)toConvert);
    }
    
    if ((toConvert instanceof CharSequence)) {
      return new JsonPrimitive(String.valueOf(toConvert));
    }
    
    if ((toConvert instanceof Number)) {
      return new JsonPrimitive((Number)toConvert);
    }
    
    if ((toConvert instanceof Level)) {
      return new JsonPrimitive(LogLevelMapping.getName((Level)toConvert));
    }
    
    if ((toConvert.getClass().isEnum()) || ((toConvert instanceof Enum))) {
      return new JsonPrimitive(toConvert.toString());
    }
    
    if ((toConvert instanceof LoggingPreferences)) {
      LoggingPreferences prefs = (LoggingPreferences)toConvert;
      JsonObject converted = new JsonObject();
      for (String logType : prefs.getEnabledLogTypes()) {
        converted.addProperty(logType, LogLevelMapping.getName(prefs.getLevel(logType)));
      }
      return converted;
    }
    
    if ((toConvert instanceof SessionLogs)) {
      return convertObject(((SessionLogs)toConvert).getAll(), maxDepth - 1);
    }
    
    if ((toConvert instanceof LogEntries)) {
      return convertObject(((LogEntries)toConvert).getAll(), maxDepth - 1);
    }
    JsonObject converted;
    if ((toConvert instanceof Map)) {
      Map<String, Object> map = (Map)toConvert;
      if ((map.size() == 1) && (map.containsKey("w3c cookie"))) {
        return convertObject(map.get("w3c cookie"));
      }
      
      converted = new JsonObject();
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        converted.add((String)entry.getKey(), convertObject(entry.getValue(), maxDepth - 1));
      }
      return converted;
    }
    
    if ((toConvert instanceof JsonElement)) {
      return (JsonElement)toConvert;
    }
    
    if ((toConvert instanceof Collection)) {
      JsonArray array = new JsonArray();
      for (Object o : (Collection)toConvert) {
        array.add(convertObject(o, maxDepth - 1));
      }
      return array;
    }
    
    if (toConvert.getClass().isArray()) {
      JsonArray converted = new JsonArray();
      int length = Array.getLength(toConvert);
      for (int i = 0; i < length; i++) {
        converted.add(convertObject(Array.get(toConvert, i), maxDepth - 1));
      }
      return converted;
    }
    
    if ((toConvert instanceof SessionId)) {
      JsonObject converted = new JsonObject();
      converted.addProperty("value", toConvert.toString());
      return converted;
    }
    
    if ((toConvert instanceof Date)) {
      return new JsonPrimitive(Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(((Date)toConvert).getTime())));
    }
    
    if ((toConvert instanceof File)) {
      return new JsonPrimitive(((File)toConvert).getAbsolutePath());
    }
    
    Method toMap = getMethod(toConvert, "toMap");
    if (toMap == null) {
      toMap = getMethod(toConvert, "asMap");
    }
    if (toMap != null) {
      try {
        return convertObject(toMap.invoke(toConvert, new Object[0]), maxDepth - 1);
      } catch (ReflectiveOperationException e) {
        throw new WebDriverException(e);
      }
    }
    
    Method toJson = getMethod(toConvert, "toJson");
    if (toJson != null) {
      try {
        Object res = toJson.invoke(toConvert, new Object[0]);
        if ((res instanceof JsonElement)) {
          return (JsonElement)res;
        }
        
        if ((res instanceof Map))
          return convertObject(res);
        if ((res instanceof Collection))
          return convertObject(res);
        if ((res instanceof String)) {
          try {
            return new JsonParser().parse((String)res);
          } catch (JsonParseException e) {
            return new JsonPrimitive((String)res);
          }
        }
      } catch (ReflectiveOperationException e) {
        throw new WebDriverException(e);
      }
    }
    try
    {
      return mapObject(toConvert, maxDepth - 1, toConvert instanceof Cookie);
    } catch (Exception e) {
      throw new WebDriverException(e);
    }
  }
  
  private Method getMethod(Object toConvert, String methodName) {
    try {
      return toConvert.getClass().getMethod(methodName, new Class[0]);
    }
    catch (NoSuchMethodException|SecurityException localNoSuchMethodException) {}
    

    return null;
  }
  
  private JsonElement mapObject(Object toConvert, int maxDepth, boolean skipNulls) throws Exception
  {
    if (maxDepth < 1) {
      return JsonNull.INSTANCE;
    }
    

    JsonObject mapped = new JsonObject();
    for (SimplePropertyDescriptor pd : SimplePropertyDescriptor.getPropertyDescriptors(toConvert.getClass())) {
      if ("class".equals(pd.getName())) {
        mapped.addProperty("class", toConvert.getClass().getName());
      }
      else
      {
        Method readMethod = pd.getReadMethod();
        if (readMethod != null)
        {


          if (readMethod.getParameterTypes().length <= 0)
          {


            readMethod.setAccessible(true);
            
            Object result = readMethod.invoke(toConvert, new Object[0]);
            if ((!skipNulls) || (result != null))
              mapped.add(pd.getName(), convertObject(result, maxDepth - 1));
          } }
      }
    }
    return mapped;
  }
}
