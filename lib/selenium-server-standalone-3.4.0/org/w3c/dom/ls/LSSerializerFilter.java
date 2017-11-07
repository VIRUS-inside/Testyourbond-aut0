package org.w3c.dom.ls;

import org.w3c.dom.traversal.NodeFilter;

public abstract interface LSSerializerFilter
  extends NodeFilter
{
  public abstract int getWhatToShow();
}
