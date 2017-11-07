package org.openqa.selenium;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

























public class DeviceRotation
{
  private int x = 0;
  private int y = 0;
  private int z = 0;
  





  public DeviceRotation(int x, int y, int z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    validateParameters(this.x, this.y, this.z);
  }
  







  public DeviceRotation(Map<String, Number> map)
  {
    if ((map == null) || (!map.containsKey("x")) || (!map.containsKey("y")) || (!map.containsKey("z"))) {
      throw new IllegalArgumentException("Could not initialize DeviceRotation with map given: " + map.toString());
    }
    x = ((Number)map.get("x")).intValue();
    y = ((Number)map.get("y")).intValue();
    z = ((Number)map.get("z")).intValue();
    validateParameters(x, y, z);
  }
  
  private void validateParameters(int x, int y, int z) {
    if ((x < 0) || (y < 0) || (z < 0))
      throw new IllegalArgumentException("DeviceRotation requires positive axis values: \nx = " + x + "\ny = " + y + "\nz = " + z);
    if ((x >= 360) || (y >= 360) || (z >= 360)) {
      throw new IllegalArgumentException("DeviceRotation requires positive axis values under 360: \nx = " + x + "\ny = " + y + "\nz = " + z);
    }
  }
  


  public int getX()
  {
    return x;
  }
  


  public int getY()
  {
    return y;
  }
  


  public int getZ()
  {
    return z;
  }
  


  public Map<String, Integer> parameters()
  {
    HashMap<String, Integer> values = new HashMap();
    values.put("x", Integer.valueOf(x));
    values.put("y", Integer.valueOf(y));
    values.put("z", Integer.valueOf(z));
    return Collections.unmodifiableMap(values);
  }
  

  public boolean equals(Object o)
  {
    if (!(o instanceof DeviceRotation)) {
      return false;
    }
    if (o == this) {
      return true;
    }
    
    DeviceRotation obj = (DeviceRotation)o;
    if ((obj.getX() != getX()) || (obj.getY() != getY()) || (obj.getZ() != getZ())) {
      return false;
    }
    return true;
  }
}
