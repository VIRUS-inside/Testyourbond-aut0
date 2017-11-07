package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Util;





















final class Attribute
  extends Instruction
{
  private QName _name;
  
  Attribute() {}
  
  public void display(int indent)
  {
    indent(indent);
    Util.println("Attribute " + _name);
    displayContents(indent + 4);
  }
  
  public void parseContents(Parser parser) {
    _name = parser.getQName(getAttribute("name"));
    parseChildren(parser);
  }
}
