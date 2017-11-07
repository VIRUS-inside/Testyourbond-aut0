package com.google.gson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
























public final class JsonArray
  extends JsonElement
  implements Iterable<JsonElement>
{
  private final List<JsonElement> elements;
  
  public JsonArray()
  {
    elements = new ArrayList();
  }
  
  JsonArray deepCopy()
  {
    JsonArray result = new JsonArray();
    for (JsonElement element : elements) {
      result.add(element.deepCopy());
    }
    return result;
  }
  




  public void add(Boolean bool)
  {
    elements.add(bool == null ? JsonNull.INSTANCE : new JsonPrimitive(bool));
  }
  




  public void add(Character character)
  {
    elements.add(character == null ? JsonNull.INSTANCE : new JsonPrimitive(character));
  }
  




  public void add(Number number)
  {
    elements.add(number == null ? JsonNull.INSTANCE : new JsonPrimitive(number));
  }
  




  public void add(String string)
  {
    elements.add(string == null ? JsonNull.INSTANCE : new JsonPrimitive(string));
  }
  




  public void add(JsonElement element)
  {
    if (element == null) {
      element = JsonNull.INSTANCE;
    }
    elements.add(element);
  }
  




  public void addAll(JsonArray array)
  {
    elements.addAll(elements);
  }
  







  public JsonElement set(int index, JsonElement element)
  {
    return (JsonElement)elements.set(index, element);
  }
  






  public boolean remove(JsonElement element)
  {
    return elements.remove(element);
  }
  








  public JsonElement remove(int index)
  {
    return (JsonElement)elements.remove(index);
  }
  





  public boolean contains(JsonElement element)
  {
    return elements.contains(element);
  }
  




  public int size()
  {
    return elements.size();
  }
  





  public Iterator<JsonElement> iterator()
  {
    return elements.iterator();
  }
  







  public JsonElement get(int i)
  {
    return (JsonElement)elements.get(i);
  }
  








  public Number getAsNumber()
  {
    if (elements.size() == 1) {
      return ((JsonElement)elements.get(0)).getAsNumber();
    }
    throw new IllegalStateException();
  }
  








  public String getAsString()
  {
    if (elements.size() == 1) {
      return ((JsonElement)elements.get(0)).getAsString();
    }
    throw new IllegalStateException();
  }
  








  public double getAsDouble()
  {
    if (elements.size() == 1) {
      return ((JsonElement)elements.get(0)).getAsDouble();
    }
    throw new IllegalStateException();
  }
  









  public BigDecimal getAsBigDecimal()
  {
    if (elements.size() == 1) {
      return ((JsonElement)elements.get(0)).getAsBigDecimal();
    }
    throw new IllegalStateException();
  }
  









  public BigInteger getAsBigInteger()
  {
    if (elements.size() == 1) {
      return ((JsonElement)elements.get(0)).getAsBigInteger();
    }
    throw new IllegalStateException();
  }
  








  public float getAsFloat()
  {
    if (elements.size() == 1) {
      return ((JsonElement)elements.get(0)).getAsFloat();
    }
    throw new IllegalStateException();
  }
  








  public long getAsLong()
  {
    if (elements.size() == 1) {
      return ((JsonElement)elements.get(0)).getAsLong();
    }
    throw new IllegalStateException();
  }
  








  public int getAsInt()
  {
    if (elements.size() == 1) {
      return ((JsonElement)elements.get(0)).getAsInt();
    }
    throw new IllegalStateException();
  }
  
  public byte getAsByte()
  {
    if (elements.size() == 1) {
      return ((JsonElement)elements.get(0)).getAsByte();
    }
    throw new IllegalStateException();
  }
  
  public char getAsCharacter()
  {
    if (elements.size() == 1) {
      return ((JsonElement)elements.get(0)).getAsCharacter();
    }
    throw new IllegalStateException();
  }
  








  public short getAsShort()
  {
    if (elements.size() == 1) {
      return ((JsonElement)elements.get(0)).getAsShort();
    }
    throw new IllegalStateException();
  }
  








  public boolean getAsBoolean()
  {
    if (elements.size() == 1) {
      return ((JsonElement)elements.get(0)).getAsBoolean();
    }
    throw new IllegalStateException();
  }
  
  public boolean equals(Object o)
  {
    return (o == this) || (((o instanceof JsonArray)) && (elements.equals(elements)));
  }
  
  public int hashCode()
  {
    return elements.hashCode();
  }
}
