package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;













public final class ScalarToken
  extends Token
{
  private final String value;
  private final boolean plain;
  private final char style;
  
  public ScalarToken(String value, Mark startMark, Mark endMark, boolean plain)
  {
    this(value, plain, startMark, endMark, '\000');
  }
  
  public ScalarToken(String value, boolean plain, Mark startMark, Mark endMark, char style) {
    super(startMark, endMark);
    this.value = value;
    this.plain = plain;
    this.style = style;
  }
  
  public boolean getPlain() {
    return plain;
  }
  
  public String getValue() {
    return value;
  }
  
  public char getStyle() {
    return style;
  }
  
  protected String getArguments()
  {
    return "value=" + value + ", plain=" + plain + ", style=" + style;
  }
  
  public Token.ID getTokenId()
  {
    return Token.ID.Scalar;
  }
}
