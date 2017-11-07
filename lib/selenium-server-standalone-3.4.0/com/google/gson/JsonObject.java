package com.google.gson;

import com.google.gson.internal.LinkedTreeMap;
import java.util.Map.Entry;
import java.util.Set;
























public final class JsonObject
  extends JsonElement
{
  private final LinkedTreeMap<String, JsonElement> members = new LinkedTreeMap();
  
  public JsonObject() {}
  
  JsonObject deepCopy() {
    JsonObject result = new JsonObject();
    for (Map.Entry<String, JsonElement> entry : members.entrySet()) {
      result.add((String)entry.getKey(), ((JsonElement)entry.getValue()).deepCopy());
    }
    return result;
  }
  







  public void add(String property, JsonElement value)
  {
    if (value == null) {
      value = JsonNull.INSTANCE;
    }
    members.put(property, value);
  }
  






  public JsonElement remove(String property)
  {
    return (JsonElement)members.remove(property);
  }
  






  public void addProperty(String property, String value)
  {
    add(property, createJsonElement(value));
  }
  






  public void addProperty(String property, Number value)
  {
    add(property, createJsonElement(value));
  }
  






  public void addProperty(String property, Boolean value)
  {
    add(property, createJsonElement(value));
  }
  






  public void addProperty(String property, Character value)
  {
    add(property, createJsonElement(value));
  }
  





  private JsonElement createJsonElement(Object value)
  {
    return value == null ? JsonNull.INSTANCE : new JsonPrimitive(value);
  }
  





  public Set<Map.Entry<String, JsonElement>> entrySet()
  {
    return members.entrySet();
  }
  




  public int size()
  {
    return members.size();
  }
  





  public boolean has(String memberName)
  {
    return members.containsKey(memberName);
  }
  





  public JsonElement get(String memberName)
  {
    return (JsonElement)members.get(memberName);
  }
  





  public JsonPrimitive getAsJsonPrimitive(String memberName)
  {
    return (JsonPrimitive)members.get(memberName);
  }
  





  public JsonArray getAsJsonArray(String memberName)
  {
    return (JsonArray)members.get(memberName);
  }
  





  public JsonObject getAsJsonObject(String memberName)
  {
    return (JsonObject)members.get(memberName);
  }
  
  public boolean equals(Object o)
  {
    return (o == this) || (((o instanceof JsonObject)) && 
      (members.equals(members)));
  }
  
  public int hashCode()
  {
    return members.hashCode();
  }
}
