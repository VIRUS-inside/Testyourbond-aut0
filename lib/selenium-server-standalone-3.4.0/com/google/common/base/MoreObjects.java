package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import javax.annotation.Nullable;












































@GwtCompatible
public final class MoreObjects
{
  public static <T> T firstNonNull(@Nullable T first, @Nullable T second)
  {
    return first != null ? first : Preconditions.checkNotNull(second);
  }
  







































  public static ToStringHelper toStringHelper(Object self)
  {
    return new ToStringHelper(self.getClass().getSimpleName(), null);
  }
  









  public static ToStringHelper toStringHelper(Class<?> clazz)
  {
    return new ToStringHelper(clazz.getSimpleName(), null);
  }
  







  public static ToStringHelper toStringHelper(String className)
  {
    return new ToStringHelper(className, null);
  }
  

  private MoreObjects() {}
  

  public static final class ToStringHelper
  {
    private final String className;
    
    private final ValueHolder holderHead = new ValueHolder(null);
    private ValueHolder holderTail = holderHead;
    private boolean omitNullValues = false;
    


    private ToStringHelper(String className)
    {
      this.className = ((String)Preconditions.checkNotNull(className));
    }
    






    @CanIgnoreReturnValue
    public ToStringHelper omitNullValues()
    {
      omitNullValues = true;
      return this;
    }
    




    @CanIgnoreReturnValue
    public ToStringHelper add(String name, @Nullable Object value)
    {
      return addHolder(name, value);
    }
    




    @CanIgnoreReturnValue
    public ToStringHelper add(String name, boolean value)
    {
      return addHolder(name, String.valueOf(value));
    }
    




    @CanIgnoreReturnValue
    public ToStringHelper add(String name, char value)
    {
      return addHolder(name, String.valueOf(value));
    }
    




    @CanIgnoreReturnValue
    public ToStringHelper add(String name, double value)
    {
      return addHolder(name, String.valueOf(value));
    }
    




    @CanIgnoreReturnValue
    public ToStringHelper add(String name, float value)
    {
      return addHolder(name, String.valueOf(value));
    }
    




    @CanIgnoreReturnValue
    public ToStringHelper add(String name, int value)
    {
      return addHolder(name, String.valueOf(value));
    }
    




    @CanIgnoreReturnValue
    public ToStringHelper add(String name, long value)
    {
      return addHolder(name, String.valueOf(value));
    }
    





    @CanIgnoreReturnValue
    public ToStringHelper addValue(@Nullable Object value)
    {
      return addHolder(value);
    }
    







    @CanIgnoreReturnValue
    public ToStringHelper addValue(boolean value)
    {
      return addHolder(String.valueOf(value));
    }
    







    @CanIgnoreReturnValue
    public ToStringHelper addValue(char value)
    {
      return addHolder(String.valueOf(value));
    }
    







    @CanIgnoreReturnValue
    public ToStringHelper addValue(double value)
    {
      return addHolder(String.valueOf(value));
    }
    







    @CanIgnoreReturnValue
    public ToStringHelper addValue(float value)
    {
      return addHolder(String.valueOf(value));
    }
    







    @CanIgnoreReturnValue
    public ToStringHelper addValue(int value)
    {
      return addHolder(String.valueOf(value));
    }
    







    @CanIgnoreReturnValue
    public ToStringHelper addValue(long value)
    {
      return addHolder(String.valueOf(value));
    }
    









    public String toString()
    {
      boolean omitNullValuesSnapshot = omitNullValues;
      String nextSeparator = "";
      StringBuilder builder = new StringBuilder(32).append(className).append('{');
      for (ValueHolder valueHolder = holderHead.next; 
          valueHolder != null; 
          valueHolder = next) {
        Object value = value;
        if ((!omitNullValuesSnapshot) || (value != null)) {
          builder.append(nextSeparator);
          nextSeparator = ", ";
          
          if (name != null) {
            builder.append(name).append('=');
          }
          if ((value != null) && (value.getClass().isArray())) {
            Object[] objectArray = { value };
            String arrayString = Arrays.deepToString(objectArray);
            builder.append(arrayString, 1, arrayString.length() - 1);
          } else {
            builder.append(value);
          }
        }
      }
      return '}';
    }
    
    private ValueHolder addHolder() {
      ValueHolder valueHolder = new ValueHolder(null);
      holderTail = (holderTail.next = valueHolder);
      return valueHolder;
    }
    
    private ToStringHelper addHolder(@Nullable Object value) {
      ValueHolder valueHolder = addHolder();
      value = value;
      return this;
    }
    
    private ToStringHelper addHolder(String name, @Nullable Object value) {
      ValueHolder valueHolder = addHolder();
      value = value;
      name = ((String)Preconditions.checkNotNull(name));
      return this;
    }
    
    private static final class ValueHolder
    {
      String name;
      Object value;
      ValueHolder next;
      
      private ValueHolder() {}
    }
  }
}
