package net.sourceforge.htmlunit.corejs.javascript.ast;

import java.util.ArrayList;
import java.util.List;
















public class XmlLiteral
  extends AstNode
{
  private List<XmlFragment> fragments = new ArrayList();
  
  public XmlLiteral() {
    type = 145;
  }
  


  public XmlLiteral(int pos)
  {
    super(pos);type = 145;
  }
  
  public XmlLiteral(int pos, int len) {
    super(pos, len);type = 145;
  }
  


  public List<XmlFragment> getFragments()
  {
    return fragments;
  }
  








  public void setFragments(List<XmlFragment> fragments)
  {
    assertNotNull(fragments);
    this.fragments.clear();
    for (XmlFragment fragment : fragments) {
      addFragment(fragment);
    }
  }
  




  public void addFragment(XmlFragment fragment)
  {
    assertNotNull(fragment);
    fragments.add(fragment);
    fragment.setParent(this);
  }
  
  public String toSource(int depth)
  {
    StringBuilder sb = new StringBuilder(250);
    for (XmlFragment frag : fragments) {
      sb.append(frag.toSource(0));
    }
    return sb.toString();
  }
  



  public void visit(NodeVisitor v)
  {
    if (v.visit(this)) {
      for (XmlFragment frag : fragments) {
        frag.visit(v);
      }
    }
  }
}
