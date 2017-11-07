package org.apache.xalan.xsltc.dom;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;






























public class BitArray
  implements Externalizable
{
  static final long serialVersionUID = -4876019880708377663L;
  private int[] _bits;
  private int _bitSize;
  private int _intSize;
  private int _mask;
  private static final int[] _masks = { Integer.MIN_VALUE, 1073741824, 536870912, 268435456, 134217728, 67108864, 33554432, 16777216, 8388608, 4194304, 2097152, 1048576, 524288, 262144, 131072, 65536, 32768, 16384, 8192, 4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1 };
  





  private static final boolean DEBUG_ASSERTIONS = false;
  





  public BitArray()
  {
    this(32);
  }
  
  public BitArray(int size) {
    if (size < 32) size = 32;
    _bitSize = size;
    _intSize = ((_bitSize >>> 5) + 1);
    _bits = new int[_intSize + 1];
  }
  
  public BitArray(int size, int[] bits) {
    if (size < 32) size = 32;
    _bitSize = size;
    _intSize = ((_bitSize >>> 5) + 1);
    _bits = bits;
  }
  



  public void setMask(int mask)
  {
    _mask = mask;
  }
  


  public int getMask()
  {
    return _mask;
  }
  


  public final int size()
  {
    return _bitSize;
  }
  









  public final boolean getBit(int bit)
  {
    return (_bits[(bit >>> 5)] & _masks[(bit % 32)]) != 0;
  }
  


  public final int getNextBit(int startBit)
  {
    for (int i = startBit >>> 5; i <= _intSize; i++) {
      int bits = _bits[i];
      if (bits != 0) {
        for (int b = startBit % 32; b < 32; b++) {
          if ((bits & _masks[b]) != 0) {
            return (i << 5) + b;
          }
        }
      }
      startBit = 0;
    }
    return -1;
  }
  






  private int _pos = Integer.MAX_VALUE;
  private int _node = 0;
  private int _int = 0;
  private int _bit = 0;
  

  public final int getBitNumber(int pos)
  {
    if (pos == _pos) { return _node;
    }
    

    if (pos < _pos) {}
    for (_int = (this._bit = this._pos = 0); 
        


        _int <= _intSize; _int += 1) {
      int bits = _bits[_int];
      if (bits != 0) {
        for (; _bit < 32; _bit += 1) {
          if (((bits & _masks[_bit]) != 0) && 
            (++_pos == pos)) {
            _node = ((_int << 5) + _bit - 1);
            return _node;
          }
        }
        
        _bit = 0;
      }
    }
    return 0;
  }
  


  public final int[] data()
  {
    return _bits;
  }
  
  int _first = Integer.MAX_VALUE;
  int _last = Integer.MIN_VALUE;
  









  public final void setBit(int bit)
  {
    if (bit >= _bitSize) return;
    int i = bit >>> 5;
    if (i < _first) _first = i;
    if (i > _last) _last = i;
    _bits[i] |= _masks[(bit % 32)];
  }
  




  public final BitArray merge(BitArray other)
  {
    if (_last == -1) {
      _bits = _bits;

    }
    else if (_last != -1) {
      int start = _first < _first ? _first : _first;
      int stop = _last > _last ? _last : _last;
      

      if (_intSize > _intSize) {
        if (stop > _intSize) stop = _intSize;
        for (int i = start; i <= stop; i++)
          _bits[i] |= _bits[i];
        _bits = _bits;
      }
      else
      {
        if (stop > _intSize) stop = _intSize;
        for (int i = start; i <= stop; i++)
          _bits[i] |= _bits[i];
      }
    }
    return this;
  }
  


  public final void resize(int newSize)
  {
    if (newSize > _bitSize) {
      _intSize = ((newSize >>> 5) + 1);
      int[] newBits = new int[_intSize + 1];
      System.arraycopy(_bits, 0, newBits, 0, (_bitSize >>> 5) + 1);
      _bits = newBits;
      _bitSize = newSize;
    }
  }
  
  public BitArray cloneArray() {
    return new BitArray(_intSize, _bits);
  }
  
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(_bitSize);
    out.writeInt(_mask);
    out.writeObject(_bits);
    out.flush();
  }
  


  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    _bitSize = in.readInt();
    _intSize = ((_bitSize >>> 5) + 1);
    _mask = in.readInt();
    _bits = ((int[])in.readObject());
  }
}
