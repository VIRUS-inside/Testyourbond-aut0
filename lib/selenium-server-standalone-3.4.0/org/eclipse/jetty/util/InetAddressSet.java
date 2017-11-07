package org.eclipse.jetty.util;

import java.net.InetAddress;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;






































public class InetAddressSet
  extends AbstractSet<String>
  implements Set<String>, Predicate<InetAddress>
{
  private Map<String, InetPattern> _patterns = new HashMap();
  
  public InetAddressSet() {}
  
  public boolean add(String pattern) {
    return _patterns.put(pattern, newInetRange(pattern)) == null;
  }
  
  protected InetPattern newInetRange(String pattern)
  {
    if (pattern == null) {
      return null;
    }
    int slash = pattern.lastIndexOf('/');
    int dash = pattern.lastIndexOf('-');
    try
    {
      if (slash >= 0) {
        return new CidrInetRange(pattern, InetAddress.getByName(pattern.substring(0, slash).trim()), StringUtil.toInt(pattern, slash + 1));
      }
      if (dash >= 0) {
        return new MinMaxInetRange(pattern, InetAddress.getByName(pattern.substring(0, dash).trim()), InetAddress.getByName(pattern.substring(dash + 1).trim()));
      }
      return new SingletonInetRange(pattern, InetAddress.getByName(pattern));
    }
    catch (Exception e)
    {
      try
      {
        if ((slash < 0) && (dash > 0)) {
          return new LegacyInetRange(pattern);
        }
      }
      catch (Exception e2) {
        e.addSuppressed(e2);
      }
      throw new IllegalArgumentException("Bad pattern: " + pattern, e);
    }
  }
  

  public boolean remove(Object pattern)
  {
    return _patterns.remove(pattern) != null;
  }
  

  public Iterator<String> iterator()
  {
    return _patterns.keySet().iterator();
  }
  

  public int size()
  {
    return _patterns.size();
  }
  


  public boolean test(InetAddress address)
  {
    if (address == null)
      return false;
    byte[] raw = address.getAddress();
    for (InetPattern pattern : _patterns.values())
      if (pattern.test(address, raw))
        return true;
    return false;
  }
  
  static abstract class InetPattern
  {
    final String _pattern;
    
    InetPattern(String pattern)
    {
      _pattern = pattern;
    }
    

    abstract boolean test(InetAddress paramInetAddress, byte[] paramArrayOfByte);
    
    public String toString()
    {
      return _pattern;
    }
  }
  
  static class SingletonInetRange extends InetAddressSet.InetPattern
  {
    final InetAddress _address;
    
    public SingletonInetRange(String pattern, InetAddress address) {
      super();
      _address = address;
    }
    
    public boolean test(InetAddress address, byte[] raw)
    {
      return _address.equals(address);
    }
  }
  
  static class MinMaxInetRange
    extends InetAddressSet.InetPattern
  {
    final int[] _min;
    final int[] _max;
    
    public MinMaxInetRange(String pattern, InetAddress min, InetAddress max)
    {
      super();
      
      byte[] raw_min = min.getAddress();
      byte[] raw_max = max.getAddress();
      if (raw_min.length != raw_max.length) {
        throw new IllegalArgumentException("Cannot mix IPv4 and IPv6: " + pattern);
      }
      if (raw_min.length == 4)
      {

        int count = 0;
        for (char c : pattern.toCharArray())
          if (c == '.')
            count++;
        if (count != 6) {
          throw new IllegalArgumentException("Legacy pattern: " + pattern);
        }
      }
      _min = new int[raw_min.length];
      _max = new int[raw_min.length];
      
      for (int i = 0; i < _min.length; i++)
      {
        _min[i] = (0xFF & raw_min[i]);
        _max[i] = (0xFF & raw_max[i]);
      }
      
      for (int i = 0; i < _min.length; i++)
      {
        if (_min[i] > _max[i])
          throw new IllegalArgumentException("min is greater than max: " + pattern);
        if (_min[i] < _max[i]) {
          break;
        }
      }
    }
    
    public boolean test(InetAddress item, byte[] raw) {
      if (raw.length != _min.length) {
        return false;
      }
      boolean min_ok = false;
      boolean max_ok = false;
      
      for (int i = 0; i < _min.length; i++)
      {
        int r = 0xFF & raw[i];
        if (!min_ok)
        {
          if (r < _min[i])
            return false;
          if (r > _min[i])
            min_ok = true;
        }
        if (!max_ok)
        {
          if (r > _max[i])
            return false;
          if (r < _max[i]) {
            max_ok = true;
          }
        }
        if ((min_ok) && (max_ok)) {
          break;
        }
      }
      return true;
    }
  }
  
  static class CidrInetRange
    extends InetAddressSet.InetPattern
  {
    final byte[] _raw;
    final int _octets;
    final int _mask;
    final int _masked;
    
    public CidrInetRange(String pattern, InetAddress address, int cidr)
    {
      super();
      _raw = address.getAddress();
      _octets = (cidr / 8);
      _mask = (0xFF & 255 << 8 - cidr % 8);
      _masked = (_mask == 0 ? 0 : _raw[_octets] & _mask);
      
      if (cidr > _raw.length * 8) {
        throw new IllegalArgumentException("CIDR too large: " + pattern);
      }
      if ((_mask != 0) && (_raw[_octets] != _masked)) {
        throw new IllegalArgumentException("CIDR bits non zero: " + pattern);
      }
      for (int o = _octets + (_mask == 0 ? 0 : 1); o < _raw.length; o++) {
        if (_raw[o] != 0)
          throw new IllegalArgumentException("CIDR bits non zero: " + pattern);
      }
    }
    
    public boolean test(InetAddress item, byte[] raw) {
      if (raw.length != _raw.length) {
        return false;
      }
      for (int o = 0; o < _octets; o++) {
        if (_raw[o] != raw[o])
          return false;
      }
      if ((_mask != 0) && ((raw[_octets] & _mask) != _masked))
        return false;
      return true;
    }
  }
  
  static class LegacyInetRange extends InetAddressSet.InetPattern
  {
    int[] _min = new int[4];
    int[] _max = new int[4];
    
    public LegacyInetRange(String pattern)
    {
      super();
      
      String[] parts = pattern.split("\\.");
      if (parts.length != 4) {
        throw new IllegalArgumentException("Bad legacy pattern: " + pattern);
      }
      for (int i = 0; i < 4; i++)
      {
        String part = parts[i].trim();
        int dash = part.indexOf('-');
        if (dash < 0) {
          int tmp103_100 = Integer.parseInt(part);_max[i] = tmp103_100;_min[i] = tmp103_100;
        }
        else {
          _min[i] = (dash == 0 ? 0 : StringUtil.toInt(part, 0));
          _max[i] = (dash == part.length() - 1 ? 'ÿ' : StringUtil.toInt(part, dash + 1));
        }
        
        if ((_min[i] < 0) || (_min[i] > _max[i]) || (_max[i] > 255)) {
          throw new IllegalArgumentException("Bad legacy pattern: " + pattern);
        }
      }
    }
    
    public boolean test(InetAddress item, byte[] raw) {
      if (raw.length != 4) {
        return false;
      }
      for (int i = 0; i < 4; i++) {
        if (((0xFF & raw[i]) < _min[i]) || ((0xFF & raw[i]) > _max[i]))
          return false;
      }
      return true;
    }
  }
}
