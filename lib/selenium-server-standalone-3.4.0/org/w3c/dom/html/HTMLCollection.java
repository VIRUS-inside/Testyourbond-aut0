package org.w3c.dom.html;

import org.w3c.dom.Node;

public abstract interface HTMLCollection
{
  public abstract int getLength();
  
  public abstract Node item(int paramInt);
  
  public abstract Node namedItem(String paramString);
}
