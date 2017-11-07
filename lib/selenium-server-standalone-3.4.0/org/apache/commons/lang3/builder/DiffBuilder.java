package org.apache.commons.lang3.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;























































































public class DiffBuilder
  implements Builder<DiffResult>
{
  private final List<Diff<?>> diffs;
  private final boolean objectsTriviallyEqual;
  private final Object left;
  private final Object right;
  private final ToStringStyle style;
  
  public DiffBuilder(Object lhs, Object rhs, ToStringStyle style, boolean testTriviallyEqual)
  {
    if (lhs == null) {
      throw new IllegalArgumentException("lhs cannot be null");
    }
    if (rhs == null) {
      throw new IllegalArgumentException("rhs cannot be null");
    }
    
    diffs = new ArrayList();
    left = lhs;
    right = rhs;
    this.style = style;
    

    objectsTriviallyEqual = ((testTriviallyEqual) && ((lhs == rhs) || (lhs.equals(rhs))));
  }
  



























  public DiffBuilder(Object lhs, Object rhs, ToStringStyle style)
  {
    this(lhs, rhs, style, true);
  }
  















  public DiffBuilder append(String fieldName, final boolean lhs, final boolean rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (lhs != rhs) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Boolean getLeft() {
          return Boolean.valueOf(lhs);
        }
        
        public Boolean getRight()
        {
          return Boolean.valueOf(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final boolean[] lhs, final boolean[] rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    if (objectsTriviallyEqual) {
      return this;
    }
    if (!Arrays.equals(lhs, rhs)) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Boolean[] getLeft() {
          return ArrayUtils.toObject(lhs);
        }
        
        public Boolean[] getRight()
        {
          return ArrayUtils.toObject(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final byte lhs, final byte rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    if (objectsTriviallyEqual) {
      return this;
    }
    if (lhs != rhs) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Byte getLeft() {
          return Byte.valueOf(lhs);
        }
        
        public Byte getRight()
        {
          return Byte.valueOf(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final byte[] lhs, final byte[] rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (!Arrays.equals(lhs, rhs)) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Byte[] getLeft() {
          return ArrayUtils.toObject(lhs);
        }
        
        public Byte[] getRight()
        {
          return ArrayUtils.toObject(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final char lhs, final char rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (lhs != rhs) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Character getLeft() {
          return Character.valueOf(lhs);
        }
        
        public Character getRight()
        {
          return Character.valueOf(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final char[] lhs, final char[] rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (!Arrays.equals(lhs, rhs)) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Character[] getLeft() {
          return ArrayUtils.toObject(lhs);
        }
        
        public Character[] getRight()
        {
          return ArrayUtils.toObject(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final double lhs, double rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (Double.doubleToLongBits(lhs) != Double.doubleToLongBits(rhs)) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Double getLeft() {
          return Double.valueOf(lhs);
        }
        
        public Double getRight()
        {
          return Double.valueOf(val$rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final double[] lhs, final double[] rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (!Arrays.equals(lhs, rhs)) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Double[] getLeft() {
          return ArrayUtils.toObject(lhs);
        }
        
        public Double[] getRight()
        {
          return ArrayUtils.toObject(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final float lhs, final float rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (Float.floatToIntBits(lhs) != Float.floatToIntBits(rhs)) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Float getLeft() {
          return Float.valueOf(lhs);
        }
        
        public Float getRight()
        {
          return Float.valueOf(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final float[] lhs, final float[] rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (!Arrays.equals(lhs, rhs)) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Float[] getLeft() {
          return ArrayUtils.toObject(lhs);
        }
        
        public Float[] getRight()
        {
          return ArrayUtils.toObject(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final int lhs, final int rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (lhs != rhs) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Integer getLeft() {
          return Integer.valueOf(lhs);
        }
        
        public Integer getRight()
        {
          return Integer.valueOf(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final int[] lhs, final int[] rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (!Arrays.equals(lhs, rhs)) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Integer[] getLeft() {
          return ArrayUtils.toObject(lhs);
        }
        
        public Integer[] getRight()
        {
          return ArrayUtils.toObject(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final long lhs, long rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (lhs != rhs) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Long getLeft() {
          return Long.valueOf(lhs);
        }
        
        public Long getRight()
        {
          return Long.valueOf(val$rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final long[] lhs, final long[] rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (!Arrays.equals(lhs, rhs)) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Long[] getLeft() {
          return ArrayUtils.toObject(lhs);
        }
        
        public Long[] getRight()
        {
          return ArrayUtils.toObject(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final short lhs, final short rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (lhs != rhs) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Short getLeft() {
          return Short.valueOf(lhs);
        }
        
        public Short getRight()
        {
          return Short.valueOf(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final short[] lhs, final short[] rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    
    if (objectsTriviallyEqual) {
      return this;
    }
    if (!Arrays.equals(lhs, rhs)) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Short[] getLeft() {
          return ArrayUtils.toObject(lhs);
        }
        
        public Short[] getRight()
        {
          return ArrayUtils.toObject(rhs);
        }
      });
    }
    return this;
  }
  















  public DiffBuilder append(String fieldName, final Object lhs, final Object rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    if (objectsTriviallyEqual) {
      return this;
    }
    if (lhs == rhs) {
      return this;
    }
    Object objectToTest;
    Object objectToTest;
    if (lhs != null) {
      objectToTest = lhs;
    }
    else {
      objectToTest = rhs;
    }
    
    if (objectToTest.getClass().isArray()) {
      if ((objectToTest instanceof boolean[])) {
        return append(fieldName, (boolean[])lhs, (boolean[])rhs);
      }
      if ((objectToTest instanceof byte[])) {
        return append(fieldName, (byte[])lhs, (byte[])rhs);
      }
      if ((objectToTest instanceof char[])) {
        return append(fieldName, (char[])lhs, (char[])rhs);
      }
      if ((objectToTest instanceof double[])) {
        return append(fieldName, (double[])lhs, (double[])rhs);
      }
      if ((objectToTest instanceof float[])) {
        return append(fieldName, (float[])lhs, (float[])rhs);
      }
      if ((objectToTest instanceof int[])) {
        return append(fieldName, (int[])lhs, (int[])rhs);
      }
      if ((objectToTest instanceof long[])) {
        return append(fieldName, (long[])lhs, (long[])rhs);
      }
      if ((objectToTest instanceof short[])) {
        return append(fieldName, (short[])lhs, (short[])rhs);
      }
      
      return append(fieldName, (Object[])lhs, (Object[])rhs);
    }
    

    if ((lhs != null) && (lhs.equals(rhs))) {
      return this;
    }
    
    diffs.add(new Diff(fieldName)
    {
      private static final long serialVersionUID = 1L;
      
      public Object getLeft() {
        return lhs;
      }
      
      public Object getRight()
      {
        return rhs;
      }
      
    });
    return this;
  }
  















  public DiffBuilder append(String fieldName, final Object[] lhs, final Object[] rhs)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    if (objectsTriviallyEqual) {
      return this;
    }
    
    if (!Arrays.equals(lhs, rhs)) {
      diffs.add(new Diff(fieldName)
      {
        private static final long serialVersionUID = 1L;
        
        public Object[] getLeft() {
          return lhs;
        }
        
        public Object[] getRight()
        {
          return rhs;
        }
      });
    }
    
    return this;
  }
  




































  public DiffBuilder append(String fieldName, DiffResult diffResult)
  {
    if (fieldName == null) {
      throw new IllegalArgumentException("Field name cannot be null");
    }
    if (diffResult == null) {
      throw new IllegalArgumentException("Diff result cannot be null");
    }
    if (objectsTriviallyEqual) {
      return this;
    }
    
    for (Diff<?> diff : diffResult.getDiffs()) {
      append(fieldName + "." + diff.getFieldName(), diff
        .getLeft(), diff.getRight());
    }
    
    return this;
  }
  









  public DiffResult build()
  {
    return new DiffResult(left, right, diffs, style);
  }
}
