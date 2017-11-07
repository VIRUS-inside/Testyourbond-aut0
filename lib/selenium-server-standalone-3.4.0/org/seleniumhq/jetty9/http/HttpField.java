package org.seleniumhq.jetty9.http;

import java.util.List;
import java.util.Objects;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.Trie;





















public class HttpField
{
  private static final String __zeroquality = "q=0";
  private final HttpHeader _header;
  private final String _name;
  private final String _value;
  private int hash = 0;
  
  public HttpField(HttpHeader header, String name, String value)
  {
    _header = header;
    _name = name;
    _value = value;
  }
  
  public HttpField(HttpHeader header, String value)
  {
    this(header, header.asString(), value);
  }
  
  public HttpField(HttpHeader header, HttpHeaderValue value)
  {
    this(header, header.asString(), value.asString());
  }
  
  public HttpField(String name, String value)
  {
    this((HttpHeader)HttpHeader.CACHE.get(name), name, value);
  }
  
  public HttpHeader getHeader()
  {
    return _header;
  }
  
  public String getName()
  {
    return _name;
  }
  
  public String getValue()
  {
    return _value;
  }
  
  public int getIntValue()
  {
    return Integer.valueOf(_value).intValue();
  }
  
  public long getLongValue()
  {
    return Long.valueOf(_value).longValue();
  }
  
  public String[] getValues()
  {
    if (_value == null) {
      return null;
    }
    QuotedCSV list = new QuotedCSV(false, new String[] { _value });
    return (String[])list.getValues().toArray(new String[list.size()]);
  }
  







  public boolean contains(String search)
  {
    if (search == null)
      return _value == null;
    if (search.length() == 0)
      return false;
    if (_value == null)
      return false;
    if (search.equals(_value)) {
      return true;
    }
    search = StringUtil.asciiToLowerCase(search);
    
    int state = 0;
    int match = 0;
    int param = 0;
    
    for (int i = 0; i < _value.length(); i++)
    {
      char c = _value.charAt(i);
      switch (state)
      {
      case 0: 
        switch (c)
        {
        case '"': 
          match = 0;
          state = 2;
          break;
        
        case ',': 
          break;
        
        case ';': 
          param = -1;
          match = -1;
          state = 5;
          break;
        
        case '\t': 
        case ' ': 
          break;
        
        default: 
          match = Character.toLowerCase(c) == search.charAt(0) ? 1 : -1;
          state = 1; }
        break;
      


      case 1: 
        switch (c)
        {

        case ',': 
          if (match == search.length())
            return true;
          state = 0;
          break;
        
        case ';': 
          param = match >= 0 ? 0 : -1;
          state = 5;
          break;
        
        default: 
          if (match > 0)
          {
            if (match < search.length()) {
              match = Character.toLowerCase(c) == search.charAt(match) ? match + 1 : -1;
            } else if ((c != ' ') && (c != '\t')) {
              match = -1;
            }
          }
          break;
        }
        
        break;
      case 2: 
        switch (c)
        {
        case '\\': 
          state = 3;
          break;
        
        case '"': 
          state = 4;
          break;
        
        default: 
          if (match >= 0)
          {
            if (match < search.length()) {
              match = Character.toLowerCase(c) == search.charAt(match) ? match + 1 : -1;
            } else
              match = -1;
          }
          break;
        }
        break;
      case 3: 
        if (match >= 0)
        {
          if (match < search.length()) {
            match = Character.toLowerCase(c) == search.charAt(match) ? match + 1 : -1;
          } else
            match = -1;
        }
        state = 2;
        break;
      
      case 4: 
        switch (c)
        {
        case '\t': 
        case ' ': 
          break;
        
        case ';': 
          state = 5;
          break;
        

        case ',': 
          if (match == search.length())
            return true;
          state = 0;
          break;
        

        default: 
          match = -1;
        }
        break;
      
      case 5: 
        switch (c)
        {

        case ',': 
          if ((param != "q=0".length()) && (match == search.length()))
            return true;
          param = 0;
          state = 0;
          break;
        
        case '\t': 
        case ' ': 
          break;
        
        default: 
          if (param >= 0)
          {
            if (param < "q=0".length()) {
              param = Character.toLowerCase(c) == "q=0".charAt(param) ? param + 1 : -1;
            } else if ((c != '0') && (c != '.'))
              param = -1;
          }
          break;
        }
        
        break;
      default: 
        throw new IllegalStateException();
      }
      
    }
    return (param != "q=0".length()) && (match == search.length());
  }
  


  public String toString()
  {
    String v = getValue();
    return getName() + ": " + (v == null ? "" : v);
  }
  
  public boolean isSameName(HttpField field)
  {
    if (field == null)
      return false;
    if (field == this)
      return true;
    if ((_header != null) && (_header == field.getHeader()))
      return true;
    if (_name.equalsIgnoreCase(field.getName()))
      return true;
    return false;
  }
  
  private int nameHashCode()
  {
    int h = hash;
    int len = _name.length();
    if ((h == 0) && (len > 0))
    {
      for (int i = 0; i < len; i++)
      {

        char c = _name.charAt(i);
        
        if ((c >= 'a') && (c <= 'z'))
          c = (char)(c - ' ');
        h = 31 * h + c;
      }
      hash = h;
    }
    return h;
  }
  

  public int hashCode()
  {
    int vhc = Objects.hashCode(_value);
    if (_header == null)
      return vhc ^ nameHashCode();
    return vhc ^ _header.hashCode();
  }
  

  public boolean equals(Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof HttpField))
      return false;
    HttpField field = (HttpField)o;
    if (_header != field.getHeader())
      return false;
    if (!_name.equalsIgnoreCase(field.getName()))
      return false;
    if ((_value == null) && (field.getValue() != null))
      return false;
    return Objects.equals(_value, field.getValue());
  }
  
  public static class IntValueHttpField extends HttpField
  {
    private final int _int;
    
    public IntValueHttpField(HttpHeader header, String name, String value, int intValue)
    {
      super(name, value);
      _int = intValue;
    }
    
    public IntValueHttpField(HttpHeader header, String name, String value)
    {
      this(header, name, value, Integer.valueOf(value).intValue());
    }
    
    public IntValueHttpField(HttpHeader header, String name, int intValue)
    {
      this(header, name, Integer.toString(intValue), intValue);
    }
    
    public IntValueHttpField(HttpHeader header, int value)
    {
      this(header, header.asString(), value);
    }
    

    public int getIntValue()
    {
      return _int;
    }
    

    public long getLongValue()
    {
      return _int;
    }
  }
  
  public static class LongValueHttpField extends HttpField
  {
    private final long _long;
    
    public LongValueHttpField(HttpHeader header, String name, String value, long longValue)
    {
      super(name, value);
      _long = longValue;
    }
    
    public LongValueHttpField(HttpHeader header, String name, String value)
    {
      this(header, name, value, Long.valueOf(value).longValue());
    }
    
    public LongValueHttpField(HttpHeader header, String name, long value)
    {
      this(header, name, Long.toString(value), value);
    }
    
    public LongValueHttpField(HttpHeader header, long value)
    {
      this(header, header.asString(), value);
    }
    

    public int getIntValue()
    {
      return (int)_long;
    }
    

    public long getLongValue()
    {
      return _long;
    }
  }
}
