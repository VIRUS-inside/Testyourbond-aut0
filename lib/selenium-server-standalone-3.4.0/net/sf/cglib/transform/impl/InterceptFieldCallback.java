package net.sf.cglib.transform.impl;

public abstract interface InterceptFieldCallback
{
  public abstract int writeInt(Object paramObject, String paramString, int paramInt1, int paramInt2);
  
  public abstract char writeChar(Object paramObject, String paramString, char paramChar1, char paramChar2);
  
  public abstract byte writeByte(Object paramObject, String paramString, byte paramByte1, byte paramByte2);
  
  public abstract boolean writeBoolean(Object paramObject, String paramString, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract short writeShort(Object paramObject, String paramString, short paramShort1, short paramShort2);
  
  public abstract float writeFloat(Object paramObject, String paramString, float paramFloat1, float paramFloat2);
  
  public abstract double writeDouble(Object paramObject, String paramString, double paramDouble1, double paramDouble2);
  
  public abstract long writeLong(Object paramObject, String paramString, long paramLong1, long paramLong2);
  
  public abstract Object writeObject(Object paramObject1, String paramString, Object paramObject2, Object paramObject3);
  
  public abstract int readInt(Object paramObject, String paramString, int paramInt);
  
  public abstract char readChar(Object paramObject, String paramString, char paramChar);
  
  public abstract byte readByte(Object paramObject, String paramString, byte paramByte);
  
  public abstract boolean readBoolean(Object paramObject, String paramString, boolean paramBoolean);
  
  public abstract short readShort(Object paramObject, String paramString, short paramShort);
  
  public abstract float readFloat(Object paramObject, String paramString, float paramFloat);
  
  public abstract double readDouble(Object paramObject, String paramString, double paramDouble);
  
  public abstract long readLong(Object paramObject, String paramString, long paramLong);
  
  public abstract Object readObject(Object paramObject1, String paramString, Object paramObject2);
}
