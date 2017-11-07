package com.sun.jna;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;


























public class Pointer
{
  public static final int SIZE;
  
  static
  {
    if ((Pointer.SIZE = Native.POINTER_SIZE) == 0) {
      throw new Error("Native library not initialized");
    }
  }
  

  public static final Pointer NULL = null;
  protected long peer;
  
  public static final Pointer createConstant(long peer) {
    return new Opaque(peer, null);
  }
  



  public static final Pointer createConstant(int peer)
  {
    return new Opaque(peer & 0xFFFFFFFFFFFFFFFF, null);
  }
  









  public Pointer(long peer)
  {
    this.peer = peer;
  }
  
  public Pointer share(long offset)
  {
    return share(offset, 0L);
  }
  


  public Pointer share(long offset, long sz)
  {
    if (offset == 0L) return this;
    return new Pointer(peer + offset);
  }
  
  public void clear(long size)
  {
    setMemory(0L, size, (byte)0);
  }
  








  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (o == null) return false;
    return ((o instanceof Pointer)) && (peer == peer);
  }
  





  public int hashCode()
  {
    return (int)((peer >>> 32) + (peer & 0xFFFFFFFFFFFFFFFF));
  }
  







  public long indexOf(long offset, byte value)
  {
    return Native.indexOf(peer + offset, value);
  }
  








  public void read(long offset, byte[] buf, int index, int length)
  {
    Native.read(peer + offset, buf, index, length);
  }
  








  public void read(long offset, short[] buf, int index, int length)
  {
    Native.read(peer + offset, buf, index, length);
  }
  








  public void read(long offset, char[] buf, int index, int length)
  {
    Native.read(peer + offset, buf, index, length);
  }
  








  public void read(long offset, int[] buf, int index, int length)
  {
    Native.read(peer + offset, buf, index, length);
  }
  








  public void read(long offset, long[] buf, int index, int length)
  {
    Native.read(peer + offset, buf, index, length);
  }
  








  public void read(long offset, float[] buf, int index, int length)
  {
    Native.read(peer + offset, buf, index, length);
  }
  








  public void read(long offset, double[] buf, int index, int length)
  {
    Native.read(peer + offset, buf, index, length);
  }
  








  public void read(long offset, Pointer[] buf, int index, int length)
  {
    for (int i = 0; i < length; i++) {
      Pointer p = getPointer(offset + i * SIZE);
      Pointer oldp = buf[(i + index)];
      
      if ((oldp == null) || (p == null) || (peer != peer)) {
        buf[(i + index)] = p;
      }
    }
  }
  














  public void write(long offset, byte[] buf, int index, int length)
  {
    Native.write(peer + offset, buf, index, length);
  }
  









  public void write(long offset, short[] buf, int index, int length)
  {
    Native.write(peer + offset, buf, index, length);
  }
  









  public void write(long offset, char[] buf, int index, int length)
  {
    Native.write(peer + offset, buf, index, length);
  }
  









  public void write(long offset, int[] buf, int index, int length)
  {
    Native.write(peer + offset, buf, index, length);
  }
  









  public void write(long offset, long[] buf, int index, int length)
  {
    Native.write(peer + offset, buf, index, length);
  }
  









  public void write(long offset, float[] buf, int index, int length)
  {
    Native.write(peer + offset, buf, index, length);
  }
  









  public void write(long offset, double[] buf, int index, int length)
  {
    Native.write(peer + offset, buf, index, length);
  }
  






  public void write(long bOff, Pointer[] buf, int index, int length)
  {
    for (int i = 0; i < length; i++) {
      setPointer(bOff + i * SIZE, buf[(index + i)]);
    }
  }
  




  Object getValue(long offset, Class type, Object currentValue)
  {
    Object result = null;
    if (Structure.class.isAssignableFrom(type)) {
      Structure s = (Structure)currentValue;
      if (Structure.ByReference.class.isAssignableFrom(type)) {
        s = Structure.updateStructureByReference(type, s, getPointer(offset));
      }
      else {
        s.useMemory(this, (int)offset, true);
        s.read();
      }
      result = s;
    }
    else if ((type == Boolean.TYPE) || (type == Boolean.class)) {
      result = Function.valueOf(getInt(offset) != 0);
    }
    else if ((type == Byte.TYPE) || (type == Byte.class)) {
      result = new Byte(getByte(offset));
    }
    else if ((type == Short.TYPE) || (type == Short.class)) {
      result = new Short(getShort(offset));
    }
    else if ((type == Character.TYPE) || (type == Character.class)) {
      result = new Character(getChar(offset));
    }
    else if ((type == Integer.TYPE) || (type == Integer.class)) {
      result = new Integer(getInt(offset));
    }
    else if ((type == Long.TYPE) || (type == Long.class)) {
      result = new Long(getLong(offset));
    }
    else if ((type == Float.TYPE) || (type == Float.class)) {
      result = new Float(getFloat(offset));
    }
    else if ((type == Double.TYPE) || (type == Double.class)) {
      result = new Double(getDouble(offset));
    }
    else if (Pointer.class.isAssignableFrom(type)) {
      Pointer p = getPointer(offset);
      if (p != null) {
        Pointer oldp = (currentValue instanceof Pointer) ? (Pointer)currentValue : null;
        
        if ((oldp == null) || (peer != peer)) {
          result = p;
        } else {
          result = oldp;
        }
      }
    } else if (type == String.class) {
      Pointer p = getPointer(offset);
      result = p != null ? p.getString(0L) : null;
    }
    else if (type == WString.class) {
      Pointer p = getPointer(offset);
      result = p != null ? new WString(p.getWideString(0L)) : null;
    }
    else if (Callback.class.isAssignableFrom(type))
    {

      Pointer fp = getPointer(offset);
      if (fp == null) {
        result = null;
      }
      else {
        Callback cb = (Callback)currentValue;
        Pointer oldfp = CallbackReference.getFunctionPointer(cb);
        if (!fp.equals(oldfp)) {
          cb = CallbackReference.getCallback(type, fp);
        }
        result = cb;
      }
    }
    else if ((Platform.HAS_BUFFERS) && (Buffer.class.isAssignableFrom(type))) {
      Pointer bp = getPointer(offset);
      if (bp == null) {
        result = null;
      }
      else {
        Pointer oldbp = currentValue == null ? null : Native.getDirectBufferPointer((Buffer)currentValue);
        
        if ((oldbp == null) || (!oldbp.equals(bp))) {
          throw new IllegalStateException("Can't autogenerate a direct buffer on memory read");
        }
        result = currentValue;
      }
    }
    else if (NativeMapped.class.isAssignableFrom(type)) {
      NativeMapped nm = (NativeMapped)currentValue;
      if (nm != null) {
        Object value = getValue(offset, nm.nativeType(), null);
        result = nm.fromNative(value, new FromNativeContext(type));
        if (nm.equals(result)) {
          result = nm;
        }
      }
      else {
        NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
        Object value = getValue(offset, tc.nativeType(), null);
        result = tc.fromNative(value, new FromNativeContext(type));
      }
    }
    else if (type.isArray()) {
      result = currentValue;
      if (result == null) {
        throw new IllegalStateException("Need an initialized array");
      }
      readArray(offset, result, type.getComponentType());
    }
    else {
      throw new IllegalArgumentException("Reading \"" + type + "\" from memory is not supported");
    }
    
    return result;
  }
  
  private void readArray(long offset, Object o, Class cls)
  {
    int length = 0;
    length = Array.getLength(o);
    Object result = o;
    
    if (cls == Byte.TYPE) {
      read(offset, (byte[])result, 0, length);
    }
    else if (cls == Short.TYPE) {
      read(offset, (short[])result, 0, length);
    }
    else if (cls == Character.TYPE) {
      read(offset, (char[])result, 0, length);
    }
    else if (cls == Integer.TYPE) {
      read(offset, (int[])result, 0, length);
    }
    else if (cls == Long.TYPE) {
      read(offset, (long[])result, 0, length);
    }
    else if (cls == Float.TYPE) {
      read(offset, (float[])result, 0, length);
    }
    else if (cls == Double.TYPE) {
      read(offset, (double[])result, 0, length);
    }
    else if (Pointer.class.isAssignableFrom(cls)) {
      read(offset, (Pointer[])result, 0, length);
    }
    else if (Structure.class.isAssignableFrom(cls)) {
      Structure[] sarray = (Structure[])result;
      if (Structure.ByReference.class.isAssignableFrom(cls)) {
        Pointer[] parray = getPointerArray(offset, sarray.length);
        for (int i = 0; i < sarray.length; i++) {
          sarray[i] = Structure.updateStructureByReference(cls, sarray[i], parray[i]);
        }
      }
      else {
        Structure first = sarray[0];
        if (first == null) {
          first = Structure.newInstance(cls, share(offset));
          first.conditionalAutoRead();
          sarray[0] = first;
        }
        else {
          first.useMemory(this, (int)offset, true);
          first.read();
        }
        Structure[] tmp = first.toArray(sarray.length);
        for (int i = 1; i < sarray.length; i++) {
          if (sarray[i] == null)
          {
            sarray[i] = tmp[i];
          }
          else {
            sarray[i].useMemory(this, (int)(offset + i * sarray[i].size()), true);
            sarray[i].read();
          }
        }
      }
    }
    else if (NativeMapped.class.isAssignableFrom(cls)) {
      NativeMapped[] array = (NativeMapped[])result;
      NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
      int size = Native.getNativeSize(result.getClass(), result) / array.length;
      for (int i = 0; i < array.length; i++) {
        Object value = getValue(offset + size * i, tc.nativeType(), array[i]);
        array[i] = ((NativeMapped)tc.fromNative(value, new FromNativeContext(cls)));
      }
    }
    else {
      throw new IllegalArgumentException("Reading array of " + cls + " from memory not supported");
    }
  }
  









  public byte getByte(long offset)
  {
    return Native.getByte(peer + offset);
  }
  







  public char getChar(long offset)
  {
    return Native.getChar(peer + offset);
  }
  







  public short getShort(long offset)
  {
    return Native.getShort(peer + offset);
  }
  







  public int getInt(long offset)
  {
    return Native.getInt(peer + offset);
  }
  







  public long getLong(long offset)
  {
    return Native.getLong(peer + offset);
  }
  







  public NativeLong getNativeLong(long offset)
  {
    return new NativeLong(NativeLong.SIZE == 8 ? getLong(offset) : getInt(offset));
  }
  







  public float getFloat(long offset)
  {
    return Native.getFloat(peer + offset);
  }
  







  public double getDouble(long offset)
  {
    return Native.getDouble(peer + offset);
  }
  









  public Pointer getPointer(long offset)
  {
    return Native.getPointer(peer + offset);
  }
  







  public ByteBuffer getByteBuffer(long offset, long length)
  {
    return Native.getDirectByteBuffer(peer + offset, length).order(ByteOrder.nativeOrder());
  }
  








  /**
   * @deprecated
   */
  public String getString(long offset, boolean wide)
  {
    return wide ? getWideString(offset) : getString(offset);
  }
  
  public String getWideString(long offset)
  {
    return Native.getWideString(peer + offset);
  }
  






  public String getString(long offset)
  {
    return getString(offset, Native.getDefaultStringEncoding());
  }
  






  public String getString(long offset, String encoding)
  {
    return Native.getString(peer + offset, encoding);
  }
  


  public byte[] getByteArray(long offset, int arraySize)
  {
    byte[] buf = new byte[arraySize];
    read(offset, buf, 0, arraySize);
    return buf;
  }
  


  public char[] getCharArray(long offset, int arraySize)
  {
    char[] buf = new char[arraySize];
    read(offset, buf, 0, arraySize);
    return buf;
  }
  


  public short[] getShortArray(long offset, int arraySize)
  {
    short[] buf = new short[arraySize];
    read(offset, buf, 0, arraySize);
    return buf;
  }
  


  public int[] getIntArray(long offset, int arraySize)
  {
    int[] buf = new int[arraySize];
    read(offset, buf, 0, arraySize);
    return buf;
  }
  


  public long[] getLongArray(long offset, int arraySize)
  {
    long[] buf = new long[arraySize];
    read(offset, buf, 0, arraySize);
    return buf;
  }
  


  public float[] getFloatArray(long offset, int arraySize)
  {
    float[] buf = new float[arraySize];
    read(offset, buf, 0, arraySize);
    return buf;
  }
  


  public double[] getDoubleArray(long offset, int arraySize)
  {
    double[] buf = new double[arraySize];
    read(offset, buf, 0, arraySize);
    return buf;
  }
  


  public Pointer[] getPointerArray(long offset)
  {
    List array = new ArrayList();
    int addOffset = 0;
    Pointer p = getPointer(offset);
    while (p != null) {
      array.add(p);
      addOffset += SIZE;
      p = getPointer(offset + addOffset);
    }
    return (Pointer[])array.toArray(new Pointer[array.size()]);
  }
  
  public Pointer[] getPointerArray(long offset, int arraySize)
  {
    Pointer[] buf = new Pointer[arraySize];
    read(offset, buf, 0, arraySize);
    return buf;
  }
  






  public String[] getStringArray(long offset)
  {
    return getStringArray(offset, -1, Native.getDefaultStringEncoding());
  }
  



  public String[] getStringArray(long offset, String encoding)
  {
    return getStringArray(offset, -1, encoding);
  }
  





  public String[] getStringArray(long offset, int length)
  {
    return getStringArray(offset, length, Native.getDefaultStringEncoding());
  }
  




  /**
   * @deprecated
   */
  public String[] getStringArray(long offset, boolean wide)
  {
    return getStringArray(offset, -1, wide);
  }
  
  public String[] getWideStringArray(long offset) {
    return getWideStringArray(offset, -1);
  }
  
  public String[] getWideStringArray(long offset, int length) {
    return getStringArray(offset, -1, "--WIDE-STRING--");
  }
  



  /**
   * @deprecated
   */
  public String[] getStringArray(long offset, int length, boolean wide)
  {
    return getStringArray(offset, length, wide ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
  }
  






  public String[] getStringArray(long offset, int length, String encoding)
  {
    List strings = new ArrayList();
    
    int addOffset = 0;
    if (length != -1) {
      Pointer p = getPointer(offset + addOffset);
      int count = 0;
      while (count++ < length) {
        String s = encoding == "--WIDE-STRING--" ? p.getWideString(0L) : p == null ? null : p.getString(0L, encoding);
        


        strings.add(s);
        if (count < length) {
          addOffset += SIZE;
          p = getPointer(offset + addOffset);
        }
      }
    } else {
      Pointer p;
      while ((p = getPointer(offset + addOffset)) != null) {
        String s = encoding == "--WIDE-STRING--" ? p.getWideString(0L) : p == null ? null : p.getString(0L, encoding);
        


        strings.add(s);
        addOffset += SIZE;
      }
    }
    return (String[])strings.toArray(new String[strings.size()]);
  }
  





  void setValue(long offset, Object value, Class type)
  {
    if ((type == Boolean.TYPE) || (type == Boolean.class)) {
      setInt(offset, Boolean.TRUE.equals(value) ? -1 : 0);
    }
    else if ((type == Byte.TYPE) || (type == Byte.class)) {
      setByte(offset, value == null ? 0 : ((Byte)value).byteValue());
    }
    else if ((type == Short.TYPE) || (type == Short.class)) {
      setShort(offset, value == null ? 0 : ((Short)value).shortValue());
    }
    else if ((type == Character.TYPE) || (type == Character.class)) {
      setChar(offset, value == null ? '\000' : ((Character)value).charValue());
    }
    else if ((type == Integer.TYPE) || (type == Integer.class)) {
      setInt(offset, value == null ? 0 : ((Integer)value).intValue());
    }
    else if ((type == Long.TYPE) || (type == Long.class)) {
      setLong(offset, value == null ? 0L : ((Long)value).longValue());
    }
    else if ((type == Float.TYPE) || (type == Float.class)) {
      setFloat(offset, value == null ? 0.0F : ((Float)value).floatValue());
    }
    else if ((type == Double.TYPE) || (type == Double.class)) {
      setDouble(offset, value == null ? 0.0D : ((Double)value).doubleValue());
    }
    else if (type == Pointer.class) {
      setPointer(offset, (Pointer)value);
    }
    else if (type == String.class) {
      setPointer(offset, (Pointer)value);
    }
    else if (type == WString.class) {
      setPointer(offset, (Pointer)value);
    }
    else if (Structure.class.isAssignableFrom(type)) {
      Structure s = (Structure)value;
      if (Structure.ByReference.class.isAssignableFrom(type)) {
        setPointer(offset, s == null ? null : s.getPointer());
        if (s != null) {
          s.autoWrite();
        }
      }
      else {
        s.useMemory(this, (int)offset, true);
        s.write();
      }
    }
    else if (Callback.class.isAssignableFrom(type)) {
      setPointer(offset, CallbackReference.getFunctionPointer((Callback)value));
    }
    else if ((Platform.HAS_BUFFERS) && (Buffer.class.isAssignableFrom(type))) {
      Pointer p = value == null ? null : Native.getDirectBufferPointer((Buffer)value);
      
      setPointer(offset, p);
    }
    else if (NativeMapped.class.isAssignableFrom(type)) {
      NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
      Class nativeType = tc.nativeType();
      setValue(offset, tc.toNative(value, new ToNativeContext()), nativeType);
    }
    else if (type.isArray()) {
      writeArray(offset, value, type.getComponentType());
    }
    else {
      throw new IllegalArgumentException("Writing " + type + " to memory is not supported");
    }
  }
  
  private void writeArray(long offset, Object value, Class cls)
  {
    if (cls == Byte.TYPE) {
      byte[] buf = (byte[])value;
      write(offset, buf, 0, buf.length);
    }
    else if (cls == Short.TYPE) {
      short[] buf = (short[])value;
      write(offset, buf, 0, buf.length);
    }
    else if (cls == Character.TYPE) {
      char[] buf = (char[])value;
      write(offset, buf, 0, buf.length);
    }
    else if (cls == Integer.TYPE) {
      int[] buf = (int[])value;
      write(offset, buf, 0, buf.length);
    }
    else if (cls == Long.TYPE) {
      long[] buf = (long[])value;
      write(offset, buf, 0, buf.length);
    }
    else if (cls == Float.TYPE) {
      float[] buf = (float[])value;
      write(offset, buf, 0, buf.length);
    }
    else if (cls == Double.TYPE) {
      double[] buf = (double[])value;
      write(offset, buf, 0, buf.length);
    }
    else if (Pointer.class.isAssignableFrom(cls)) {
      Pointer[] buf = (Pointer[])value;
      write(offset, buf, 0, buf.length);
    }
    else if (Structure.class.isAssignableFrom(cls)) {
      Structure[] sbuf = (Structure[])value;
      if (Structure.ByReference.class.isAssignableFrom(cls)) {
        Pointer[] buf = new Pointer[sbuf.length];
        for (int i = 0; i < sbuf.length; i++) {
          if (sbuf[i] == null) {
            buf[i] = null;
          }
          else {
            buf[i] = sbuf[i].getPointer();
            sbuf[i].write();
          }
        }
        write(offset, buf, 0, buf.length);
      }
      else {
        Structure first = sbuf[0];
        if (first == null) {
          first = Structure.newInstance(cls, share(offset));
          sbuf[0] = first;
        }
        else {
          first.useMemory(this, (int)offset, true);
        }
        first.write();
        Structure[] tmp = first.toArray(sbuf.length);
        for (int i = 1; i < sbuf.length; i++) {
          if (sbuf[i] == null) {
            sbuf[i] = tmp[i];
          }
          else {
            sbuf[i].useMemory(this, (int)(offset + i * sbuf[i].size()), true);
          }
          sbuf[i].write();
        }
      }
    }
    else if (NativeMapped.class.isAssignableFrom(cls)) {
      NativeMapped[] buf = (NativeMapped[])value;
      NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
      Class nativeType = tc.nativeType();
      int size = Native.getNativeSize(value.getClass(), value) / buf.length;
      for (int i = 0; i < buf.length; i++) {
        Object element = tc.toNative(buf[i], new ToNativeContext());
        setValue(offset + i * size, element, nativeType);
      }
    }
    else {
      throw new IllegalArgumentException("Writing array of " + cls + " to memory not supported");
    }
  }
  





  public void setMemory(long offset, long length, byte value)
  {
    Native.setMemory(peer + offset, length, value);
  }
  








  public void setByte(long offset, byte value)
  {
    Native.setByte(peer + offset, value);
  }
  








  public void setShort(long offset, short value)
  {
    Native.setShort(peer + offset, value);
  }
  








  public void setChar(long offset, char value)
  {
    Native.setChar(peer + offset, value);
  }
  








  public void setInt(long offset, int value)
  {
    Native.setInt(peer + offset, value);
  }
  








  public void setLong(long offset, long value)
  {
    Native.setLong(peer + offset, value);
  }
  








  public void setNativeLong(long offset, NativeLong value)
  {
    if (NativeLong.SIZE == 8) {
      setLong(offset, value.longValue());
    } else {
      setInt(offset, value.intValue());
    }
  }
  








  public void setFloat(long offset, float value)
  {
    Native.setFloat(peer + offset, value);
  }
  








  public void setDouble(long offset, double value)
  {
    Native.setDouble(peer + offset, value);
  }
  










  public void setPointer(long offset, Pointer value)
  {
    Native.setPointer(peer + offset, value != null ? peer : 0L);
  }
  









  /**
   * @deprecated
   */
  public void setString(long offset, String value, boolean wide)
  {
    if (wide) {
      setWideString(offset, value);
    }
    else {
      setString(offset, value);
    }
  }
  







  public void setWideString(long offset, String value)
  {
    Native.setWideString(peer + offset, value);
  }
  







  public void setString(long offset, WString value)
  {
    setWideString(offset, value == null ? null : value.toString());
  }
  








  public void setString(long offset, String value)
  {
    setString(offset, value, Native.getDefaultStringEncoding());
  }
  








  public void setString(long offset, String value, String encoding)
  {
    byte[] data = Native.getBytes(value, encoding);
    write(offset, data, 0, data.length);
    setByte(offset + data.length, (byte)0);
  }
  
  public String dump(long offset, int size)
  {
    String LS = System.getProperty("line.separator");
    String contents = "memory dump" + LS;
    int BYTES_PER_ROW = 4;
    byte[] buf = getByteArray(offset, size);
    for (int i = 0; i < buf.length; i++) {
      if (i % 4 == 0) contents = contents + "[";
      if ((buf[i] >= 0) && (buf[i] < 16))
        contents = contents + "0";
      contents = contents + Integer.toHexString(buf[i] & 0xFF);
      if ((i % 4 == 3) && (i < buf.length - 1))
        contents = contents + "]" + LS;
    }
    if (!contents.endsWith("]" + LS)) {
      contents = contents + "]" + LS;
    }
    return contents;
  }
  
  public String toString() {
    return "native@0x" + Long.toHexString(peer);
  }
  
  public static long nativeValue(Pointer p)
  {
    return p == null ? 0L : peer;
  }
  


  public static void nativeValue(Pointer p, long value) { peer = value; }
  
  Pointer() {}
  
  private static class Opaque extends Pointer {
    private Opaque(long peer) { super(); }
    private final String MSG = "This pointer is opaque: " + this;
    
    public Pointer share(long offset, long size) { throw new UnsupportedOperationException(MSG); }
    
    public void clear(long size) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public long indexOf(long offset, byte value) { throw new UnsupportedOperationException(MSG); }
    
    public void read(long bOff, byte[] buf, int index, int length) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void read(long bOff, char[] buf, int index, int length) { throw new UnsupportedOperationException(MSG); }
    
    public void read(long bOff, short[] buf, int index, int length) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void read(long bOff, int[] buf, int index, int length) { throw new UnsupportedOperationException(MSG); }
    
    public void read(long bOff, long[] buf, int index, int length) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void read(long bOff, float[] buf, int index, int length) { throw new UnsupportedOperationException(MSG); }
    
    public void read(long bOff, double[] buf, int index, int length) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void read(long bOff, Pointer[] buf, int index, int length) { throw new UnsupportedOperationException(MSG); }
    
    public void write(long bOff, byte[] buf, int index, int length) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void write(long bOff, char[] buf, int index, int length) { throw new UnsupportedOperationException(MSG); }
    
    public void write(long bOff, short[] buf, int index, int length) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void write(long bOff, int[] buf, int index, int length) { throw new UnsupportedOperationException(MSG); }
    
    public void write(long bOff, long[] buf, int index, int length) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void write(long bOff, float[] buf, int index, int length) { throw new UnsupportedOperationException(MSG); }
    
    public void write(long bOff, double[] buf, int index, int length) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void write(long bOff, Pointer[] buf, int index, int length) { throw new UnsupportedOperationException(MSG); }
    
    public ByteBuffer getByteBuffer(long offset, long length) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public byte getByte(long bOff) { throw new UnsupportedOperationException(MSG); }
    
    public char getChar(long bOff) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public short getShort(long bOff) { throw new UnsupportedOperationException(MSG); }
    
    public int getInt(long bOff) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public long getLong(long bOff) { throw new UnsupportedOperationException(MSG); }
    
    public float getFloat(long bOff) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public double getDouble(long bOff) { throw new UnsupportedOperationException(MSG); }
    
    public Pointer getPointer(long bOff) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public String getString(long bOff, String encoding) { throw new UnsupportedOperationException(MSG); }
    
    public String getWideString(long bOff) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void setByte(long bOff, byte value) { throw new UnsupportedOperationException(MSG); }
    
    public void setChar(long bOff, char value) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void setShort(long bOff, short value) { throw new UnsupportedOperationException(MSG); }
    
    public void setInt(long bOff, int value) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void setLong(long bOff, long value) { throw new UnsupportedOperationException(MSG); }
    
    public void setFloat(long bOff, float value) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void setDouble(long bOff, double value) { throw new UnsupportedOperationException(MSG); }
    
    public void setPointer(long offset, Pointer value) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void setString(long offset, String value, String encoding) { throw new UnsupportedOperationException(MSG); }
    
    public void setWideString(long offset, String value) {
      throw new UnsupportedOperationException(MSG);
    }
    
    public void setMemory(long offset, long size, byte value) { throw new UnsupportedOperationException(MSG); }
    
    public String toString() {
      return "const@0x" + Long.toHexString(peer);
    }
  }
}
