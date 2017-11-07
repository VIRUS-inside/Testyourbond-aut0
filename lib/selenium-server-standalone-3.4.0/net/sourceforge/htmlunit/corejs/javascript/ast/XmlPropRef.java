package net.sourceforge.htmlunit.corejs.javascript.ast;
















public class XmlPropRef
  extends XmlRef
{
  private Name propName;
  














  public XmlPropRef()
  {
    type = 79;
  }
  


  public XmlPropRef(int pos)
  {
    super(pos);type = 79;
  }
  
  public XmlPropRef(int pos, int len) {
    super(pos, len);type = 79;
  }
  


  public Name getPropName()
  {
    return propName;
  }
  





  public void setPropName(Name propName)
  {
    assertNotNull(propName);
    this.propName = propName;
    propName.setParent(this);
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder();
    sb.append(makeIndent(depth));
    if (isAttributeAccess()) {
      sb.append("@");
    }
    if (namespace != null) {
      sb.append(namespace.toSource(0));
      sb.append("::");
    }
    sb.append(propName.toSource(0));
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      if (namespace != null) {
        namespace.visit(v);
      }
      propName.visit(v);
    }
  }
}
