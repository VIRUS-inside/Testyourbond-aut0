package org.jsoup.nodes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map.Entry;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;




public class Attribute
  implements Map.Entry<String, String>, Cloneable
{
  private static final String[] booleanAttributes = { "allowfullscreen", "async", "autofocus", "checked", "compact", "declare", "default", "defer", "disabled", "formnovalidate", "hidden", "inert", "ismap", "itemscope", "multiple", "muted", "nohref", "noresize", "noshade", "novalidate", "nowrap", "open", "readonly", "required", "reversed", "seamless", "selected", "sortable", "truespeed", "typemustmatch" };
  



  private String key;
  



  private String value;
  



  public Attribute(String key, String value)
  {
    Validate.notNull(key);
    Validate.notNull(value);
    this.key = key.trim();
    Validate.notEmpty(key);
    this.value = value;
  }
  



  public String getKey()
  {
    return key;
  }
  



  public void setKey(String key)
  {
    Validate.notEmpty(key);
    this.key = key.trim();
  }
  



  public String getValue()
  {
    return value;
  }
  



  public String setValue(String value)
  {
    Validate.notNull(value);
    String old = this.value;
    this.value = value;
    return old;
  }
  



  public String html()
  {
    StringBuilder accum = new StringBuilder();
    try
    {
      html(accum, new Document("").outputSettings());
    } catch (IOException exception) {
      throw new SerializationException(exception);
    }
    return accum.toString();
  }
  
  protected void html(Appendable accum, Document.OutputSettings out) throws IOException {
    accum.append(key);
    if (!shouldCollapseAttribute(out)) {
      accum.append("=\"");
      Entities.escape(accum, value, out, true, false, false);
      accum.append('"');
    }
  }
  




  public String toString()
  {
    return html();
  }
  





  public static Attribute createFromEncoded(String unencodedKey, String encodedValue)
  {
    String value = Entities.unescape(encodedValue, true);
    return new Attribute(unencodedKey, value);
  }
  
  protected boolean isDataAttribute() {
    return (key.startsWith("data-")) && (key.length() > "data-".length());
  }
  







  protected final boolean shouldCollapseAttribute(Document.OutputSettings out)
  {
    return (("".equals(value)) || (value.equalsIgnoreCase(key))) && (out.syntax() == Document.OutputSettings.Syntax.html) && (isBooleanAttribute());
  }
  
  protected boolean isBooleanAttribute() {
    return Arrays.binarySearch(booleanAttributes, key) >= 0;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof Attribute)) { return false;
    }
    Attribute attribute = (Attribute)o;
    
    if (key != null ? !key.equals(key) : key != null) return false;
    return value != null ? value.equals(value) : value == null;
  }
  
  public int hashCode()
  {
    int result = key != null ? key.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }
  
  public Attribute clone()
  {
    try {
      return (Attribute)super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}
