package javax.xml.stream.events;

import javax.xml.namespace.QName;

public abstract interface Attribute
  extends XMLEvent
{
  public abstract String getDTDType();
  
  public abstract QName getName();
  
  public abstract String getValue();
  
  public abstract boolean isSpecified();
}
