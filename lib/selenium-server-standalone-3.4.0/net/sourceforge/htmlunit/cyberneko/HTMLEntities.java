package net.sourceforge.htmlunit.cyberneko;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;






























public class HTMLEntities
{
  protected static final Map<String, String> ENTITIES;
  protected static final IntProperties SEITITNE = new IntProperties();
  



  static
  {
    Properties props = new Properties();
    
    load0(props, "res/HTMLlat1.properties");
    load0(props, "res/HTMLspecial.properties");
    load0(props, "res/HTMLsymbol.properties");
    load0(props, "res/XMLbuiltin.properties");
    
    Map<String, String> entities = new HashMap();
    

    Enumeration<?> keys = props.propertyNames();
    while (keys.hasMoreElements()) {
      String key = (String)keys.nextElement();
      String value = props.getProperty(key);
      entities.put(key, value);
      if (value.length() == 1) {
        int ivalue = value.charAt(0);
        SEITITNE.put(ivalue, key);
      }
    }
    
    ENTITIES = Collections.unmodifiableMap(entities);
  }
  



  public HTMLEntities() {}
  


  public static int get(String name)
  {
    String value = (String)ENTITIES.get(name);
    return value != null ? value.charAt(0) : -1;
  }
  



  public static String get(int c)
  {
    return SEITITNE.get(c);
  }
  



  private static void load0(Properties props, String filename)
  {
    try
    {
      InputStream stream = HTMLEntities.class.getResourceAsStream(filename);
      props.load(stream);
      stream.close();
    }
    catch (IOException e) {
      System.err.println("error: unable to load resource \"" + filename + "\"");
    }
  }
  

  static class IntProperties
  {
    IntProperties() {}
    
    private Entry[] entries = new Entry[101];
    
    public void put(int key, String value) { int hash = key % entries.length;
      Entry entry = new Entry(key, value, entries[hash]);
      entries[hash] = entry;
    }
    
    public String get(int key) { int hash = key % entries.length;
      Entry entry = entries[hash];
      while (entry != null) {
        if (key == key) {
          return value;
        }
        entry = next;
      }
      return null;
    }
    
    static class Entry { public int key;
      public String value;
      public Entry next;
      
      public Entry(int key, String value, Entry next) { this.key = key;
        this.value = value;
        this.next = next;
      }
    }
  }
}
