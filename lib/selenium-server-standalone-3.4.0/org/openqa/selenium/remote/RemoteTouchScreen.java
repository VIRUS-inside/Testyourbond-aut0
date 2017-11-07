package org.openqa.selenium.remote;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.internal.Coordinates;

















public class RemoteTouchScreen
  implements TouchScreen
{
  private final ExecuteMethod executeMethod;
  
  public RemoteTouchScreen(ExecuteMethod executeMethod)
  {
    this.executeMethod = executeMethod;
  }
  
  public void singleTap(Coordinates where) {
    Map<String, Object> singleTapParams = CoordinatesUtils.paramsFromCoordinates(where);
    executeMethod.execute("touchSingleTap", singleTapParams);
  }
  
  public void down(int x, int y) {
    Map<String, Object> downParams = new HashMap();
    downParams.put("x", Integer.valueOf(x));
    downParams.put("y", Integer.valueOf(y));
    executeMethod.execute("touchDown", downParams);
  }
  
  public void up(int x, int y) {
    Map<String, Object> upParams = new HashMap();
    upParams.put("x", Integer.valueOf(x));
    upParams.put("y", Integer.valueOf(y));
    executeMethod.execute("touchUp", upParams);
  }
  
  public void move(int x, int y) {
    Map<String, Object> moveParams = new HashMap();
    moveParams.put("x", Integer.valueOf(x));
    moveParams.put("y", Integer.valueOf(y));
    executeMethod.execute("touchMove", moveParams);
  }
  
  public void scroll(Coordinates where, int xOffset, int yOffset) {
    Map<String, Object> scrollParams = CoordinatesUtils.paramsFromCoordinates(where);
    scrollParams.put("xoffset", Integer.valueOf(xOffset));
    scrollParams.put("yoffset", Integer.valueOf(yOffset));
    executeMethod.execute("touchScroll", scrollParams);
  }
  
  public void doubleTap(Coordinates where) {
    Map<String, Object> doubleTapParams = CoordinatesUtils.paramsFromCoordinates(where);
    executeMethod.execute("touchDoubleTap", doubleTapParams);
  }
  
  public void longPress(Coordinates where) {
    Map<String, Object> longPressParams = CoordinatesUtils.paramsFromCoordinates(where);
    executeMethod.execute("touchLongPress", longPressParams);
  }
  
  public void scroll(int xOffset, int yOffset) {
    Map<String, Object> scrollParams = new HashMap();
    scrollParams.put("xoffset", Integer.valueOf(xOffset));
    scrollParams.put("yoffset", Integer.valueOf(yOffset));
    executeMethod.execute("touchScroll", scrollParams);
  }
  
  public void flick(int xSpeed, int ySpeed) {
    Map<String, Object> flickParams = new HashMap();
    flickParams.put("xspeed", Integer.valueOf(xSpeed));
    flickParams.put("yspeed", Integer.valueOf(ySpeed));
    executeMethod.execute("touchFlick", flickParams);
  }
  
  public void flick(Coordinates where, int xOffset, int yOffset, int speed) {
    Map<String, Object> flickParams = CoordinatesUtils.paramsFromCoordinates(where);
    flickParams.put("xoffset", Integer.valueOf(xOffset));
    flickParams.put("yoffset", Integer.valueOf(yOffset));
    flickParams.put("speed", Integer.valueOf(speed));
    executeMethod.execute("touchFlick", flickParams);
  }
}
