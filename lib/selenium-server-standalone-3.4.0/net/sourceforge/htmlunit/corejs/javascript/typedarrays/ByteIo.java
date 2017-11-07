package net.sourceforge.htmlunit.corejs.javascript.typedarrays;


public class ByteIo
{
  public ByteIo() {}
  

  public static Object readInt8(byte[] buf, int offset)
  {
    return Byte.valueOf(buf[offset]);
  }
  
  public static void writeInt8(byte[] buf, int offset, int val) {
    buf[offset] = ((byte)val);
  }
  
  public static Object readUint8(byte[] buf, int offset) {
    return Integer.valueOf(buf[offset] & 0xFF);
  }
  
  public static void writeUint8(byte[] buf, int offset, int val) {
    buf[offset] = ((byte)(val & 0xFF));
  }
  

  private static short doReadInt16(byte[] buf, int offset, boolean littleEndian)
  {
    if (littleEndian) {
      return (short)(buf[offset] & 0xFF | (buf[(offset + 1)] & 0xFF) << 8);
    }
    
    return (short)((buf[offset] & 0xFF) << 8 | buf[(offset + 1)] & 0xFF);
  }
  
  private static void doWriteInt16(byte[] buf, int offset, int val, boolean littleEndian)
  {
    if (littleEndian) {
      buf[offset] = ((byte)(val & 0xFF));
      buf[(offset + 1)] = ((byte)(val >>> 8 & 0xFF));
    } else {
      buf[offset] = ((byte)(val >>> 8 & 0xFF));
      buf[(offset + 1)] = ((byte)(val & 0xFF));
    }
  }
  
  public static Object readInt16(byte[] buf, int offset, boolean littleEndian)
  {
    return Short.valueOf(doReadInt16(buf, offset, littleEndian));
  }
  
  public static void writeInt16(byte[] buf, int offset, int val, boolean littleEndian)
  {
    doWriteInt16(buf, offset, val, littleEndian);
  }
  
  public static Object readUint16(byte[] buf, int offset, boolean littleEndian)
  {
    return Integer.valueOf(doReadInt16(buf, offset, littleEndian) & 0xFFFF);
  }
  
  public static void writeUint16(byte[] buf, int offset, int val, boolean littleEndian)
  {
    doWriteInt16(buf, offset, val & 0xFFFF, littleEndian);
  }
  
  public static Object readInt32(byte[] buf, int offset, boolean littleEndian)
  {
    if (littleEndian) {
      return Integer.valueOf(buf[offset] & 0xFF | (buf[(offset + 1)] & 0xFF) << 8 | (buf[(offset + 2)] & 0xFF) << 16 | (buf[(offset + 3)] & 0xFF) << 24);
    }
    

    return Integer.valueOf((buf[offset] & 0xFF) << 24 | (buf[(offset + 1)] & 0xFF) << 16 | (buf[(offset + 2)] & 0xFF) << 8 | buf[(offset + 3)] & 0xFF);
  }
  

  public static void writeInt32(byte[] buf, int offset, int val, boolean littleEndian)
  {
    if (littleEndian) {
      buf[offset] = ((byte)(val & 0xFF));
      buf[(offset + 1)] = ((byte)(val >>> 8 & 0xFF));
      buf[(offset + 2)] = ((byte)(val >>> 16 & 0xFF));
      buf[(offset + 3)] = ((byte)(val >>> 24 & 0xFF));
    } else {
      buf[offset] = ((byte)(val >>> 24 & 0xFF));
      buf[(offset + 1)] = ((byte)(val >>> 16 & 0xFF));
      buf[(offset + 2)] = ((byte)(val >>> 8 & 0xFF));
      buf[(offset + 3)] = ((byte)(val & 0xFF));
    }
  }
  
  public static long readUint32Primitive(byte[] buf, int offset, boolean littleEndian)
  {
    if (littleEndian) {
      return (buf[offset] & 0xFF | (buf[(offset + 1)] & 0xFF) << 8 | (buf[(offset + 2)] & 0xFF) << 16 | (buf[(offset + 3)] & 0xFF) << 24) & 0xFFFFFFFF;
    }
    

    return ((buf[offset] & 0xFF) << 24 | (buf[(offset + 1)] & 0xFF) << 16 | (buf[(offset + 2)] & 0xFF) << 8 | buf[(offset + 3)] & 0xFF) & 0xFFFFFFFF;
  }
  



  public static void writeUint32(byte[] buf, int offset, long val, boolean littleEndian)
  {
    if (littleEndian) {
      buf[offset] = ((byte)(int)(val & 0xFF));
      buf[(offset + 1)] = ((byte)(int)(val >>> 8 & 0xFF));
      buf[(offset + 2)] = ((byte)(int)(val >>> 16 & 0xFF));
      buf[(offset + 3)] = ((byte)(int)(val >>> 24 & 0xFF));
    } else {
      buf[offset] = ((byte)(int)(val >>> 24 & 0xFF));
      buf[(offset + 1)] = ((byte)(int)(val >>> 16 & 0xFF));
      buf[(offset + 2)] = ((byte)(int)(val >>> 8 & 0xFF));
      buf[(offset + 3)] = ((byte)(int)(val & 0xFF));
    }
  }
  
  public static Object readUint32(byte[] buf, int offset, boolean littleEndian)
  {
    return Long.valueOf(readUint32Primitive(buf, offset, littleEndian));
  }
  
  public static long readUint64Primitive(byte[] buf, int offset, boolean littleEndian)
  {
    if (littleEndian) {
      return buf[offset] & 0xFF | (buf[(offset + 1)] & 0xFF) << 8 | (buf[(offset + 2)] & 0xFF) << 16 | (buf[(offset + 3)] & 0xFF) << 24 | (buf[(offset + 4)] & 0xFF) << 32 | (buf[(offset + 5)] & 0xFF) << 40 | (buf[(offset + 6)] & 0xFF) << 48 | (buf[(offset + 7)] & 0xFF) << 56;
    }
    





    return (buf[offset] & 0xFF) << 56 | (buf[(offset + 1)] & 0xFF) << 48 | (buf[(offset + 2)] & 0xFF) << 40 | (buf[(offset + 3)] & 0xFF) << 32 | (buf[(offset + 4)] & 0xFF) << 24 | (buf[(offset + 5)] & 0xFF) << 16 | (buf[(offset + 6)] & 0xFF) << 8 | (buf[(offset + 7)] & 0xFF) << 0;
  }
  







  public static void writeUint64(byte[] buf, int offset, long val, boolean littleEndian)
  {
    if (littleEndian) {
      buf[offset] = ((byte)(int)(val & 0xFF));
      buf[(offset + 1)] = ((byte)(int)(val >>> 8 & 0xFF));
      buf[(offset + 2)] = ((byte)(int)(val >>> 16 & 0xFF));
      buf[(offset + 3)] = ((byte)(int)(val >>> 24 & 0xFF));
      buf[(offset + 4)] = ((byte)(int)(val >>> 32 & 0xFF));
      buf[(offset + 5)] = ((byte)(int)(val >>> 40 & 0xFF));
      buf[(offset + 6)] = ((byte)(int)(val >>> 48 & 0xFF));
      buf[(offset + 7)] = ((byte)(int)(val >>> 56 & 0xFF));
    } else {
      buf[offset] = ((byte)(int)(val >>> 56 & 0xFF));
      buf[(offset + 1)] = ((byte)(int)(val >>> 48 & 0xFF));
      buf[(offset + 2)] = ((byte)(int)(val >>> 40 & 0xFF));
      buf[(offset + 3)] = ((byte)(int)(val >>> 32 & 0xFF));
      buf[(offset + 4)] = ((byte)(int)(val >>> 24 & 0xFF));
      buf[(offset + 5)] = ((byte)(int)(val >>> 16 & 0xFF));
      buf[(offset + 6)] = ((byte)(int)(val >>> 8 & 0xFF));
      buf[(offset + 7)] = ((byte)(int)(val & 0xFF));
    }
  }
  
  public static Object readFloat32(byte[] buf, int offset, boolean littleEndian)
  {
    long base = readUint32Primitive(buf, offset, littleEndian);
    return Float.valueOf(Float.intBitsToFloat((int)base));
  }
  
  public static void writeFloat32(byte[] buf, int offset, double val, boolean littleEndian)
  {
    long base = Float.floatToIntBits((float)val);
    writeUint32(buf, offset, base, littleEndian);
  }
  
  public static Object readFloat64(byte[] buf, int offset, boolean littleEndian)
  {
    long base = readUint64Primitive(buf, offset, littleEndian);
    return Double.valueOf(Double.longBitsToDouble(base));
  }
  
  public static void writeFloat64(byte[] buf, int offset, double val, boolean littleEndian)
  {
    long base = Double.doubleToLongBits(val);
    writeUint64(buf, offset, base, littleEndian);
  }
}
