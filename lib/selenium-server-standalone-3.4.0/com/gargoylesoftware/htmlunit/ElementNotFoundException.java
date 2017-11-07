package com.gargoylesoftware.htmlunit;








public class ElementNotFoundException
  extends RuntimeException
{
  private final String elementName_;
  






  private final String attributeName_;
  






  private final String attributeValue_;
  






  public ElementNotFoundException(String elementName, String attributeName, String attributeValue)
  {
    super("elementName=[" + elementName + "] attributeName=[" + attributeName + "] attributeValue=[" + attributeValue + "]");
    
    elementName_ = elementName;
    attributeName_ = attributeName;
    attributeValue_ = attributeValue;
  }
  




  public String getElementName()
  {
    return elementName_;
  }
  




  public String getAttributeName()
  {
    return attributeName_;
  }
  




  public String getAttributeValue()
  {
    return attributeValue_;
  }
}
