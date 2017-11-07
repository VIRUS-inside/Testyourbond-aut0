package org.seleniumhq.jetty9.util;

import java.nio.ByteBuffer;























public class Utf8LineParser
{
  private State state;
  private Utf8StringBuilder utf;
  
  private static enum State
  {
    START, 
    PARSE, 
    END;
    

    private State() {}
  }
  
  public Utf8LineParser()
  {
    state = State.START;
  }
  











  public String parse(ByteBuffer buf)
  {
    while (buf.remaining() > 0)
    {
      byte b = buf.get();
      if (parseByte(b))
      {
        state = State.START;
        return utf.toString();
      }
    }
    
    return null;
  }
  
  private boolean parseByte(byte b)
  {
    switch (1.$SwitchMap$org$eclipse$jetty$util$Utf8LineParser$State[state.ordinal()])
    {
    case 1: 
      utf = new Utf8StringBuilder();
      state = State.PARSE;
      return parseByte(b);
    
    case 2: 
      if ((utf.isUtf8SequenceComplete()) && ((b == 13) || (b == 10)))
      {
        state = State.END;
        return parseByte(b);
      }
      utf.append(b);
      break;
    case 3: 
      if (b == 10)
      {

        state = State.START;
        return true;
      }
      break;
    }
    return false;
  }
}
