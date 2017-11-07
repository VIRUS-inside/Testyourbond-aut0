package org.yaml.snakeyaml.tokens;

import java.util.List;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;














public final class DirectiveToken<T>
  extends Token
{
  private final String name;
  private final List<T> value;
  
  public DirectiveToken(String name, List<T> value, Mark startMark, Mark endMark)
  {
    super(startMark, endMark);
    this.name = name;
    if ((value != null) && (value.size() != 2)) {
      throw new YAMLException("Two strings must be provided instead of " + String.valueOf(value.size()));
    }
    
    this.value = value;
  }
  
  public String getName() {
    return name;
  }
  
  public List<T> getValue() {
    return value;
  }
  
  protected String getArguments()
  {
    if (value != null) {
      return "name=" + name + ", value=[" + value.get(0) + ", " + value.get(1) + "]";
    }
    return "name=" + name;
  }
  

  public Token.ID getTokenId()
  {
    return Token.ID.Directive;
  }
}
