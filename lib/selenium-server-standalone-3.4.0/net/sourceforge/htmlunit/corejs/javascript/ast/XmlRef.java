package net.sourceforge.htmlunit.corejs.javascript.ast;

















public abstract class XmlRef
  extends AstNode
{
  protected Name namespace;
  














  protected int atPos = -1;
  protected int colonPos = -1;
  
  public XmlRef() {}
  
  public XmlRef(int pos)
  {
    super(pos);
  }
  
  public XmlRef(int pos, int len) {
    super(pos, len);
  }
  


  public Name getNamespace()
  {
    return namespace;
  }
  


  public void setNamespace(Name namespace)
  {
    this.namespace = namespace;
    if (namespace != null) {
      namespace.setParent(this);
    }
  }
  

  public boolean isAttributeAccess()
  {
    return atPos >= 0;
  }
  



  public int getAtPos()
  {
    return atPos;
  }
  


  public void setAtPos(int atPos)
  {
    this.atPos = atPos;
  }
  



  public int getColonPos()
  {
    return colonPos;
  }
  


  public void setColonPos(int colonPos)
  {
    this.colonPos = colonPos;
  }
}
