package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.SystemUtils;

































































public class MultilineRecursiveToStringStyle
  extends RecursiveToStringStyle
{
  private static final long serialVersionUID = 1L;
  private int indent = 2;
  

  private int spaces = 2;
  



  public MultilineRecursiveToStringStyle()
  {
    resetIndent();
  }
  



  private void resetIndent()
  {
    setArrayStart("{" + SystemUtils.LINE_SEPARATOR + spacer(spaces));
    setArraySeparator("," + SystemUtils.LINE_SEPARATOR + spacer(spaces));
    setArrayEnd(SystemUtils.LINE_SEPARATOR + spacer(spaces - indent) + "}");
    
    setContentStart("[" + SystemUtils.LINE_SEPARATOR + spacer(spaces));
    setFieldSeparator("," + SystemUtils.LINE_SEPARATOR + spacer(spaces));
    setContentEnd(SystemUtils.LINE_SEPARATOR + spacer(spaces - indent) + "]");
  }
  





  private StringBuilder spacer(int spaces)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < spaces; i++) {
      sb.append(" ");
    }
    return sb;
  }
  
  public void appendDetail(StringBuffer buffer, String fieldName, Object value)
  {
    if ((!ClassUtils.isPrimitiveWrapper(value.getClass())) && (!String.class.equals(value.getClass())) && 
      (accept(value.getClass()))) {
      spaces += indent;
      resetIndent();
      buffer.append(ReflectionToStringBuilder.toString(value, this));
      spaces -= indent;
      resetIndent();
    } else {
      super.appendDetail(buffer, fieldName, value);
    }
  }
  
  protected void appendDetail(StringBuffer buffer, String fieldName, Object[] array)
  {
    spaces += indent;
    resetIndent();
    super.appendDetail(buffer, fieldName, array);
    spaces -= indent;
    resetIndent();
  }
  
  protected void reflectionAppendArrayDetail(StringBuffer buffer, String fieldName, Object array)
  {
    spaces += indent;
    resetIndent();
    super.appendDetail(buffer, fieldName, array);
    spaces -= indent;
    resetIndent();
  }
  
  protected void appendDetail(StringBuffer buffer, String fieldName, long[] array)
  {
    spaces += indent;
    resetIndent();
    super.appendDetail(buffer, fieldName, array);
    spaces -= indent;
    resetIndent();
  }
  
  protected void appendDetail(StringBuffer buffer, String fieldName, int[] array)
  {
    spaces += indent;
    resetIndent();
    super.appendDetail(buffer, fieldName, array);
    spaces -= indent;
    resetIndent();
  }
  
  protected void appendDetail(StringBuffer buffer, String fieldName, short[] array)
  {
    spaces += indent;
    resetIndent();
    super.appendDetail(buffer, fieldName, array);
    spaces -= indent;
    resetIndent();
  }
  
  protected void appendDetail(StringBuffer buffer, String fieldName, byte[] array)
  {
    spaces += indent;
    resetIndent();
    super.appendDetail(buffer, fieldName, array);
    spaces -= indent;
    resetIndent();
  }
  
  protected void appendDetail(StringBuffer buffer, String fieldName, char[] array)
  {
    spaces += indent;
    resetIndent();
    super.appendDetail(buffer, fieldName, array);
    spaces -= indent;
    resetIndent();
  }
  
  protected void appendDetail(StringBuffer buffer, String fieldName, double[] array)
  {
    spaces += indent;
    resetIndent();
    super.appendDetail(buffer, fieldName, array);
    spaces -= indent;
    resetIndent();
  }
  
  protected void appendDetail(StringBuffer buffer, String fieldName, float[] array)
  {
    spaces += indent;
    resetIndent();
    super.appendDetail(buffer, fieldName, array);
    spaces -= indent;
    resetIndent();
  }
  
  protected void appendDetail(StringBuffer buffer, String fieldName, boolean[] array)
  {
    spaces += indent;
    resetIndent();
    super.appendDetail(buffer, fieldName, array);
    spaces -= indent;
    resetIndent();
  }
}
