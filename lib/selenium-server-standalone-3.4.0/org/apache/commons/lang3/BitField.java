package org.apache.commons.lang3;



























public class BitField
{
  private final int _mask;
  
























  private final int _shift_count;
  

























  public BitField(int mask)
  {
    _mask = mask;
    _shift_count = (mask != 0 ? Integer.numberOfTrailingZeros(mask) : 0);
  }
  













  public int getValue(int holder)
  {
    return getRawValue(holder) >> _shift_count;
  }
  













  public short getShortValue(short holder)
  {
    return (short)getValue(holder);
  }
  






  public int getRawValue(int holder)
  {
    return holder & _mask;
  }
  






  public short getShortRawValue(short holder)
  {
    return (short)getRawValue(holder);
  }
  












  public boolean isSet(int holder)
  {
    return (holder & _mask) != 0;
  }
  











  public boolean isAllSet(int holder)
  {
    return (holder & _mask) == _mask;
  }
  









  public int setValue(int holder, int value)
  {
    return holder & (_mask ^ 0xFFFFFFFF) | value << _shift_count & _mask;
  }
  









  public short setShortValue(short holder, short value)
  {
    return (short)setValue(holder, value);
  }
  







  public int clear(int holder)
  {
    return holder & (_mask ^ 0xFFFFFFFF);
  }
  







  public short clearShort(short holder)
  {
    return (short)clear(holder);
  }
  








  public byte clearByte(byte holder)
  {
    return (byte)clear(holder);
  }
  







  public int set(int holder)
  {
    return holder | _mask;
  }
  







  public short setShort(short holder)
  {
    return (short)set(holder);
  }
  








  public byte setByte(byte holder)
  {
    return (byte)set(holder);
  }
  








  public int setBoolean(int holder, boolean flag)
  {
    return flag ? set(holder) : clear(holder);
  }
  








  public short setShortBoolean(short holder, boolean flag)
  {
    return flag ? setShort(holder) : clearShort(holder);
  }
  








  public byte setByteBoolean(byte holder, boolean flag)
  {
    return flag ? setByte(holder) : clearByte(holder);
  }
}
