package org.seleniumhq.jetty9.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


























public class QuotedCSV
  implements Iterable<String>
{
  private static enum State
  {
    VALUE,  PARAM_NAME,  PARAM_VALUE;
    private State() {} }
  protected final List<String> _values = new ArrayList();
  
  protected final boolean _keepQuotes;
  
  public QuotedCSV(String... values)
  {
    this(true, values);
  }
  

  public QuotedCSV(boolean keepQuotes, String... values)
  {
    _keepQuotes = keepQuotes;
    for (String v : values) {
      addValue(v);
    }
  }
  



  public void addValue(String value)
  {
    StringBuffer buffer = new StringBuffer();
    
    int l = value.length();
    State state = State.VALUE;
    boolean quoted = false;
    boolean sloshed = false;
    int nws_length = 0;
    int last_length = 0;
    int value_length = -1;
    int param_name = -1;
    int param_value = -1;
    
    for (int i = 0; i <= l; i++)
    {
      char c = i == l ? '\000' : value.charAt(i);
      

      if ((quoted) && (c != 0))
      {
        if (sloshed) {
          sloshed = false;
        }
        else {
          switch (c)
          {
          case '\\': 
            sloshed = true;
            if (_keepQuotes) break;
            break;
          
          case '"': 
            quoted = false;
            if (!_keepQuotes) {
              continue;
            }
          }
          
        }
        buffer.append(c);
        nws_length = buffer.length();

      }
      else
      {
        switch (c)
        {
        case '\t': 
        case ' ': 
          if (buffer.length() > last_length) {
            buffer.append(c);
          }
          break;
        case '"': 
          quoted = true;
          if (_keepQuotes)
          {
            if ((state == State.PARAM_VALUE) && (param_value < 0))
              param_value = nws_length;
            buffer.append(c);
          }
          else if ((state == State.PARAM_VALUE) && (param_value < 0)) {
            param_value = nws_length; }
          nws_length = buffer.length();
          break;
        
        case ';': 
          buffer.setLength(nws_length);
          if (state == State.VALUE)
          {
            parsedValue(buffer);
            value_length = buffer.length();
          }
          else {
            parsedParam(buffer, value_length, param_name, param_value); }
          nws_length = buffer.length();
          param_name = param_value = -1;
          buffer.append(c);
          nws_length++;last_length = nws_length;
          state = State.PARAM_NAME;
          break;
        
        case '\000': 
        case ',': 
          if (nws_length > 0)
          {
            buffer.setLength(nws_length);
            switch (1.$SwitchMap$org$eclipse$jetty$http$QuotedCSV$State[state.ordinal()])
            {
            case 1: 
              parsedValue(buffer);
              value_length = buffer.length();
              break;
            case 2: 
            case 3: 
              parsedParam(buffer, value_length, param_name, param_value);
            }
            
            _values.add(buffer.toString());
          }
          buffer.setLength(0);
          last_length = 0;
          nws_length = 0;
          value_length = param_name = param_value = -1;
          state = State.VALUE;
          break;
        
        case '=': 
          switch (1.$SwitchMap$org$eclipse$jetty$http$QuotedCSV$State[state.ordinal()])
          {

          case 1: 
            value_length = param_name = 0;
            buffer.setLength(nws_length);
            buffer.append(c);
            nws_length++;last_length = nws_length;
            state = State.PARAM_VALUE;
            break;
          
          case 2: 
            buffer.setLength(nws_length);
            buffer.append(c);
            nws_length++;last_length = nws_length;
            state = State.PARAM_VALUE;
            break;
          
          case 3: 
            if (param_value < 0)
              param_value = nws_length;
            buffer.append(c);
            nws_length = buffer.length();
          }
          
          break;
        

        default: 
          switch (1.$SwitchMap$org$eclipse$jetty$http$QuotedCSV$State[state.ordinal()])
          {

          case 1: 
            buffer.append(c);
            nws_length = buffer.length();
            break;
          


          case 2: 
            if (param_name < 0)
              param_name = nws_length;
            buffer.append(c);
            nws_length = buffer.length();
            break;
          


          case 3: 
            if (param_value < 0)
              param_value = nws_length;
            buffer.append(c);
            nws_length = buffer.length();
          }
          
          



          break;
        }
        
      }
    }
  }
  




  protected void parsedValue(StringBuffer buffer) {}
  



  protected void parsedParam(StringBuffer buffer, int valueLength, int paramName, int paramValue) {}
  



  public int size()
  {
    return _values.size();
  }
  
  public boolean isEmpty()
  {
    return _values.isEmpty();
  }
  
  public List<String> getValues()
  {
    return _values;
  }
  

  public Iterator<String> iterator()
  {
    return _values.iterator();
  }
  

  public static String unquote(String s)
  {
    int l = s.length();
    if ((s == null) || (l == 0)) {
      return s;
    }
    
    for (int i = 0; 
        i < l; i++)
    {
      char c = s.charAt(i);
      if (c == '"')
        break;
    }
    if (i == l) {
      return s;
    }
    boolean quoted = true;
    boolean sloshed = false;
    StringBuffer buffer = new StringBuffer();
    buffer.append(s, 0, i);
    i++;
    for (; i < l; i++)
    {
      char c = s.charAt(i);
      if (quoted)
      {
        if (sloshed)
        {
          buffer.append(c);
          sloshed = false;
        }
        else if (c == '"') {
          quoted = false;
        } else if (c == '\\') {
          sloshed = true;
        } else {
          buffer.append(c);
        }
      } else if (c == '"') {
        quoted = true;
      } else
        buffer.append(c);
    }
    return buffer.toString();
  }
}
