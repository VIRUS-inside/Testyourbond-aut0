package org.openqa.selenium.remote;

import com.google.common.collect.Maps;
import java.util.Map;
import org.openqa.selenium.interactions.internal.Coordinates;




















class CoordinatesUtils
{
  CoordinatesUtils() {}
  
  static Map<String, Object> paramsFromCoordinates(Coordinates where)
  {
    Map<String, Object> params = Maps.newHashMap();
    
    if (where != null) {
      String id = (String)where.getAuxiliary();
      params.put("element", id);
    }
    
    return params;
  }
}
