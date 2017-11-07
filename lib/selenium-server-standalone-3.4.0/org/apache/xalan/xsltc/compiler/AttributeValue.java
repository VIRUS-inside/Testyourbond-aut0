package org.apache.xalan.xsltc.compiler;







abstract class AttributeValue
  extends Expression
{
  AttributeValue() {}
  






  public static final AttributeValue create(SyntaxTreeNode parent, String text, Parser parser)
  {
    AttributeValue result;
    





    AttributeValue result;
    




    if (text.indexOf('{') != -1) {
      result = new AttributeValueTemplate(text, parser, parent);
    } else { AttributeValue result;
      if (text.indexOf('}') != -1) {
        result = new AttributeValueTemplate(text, parser, parent);
      }
      else {
        result = new SimpleAttributeValue(text);
        result.setParser(parser);
        result.setParent(parent);
      } }
    return result;
  }
}
