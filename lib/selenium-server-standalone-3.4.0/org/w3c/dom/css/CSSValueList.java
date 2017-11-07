package org.w3c.dom.css;

public abstract interface CSSValueList
  extends CSSValue
{
  public abstract int getLength();
  
  public abstract CSSValue item(int paramInt);
}
