package javax.xml.transform.dom;

import javax.xml.transform.SourceLocator;
import org.w3c.dom.Node;

public abstract interface DOMLocator
  extends SourceLocator
{
  public abstract Node getOriginatingNode();
}
