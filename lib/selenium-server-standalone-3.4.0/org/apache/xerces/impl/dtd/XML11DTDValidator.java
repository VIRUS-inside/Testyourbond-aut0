package org.apache.xerces.impl.dtd;

import org.apache.xerces.impl.dv.DTDDVFactory;
import org.apache.xerces.xni.parser.XMLComponentManager;

public class XML11DTDValidator
  extends XMLDTDValidator
{
  protected static final String DTD_VALIDATOR_PROPERTY = "http://apache.org/xml/properties/internal/validator/dtd";
  
  public XML11DTDValidator() {}
  
  public void reset(XMLComponentManager paramXMLComponentManager)
  {
    XMLDTDValidator localXMLDTDValidator = null;
    if (((localXMLDTDValidator = (XMLDTDValidator)paramXMLComponentManager.getProperty("http://apache.org/xml/properties/internal/validator/dtd")) != null) && (localXMLDTDValidator != this)) {
      fGrammarBucket = localXMLDTDValidator.getGrammarBucket();
    }
    super.reset(paramXMLComponentManager);
  }
  
  protected void init()
  {
    if ((fValidation) || (fDynamicValidation))
    {
      super.init();
      try
      {
        fValID = fDatatypeValidatorFactory.getBuiltInDV("XML11ID");
        fValIDRef = fDatatypeValidatorFactory.getBuiltInDV("XML11IDREF");
        fValIDRefs = fDatatypeValidatorFactory.getBuiltInDV("XML11IDREFS");
        fValNMTOKEN = fDatatypeValidatorFactory.getBuiltInDV("XML11NMTOKEN");
        fValNMTOKENS = fDatatypeValidatorFactory.getBuiltInDV("XML11NMTOKENS");
      }
      catch (Exception localException)
      {
        localException.printStackTrace(System.err);
      }
    }
  }
}
