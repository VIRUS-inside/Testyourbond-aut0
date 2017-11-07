package org.seleniumhq.jetty9.util;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;




































/**
 * @deprecated
 */
public class IPAddressMap<TYPE>
  extends HashMap<String, TYPE>
{
  private final HashMap<String, IPAddrPattern> _patterns = new HashMap();
  



  public IPAddressMap()
  {
    super(11);
  }
  





  public IPAddressMap(int capacity)
  {
    super(capacity);
  }
  







  public TYPE put(String addrSpec, TYPE object)
    throws IllegalArgumentException
  {
    if ((addrSpec == null) || (addrSpec.trim().length() == 0)) {
      throw new IllegalArgumentException("Invalid IP address pattern: " + addrSpec);
    }
    String spec = addrSpec.trim();
    if (_patterns.get(spec) == null) {
      _patterns.put(spec, new IPAddrPattern(spec));
    }
    return super.put(spec, object);
  }
  







  public TYPE get(Object key)
  {
    return super.get(key);
  }
  








  public TYPE match(String addr)
  {
    Map.Entry<String, TYPE> entry = getMatch(addr);
    return entry == null ? null : entry.getValue();
  }
  








  public Map.Entry<String, TYPE> getMatch(String addr)
  {
    if (addr != null)
    {
      for (Map.Entry<String, TYPE> entry : super.entrySet())
      {
        if (((IPAddrPattern)_patterns.get(entry.getKey())).match(addr))
        {
          return entry;
        }
      }
    }
    return null;
  }
  








  public Object getLazyMatches(String addr)
  {
    if (addr == null) {
      return LazyList.getList(super.entrySet());
    }
    Object entries = null;
    for (Map.Entry<String, TYPE> entry : super.entrySet())
    {
      if (((IPAddrPattern)_patterns.get(entry.getKey())).match(addr))
      {
        entries = LazyList.add(entries, entry);
      }
    }
    return entries;
  }
  







  private static class IPAddrPattern
  {
    private final IPAddressMap.OctetPattern[] _octets = new IPAddressMap.OctetPattern[4];
    






    public IPAddrPattern(String value)
      throws IllegalArgumentException
    {
      if ((value == null) || (value.trim().length() == 0)) {
        throw new IllegalArgumentException("Invalid IP address pattern: " + value);
      }
      try
      {
        StringTokenizer parts = new StringTokenizer(value, ".");
        

        for (int idx = 0; idx < 4; idx++)
        {
          String part = parts.hasMoreTokens() ? parts.nextToken().trim() : "0-255";
          
          int len = part.length();
          if ((len == 0) && (parts.hasMoreTokens())) {
            throw new IllegalArgumentException("Invalid IP address pattern: " + value);
          }
          _octets[idx] = new IPAddressMap.OctetPattern(len == 0 ? "0-255" : part);
        }
      }
      catch (IllegalArgumentException ex)
      {
        throw new IllegalArgumentException("Invalid IP address pattern: " + value, ex);
      }
    }
    









    public boolean match(String value)
      throws IllegalArgumentException
    {
      if ((value == null) || (value.trim().length() == 0)) {
        throw new IllegalArgumentException("Invalid IP address: " + value);
      }
      try
      {
        StringTokenizer parts = new StringTokenizer(value, ".");
        
        boolean result = true;
        for (int idx = 0; idx < 4; idx++)
        {
          if (!parts.hasMoreTokens()) {
            throw new IllegalArgumentException("Invalid IP address: " + value);
          }
          if (!(result &= _octets[idx].match(parts.nextToken())))
            break;
        }
        return result;
      }
      catch (IllegalArgumentException ex)
      {
        throw new IllegalArgumentException("Invalid IP address: " + value, ex);
      }
    }
  }
  






  private static class OctetPattern
    extends BitSet
  {
    private final BitSet _mask = new BitSet(256);
    







    public OctetPattern(String octetSpec)
      throws IllegalArgumentException
    {
      try
      {
        if (octetSpec != null)
        {
          String spec = octetSpec.trim();
          if (spec.length() == 0)
          {
            _mask.set(0, 255);
          }
          else
          {
            StringTokenizer parts = new StringTokenizer(spec, ",");
            while (parts.hasMoreTokens())
            {
              String part = parts.nextToken().trim();
              if (part.length() > 0)
              {
                if (part.indexOf('-') < 0)
                {
                  Integer value = Integer.valueOf(part);
                  _mask.set(value.intValue());
                }
                else
                {
                  int low = 0;int high = 255;
                  
                  String[] bounds = part.split("-", -2);
                  if (bounds.length != 2)
                  {
                    throw new IllegalArgumentException("Invalid octet spec: " + octetSpec);
                  }
                  
                  if (bounds[0].length() > 0)
                  {
                    low = Integer.parseInt(bounds[0]);
                  }
                  if (bounds[1].length() > 0)
                  {
                    high = Integer.parseInt(bounds[1]);
                  }
                  
                  if (low > high)
                  {
                    throw new IllegalArgumentException("Invalid octet spec: " + octetSpec);
                  }
                  
                  _mask.set(low, high + 1);
                }
              }
            }
          }
        }
      }
      catch (NumberFormatException ex)
      {
        throw new IllegalArgumentException("Invalid octet spec: " + octetSpec, ex);
      }
    }
    








    public boolean match(String value)
      throws IllegalArgumentException
    {
      if ((value == null) || (value.trim().length() == 0)) {
        throw new IllegalArgumentException("Invalid octet: " + value);
      }
      try
      {
        int number = Integer.parseInt(value);
        return match(number);
      }
      catch (NumberFormatException ex)
      {
        throw new IllegalArgumentException("Invalid octet: " + value);
      }
    }
    








    public boolean match(int number)
      throws IllegalArgumentException
    {
      if ((number < 0) || (number > 255)) {
        throw new IllegalArgumentException("Invalid octet: " + number);
      }
      return _mask.get(number);
    }
  }
}
