package org.seleniumhq.jetty9.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.seleniumhq.jetty9.util.ArrayTernaryTrie;
import org.seleniumhq.jetty9.util.QuotedStringTokenizer;
import org.seleniumhq.jetty9.util.Trie;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;






























public class HttpFields
  implements Iterable<HttpField>
{
  @Deprecated
  public static final String __separators = ", \t";
  private static final Logger LOG = Log.getLogger(HttpFields.class);
  

  private HttpField[] _fields;
  
  private int _size;
  

  public HttpFields()
  {
    _fields = new HttpField[20];
  }
  





  public HttpFields(int capacity)
  {
    _fields = new HttpField[capacity];
  }
  





  public HttpFields(HttpFields fields)
  {
    _fields = ((HttpField[])Arrays.copyOf(_fields, _fields.length + 10));
    _size = _size;
  }
  
  public int size()
  {
    return _size;
  }
  

  public Iterator<HttpField> iterator()
  {
    return new Itr(null);
  }
  
  public Stream<HttpField> stream()
  {
    return StreamSupport.stream(Arrays.spliterator(_fields, 0, _size), false);
  }
  




  public Set<String> getFieldNamesCollection()
  {
    Set<String> set = new HashSet(_size);
    for (HttpField f : this)
    {
      if (f != null)
        set.add(f.getName());
    }
    return set;
  }
  





  public Enumeration<String> getFieldNames()
  {
    return Collections.enumeration(getFieldNamesCollection());
  }
  





  public HttpField getField(int index)
  {
    if (index >= _size)
      throw new NoSuchElementException();
    return _fields[index];
  }
  
  public HttpField getField(HttpHeader header)
  {
    for (int i = 0; i < _size; i++)
    {
      HttpField f = _fields[i];
      if (f.getHeader() == header)
        return f;
    }
    return null;
  }
  
  public HttpField getField(String name)
  {
    for (int i = 0; i < _size; i++)
    {
      HttpField f = _fields[i];
      if (f.getName().equalsIgnoreCase(name))
        return f;
    }
    return null;
  }
  
  public boolean contains(HttpField field)
  {
    for (int i = _size; i-- > 0;)
    {
      HttpField f = _fields[i];
      if ((f.isSameName(field)) && ((f.equals(field)) || (f.contains(field.getValue()))))
        return true;
    }
    return false;
  }
  
  public boolean contains(HttpHeader header, String value)
  {
    for (int i = _size; i-- > 0;)
    {
      HttpField f = _fields[i];
      if ((f.getHeader() == header) && (f.contains(value)))
        return true;
    }
    return false;
  }
  
  public boolean contains(String name, String value)
  {
    for (int i = _size; i-- > 0;)
    {
      HttpField f = _fields[i];
      if ((f.getName().equalsIgnoreCase(name)) && (f.contains(value)))
        return true;
    }
    return false;
  }
  
  public boolean contains(HttpHeader header)
  {
    for (int i = _size; i-- > 0;)
    {
      HttpField f = _fields[i];
      if (f.getHeader() == header)
        return true;
    }
    return false;
  }
  
  public boolean containsKey(String name)
  {
    for (int i = _size; i-- > 0;)
    {
      HttpField f = _fields[i];
      if (f.getName().equalsIgnoreCase(name))
        return true;
    }
    return false;
  }
  
  @Deprecated
  public String getStringField(HttpHeader header)
  {
    return get(header);
  }
  
  public String get(HttpHeader header)
  {
    for (int i = 0; i < _size; i++)
    {
      HttpField f = _fields[i];
      if (f.getHeader() == header)
        return f.getValue();
    }
    return null;
  }
  
  @Deprecated
  public String getStringField(String name)
  {
    return get(name);
  }
  
  public String get(String header)
  {
    for (int i = 0; i < _size; i++)
    {
      HttpField f = _fields[i];
      if (f.getName().equalsIgnoreCase(header))
        return f.getValue();
    }
    return null;
  }
  






  public List<String> getValuesList(HttpHeader header)
  {
    List<String> list = new ArrayList();
    for (HttpField f : this)
      if (f.getHeader() == header)
        list.add(f.getValue());
    return list;
  }
  






  public List<String> getValuesList(String name)
  {
    List<String> list = new ArrayList();
    for (HttpField f : this)
      if (f.getName().equalsIgnoreCase(name))
        list.add(f.getValue());
    return list;
  }
  








  public boolean addCSV(HttpHeader header, String... values)
  {
    QuotedCSV existing = null;
    for (HttpField f : this)
    {
      if (f.getHeader() == header)
      {
        if (existing == null)
          existing = new QuotedCSV(false, new String[0]);
        existing.addValue(f.getValue());
      }
    }
    
    String value = addCSV(existing, values);
    if (value != null)
    {
      add(header, value);
      return true;
    }
    return false;
  }
  







  public boolean addCSV(String name, String... values)
  {
    QuotedCSV existing = null;
    for (HttpField f : this)
    {
      if (f.getName().equalsIgnoreCase(name))
      {
        if (existing == null)
          existing = new QuotedCSV(false, new String[0]);
        existing.addValue(f.getValue());
      }
    }
    String value = addCSV(existing, values);
    if (value != null)
    {
      add(name, value);
      return true;
    }
    return false;
  }
  

  protected String addCSV(QuotedCSV existing, String... values)
  {
    boolean add = true;
    int i; if ((existing != null) && (!existing.isEmpty()))
    {
      add = false;
      
      for (i = values.length; i-- > 0;)
      {
        unquoted = QuotedCSV.unquote(values[i]);
        if (existing.getValues().contains(unquoted)) {
          values[i] = null;
        } else
          add = true;
      }
    }
    String unquoted;
    if (add)
    {
      StringBuilder value = new StringBuilder();
      for (String v : values)
      {
        if (v != null)
        {
          if (value.length() > 0)
            value.append(", ");
          value.append(v);
        } }
      if (value.length() > 0) {
        return value.toString();
      }
    }
    return null;
  }
  








  public List<String> getCSV(HttpHeader header, boolean keepQuotes)
  {
    QuotedCSV values = null;
    for (HttpField f : this)
    {
      if (f.getHeader() == header)
      {
        if (values == null)
          values = new QuotedCSV(keepQuotes, new String[0]);
        values.addValue(f.getValue());
      }
    }
    return values == null ? Collections.emptyList() : values.getValues();
  }
  








  public List<String> getCSV(String name, boolean keepQuotes)
  {
    QuotedCSV values = null;
    for (HttpField f : this)
    {
      if (f.getName().equalsIgnoreCase(name))
      {
        if (values == null)
          values = new QuotedCSV(keepQuotes, new String[0]);
        values.addValue(f.getValue());
      }
    }
    return values == null ? Collections.emptyList() : values.getValues();
  }
  







  public List<String> getQualityCSV(HttpHeader header)
  {
    QuotedQualityCSV values = null;
    for (HttpField f : this)
    {
      if (f.getHeader() == header)
      {
        if (values == null)
          values = new QuotedQualityCSV();
        values.addValue(f.getValue());
      }
    }
    
    return values == null ? Collections.emptyList() : values.getValues();
  }
  







  public List<String> getQualityCSV(String name)
  {
    QuotedQualityCSV values = null;
    for (HttpField f : this)
    {
      if (f.getName().equalsIgnoreCase(name))
      {
        if (values == null)
          values = new QuotedQualityCSV();
        values.addValue(f.getValue());
      }
    }
    return values == null ? Collections.emptyList() : values.getValues();
  }
  






  public Enumeration<String> getValues(final String name)
  {
    for (int i = 0; i < _size; i++)
    {
      final HttpField f = _fields[i];
      
      if ((f.getName().equalsIgnoreCase(name)) && (f.getValue() != null))
      {
        final int first = i;
        new Enumeration()
        {
          HttpField field = f;
          int i = first + 1;
          

          public boolean hasMoreElements()
          {
            if (field == null)
            {
              while (i < _size)
              {
                field = _fields[(i++)];
                if ((field.getName().equalsIgnoreCase(name)) && (field.getValue() != null))
                  return true;
              }
              field = null;
              return false;
            }
            return true;
          }
          
          public String nextElement()
            throws NoSuchElementException
          {
            if (hasMoreElements())
            {
              String value = field.getValue();
              field = null;
              return value;
            }
            throw new NoSuchElementException();
          }
        };
      }
    }
    
    List<String> empty = Collections.emptyList();
    return Collections.enumeration(empty);
  }
  









  @Deprecated
  public Enumeration<String> getValues(String name, final String separators)
  {
    final Enumeration<String> e = getValues(name);
    if (e == null)
      return null;
    new Enumeration()
    {
      QuotedStringTokenizer tok = null;
      

      public boolean hasMoreElements()
      {
        if ((tok != null) && (tok.hasMoreElements())) return true;
        while (e.hasMoreElements())
        {
          String value = (String)e.nextElement();
          if (value != null)
          {
            tok = new QuotedStringTokenizer(value, separators, false, false);
            if (tok.hasMoreElements()) return true;
          }
        }
        tok = null;
        return false;
      }
      
      public String nextElement()
        throws NoSuchElementException
      {
        if (!hasMoreElements()) throw new NoSuchElementException();
        String next = (String)tok.nextElement();
        if (next != null) next = next.trim();
        return next;
      }
    };
  }
  
  public void put(HttpField field)
  {
    boolean put = false;
    for (int i = _size; i-- > 0;)
    {
      HttpField f = _fields[i];
      if (f.isSameName(field))
      {
        if (put)
        {
          System.arraycopy(_fields, i + 1, _fields, i, --_size - i);
        }
        else
        {
          _fields[i] = field;
          put = true;
        }
      }
    }
    if (!put) {
      add(field);
    }
  }
  





  public void put(String name, String value)
  {
    if (value == null) {
      remove(name);
    } else {
      put(new HttpField(name, value));
    }
  }
  
  public void put(HttpHeader header, HttpHeaderValue value) {
    put(header, value.toString());
  }
  






  public void put(HttpHeader header, String value)
  {
    if (value == null) {
      remove(header);
    } else {
      put(new HttpField(header, value));
    }
  }
  





  public void put(String name, List<String> list)
  {
    remove(name);
    for (String v : list) {
      if (v != null) {
        add(name, v);
      }
    }
  }
  





  public void add(String name, String value)
  {
    if (value == null) {
      return;
    }
    HttpField field = new HttpField(name, value);
    add(field);
  }
  
  public void add(HttpHeader header, HttpHeaderValue value)
  {
    add(header, value.toString());
  }
  







  public void add(HttpHeader header, String value)
  {
    if (value == null) { throw new IllegalArgumentException("null value");
    }
    HttpField field = new HttpField(header, value);
    add(field);
  }
  






  public HttpField remove(HttpHeader name)
  {
    HttpField removed = null;
    for (int i = _size; i-- > 0;)
    {
      HttpField f = _fields[i];
      if (f.getHeader() == name)
      {
        removed = f;
        System.arraycopy(_fields, i + 1, _fields, i, --_size - i);
      }
    }
    return removed;
  }
  






  public HttpField remove(String name)
  {
    HttpField removed = null;
    for (int i = _size; i-- > 0;)
    {
      HttpField f = _fields[i];
      if (f.getName().equalsIgnoreCase(name))
      {
        removed = f;
        System.arraycopy(_fields, i + 1, _fields, i, --_size - i);
      }
    }
    return removed;
  }
  







  public long getLongField(String name)
    throws NumberFormatException
  {
    HttpField field = getField(name);
    return field == null ? -1L : field.getLongValue();
  }
  







  public long getDateField(String name)
  {
    HttpField field = getField(name);
    if (field == null) {
      return -1L;
    }
    String val = valueParameters(field.getValue(), null);
    if (val == null) {
      return -1L;
    }
    long date = DateParser.parseDate(val);
    if (date == -1L)
      throw new IllegalArgumentException("Cannot convert date: " + val);
    return date;
  }
  







  public void putLongField(HttpHeader name, long value)
  {
    String v = Long.toString(value);
    put(name, v);
  }
  






  public void putLongField(String name, long value)
  {
    String v = Long.toString(value);
    put(name, v);
  }
  







  public void putDateField(HttpHeader name, long date)
  {
    String d = DateGenerator.formatDate(date);
    put(name, d);
  }
  






  public void putDateField(String name, long date)
  {
    String d = DateGenerator.formatDate(date);
    put(name, d);
  }
  






  public void addDateField(String name, long date)
  {
    String d = DateGenerator.formatDate(date);
    add(name, d);
  }
  

  public int hashCode()
  {
    int hash = 0;
    for (HttpField field : _fields)
      hash += field.hashCode();
    return hash;
  }
  

  public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (!(o instanceof HttpFields)) {
      return false;
    }
    HttpFields that = (HttpFields)o;
    

    if (size() != that.size()) {
      return false;
    }
    Iterator localIterator1 = iterator(); if (localIterator1.hasNext()) { HttpField fi = (HttpField)localIterator1.next();
      
      Iterator localIterator2 = that.iterator(); for (;;) { if (!localIterator2.hasNext()) break label103; HttpField fa = (HttpField)localIterator2.next();
        
        if (fi.equals(fa)) break;
      }
      label103:
      return false;
    }
    return true;
  }
  

  public String toString()
  {
    try
    {
      StringBuilder buffer = new StringBuilder();
      for (HttpField field : this)
      {
        if (field != null)
        {
          String tmp = field.getName();
          if (tmp != null) buffer.append(tmp);
          buffer.append(": ");
          tmp = field.getValue();
          if (tmp != null) buffer.append(tmp);
          buffer.append("\r\n");
        }
      }
      buffer.append("\r\n");
      return buffer.toString();
    }
    catch (Exception e)
    {
      LOG.warn(e);
      return e.toString();
    }
  }
  
  public void clear()
  {
    _size = 0;
  }
  
  public void add(HttpField field)
  {
    if (field != null)
    {
      if (_size == _fields.length)
        _fields = ((HttpField[])Arrays.copyOf(_fields, _size * 2));
      _fields[(_size++)] = field;
    }
  }
  
  public void addAll(HttpFields fields)
  {
    for (int i = 0; i < _size; i++) {
      add(_fields[i]);
    }
  }
  





  public void add(HttpFields fields)
  {
    if (fields == null) { return;
    }
    Enumeration<String> e = fields.getFieldNames();
    while (e.hasMoreElements())
    {
      String name = (String)e.nextElement();
      Enumeration<String> values = fields.getValues(name);
      while (values.hasMoreElements()) {
        add(name, (String)values.nextElement());
      }
    }
  }
  












  public static String stripParameters(String value)
  {
    if (value == null) { return null;
    }
    int i = value.indexOf(';');
    if (i < 0) return value;
    return value.substring(0, i).trim();
  }
  














  public static String valueParameters(String value, Map<String, String> parameters)
  {
    if (value == null) { return null;
    }
    int i = value.indexOf(';');
    if (i < 0) return value;
    if (parameters == null) { return value.substring(0, i).trim();
    }
    StringTokenizer tok1 = new QuotedStringTokenizer(value.substring(i), ";", false, true);
    while (tok1.hasMoreTokens())
    {
      String token = tok1.nextToken();
      StringTokenizer tok2 = new QuotedStringTokenizer(token, "= ");
      if (tok2.hasMoreTokens())
      {
        String paramName = tok2.nextToken();
        String paramVal = null;
        if (tok2.hasMoreTokens()) paramVal = tok2.nextToken();
        parameters.put(paramName, paramVal);
      }
    }
    
    return value.substring(0, i).trim();
  }
  
  @Deprecated
  private static final Float __one = new Float("1.0");
  @Deprecated
  private static final Float __zero = new Float("0.0");
  @Deprecated
  private static final Trie<Float> __qualities = new ArrayTernaryTrie();
  
  static {
    __qualities.put("*", __one);
    __qualities.put("1.0", __one);
    __qualities.put("1", __one);
    __qualities.put("0.9", new Float("0.9"));
    __qualities.put("0.8", new Float("0.8"));
    __qualities.put("0.7", new Float("0.7"));
    __qualities.put("0.66", new Float("0.66"));
    __qualities.put("0.6", new Float("0.6"));
    __qualities.put("0.5", new Float("0.5"));
    __qualities.put("0.4", new Float("0.4"));
    __qualities.put("0.33", new Float("0.33"));
    __qualities.put("0.3", new Float("0.3"));
    __qualities.put("0.2", new Float("0.2"));
    __qualities.put("0.1", new Float("0.1"));
    __qualities.put("0", __zero);
    __qualities.put("0.0", __zero);
  }
  
  @Deprecated
  public static Float getQuality(String value)
  {
    if (value == null) { return __zero;
    }
    int qe = value.indexOf(";");
    if ((qe++ < 0) || (qe == value.length())) { return __one;
    }
    if (value.charAt(qe++) == 'q')
    {
      qe++;
      Float q = (Float)__qualities.get(value, qe, value.length() - qe);
      if (q != null) {
        return q;
      }
    }
    Map<String, String> params = new HashMap(4);
    valueParameters(value, params);
    String qs = (String)params.get("q");
    if (qs == null)
      qs = "*";
    Float q = (Float)__qualities.get(qs);
    if (q == null)
    {
      try
      {
        q = new Float(qs);
      }
      catch (Exception e)
      {
        q = __one;
      }
    }
    return q;
  }
  






  @Deprecated
  public static List<String> qualityList(Enumeration<String> e)
  {
    if ((e == null) || (!e.hasMoreElements())) {
      return Collections.emptyList();
    }
    QuotedQualityCSV values = new QuotedQualityCSV();
    while (e.hasMoreElements())
      values.addValue((String)e.nextElement());
    return values.getValues();
  }
  
  private class Itr
    implements Iterator<HttpField>
  {
    int _cursor;
    int _last = -1;
    
    private Itr() {}
    
    public boolean hasNext() { return _cursor != _size; }
    

    public HttpField next()
    {
      int i = _cursor;
      if (i >= _size)
        throw new NoSuchElementException();
      _cursor = (i + 1);
      return _fields[(this._last = i)];
    }
    
    public void remove()
    {
      if (_last < 0) {
        throw new IllegalStateException();
      }
      System.arraycopy(_fields, _last + 1, _fields, _last, HttpFields.access$106(HttpFields.this) - _last);
      _cursor = _last;
      _last = -1;
    }
  }
}
