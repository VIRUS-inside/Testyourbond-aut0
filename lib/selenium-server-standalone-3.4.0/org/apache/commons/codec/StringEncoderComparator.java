package org.apache.commons.codec;

import java.util.Comparator;




































public class StringEncoderComparator
  implements Comparator
{
  private final StringEncoder stringEncoder;
  
  @Deprecated
  public StringEncoderComparator()
  {
    stringEncoder = null;
  }
  





  public StringEncoderComparator(StringEncoder stringEncoder)
  {
    this.stringEncoder = stringEncoder;
  }
  














  public int compare(Object o1, Object o2)
  {
    int compareCode = 0;
    

    try
    {
      Comparable<Comparable<?>> s1 = (Comparable)stringEncoder.encode(o1);
      Comparable<?> s2 = (Comparable)stringEncoder.encode(o2);
      compareCode = s1.compareTo(s2);
    } catch (EncoderException ee) {
      compareCode = 0;
    }
    return compareCode;
  }
}