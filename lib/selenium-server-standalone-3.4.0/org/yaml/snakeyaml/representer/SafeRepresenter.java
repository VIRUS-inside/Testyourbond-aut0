package org.yaml.snakeyaml.representer;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.reader.StreamReader;


















class SafeRepresenter
  extends BaseRepresenter
{
  protected Map<Class<? extends Object>, Tag> classTags;
  protected TimeZone timeZone = null;
  
  public SafeRepresenter() {
    nullRepresenter = new RepresentNull();
    representers.put(String.class, new RepresentString());
    representers.put(Boolean.class, new RepresentBoolean());
    representers.put(Character.class, new RepresentString());
    representers.put([B.class, new RepresentByteArray());
    
    Represent primitiveArray = new RepresentPrimitiveArray();
    representers.put([S.class, primitiveArray);
    representers.put([I.class, primitiveArray);
    representers.put([J.class, primitiveArray);
    representers.put([F.class, primitiveArray);
    representers.put([D.class, primitiveArray);
    representers.put([C.class, primitiveArray);
    representers.put([Z.class, primitiveArray);
    
    multiRepresenters.put(Number.class, new RepresentNumber());
    multiRepresenters.put(List.class, new RepresentList());
    multiRepresenters.put(Map.class, new RepresentMap());
    multiRepresenters.put(Set.class, new RepresentSet());
    multiRepresenters.put(Iterator.class, new RepresentIterator());
    multiRepresenters.put(new Object[0].getClass(), new RepresentArray());
    multiRepresenters.put(Date.class, new RepresentDate());
    multiRepresenters.put(Enum.class, new RepresentEnum());
    multiRepresenters.put(Calendar.class, new RepresentDate());
    classTags = new HashMap();
  }
  
  protected Tag getTag(Class<?> clazz, Tag defaultTag) {
    if (classTags.containsKey(clazz)) {
      return (Tag)classTags.get(clazz);
    }
    return defaultTag;
  }
  










  public Tag addClassTag(Class<? extends Object> clazz, Tag tag)
  {
    if (tag == null) {
      throw new NullPointerException("Tag must be provided.");
    }
    return (Tag)classTags.put(clazz, tag);
  }
  
  protected class RepresentNull implements Represent { protected RepresentNull() {}
    
    public Node representData(Object data) { return representScalar(Tag.NULL, "null"); }
  }
  

  public static Pattern MULTILINE_PATTERN = Pattern.compile("\n|| | ");
  
  protected class RepresentString implements Represent { protected RepresentString() {}
    
    public Node representData(Object data) { Tag tag = Tag.STR;
      Character style = null;
      String value = data.toString();
      if (StreamReader.NON_PRINTABLE.matcher(value).find()) {
        tag = Tag.BINARY;
        char[] binary;
        try {
          binary = Base64Coder.encode(value.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
          throw new YAMLException(e);
        }
        value = String.valueOf(binary);
        style = Character.valueOf('|');
      }
      

      if ((defaultScalarStyle == null) && (SafeRepresenter.MULTILINE_PATTERN.matcher(value).find())) {
        style = Character.valueOf('|');
      }
      return representScalar(tag, value, style);
    } }
  
  protected class RepresentBoolean implements Represent { protected RepresentBoolean() {}
    
    public Node representData(Object data) { String value;
      String value;
      if (Boolean.TRUE.equals(data)) {
        value = "true";
      } else {
        value = "false";
      }
      return representScalar(Tag.BOOL, value);
    } }
  
  protected class RepresentNumber implements Represent { protected RepresentNumber() {}
    
    public Node representData(Object data) { String value;
      Tag tag;
      String value;
      if (((data instanceof Byte)) || ((data instanceof Short)) || ((data instanceof Integer)) || ((data instanceof Long)) || ((data instanceof BigInteger)))
      {
        Tag tag = Tag.INT;
        value = data.toString();
      } else {
        Number number = (Number)data;
        tag = Tag.FLOAT;
        String value; if (number.equals(Double.valueOf(NaN.0D))) {
          value = ".NaN"; } else { String value;
          if (number.equals(Double.valueOf(Double.POSITIVE_INFINITY))) {
            value = ".inf"; } else { String value;
            if (number.equals(Double.valueOf(Double.NEGATIVE_INFINITY))) {
              value = "-.inf";
            } else
              value = number.toString();
          }
        } }
      return representScalar(getTag(data.getClass(), tag), value);
    }
  }
  
  protected class RepresentList implements Represent {
    protected RepresentList() {}
    
    public Node representData(Object data) { return representSequence(getTag(data.getClass(), Tag.SEQ), (List)data, null); }
  }
  
  protected class RepresentIterator implements Represent {
    protected RepresentIterator() {}
    
    public Node representData(Object data) {
      Iterator<Object> iter = (Iterator)data;
      return representSequence(getTag(data.getClass(), Tag.SEQ), new SafeRepresenter.IteratorWrapper(iter), null);
    }
  }
  
  private static class IteratorWrapper implements Iterable<Object>
  {
    private Iterator<Object> iter;
    
    public IteratorWrapper(Iterator<Object> iter) {
      this.iter = iter;
    }
    

    public Iterator<Object> iterator() { return iter; }
  }
  
  protected class RepresentArray implements Represent {
    protected RepresentArray() {}
    
    public Node representData(Object data) { Object[] array = (Object[])data;
      List<Object> list = Arrays.asList(array);
      return representSequence(Tag.SEQ, list, null);
    }
  }
  
  protected class RepresentPrimitiveArray
    implements Represent
  {
    protected RepresentPrimitiveArray() {}
    
    public Node representData(Object data)
    {
      Class<?> type = data.getClass().getComponentType();
      
      if (Byte.TYPE == type)
        return representSequence(Tag.SEQ, asByteList(data), null);
      if (Short.TYPE == type)
        return representSequence(Tag.SEQ, asShortList(data), null);
      if (Integer.TYPE == type)
        return representSequence(Tag.SEQ, asIntList(data), null);
      if (Long.TYPE == type)
        return representSequence(Tag.SEQ, asLongList(data), null);
      if (Float.TYPE == type)
        return representSequence(Tag.SEQ, asFloatList(data), null);
      if (Double.TYPE == type)
        return representSequence(Tag.SEQ, asDoubleList(data), null);
      if (Character.TYPE == type)
        return representSequence(Tag.SEQ, asCharList(data), null);
      if (Boolean.TYPE == type) {
        return representSequence(Tag.SEQ, asBooleanList(data), null);
      }
      
      throw new YAMLException("Unexpected primitive '" + type.getCanonicalName() + "'");
    }
    
    private List<Byte> asByteList(Object in) {
      byte[] array = (byte[])in;
      List<Byte> list = new ArrayList(array.length);
      for (int i = 0; i < array.length; i++)
        list.add(Byte.valueOf(array[i]));
      return list;
    }
    
    private List<Short> asShortList(Object in) {
      short[] array = (short[])in;
      List<Short> list = new ArrayList(array.length);
      for (int i = 0; i < array.length; i++)
        list.add(Short.valueOf(array[i]));
      return list;
    }
    
    private List<Integer> asIntList(Object in) {
      int[] array = (int[])in;
      List<Integer> list = new ArrayList(array.length);
      for (int i = 0; i < array.length; i++)
        list.add(Integer.valueOf(array[i]));
      return list;
    }
    
    private List<Long> asLongList(Object in) {
      long[] array = (long[])in;
      List<Long> list = new ArrayList(array.length);
      for (int i = 0; i < array.length; i++)
        list.add(Long.valueOf(array[i]));
      return list;
    }
    
    private List<Float> asFloatList(Object in) {
      float[] array = (float[])in;
      List<Float> list = new ArrayList(array.length);
      for (int i = 0; i < array.length; i++)
        list.add(Float.valueOf(array[i]));
      return list;
    }
    
    private List<Double> asDoubleList(Object in) {
      double[] array = (double[])in;
      List<Double> list = new ArrayList(array.length);
      for (int i = 0; i < array.length; i++)
        list.add(Double.valueOf(array[i]));
      return list;
    }
    
    private List<Character> asCharList(Object in) {
      char[] array = (char[])in;
      List<Character> list = new ArrayList(array.length);
      for (int i = 0; i < array.length; i++)
        list.add(Character.valueOf(array[i]));
      return list;
    }
    
    private List<Boolean> asBooleanList(Object in) {
      boolean[] array = (boolean[])in;
      List<Boolean> list = new ArrayList(array.length);
      for (int i = 0; i < array.length; i++)
        list.add(Boolean.valueOf(array[i]));
      return list;
    }
  }
  
  protected class RepresentMap implements Represent {
    protected RepresentMap() {}
    
    public Node representData(Object data) { return representMapping(getTag(data.getClass(), Tag.MAP), (Map)data, null); }
  }
  
  protected class RepresentSet implements Represent
  {
    protected RepresentSet() {}
    
    public Node representData(Object data) {
      Map<Object, Object> value = new LinkedHashMap();
      Set<Object> set = (Set)data;
      for (Object key : set) {
        value.put(key, null);
      }
      return representMapping(getTag(data.getClass(), Tag.SET), value, null);
    }
  }
  
  protected class RepresentDate implements Represent { protected RepresentDate() {}
    
    public Node representData(Object data) { Calendar calendar;
      Calendar calendar;
      if ((data instanceof Calendar)) {
        calendar = (Calendar)data;
      } else {
        calendar = Calendar.getInstance(getTimeZone() == null ? TimeZone.getTimeZone("UTC") : timeZone);
        
        calendar.setTime((Date)data);
      }
      int years = calendar.get(1);
      int months = calendar.get(2) + 1;
      int days = calendar.get(5);
      int hour24 = calendar.get(11);
      int minutes = calendar.get(12);
      int seconds = calendar.get(13);
      int millis = calendar.get(14);
      StringBuilder buffer = new StringBuilder(String.valueOf(years));
      while (buffer.length() < 4)
      {
        buffer.insert(0, "0");
      }
      buffer.append("-");
      if (months < 10) {
        buffer.append("0");
      }
      buffer.append(String.valueOf(months));
      buffer.append("-");
      if (days < 10) {
        buffer.append("0");
      }
      buffer.append(String.valueOf(days));
      buffer.append("T");
      if (hour24 < 10) {
        buffer.append("0");
      }
      buffer.append(String.valueOf(hour24));
      buffer.append(":");
      if (minutes < 10) {
        buffer.append("0");
      }
      buffer.append(String.valueOf(minutes));
      buffer.append(":");
      if (seconds < 10) {
        buffer.append("0");
      }
      buffer.append(String.valueOf(seconds));
      if (millis > 0) {
        if (millis < 10) {
          buffer.append(".00");
        } else if (millis < 100) {
          buffer.append(".0");
        } else {
          buffer.append(".");
        }
        buffer.append(String.valueOf(millis));
      }
      if (TimeZone.getTimeZone("UTC").equals(calendar.getTimeZone())) {
        buffer.append("Z");
      }
      else {
        int gmtOffset = calendar.getTimeZone().getOffset(calendar.get(0), calendar.get(1), calendar.get(2), calendar.get(5), calendar.get(7), calendar.get(14));
        


        int minutesOffset = gmtOffset / 60000;
        int hoursOffset = minutesOffset / 60;
        int partOfHour = minutesOffset % 60;
        buffer.append((hoursOffset > 0 ? "+" : "") + hoursOffset + ":" + (partOfHour < 10 ? "0" + partOfHour : Integer.valueOf(partOfHour)));
      }
      
      return representScalar(getTag(data.getClass(), Tag.TIMESTAMP), buffer.toString(), null);
    }
  }
  
  protected class RepresentEnum implements Represent { protected RepresentEnum() {}
    
    public Node representData(Object data) { Tag tag = new Tag(data.getClass());
      return representScalar(getTag(data.getClass(), tag), ((Enum)data).name());
    }
  }
  
  protected class RepresentByteArray implements Represent { protected RepresentByteArray() {}
    
    public Node representData(Object data) { char[] binary = Base64Coder.encode((byte[])data);
      return representScalar(Tag.BINARY, String.valueOf(binary), Character.valueOf('|'));
    }
  }
  
  public TimeZone getTimeZone() {
    return timeZone;
  }
  
  public void setTimeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
  }
}
