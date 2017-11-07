package org.apache.xerces.impl.dv.dtd;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.xerces.impl.dv.DatatypeValidator;

public class XML11DTDDVFactoryImpl
  extends DTDDVFactoryImpl
{
  static final Hashtable fXML11BuiltInTypes = new Hashtable();
  
  public XML11DTDDVFactoryImpl() {}
  
  public DatatypeValidator getBuiltInDV(String paramString)
  {
    if (fXML11BuiltInTypes.get(paramString) != null) {
      return (DatatypeValidator)fXML11BuiltInTypes.get(paramString);
    }
    return (DatatypeValidator)DTDDVFactoryImpl.fBuiltInTypes.get(paramString);
  }
  
  public Hashtable getBuiltInTypes()
  {
    Hashtable localHashtable = (Hashtable)DTDDVFactoryImpl.fBuiltInTypes.clone();
    Iterator localIterator = fXML11BuiltInTypes.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject1 = localEntry.getKey();
      Object localObject2 = localEntry.getValue();
      localHashtable.put(localObject1, localObject2);
    }
    return localHashtable;
  }
  
  static
  {
    fXML11BuiltInTypes.put("XML11ID", new XML11IDDatatypeValidator());
    Object localObject = new XML11IDREFDatatypeValidator();
    fXML11BuiltInTypes.put("XML11IDREF", localObject);
    fXML11BuiltInTypes.put("XML11IDREFS", new ListDatatypeValidator((DatatypeValidator)localObject));
    localObject = new XML11NMTOKENDatatypeValidator();
    fXML11BuiltInTypes.put("XML11NMTOKEN", localObject);
    fXML11BuiltInTypes.put("XML11NMTOKENS", new ListDatatypeValidator((DatatypeValidator)localObject));
  }
}
