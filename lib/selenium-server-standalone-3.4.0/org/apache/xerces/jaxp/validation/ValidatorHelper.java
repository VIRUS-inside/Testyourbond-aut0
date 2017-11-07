package org.apache.xerces.jaxp.validation;

import java.io.IOException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.xml.sax.SAXException;

abstract interface ValidatorHelper
{
  public abstract void validate(Source paramSource, Result paramResult)
    throws SAXException, IOException;
}
