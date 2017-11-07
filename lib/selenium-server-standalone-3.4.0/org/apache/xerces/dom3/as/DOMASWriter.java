package org.apache.xerces.dom3.as;

import java.io.OutputStream;
import org.w3c.dom.ls.LSSerializer;

/**
 * @deprecated
 */
public abstract interface DOMASWriter
  extends LSSerializer
{
  public abstract void writeASModel(OutputStream paramOutputStream, ASModel paramASModel)
    throws Exception;
}
