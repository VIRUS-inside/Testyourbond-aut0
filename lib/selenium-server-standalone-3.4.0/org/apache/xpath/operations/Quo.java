package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;































/**
 * @deprecated
 */
public class Quo
  extends Operation
{
  static final long serialVersionUID = 693765299196169905L;
  
  public Quo() {}
  
  public XObject operate(XObject left, XObject right)
    throws TransformerException
  {
    return new XNumber((int)(left.num() / right.num()));
  }
}
