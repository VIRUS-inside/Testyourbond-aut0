package org.apache.xerces.impl.dtd;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLDocumentHandler;
import org.apache.xerces.xni.XNIException;

public class XML11NSDTDValidator
  extends XML11DTDValidator
{
  private final QName fAttributeQName = new QName();
  
  public XML11NSDTDValidator() {}
  
  protected final void startNamespaceScope(QName paramQName, XMLAttributes paramXMLAttributes, Augmentations paramAugmentations)
    throws XNIException
  {
    fNamespaceContext.pushContext();
    if (prefix == XMLSymbols.PREFIX_XMLNS) {
      fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[] { rawname }, (short)2);
    }
    int i = paramXMLAttributes.getLength();
    String str3;
    for (int j = 0; j < i; j++)
    {
      str1 = paramXMLAttributes.getLocalName(j);
      String str2 = paramXMLAttributes.getPrefix(j);
      if ((str2 == XMLSymbols.PREFIX_XMLNS) || ((str2 == XMLSymbols.EMPTY_STRING) && (str1 == XMLSymbols.PREFIX_XMLNS)))
      {
        str3 = fSymbolTable.addSymbol(paramXMLAttributes.getValue(j));
        if ((str2 == XMLSymbols.PREFIX_XMLNS) && (str1 == XMLSymbols.PREFIX_XMLNS)) {
          fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { paramXMLAttributes.getQName(j) }, (short)2);
        }
        if (str3 == NamespaceContext.XMLNS_URI) {
          fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[] { paramXMLAttributes.getQName(j) }, (short)2);
        }
        if (str1 == XMLSymbols.PREFIX_XML)
        {
          if (str3 != NamespaceContext.XML_URI) {
            fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { paramXMLAttributes.getQName(j) }, (short)2);
          }
        }
        else if (str3 == NamespaceContext.XML_URI) {
          fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[] { paramXMLAttributes.getQName(j) }, (short)2);
        }
        str2 = str1 != XMLSymbols.PREFIX_XMLNS ? str1 : XMLSymbols.EMPTY_STRING;
        fNamespaceContext.declarePrefix(str2, str3.length() != 0 ? str3 : null);
      }
    }
    String str1 = prefix != null ? prefix : XMLSymbols.EMPTY_STRING;
    uri = fNamespaceContext.getURI(str1);
    if ((prefix == null) && (uri != null)) {
      prefix = XMLSymbols.EMPTY_STRING;
    }
    if ((prefix != null) && (uri == null)) {
      fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[] { prefix, rawname }, (short)2);
    }
    for (int k = 0; k < i; k++)
    {
      paramXMLAttributes.getName(k, fAttributeQName);
      str3 = fAttributeQName.prefix != null ? fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
      String str4 = fAttributeQName.rawname;
      if (str4 == XMLSymbols.PREFIX_XMLNS)
      {
        fAttributeQName.uri = fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS);
        paramXMLAttributes.setName(k, fAttributeQName);
      }
      else if (str3 != XMLSymbols.EMPTY_STRING)
      {
        fAttributeQName.uri = fNamespaceContext.getURI(str3);
        if (fAttributeQName.uri == null) {
          fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[] { rawname, str4, str3 }, (short)2);
        }
        paramXMLAttributes.setName(k, fAttributeQName);
      }
    }
    int m = paramXMLAttributes.getLength();
    for (int n = 0; n < m - 1; n++)
    {
      String str5 = paramXMLAttributes.getURI(n);
      if ((str5 != null) && (str5 != NamespaceContext.XMLNS_URI))
      {
        String str6 = paramXMLAttributes.getLocalName(n);
        for (int i1 = n + 1; i1 < m; i1++)
        {
          String str7 = paramXMLAttributes.getLocalName(i1);
          String str8 = paramXMLAttributes.getURI(i1);
          if ((str6 == str7) && (str5 == str8)) {
            fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[] { rawname, str6, str5 }, (short)2);
          }
        }
      }
    }
  }
  
  protected void endNamespaceScope(QName paramQName, Augmentations paramAugmentations, boolean paramBoolean)
    throws XNIException
  {
    String str = prefix != null ? prefix : XMLSymbols.EMPTY_STRING;
    uri = fNamespaceContext.getURI(str);
    if (uri != null) {
      prefix = str;
    }
    if ((fDocumentHandler != null) && (!paramBoolean)) {
      fDocumentHandler.endElement(paramQName, paramAugmentations);
    }
    fNamespaceContext.popContext();
  }
}
