package com.sun.jna;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


































public class Memory
  extends Pointer
{
  private static final Map buffers = Collections.synchronizedMap(Platform.HAS_BUFFERS ? new WeakIdentityHashMap() : new HashMap());
  

  protected long size;
  


  public static void purge()
  {
    buffers.size();
  }
  


  private class SharedMemory
    extends Memory
  {
    public SharedMemory(long offset, long size)
    {
      this.size = size;
      peer = (peer + offset);
    }
    
    protected void dispose() {
      peer = 0L;
    }
    
    protected void boundsCheck(long off, long sz) {
      Memory.this.boundsCheck(peer - peer + off, sz);
    }
    
    public String toString() { return super.toString() + " (shared from " + Memory.this.toString() + ")"; }
  }
  





  public Memory(long size)
  {
    this.size = size;
    if (size <= 0L) {
      throw new IllegalArgumentException("Allocation size must be greater than zero");
    }
    peer = malloc(size);
    if (peer == 0L) {
      throw new OutOfMemoryError("Cannot allocate " + size + " bytes");
    }
  }
  


  protected Memory() {}
  


  public Pointer share(long offset)
  {
    return share(offset, size() - offset);
  }
  






  public Pointer share(long offset, long sz)
  {
    boundsCheck(offset, sz);
    return new SharedMemory(offset, sz);
  }
  







  public Memory align(int byteBoundary)
  {
    if (byteBoundary <= 0) {
      throw new IllegalArgumentException("Byte boundary must be positive: " + byteBoundary);
    }
    for (int i = 0; i < 32; i++) {
      if (byteBoundary == 1 << i) {
        long mask = byteBoundary - 1L ^ 0xFFFFFFFFFFFFFFFF;
        
        if ((peer & mask) != peer) {
          long newPeer = peer + byteBoundary - 1L & mask;
          long newSize = peer + size - newPeer;
          if (newSize <= 0L) {
            throw new IllegalArgumentException("Insufficient memory to align to the requested boundary");
          }
          return (Memory)share(newPeer - peer, newSize);
        }
        return this;
      }
    }
    throw new IllegalArgumentException("Byte boundary must be a power of two");
  }
  
  protected void finalize()
  {
    dispose();
  }
  
  protected void dispose()
  {
    free(peer);
    peer = 0L;
  }
  
  public void clear()
  {
    clear(size);
  }
  
  public boolean valid()
  {
    return peer != 0L;
  }
  
  public long size() {
    return size;
  }
  




  protected void boundsCheck(long off, long sz)
  {
    if (off < 0L) {
      throw new IndexOutOfBoundsException("Invalid offset: " + off);
    }
    if (off + sz > size) {
      String msg = "Bounds exceeds available space : size=" + size + ", offset=" + (off + sz);
      
      throw new IndexOutOfBoundsException(msg);
    }
  }
  











  public void read(long bOff, byte[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 1L);
    super.read(bOff, buf, index, length);
  }
  








  public void read(long bOff, short[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 2L);
    super.read(bOff, buf, index, length);
  }
  








  public void read(long bOff, char[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 2L);
    super.read(bOff, buf, index, length);
  }
  








  public void read(long bOff, int[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 4L);
    super.read(bOff, buf, index, length);
  }
  








  public void read(long bOff, long[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 8L);
    super.read(bOff, buf, index, length);
  }
  








  public void read(long bOff, float[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 4L);
    super.read(bOff, buf, index, length);
  }
  









  public void read(long bOff, double[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 8L);
    super.read(bOff, buf, index, length);
  }
  














  public void write(long bOff, byte[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 1L);
    super.write(bOff, buf, index, length);
  }
  








  public void write(long bOff, short[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 2L);
    super.write(bOff, buf, index, length);
  }
  








  public void write(long bOff, char[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 2L);
    super.write(bOff, buf, index, length);
  }
  








  public void write(long bOff, int[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 4L);
    super.write(bOff, buf, index, length);
  }
  








  public void write(long bOff, long[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 8L);
    super.write(bOff, buf, index, length);
  }
  








  public void write(long bOff, float[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 4L);
    super.write(bOff, buf, index, length);
  }
  








  public void write(long bOff, double[] buf, int index, int length)
  {
    boundsCheck(bOff, length * 8L);
    super.write(bOff, buf, index, length);
  }
  














  public byte getByte(long offset)
  {
    boundsCheck(offset, 1L);
    return super.getByte(offset);
  }
  








  public char getChar(long offset)
  {
    boundsCheck(offset, 1L);
    return super.getChar(offset);
  }
  








  public short getShort(long offset)
  {
    boundsCheck(offset, 2L);
    return super.getShort(offset);
  }
  








  public int getInt(long offset)
  {
    boundsCheck(offset, 4L);
    return super.getInt(offset);
  }
  








  public long getLong(long offset)
  {
    boundsCheck(offset, 8L);
    return super.getLong(offset);
  }
  








  public float getFloat(long offset)
  {
    boundsCheck(offset, 4L);
    return super.getFloat(offset);
  }
  








  public double getDouble(long offset)
  {
    boundsCheck(offset, 8L);
    return super.getDouble(offset);
  }
  








  public Pointer getPointer(long offset)
  {
    boundsCheck(offset, Pointer.SIZE);
    return super.getPointer(offset);
  }
  











  public ByteBuffer getByteBuffer(long offset, long length)
  {
    boundsCheck(offset, length);
    ByteBuffer b = super.getByteBuffer(offset, length);
    

    buffers.put(b, this);
    return b;
  }
  
  public String getString(long offset, String encoding)
  {
    boundsCheck(offset, 0L);
    return super.getString(offset, encoding);
  }
  
  public String getWideString(long offset)
  {
    boundsCheck(offset, 0L);
    return super.getWideString(offset);
  }
  











  public void setByte(long offset, byte value)
  {
    boundsCheck(offset, 1L);
    super.setByte(offset, value);
  }
  








  public void setChar(long offset, char value)
  {
    boundsCheck(offset, Native.WCHAR_SIZE);
    super.setChar(offset, value);
  }
  








  public void setShort(long offset, short value)
  {
    boundsCheck(offset, 2L);
    super.setShort(offset, value);
  }
  








  public void setInt(long offset, int value)
  {
    boundsCheck(offset, 4L);
    super.setInt(offset, value);
  }
  








  public void setLong(long offset, long value)
  {
    boundsCheck(offset, 8L);
    super.setLong(offset, value);
  }
  








  public void setFloat(long offset, float value)
  {
    boundsCheck(offset, 4L);
    super.setFloat(offset, value);
  }
  








  public void setDouble(long offset, double value)
  {
    boundsCheck(offset, 8L);
    super.setDouble(offset, value);
  }
  








  public void setPointer(long offset, Pointer value)
  {
    boundsCheck(offset, Pointer.SIZE);
    super.setPointer(offset, value);
  }
  
  public void setString(long offset, String value, String encoding) {
    boundsCheck(offset, Native.getBytes(value, encoding).length + 1L);
    super.setString(offset, value, encoding);
  }
  
  public void setWideString(long offset, String value) {
    boundsCheck(offset, (value.length() + 1L) * Native.WCHAR_SIZE);
    super.setWideString(offset, value);
  }
  
  public String toString() {
    return "allocated@0x" + Long.toHexString(peer) + " (" + size + " bytes)";
  }
  
  protected static void free(long p)
  {
    Native.free(p);
  }
  
  protected static long malloc(long size) {
    return Native.malloc(size);
  }
  
  public String dump()
  {
    return dump(0L, (int)size());
  }
}
