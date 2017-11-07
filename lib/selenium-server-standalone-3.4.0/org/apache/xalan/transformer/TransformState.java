package org.apache.xalan.transformer;

import javax.xml.transform.Transformer;
import org.apache.xalan.templates.ElemTemplate;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xml.serializer.TransformStateSetter;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

public abstract interface TransformState
  extends TransformStateSetter
{
  public abstract ElemTemplateElement getCurrentElement();
  
  public abstract Node getCurrentNode();
  
  public abstract ElemTemplate getCurrentTemplate();
  
  public abstract ElemTemplate getMatchedTemplate();
  
  public abstract Node getMatchedNode();
  
  public abstract NodeIterator getContextNodeList();
  
  public abstract Transformer getTransformer();
}
