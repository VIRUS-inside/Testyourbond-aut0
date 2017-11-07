package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.DataInput;

@GwtIncompatible
public abstract interface ByteArrayDataInput
  extends DataInput
{
  public abstract void readFully(byte[] paramArrayOfByte);
  
  public abstract void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract int skipBytes(int paramInt);
  
  @CanIgnoreReturnValue
  public abstract boolean readBoolean();
  
  @CanIgnoreReturnValue
  public abstract byte readByte();
  
  @CanIgnoreReturnValue
  public abstract int readUnsignedByte();
  
  @CanIgnoreReturnValue
  public abstract short readShort();
  
  @CanIgnoreReturnValue
  public abstract int readUnsignedShort();
  
  @CanIgnoreReturnValue
  public abstract char readChar();
  
  @CanIgnoreReturnValue
  public abstract int readInt();
  
  @CanIgnoreReturnValue
  public abstract long readLong();
  
  @CanIgnoreReturnValue
  public abstract float readFloat();
  
  @CanIgnoreReturnValue
  public abstract double readDouble();
  
  @CanIgnoreReturnValue
  public abstract String readLine();
  
  @CanIgnoreReturnValue
  public abstract String readUTF();
}
