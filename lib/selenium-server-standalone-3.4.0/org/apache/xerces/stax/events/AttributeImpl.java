package org.apache.xerces.stax.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;

public class AttributeImpl
  extends XMLEventImpl
  implements Attribute
{
  private final boolean fIsSpecified;
  private final QName fName;
  private final String fValue;
  private final String fDtdType;
  
  public AttributeImpl(QName paramQName, String paramString1, String paramString2, boolean paramBoolean, Location paramLocation)
  {
    this(10, paramQName, paramString1, paramString2, paramBoolean, paramLocation);
  }
  
  protected AttributeImpl(int paramInt, QName paramQName, String paramString1, String paramString2, boolean paramBoolean, Location paramLocation)
  {
    super(paramInt, paramLocation);
    fName = paramQName;
    fValue = paramString1;
    fDtdType = paramString2;
    fIsSpecified = paramBoolean;
  }
  
  public final QName getName()
  {
    return fName;
  }
  
  public final String getValue()
  {
    return fValue;
  }
  
  public final String getDTDType()
  {
    return fDtdType;
  }
  
  public final boolean isSpecified()
  {
    return fIsSpecified;
  }
  
  public final void writeAsEncodedUnicode(Writer paramWriter)
    throws XMLStreamException
  {
    try
    {
      String str = fName.getPrefix();
      if ((str != null) && (str.length() > 0))
      {
        paramWriter.write(str);
        paramWriter.write(58);
      }
      paramWriter.write(fName.getLocalPart());
      paramWriter.write("=\"");
      paramWriter.write(fValue);
      paramWriter.write(34);
    }
    catch (IOException localIOException)
    {
      throw new XMLStreamException(localIOException);
    }
  }
}
