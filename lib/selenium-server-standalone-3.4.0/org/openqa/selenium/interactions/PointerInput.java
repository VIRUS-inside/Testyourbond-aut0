package org.openqa.selenium.interactions;

import com.google.common.base.Preconditions;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.openqa.selenium.WebElement;






















public class PointerInput
  implements InputSource, Encodable
{
  private final Kind kind;
  private final String name;
  
  public PointerInput(Kind kind, String name)
  {
    this.kind = ((Kind)Preconditions.checkNotNull(kind, "Must set kind of pointer device"));
    this.name = ((String)Optional.ofNullable(name).orElse(UUID.randomUUID().toString()));
  }
  
  public SourceType getInputType()
  {
    return SourceType.POINTER;
  }
  
  public Map<String, Object> encode()
  {
    Map<String, Object> toReturn = new HashMap();
    
    toReturn.put("type", "pointer");
    toReturn.put("id", name);
    
    Map<String, Object> parameters = new HashMap();
    parameters.put("pointerType", kind.getWireName());
    toReturn.put("parameters", parameters);
    
    return toReturn;
  }
  
  public Interaction createPointerMove(Duration duration, Origin origin, int x, int y) {
    return new Move(this, duration, origin, x, y);
  }
  
  public Interaction createPointerDown(int button) {
    return new PointerPress(this, PointerInput.PointerPress.Direction.DOWN, button);
  }
  
  public Interaction createPointerUp(int button) {
    return new PointerPress(this, PointerInput.PointerPress.Direction.UP, button);
  }
  
  private static class PointerPress extends Interaction implements Encodable
  {
    private final Direction direction;
    private final int button;
    
    public PointerPress(InputSource source, Direction direction, int button) {
      super();
      
      Preconditions.checkState(button >= 0, "Button must be greater than or equal to 0: %d", button);
      

      this.direction = ((Direction)Preconditions.checkNotNull(direction));
      this.button = button;
    }
    
    public Map<String, Object> encode()
    {
      Map<String, Object> toReturn = new HashMap();
      
      toReturn.put("type", direction.getType());
      toReturn.put("button", Integer.valueOf(button));
      
      return toReturn;
    }
    
    static enum Direction {
      DOWN("pointerDown"), 
      UP("pointerUp");
      
      private final String type;
      
      private Direction(String type) {
        this.type = type;
      }
      
      public String getType() {
        return type;
      }
    }
  }
  

  private static class Move
    extends Interaction
    implements Encodable
  {
    private final PointerInput.Origin origin;
    
    private final int x;
    private final int y;
    private final Duration duration;
    
    protected Move(InputSource source, Duration duration, PointerInput.Origin origin, int x, int y)
    {
      super();
      
      Preconditions.checkState(
        !duration.isNegative(), "Duration value must be 0 or greater: %s", duration);
      


      this.origin = ((PointerInput.Origin)Preconditions.checkNotNull(origin, "Origin of move must be set"));
      this.x = x;
      this.y = y;
      this.duration = duration;
    }
    
    protected boolean isValidFor(SourceType sourceType)
    {
      return SourceType.POINTER == sourceType;
    }
    
    public Map<String, Object> encode()
    {
      Map<String, Object> toReturn = new HashMap();
      
      toReturn.put("type", "pointerMove");
      toReturn.put("duration", Long.valueOf(duration.toMillis()));
      toReturn.put("origin", origin.asArg());
      
      toReturn.put("x", Integer.valueOf(x));
      toReturn.put("y", Integer.valueOf(y));
      
      return toReturn;
    }
  }
  
  public static enum Kind {
    MOUSE("mouse"), 
    PEN("pen"), 
    TOUCH("touch");
    
    private final String wireName;
    
    private Kind(String pointerSubType)
    {
      wireName = pointerSubType;
    }
    
    public String getWireName() {
      return wireName;
    }
  }
  
  public static enum MouseButton {
    LEFT(0), 
    MIDDLE(1), 
    RIGHT(2);
    
    private final int button;
    
    private MouseButton(int button)
    {
      this.button = button;
    }
    
    public int asArg() {
      return button;
    }
  }
  
  public static final class Origin {
    private final Object originObject;
    
    public Object asArg() {
      return originObject;
    }
    
    private Origin(Object originObject) {
      this.originObject = originObject;
    }
    
    public static Origin pointer() {
      return new Origin("pointer");
    }
    
    public static Origin viewport() {
      return new Origin("viewport");
    }
    
    public static Origin fromElement(WebElement element) {
      return new Origin(Preconditions.checkNotNull(element));
    }
  }
}
