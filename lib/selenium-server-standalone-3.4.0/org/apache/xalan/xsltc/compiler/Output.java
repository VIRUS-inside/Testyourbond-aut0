package org.apache.xalan.xsltc.compiler;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.serializer.Encodings;
import org.apache.xml.utils.XML11Char;
































final class Output
  extends TopLevelElement
{
  private String _version;
  private String _method;
  private String _encoding;
  private boolean _omitHeader = false;
  private String _standalone;
  private String _doctypePublic;
  private String _doctypeSystem;
  private String _cdata;
  private boolean _indent = false;
  
  private String _mediaType;
  
  private String _indentamount;
  private boolean _disabled = false;
  
  private static final String STRING_SIG = "Ljava/lang/String;";
  
  private static final String XML_VERSION = "1.0";
  private static final String HTML_VERSION = "4.0";
  
  Output() {}
  
  public void display(int indent)
  {
    indent(indent);
    Util.println("Output " + _method);
  }
  




  public void disable()
  {
    _disabled = true;
  }
  
  public boolean enabled() {
    return !_disabled;
  }
  
  public String getCdata() {
    return _cdata;
  }
  
  public String getOutputMethod() {
    return _method;
  }
  
  private void transferAttribute(Output previous, String qname) {
    if ((!hasAttribute(qname)) && (previous.hasAttribute(qname))) {
      addAttribute(qname, previous.getAttribute(qname));
    }
  }
  
  public void mergeOutput(Output previous)
  {
    transferAttribute(previous, "version");
    transferAttribute(previous, "method");
    transferAttribute(previous, "encoding");
    transferAttribute(previous, "doctype-system");
    transferAttribute(previous, "doctype-public");
    transferAttribute(previous, "media-type");
    transferAttribute(previous, "indent");
    transferAttribute(previous, "omit-xml-declaration");
    transferAttribute(previous, "standalone");
    

    if (previous.hasAttribute("cdata-section-elements"))
    {
      addAttribute("cdata-section-elements", previous.getAttribute("cdata-section-elements") + ' ' + getAttribute("cdata-section-elements"));
    }
    



    String prefix = lookupPrefix("http://xml.apache.org/xalan");
    if (prefix != null) {
      transferAttribute(previous, prefix + ':' + "indent-amount");
    }
    prefix = lookupPrefix("http://xml.apache.org/xslt");
    if (prefix != null) {
      transferAttribute(previous, prefix + ':' + "indent-amount");
    }
  }
  


  public void parseContents(Parser parser)
  {
    Properties outputProperties = new Properties();
    

    parser.setOutput(this);
    

    if (_disabled) { return;
    }
    String attrib = null;
    

    _version = getAttribute("version");
    if (_version.equals("")) {
      _version = null;
    }
    else {
      outputProperties.setProperty("version", _version);
    }
    

    _method = getAttribute("method");
    if (_method.equals("")) {
      _method = null;
    }
    if (_method != null) {
      _method = _method.toLowerCase();
      if ((_method.equals("xml")) || (_method.equals("html")) || (_method.equals("text")) || ((XML11Char.isXML11ValidQName(_method)) && (_method.indexOf(":") > 0)))
      {


        outputProperties.setProperty("method", _method);
      } else {
        reportError(this, parser, "INVALID_METHOD_IN_OUTPUT", _method);
      }
    }
    

    _encoding = getAttribute("encoding");
    if (_encoding.equals("")) {
      _encoding = null;
    }
    else
    {
      try
      {
        String canonicalEncoding = Encodings.convertMime2JavaEncoding(_encoding);
        writer = new OutputStreamWriter(System.out, canonicalEncoding);
      }
      catch (UnsupportedEncodingException e) {
        OutputStreamWriter writer;
        ErrorMsg msg = new ErrorMsg("UNSUPPORTED_ENCODING", _encoding, this);
        
        parser.reportError(4, msg);
      }
      outputProperties.setProperty("encoding", _encoding);
    }
    

    attrib = getAttribute("omit-xml-declaration");
    if (!attrib.equals("")) {
      if (attrib.equals("yes")) {
        _omitHeader = true;
      }
      outputProperties.setProperty("omit-xml-declaration", attrib);
    }
    

    _standalone = getAttribute("standalone");
    if (_standalone.equals("")) {
      _standalone = null;
    }
    else {
      outputProperties.setProperty("standalone", _standalone);
    }
    

    _doctypeSystem = getAttribute("doctype-system");
    if (_doctypeSystem.equals("")) {
      _doctypeSystem = null;
    }
    else {
      outputProperties.setProperty("doctype-system", _doctypeSystem);
    }
    

    _doctypePublic = getAttribute("doctype-public");
    if (_doctypePublic.equals("")) {
      _doctypePublic = null;
    }
    else {
      outputProperties.setProperty("doctype-public", _doctypePublic);
    }
    

    _cdata = getAttribute("cdata-section-elements");
    if (_cdata.equals("")) {
      _cdata = null;
    }
    else {
      StringBuffer expandedNames = new StringBuffer();
      StringTokenizer tokens = new StringTokenizer(_cdata);
      

      while (tokens.hasMoreTokens()) {
        String qname = tokens.nextToken();
        if (!XML11Char.isXML11ValidQName(qname)) {
          ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", qname, this);
          parser.reportError(3, err);
        }
        expandedNames.append(parser.getQName(qname).toString()).append(' ');
      }
      
      _cdata = expandedNames.toString();
      outputProperties.setProperty("cdata-section-elements", _cdata);
    }
    


    attrib = getAttribute("indent");
    if (!attrib.equals("")) {
      if (attrib.equals("yes")) {
        _indent = true;
      }
      outputProperties.setProperty("indent", attrib);
    }
    else if ((_method != null) && (_method.equals("html"))) {
      _indent = true;
    }
    

    _indentamount = getAttribute(lookupPrefix("http://xml.apache.org/xalan"), "indent-amount");
    

    if (_indentamount.equals("")) {
      _indentamount = getAttribute(lookupPrefix("http://xml.apache.org/xslt"), "indent-amount");
    }
    
    if (!_indentamount.equals("")) {
      outputProperties.setProperty("indent_amount", _indentamount);
    }
    

    _mediaType = getAttribute("media-type");
    if (_mediaType.equals("")) {
      _mediaType = null;
    }
    else {
      outputProperties.setProperty("media-type", _mediaType);
    }
    

    if (_method != null) {
      if (_method.equals("html")) {
        if (_version == null) {
          _version = "4.0";
        }
        if (_mediaType == null) {
          _mediaType = "text/html";
        }
      }
      else if ((_method.equals("text")) && 
        (_mediaType == null)) {
        _mediaType = "text/plain";
      }
    }
    


    parser.getCurrentStylesheet().setOutputProperties(outputProperties);
  }
  





  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    if (_disabled) { return;
    }
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    int field = 0;
    il.append(classGen.loadTranslet());
    

    if ((_version != null) && (!_version.equals("1.0"))) {
      field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_version", "Ljava/lang/String;");
      il.append(DUP);
      il.append(new PUSH(cpg, _version));
      il.append(new PUTFIELD(field));
    }
    

    if (_method != null) {
      field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_method", "Ljava/lang/String;");
      il.append(DUP);
      il.append(new PUSH(cpg, _method));
      il.append(new PUTFIELD(field));
    }
    

    if (_encoding != null) {
      field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_encoding", "Ljava/lang/String;");
      il.append(DUP);
      il.append(new PUSH(cpg, _encoding));
      il.append(new PUTFIELD(field));
    }
    

    if (_omitHeader) {
      field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_omitHeader", "Z");
      il.append(DUP);
      il.append(new PUSH(cpg, _omitHeader));
      il.append(new PUTFIELD(field));
    }
    

    if (_standalone != null) {
      field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_standalone", "Ljava/lang/String;");
      il.append(DUP);
      il.append(new PUSH(cpg, _standalone));
      il.append(new PUTFIELD(field));
    }
    

    field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_doctypeSystem", "Ljava/lang/String;");
    il.append(DUP);
    il.append(new PUSH(cpg, _doctypeSystem));
    il.append(new PUTFIELD(field));
    field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_doctypePublic", "Ljava/lang/String;");
    il.append(DUP);
    il.append(new PUSH(cpg, _doctypePublic));
    il.append(new PUTFIELD(field));
    

    if (_mediaType != null) {
      field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_mediaType", "Ljava/lang/String;");
      il.append(DUP);
      il.append(new PUSH(cpg, _mediaType));
      il.append(new PUTFIELD(field));
    }
    

    if (_indent) {
      field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_indent", "Z");
      il.append(DUP);
      il.append(new PUSH(cpg, _indent));
      il.append(new PUTFIELD(field));
    }
    

    if ((_indentamount != null) && (!_indentamount.equals(""))) {
      field = cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "_indentamount", "I");
      il.append(DUP);
      il.append(new PUSH(cpg, Integer.parseInt(_indentamount)));
      il.append(new PUTFIELD(field));
    }
    

    if (_cdata != null) {
      int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "addCdataElement", "(Ljava/lang/String;)V");
      


      StringTokenizer tokens = new StringTokenizer(_cdata);
      while (tokens.hasMoreTokens()) {
        il.append(DUP);
        il.append(new PUSH(cpg, tokens.nextToken()));
        il.append(new INVOKEVIRTUAL(index));
      }
    }
    il.append(POP);
  }
}
