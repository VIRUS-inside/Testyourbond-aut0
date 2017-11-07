package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xalan.xsltc.runtime.AttributeList;
import org.apache.xml.serializer.ElemDesc;
import org.apache.xml.serializer.ToHTMLStream;



























final class LiteralElement
  extends Instruction
{
  private String _name;
  private LiteralElement _literalElemParent = null;
  private Vector _attributeElements = null;
  private Hashtable _accessedPrefixes = null;
  



  private boolean _allAttributesUnique = false;
  
  private static final String XMLNS_STRING = "xmlns";
  
  LiteralElement() {}
  
  public QName getName()
  {
    return _qname;
  }
  


  public void display(int indent)
  {
    indent(indent);
    Util.println("LiteralElement name = " + _name);
    displayContents(indent + 4);
  }
  


  private String accessedNamespace(String prefix)
  {
    if (_literalElemParent != null) {
      String result = _literalElemParent.accessedNamespace(prefix);
      if (result != null) {
        return result;
      }
    }
    return _accessedPrefixes != null ? (String)_accessedPrefixes.get(prefix) : null;
  }
  








  public void registerNamespace(String prefix, String uri, SymbolTable stable, boolean declared)
  {
    if (_literalElemParent != null) {
      String parentUri = _literalElemParent.accessedNamespace(prefix);
      if ((parentUri != null) && (parentUri.equals(uri))) {
        return;
      }
    }
    

    if (_accessedPrefixes == null) {
      _accessedPrefixes = new Hashtable();

    }
    else if (!declared)
    {
      String old = (String)_accessedPrefixes.get(prefix);
      if (old != null) {
        if (old.equals(uri)) {
          return;
        }
        prefix = stable.generateNamespacePrefix();
      }
    }
    

    if (!prefix.equals("xml")) {
      _accessedPrefixes.put(prefix, uri);
    }
  }
  





  private String translateQName(QName qname, SymbolTable stable)
  {
    String localname = qname.getLocalPart();
    String prefix = qname.getPrefix();
    

    if (prefix == null) {
      prefix = "";
    } else if (prefix.equals("xmlns")) {
      return "xmlns";
    }
    
    String alternative = stable.lookupPrefixAlias(prefix);
    if (alternative != null) {
      stable.excludeNamespaces(prefix);
      prefix = alternative;
    }
    

    String uri = lookupNamespace(prefix);
    if (uri == null) { return localname;
    }
    
    registerNamespace(prefix, uri, stable, false);
    

    if (prefix != "") {
      return prefix + ":" + localname;
    }
    return localname;
  }
  


  public void addAttribute(SyntaxTreeNode attribute)
  {
    if (_attributeElements == null) {
      _attributeElements = new Vector(2);
    }
    _attributeElements.add(attribute);
  }
  


  public void setFirstAttribute(SyntaxTreeNode attribute)
  {
    if (_attributeElements == null) {
      _attributeElements = new Vector(2);
    }
    _attributeElements.insertElementAt(attribute, 0);
  }
  



  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    if (_attributeElements != null) {
      int count = _attributeElements.size();
      for (int i = 0; i < count; i++) {
        SyntaxTreeNode node = (SyntaxTreeNode)_attributeElements.elementAt(i);
        
        node.typeCheck(stable);
      }
    }
    typeCheckContents(stable);
    return Type.Void;
  }
  




  public Enumeration getNamespaceScope(SyntaxTreeNode node)
  {
    Hashtable all = new Hashtable();
    
    while (node != null) {
      Hashtable mapping = node.getPrefixMapping();
      if (mapping != null) {
        Enumeration prefixes = mapping.keys();
        while (prefixes.hasMoreElements()) {
          String prefix = (String)prefixes.nextElement();
          if (!all.containsKey(prefix)) {
            all.put(prefix, mapping.get(prefix));
          }
        }
      }
      node = node.getParent();
    }
    return all.keys();
  }
  



  public void parseContents(Parser parser)
  {
    SymbolTable stable = parser.getSymbolTable();
    stable.setCurrentNode(this);
    

    SyntaxTreeNode parent = getParent();
    if ((parent != null) && ((parent instanceof LiteralElement))) {
      _literalElemParent = ((LiteralElement)parent);
    }
    
    _name = translateQName(_qname, stable);
    

    int count = _attributes.getLength();
    for (int i = 0; i < count; i++) {
      QName qname = parser.getQName(_attributes.getQName(i));
      String uri = qname.getNamespace();
      String val = _attributes.getValue(i);
      



      if (qname.equals(parser.getUseAttributeSets())) {
        if (!Util.isValidQNames(val)) {
          ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", val, this);
          parser.reportError(3, err);
        }
        setFirstAttribute(new UseAttributeSets(val, parser));

      }
      else if (qname.equals(parser.getExtensionElementPrefixes())) {
        stable.excludeNamespaces(val);

      }
      else if (qname.equals(parser.getExcludeResultPrefixes())) {
        stable.excludeNamespaces(val);
      }
      else
      {
        String prefix = qname.getPrefix();
        if (((prefix == null) || (!prefix.equals("xmlns"))) && ((prefix != null) || (!qname.getLocalPart().equals("xmlns"))) && ((uri == null) || (!uri.equals("http://www.w3.org/1999/XSL/Transform"))))
        {






          String name = translateQName(qname, stable);
          LiteralAttribute attr = new LiteralAttribute(name, val, parser, this);
          addAttribute(attr);
          attr.setParent(this);
          attr.parseContents(parser);
        }
      }
    }
    

    Enumeration include = getNamespaceScope(this);
    while (include.hasMoreElements()) {
      String prefix = (String)include.nextElement();
      if (!prefix.equals("xml")) {
        String uri = lookupNamespace(prefix);
        if ((uri != null) && (!stable.isExcludedNamespace(uri))) {
          registerNamespace(prefix, uri, stable, true);
        }
      }
    }
    
    parseChildren(parser);
    

    for (int i = 0; i < count; i++) {
      QName qname = parser.getQName(_attributes.getQName(i));
      String val = _attributes.getValue(i);
      

      if (qname.equals(parser.getExtensionElementPrefixes())) {
        stable.unExcludeNamespaces(val);

      }
      else if (qname.equals(parser.getExcludeResultPrefixes())) {
        stable.unExcludeNamespaces(val);
      }
    }
  }
  
  protected boolean contextDependent() {
    return dependentContents();
  }
  







  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    _allAttributesUnique = checkAttributesUnique();
    

    il.append(methodGen.loadHandler());
    
    il.append(new PUSH(cpg, _name));
    il.append(DUP2);
    il.append(methodGen.startElement());
    

    int j = 0;
    while (j < elementCount()) {
      SyntaxTreeNode item = (SyntaxTreeNode)elementAt(j);
      if ((item instanceof Variable)) {
        item.translate(classGen, methodGen);
      }
      j++;
    }
    

    if (_accessedPrefixes != null) {
      boolean declaresDefaultNS = false;
      Enumeration e = _accessedPrefixes.keys();
      
      while (e.hasMoreElements()) {
        String prefix = (String)e.nextElement();
        String uri = (String)_accessedPrefixes.get(prefix);
        
        if ((uri != "") || (prefix != ""))
        {

          if (prefix == "") {
            declaresDefaultNS = true;
          }
          il.append(methodGen.loadHandler());
          il.append(new PUSH(cpg, prefix));
          il.append(new PUSH(cpg, uri));
          il.append(methodGen.namespace());
        }
      }
      




      if ((!declaresDefaultNS) && ((_parent instanceof XslElement)) && (((XslElement)_parent).declaresDefaultNS()))
      {

        il.append(methodGen.loadHandler());
        il.append(new PUSH(cpg, ""));
        il.append(new PUSH(cpg, ""));
        il.append(methodGen.namespace());
      }
    }
    

    if (_attributeElements != null) {
      int count = _attributeElements.size();
      for (int i = 0; i < count; i++) {
        SyntaxTreeNode node = (SyntaxTreeNode)_attributeElements.elementAt(i);
        
        if (!(node instanceof XslAttribute)) {
          node.translate(classGen, methodGen);
        }
      }
    }
    

    translateContents(classGen, methodGen);
    

    il.append(methodGen.endElement());
  }
  


  private boolean isHTMLOutput()
  {
    return getStylesheet().getOutputMethod() == 2;
  }
  




  public ElemDesc getElemDesc()
  {
    if (isHTMLOutput()) {
      return ToHTMLStream.getElemDesc(_name);
    }
    
    return null;
  }
  


  public boolean allAttributesUnique()
  {
    return _allAttributesUnique;
  }
  


  private boolean checkAttributesUnique()
  {
    boolean hasHiddenXslAttribute = canProduceAttributeNodes(this, true);
    if (hasHiddenXslAttribute) {
      return false;
    }
    if (_attributeElements != null) {
      int numAttrs = _attributeElements.size();
      Hashtable attrsTable = null;
      for (int i = 0; i < numAttrs; i++) {
        SyntaxTreeNode node = (SyntaxTreeNode)_attributeElements.elementAt(i);
        
        if ((node instanceof UseAttributeSets)) {
          return false;
        }
        if ((node instanceof XslAttribute)) {
          if (attrsTable == null) {
            attrsTable = new Hashtable();
            for (int k = 0; k < i; k++) {
              SyntaxTreeNode n = (SyntaxTreeNode)_attributeElements.elementAt(k);
              if ((n instanceof LiteralAttribute)) {
                LiteralAttribute literalAttr = (LiteralAttribute)n;
                attrsTable.put(literalAttr.getName(), literalAttr);
              }
            }
          }
          
          XslAttribute xslAttr = (XslAttribute)node;
          AttributeValue attrName = xslAttr.getName();
          if ((attrName instanceof AttributeValueTemplate)) {
            return false;
          }
          if ((attrName instanceof SimpleAttributeValue)) {
            SimpleAttributeValue simpleAttr = (SimpleAttributeValue)attrName;
            String name = simpleAttr.toString();
            if ((name != null) && (attrsTable.get(name) != null))
              return false;
            if (name != null) {
              attrsTable.put(name, xslAttr);
            }
          }
        }
      }
    }
    return true;
  }
  





  private boolean canProduceAttributeNodes(SyntaxTreeNode node, boolean ignoreXslAttribute)
  {
    Vector contents = node.getContents();
    int size = contents.size();
    for (int i = 0; i < size; i++) {
      SyntaxTreeNode child = (SyntaxTreeNode)contents.elementAt(i);
      if ((child instanceof Text)) {
        Text text = (Text)child;
        if (!text.isIgnore())
        {

          return false;
        }
      }
      else {
        if (((child instanceof LiteralElement)) || ((child instanceof ValueOf)) || ((child instanceof XslElement)) || ((child instanceof Comment)) || ((child instanceof Number)) || ((child instanceof ProcessingInstruction)))
        {




          return false; }
        if ((child instanceof XslAttribute)) {
          if (!ignoreXslAttribute)
          {

            return true;
          }
          
        }
        else
        {
          if (((child instanceof CallTemplate)) || ((child instanceof ApplyTemplates)) || ((child instanceof Copy)) || ((child instanceof CopyOf)))
          {


            return true; }
          if ((((child instanceof If)) || ((child instanceof ForEach))) && (canProduceAttributeNodes(child, false)))
          {

            return true;
          }
          if ((child instanceof Choose)) {
            Vector chooseContents = child.getContents();
            int num = chooseContents.size();
            for (int k = 0; k < num; k++) {
              SyntaxTreeNode chooseChild = (SyntaxTreeNode)chooseContents.elementAt(k);
              if ((((chooseChild instanceof When)) || ((chooseChild instanceof Otherwise))) && 
                (canProduceAttributeNodes(chooseChild, false)))
                return true;
            }
          }
        }
      } }
    return false;
  }
}
