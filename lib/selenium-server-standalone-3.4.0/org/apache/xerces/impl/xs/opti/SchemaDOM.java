package org.apache.xerces.impl.xs.opti;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLString;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SchemaDOM
  extends DefaultDocument
{
  static final int relationsRowResizeFactor = 15;
  static final int relationsColResizeFactor = 10;
  NodeImpl[][] relations;
  ElementImpl parent;
  int currLoc;
  int nextFreeLoc;
  boolean hidden;
  boolean inCDATA;
  private StringBuffer fAnnotationBuffer = null;
  
  public SchemaDOM()
  {
    reset();
  }
  
  public ElementImpl startElement(QName paramQName, XMLAttributes paramXMLAttributes, int paramInt1, int paramInt2, int paramInt3)
  {
    ElementImpl localElementImpl = new ElementImpl(paramInt1, paramInt2, paramInt3);
    processElement(paramQName, paramXMLAttributes, localElementImpl);
    parent = localElementImpl;
    return localElementImpl;
  }
  
  public ElementImpl emptyElement(QName paramQName, XMLAttributes paramXMLAttributes, int paramInt1, int paramInt2, int paramInt3)
  {
    ElementImpl localElementImpl = new ElementImpl(paramInt1, paramInt2, paramInt3);
    processElement(paramQName, paramXMLAttributes, localElementImpl);
    return localElementImpl;
  }
  
  public ElementImpl startElement(QName paramQName, XMLAttributes paramXMLAttributes, int paramInt1, int paramInt2)
  {
    return startElement(paramQName, paramXMLAttributes, paramInt1, paramInt2, -1);
  }
  
  public ElementImpl emptyElement(QName paramQName, XMLAttributes paramXMLAttributes, int paramInt1, int paramInt2)
  {
    return emptyElement(paramQName, paramXMLAttributes, paramInt1, paramInt2, -1);
  }
  
  private void processElement(QName paramQName, XMLAttributes paramXMLAttributes, ElementImpl paramElementImpl)
  {
    prefix = prefix;
    localpart = localpart;
    rawname = rawname;
    uri = uri;
    schemaDOM = this;
    Attr[] arrayOfAttr = new Attr[paramXMLAttributes.getLength()];
    for (int i = 0; i < paramXMLAttributes.getLength(); i++) {
      arrayOfAttr[i] = new AttrImpl(paramElementImpl, paramXMLAttributes.getPrefix(i), paramXMLAttributes.getLocalName(i), paramXMLAttributes.getQName(i), paramXMLAttributes.getURI(i), paramXMLAttributes.getValue(i));
    }
    attrs = arrayOfAttr;
    if (nextFreeLoc == relations.length) {
      resizeRelations();
    }
    if (relations[currLoc][0] != parent)
    {
      relations[nextFreeLoc][0] = parent;
      currLoc = (nextFreeLoc++);
    }
    int j = 0;
    int k = 1;
    for (k = 1; k < relations[currLoc].length; k++) {
      if (relations[currLoc][k] == null)
      {
        j = 1;
        break;
      }
    }
    if (j == 0) {
      resizeRelations(currLoc);
    }
    relations[currLoc][k] = paramElementImpl;
    parent.parentRow = currLoc;
    row = currLoc;
    col = k;
  }
  
  public void endElement()
  {
    currLoc = parent.row;
    parent = ((ElementImpl)relations[currLoc][0]);
  }
  
  void comment(XMLString paramXMLString)
  {
    fAnnotationBuffer.append("<!--");
    if (length > 0) {
      fAnnotationBuffer.append(ch, offset, length);
    }
    fAnnotationBuffer.append("-->");
  }
  
  void processingInstruction(String paramString, XMLString paramXMLString)
  {
    fAnnotationBuffer.append("<?").append(paramString);
    if (length > 0) {
      fAnnotationBuffer.append(' ').append(ch, offset, length);
    }
    fAnnotationBuffer.append("?>");
  }
  
  void characters(XMLString paramXMLString)
  {
    if (!inCDATA)
    {
      StringBuffer localStringBuffer = fAnnotationBuffer;
      for (int i = offset; i < offset + length; i++)
      {
        char c = ch[i];
        if (c == '&') {
          localStringBuffer.append("&amp;");
        } else if (c == '<') {
          localStringBuffer.append("&lt;");
        } else if (c == '>') {
          localStringBuffer.append("&gt;");
        } else if (c == '\r') {
          localStringBuffer.append("&#xD;");
        } else {
          localStringBuffer.append(c);
        }
      }
    }
    else
    {
      fAnnotationBuffer.append(ch, offset, length);
    }
  }
  
  void charactersRaw(String paramString)
  {
    fAnnotationBuffer.append(paramString);
  }
  
  void endAnnotation(QName paramQName, ElementImpl paramElementImpl)
  {
    fAnnotationBuffer.append("\n</").append(rawname).append(">");
    fAnnotation = fAnnotationBuffer.toString();
    fAnnotationBuffer = null;
  }
  
  void endAnnotationElement(QName paramQName)
  {
    endAnnotationElement(rawname);
  }
  
  void endAnnotationElement(String paramString)
  {
    fAnnotationBuffer.append("</").append(paramString).append(">");
  }
  
  void endSyntheticAnnotationElement(QName paramQName, boolean paramBoolean)
  {
    endSyntheticAnnotationElement(rawname, paramBoolean);
  }
  
  void endSyntheticAnnotationElement(String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      fAnnotationBuffer.append("\n</").append(paramString).append(">");
      parent.fSyntheticAnnotation = fAnnotationBuffer.toString();
      fAnnotationBuffer = null;
    }
    else
    {
      fAnnotationBuffer.append("</").append(paramString).append(">");
    }
  }
  
  void startAnnotationCDATA()
  {
    inCDATA = true;
    fAnnotationBuffer.append("<![CDATA[");
  }
  
  void endAnnotationCDATA()
  {
    fAnnotationBuffer.append("]]>");
    inCDATA = false;
  }
  
  private void resizeRelations()
  {
    NodeImpl[][] arrayOfNodeImpl; = new NodeImpl[relations.length + 15][];
    System.arraycopy(relations, 0, arrayOfNodeImpl;, 0, relations.length);
    for (int i = relations.length; i < arrayOfNodeImpl;.length; i++) {
      arrayOfNodeImpl;[i] = new NodeImpl[10];
    }
    relations = arrayOfNodeImpl;;
  }
  
  private void resizeRelations(int paramInt)
  {
    NodeImpl[] arrayOfNodeImpl = new NodeImpl[relations[paramInt].length + 10];
    System.arraycopy(relations[paramInt], 0, arrayOfNodeImpl, 0, relations[paramInt].length);
    relations[paramInt] = arrayOfNodeImpl;
  }
  
  public void reset()
  {
    if (relations != null) {
      for (int i = 0; i < relations.length; i++) {
        for (int j = 0; j < relations[i].length; j++) {
          relations[i][j] = null;
        }
      }
    }
    relations = new NodeImpl[15][];
    parent = new ElementImpl(0, 0, 0);
    parent.rawname = "DOCUMENT_NODE";
    currLoc = 0;
    nextFreeLoc = 1;
    inCDATA = false;
    for (int k = 0; k < 15; k++) {
      relations[k] = new NodeImpl[10];
    }
    relations[currLoc][0] = parent;
  }
  
  public void printDOM() {}
  
  public static void traverse(Node paramNode, int paramInt)
  {
    indent(paramInt);
    System.out.print("<" + paramNode.getNodeName());
    Object localObject;
    if (paramNode.hasAttributes())
    {
      localObject = paramNode.getAttributes();
      for (int i = 0; i < ((NamedNodeMap)localObject).getLength(); i++) {
        System.out.print("  " + ((Attr)((NamedNodeMap)localObject).item(i)).getName() + "=\"" + ((Attr)((NamedNodeMap)localObject).item(i)).getValue() + "\"");
      }
    }
    if (paramNode.hasChildNodes())
    {
      System.out.println(">");
      paramInt += 4;
      for (localObject = paramNode.getFirstChild(); localObject != null; localObject = ((Node)localObject).getNextSibling()) {
        traverse((Node)localObject, paramInt);
      }
      paramInt -= 4;
      indent(paramInt);
      System.out.println("</" + paramNode.getNodeName() + ">");
    }
    else
    {
      System.out.println("/>");
    }
  }
  
  public static void indent(int paramInt)
  {
    for (int i = 0; i < paramInt; i++) {
      System.out.print(' ');
    }
  }
  
  public Element getDocumentElement()
  {
    return (ElementImpl)relations[0][1];
  }
  
  public DOMImplementation getImplementation()
  {
    return SchemaDOMImplementation.getDOMImplementation();
  }
  
  void startAnnotation(QName paramQName, XMLAttributes paramXMLAttributes, NamespaceContext paramNamespaceContext)
  {
    startAnnotation(rawname, paramXMLAttributes, paramNamespaceContext);
  }
  
  void startAnnotation(String paramString, XMLAttributes paramXMLAttributes, NamespaceContext paramNamespaceContext)
  {
    if (fAnnotationBuffer == null) {
      fAnnotationBuffer = new StringBuffer(256);
    }
    fAnnotationBuffer.append("<").append(paramString).append(" ");
    ArrayList localArrayList = new ArrayList();
    String str1;
    String str2;
    for (int i = 0; i < paramXMLAttributes.getLength(); i++)
    {
      localObject = paramXMLAttributes.getValue(i);
      str1 = paramXMLAttributes.getPrefix(i);
      str2 = paramXMLAttributes.getQName(i);
      if ((str1 == XMLSymbols.PREFIX_XMLNS) || (str2 == XMLSymbols.PREFIX_XMLNS)) {
        localArrayList.add(str1 == XMLSymbols.PREFIX_XMLNS ? paramXMLAttributes.getLocalName(i) : XMLSymbols.EMPTY_STRING);
      }
      fAnnotationBuffer.append(str2).append("=\"").append(processAttValue((String)localObject)).append("\" ");
    }
    Object localObject = paramNamespaceContext.getAllPrefixes();
    while (((Enumeration)localObject).hasMoreElements())
    {
      str1 = (String)((Enumeration)localObject).nextElement();
      str2 = paramNamespaceContext.getURI(str1);
      if (str2 == null) {
        str2 = XMLSymbols.EMPTY_STRING;
      }
      if (!localArrayList.contains(str1)) {
        if (str1 == XMLSymbols.EMPTY_STRING) {
          fAnnotationBuffer.append("xmlns").append("=\"").append(processAttValue(str2)).append("\" ");
        } else {
          fAnnotationBuffer.append("xmlns:").append(str1).append("=\"").append(processAttValue(str2)).append("\" ");
        }
      }
    }
    fAnnotationBuffer.append(">\n");
  }
  
  void startAnnotationElement(QName paramQName, XMLAttributes paramXMLAttributes)
  {
    startAnnotationElement(rawname, paramXMLAttributes);
  }
  
  void startAnnotationElement(String paramString, XMLAttributes paramXMLAttributes)
  {
    fAnnotationBuffer.append("<").append(paramString);
    for (int i = 0; i < paramXMLAttributes.getLength(); i++)
    {
      String str = paramXMLAttributes.getValue(i);
      fAnnotationBuffer.append(" ").append(paramXMLAttributes.getQName(i)).append("=\"").append(processAttValue(str)).append("\"");
    }
    fAnnotationBuffer.append(">");
  }
  
  private static String processAttValue(String paramString)
  {
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      int k = paramString.charAt(j);
      if ((k == 34) || (k == 60) || (k == 38) || (k == 9) || (k == 10) || (k == 13)) {
        return escapeAttValue(paramString, j);
      }
    }
    return paramString;
  }
  
  private static String escapeAttValue(String paramString, int paramInt)
  {
    int j = paramString.length();
    StringBuffer localStringBuffer = new StringBuffer(j);
    localStringBuffer.append(paramString.substring(0, paramInt));
    for (int i = paramInt; i < j; i++)
    {
      char c = paramString.charAt(i);
      if (c == '"') {
        localStringBuffer.append("&quot;");
      } else if (c == '<') {
        localStringBuffer.append("&lt;");
      } else if (c == '&') {
        localStringBuffer.append("&amp;");
      } else if (c == '\t') {
        localStringBuffer.append("&#x9;");
      } else if (c == '\n') {
        localStringBuffer.append("&#xA;");
      } else if (c == '\r') {
        localStringBuffer.append("&#xD;");
      } else {
        localStringBuffer.append(c);
      }
    }
    return localStringBuffer.toString();
  }
}
