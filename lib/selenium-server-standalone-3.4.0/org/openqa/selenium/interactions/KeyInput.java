package org.openqa.selenium.interactions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;



















public class KeyInput
  implements InputSource, Encodable
{
  private final String name;
  
  public KeyInput(String name)
  {
    this.name = ((String)Optional.ofNullable(name).orElse(UUID.randomUUID().toString()));
  }
  
  public SourceType getInputType()
  {
    return SourceType.KEY;
  }
  
  public Interaction createKeyDown(int codePoint) {
    return new TypingInteraction(this, "keyDown", codePoint);
  }
  
  public Interaction createKeyUp(int codePoint) {
    return new TypingInteraction(this, "keyUp", codePoint);
  }
  
  public Map<String, Object> encode()
  {
    Map<String, Object> toReturn = new HashMap();
    
    toReturn.put("type", "key");
    toReturn.put("id", name);
    
    return toReturn;
  }
  
  private static class TypingInteraction extends Interaction implements Encodable
  {
    private final String type;
    private final String value;
    
    TypingInteraction(InputSource source, String type, int codePoint) {
      super();
      
      this.type = type;
      value = new StringBuilder().appendCodePoint(codePoint).toString();
    }
    
    public Map<String, Object> encode()
    {
      HashMap<String, Object> toReturn = new HashMap();
      
      toReturn.put("type", type);
      toReturn.put("value", value);
      
      return toReturn;
    }
  }
}
