package org.openqa.selenium.remote;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;




















public class RemoteMouse
  implements Mouse
{
  protected final ExecuteMethod executor;
  
  public RemoteMouse(ExecuteMethod executor)
  {
    this.executor = executor;
  }
  
  protected Map<String, Object> paramsFromCoordinates(Coordinates where)
  {
    Map<String, Object> params = Maps.newHashMap();
    
    if (where != null) {
      String id = (String)where.getAuxiliary();
      params.put("element", id);
    }
    
    return params;
  }
  
  protected void moveIfNeeded(Coordinates where) {
    if (where != null) {
      mouseMove(where);
    }
  }
  
  public void click(Coordinates where) {
    moveIfNeeded(where);
    
    executor.execute("mouseClick", ImmutableMap.of("button", Integer.valueOf(0)));
  }
  
  public void contextClick(Coordinates where) {
    moveIfNeeded(where);
    
    executor.execute("mouseClick", ImmutableMap.of("button", Integer.valueOf(2)));
  }
  
  public void doubleClick(Coordinates where) {
    moveIfNeeded(where);
    
    executor.execute("mouseDoubleClick", ImmutableMap.of());
  }
  
  public void mouseDown(Coordinates where) {
    moveIfNeeded(where);
    
    executor.execute("mouseButtonDown", ImmutableMap.of());
  }
  
  public void mouseUp(Coordinates where) {
    moveIfNeeded(where);
    
    executor.execute("mouseButtonUp", ImmutableMap.of());
  }
  
  public void mouseMove(Coordinates where) {
    Map<String, Object> moveParams = paramsFromCoordinates(where);
    
    executor.execute("mouseMoveTo", moveParams);
  }
  
  public void mouseMove(Coordinates where, long xOffset, long yOffset) {
    Map<String, Object> moveParams = paramsFromCoordinates(where);
    moveParams.put("xoffset", Long.valueOf(xOffset));
    moveParams.put("yoffset", Long.valueOf(yOffset));
    
    executor.execute("mouseMoveTo", moveParams);
  }
}
