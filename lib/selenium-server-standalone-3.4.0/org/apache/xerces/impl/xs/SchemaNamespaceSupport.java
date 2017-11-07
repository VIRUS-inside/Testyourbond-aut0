package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.xs.opti.ElementImpl;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SchemaNamespaceSupport
  extends NamespaceSupport
{
  private SchemaRootContext fSchemaRootContext = null;
  
  public SchemaNamespaceSupport(Element paramElement, SymbolTable paramSymbolTable)
  {
    if ((paramElement != null) && (!(paramElement instanceof ElementImpl)))
    {
      Document localDocument = paramElement.getOwnerDocument();
      if ((localDocument != null) && (paramElement != localDocument.getDocumentElement())) {
        fSchemaRootContext = new SchemaRootContext(paramElement, paramSymbolTable);
      }
    }
  }
  
  public SchemaNamespaceSupport(SchemaNamespaceSupport paramSchemaNamespaceSupport)
  {
    fSchemaRootContext = fSchemaRootContext;
    fNamespaceSize = fNamespaceSize;
    if (fNamespace.length < fNamespaceSize) {
      fNamespace = new String[fNamespaceSize];
    }
    System.arraycopy(fNamespace, 0, fNamespace, 0, fNamespaceSize);
    fCurrentContext = fCurrentContext;
    if (fContext.length <= fCurrentContext) {
      fContext = new int[fCurrentContext + 1];
    }
    System.arraycopy(fContext, 0, fContext, 0, fCurrentContext + 1);
  }
  
  public void setEffectiveContext(String[] paramArrayOfString)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0)) {
      return;
    }
    pushContext();
    int i = fNamespaceSize + paramArrayOfString.length;
    if (fNamespace.length < i)
    {
      String[] arrayOfString = new String[i];
      System.arraycopy(fNamespace, 0, arrayOfString, 0, fNamespace.length);
      fNamespace = arrayOfString;
    }
    System.arraycopy(paramArrayOfString, 0, fNamespace, fNamespaceSize, paramArrayOfString.length);
    fNamespaceSize = i;
  }
  
  public String[] getEffectiveLocalContext()
  {
    String[] arrayOfString = null;
    if (fCurrentContext >= 3)
    {
      int i = fContext[3];
      int j = fNamespaceSize - i;
      if (j > 0)
      {
        arrayOfString = new String[j];
        System.arraycopy(fNamespace, i, arrayOfString, 0, j);
      }
    }
    return arrayOfString;
  }
  
  public void makeGlobal()
  {
    if (fCurrentContext >= 3)
    {
      fCurrentContext = 3;
      fNamespaceSize = fContext[3];
    }
  }
  
  public String getURI(String paramString)
  {
    String str = super.getURI(paramString);
    if ((str == null) && (fSchemaRootContext != null))
    {
      if (!fSchemaRootContext.fDOMContextBuilt)
      {
        fSchemaRootContext.fillNamespaceContext();
        fSchemaRootContext.fDOMContextBuilt = true;
      }
      if ((fSchemaRootContext.fNamespaceSize > 0) && (!containsPrefix(paramString))) {
        str = fSchemaRootContext.getURI(paramString);
      }
    }
    return str;
  }
  
  static final class SchemaRootContext
  {
    String[] fNamespace = new String[32];
    int fNamespaceSize = 0;
    boolean fDOMContextBuilt = false;
    private final Element fSchemaRoot;
    private final SymbolTable fSymbolTable;
    private final QName fAttributeQName = new QName();
    
    SchemaRootContext(Element paramElement, SymbolTable paramSymbolTable)
    {
      fSchemaRoot = paramElement;
      fSymbolTable = paramSymbolTable;
    }
    
    void fillNamespaceContext()
    {
      if (fSchemaRoot != null) {
        for (Node localNode = fSchemaRoot.getParentNode(); localNode != null; localNode = localNode.getParentNode()) {
          if (1 == localNode.getNodeType())
          {
            NamedNodeMap localNamedNodeMap = localNode.getAttributes();
            int i = localNamedNodeMap.getLength();
            for (int j = 0; j < i; j++)
            {
              Attr localAttr = (Attr)localNamedNodeMap.item(j);
              String str = localAttr.getValue();
              if (str == null) {
                str = XMLSymbols.EMPTY_STRING;
              }
              fillQName(fAttributeQName, localAttr);
              if (fAttributeQName.uri == NamespaceContext.XMLNS_URI) {
                if (fAttributeQName.prefix == XMLSymbols.PREFIX_XMLNS) {
                  declarePrefix(fAttributeQName.localpart, str.length() != 0 ? fSymbolTable.addSymbol(str) : null);
                } else {
                  declarePrefix(XMLSymbols.EMPTY_STRING, str.length() != 0 ? fSymbolTable.addSymbol(str) : null);
                }
              }
            }
          }
        }
      }
    }
    
    String getURI(String paramString)
    {
      for (int i = 0; i < fNamespaceSize; i += 2) {
        if (fNamespace[i] == paramString) {
          return fNamespace[(i + 1)];
        }
      }
      return null;
    }
    
    private void declarePrefix(String paramString1, String paramString2)
    {
      if (fNamespaceSize == fNamespace.length)
      {
        String[] arrayOfString = new String[fNamespaceSize * 2];
        System.arraycopy(fNamespace, 0, arrayOfString, 0, fNamespaceSize);
        fNamespace = arrayOfString;
      }
      fNamespace[(fNamespaceSize++)] = paramString1;
      fNamespace[(fNamespaceSize++)] = paramString2;
    }
    
    private void fillQName(QName paramQName, Node paramNode)
    {
      String str1 = paramNode.getPrefix();
      String str2 = paramNode.getLocalName();
      String str3 = paramNode.getNodeName();
      String str4 = paramNode.getNamespaceURI();
      prefix = (str1 != null ? fSymbolTable.addSymbol(str1) : XMLSymbols.EMPTY_STRING);
      localpart = (str2 != null ? fSymbolTable.addSymbol(str2) : XMLSymbols.EMPTY_STRING);
      rawname = (str3 != null ? fSymbolTable.addSymbol(str3) : XMLSymbols.EMPTY_STRING);
      uri = ((str4 != null) && (str4.length() > 0) ? fSymbolTable.addSymbol(str4) : null);
    }
  }
}
