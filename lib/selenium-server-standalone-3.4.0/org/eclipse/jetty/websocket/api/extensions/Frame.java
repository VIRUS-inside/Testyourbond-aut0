package org.eclipse.jetty.websocket.api.extensions;

import java.nio.ByteBuffer;

public abstract interface Frame { public abstract byte[] getMask();
  
  public abstract byte getOpCode();
  
  public abstract ByteBuffer getPayload();
  
  public abstract int getPayloadLength();
  
  public abstract Type getType();
  
  public abstract boolean hasPayload();
  
  public abstract boolean isFin();
  
  @Deprecated
  public abstract boolean isLast();
  
  public abstract boolean isMasked();
  
  public abstract boolean isRsv1();
  
  public abstract boolean isRsv2();
  
  public abstract boolean isRsv3();
  
  public static enum Type { CONTINUATION((byte)0), 
    TEXT((byte)1), 
    BINARY((byte)2), 
    CLOSE((byte)8), 
    PING((byte)9), 
    PONG((byte)10);
    
    private byte opcode;
    
    public static Type from(byte op) { for (Type type : )
      {
        if (opcode == op)
        {
          return type;
        }
      }
      throw new IllegalArgumentException("OpCode " + op + " is not a valid Frame.Type");
    }
    


    private Type(byte code)
    {
      opcode = code;
    }
    
    public byte getOpCode()
    {
      return opcode;
    }
    
    public boolean isControl()
    {
      return opcode >= CLOSE.getOpCode();
    }
    
    public boolean isData()
    {
      return (opcode == TEXT.getOpCode() ? 1 : 0) | (opcode == BINARY.getOpCode() ? 1 : 0);
    }
    
    public boolean isContinuation()
    {
      return opcode == CONTINUATION.getOpCode();
    }
    

    public String toString()
    {
      return name();
    }
  }
}
