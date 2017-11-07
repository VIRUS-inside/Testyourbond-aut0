package org.jsoup.nodes;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;




public class Document
  extends Element
{
  private OutputSettings outputSettings = new OutputSettings();
  private QuirksMode quirksMode = QuirksMode.noQuirks;
  private String location;
  private boolean updateMetaCharset = false;
  





  public Document(String baseUri)
  {
    super(Tag.valueOf("#root", ParseSettings.htmlDefault), baseUri);
    location = baseUri;
  }
  




  public static Document createShell(String baseUri)
  {
    Validate.notNull(baseUri);
    
    Document doc = new Document(baseUri);
    Element html = doc.appendElement("html");
    html.appendElement("head");
    html.appendElement("body");
    
    return doc;
  }
  




  public String location()
  {
    return location;
  }
  



  public Element head()
  {
    return findFirstElementByTagName("head", this);
  }
  



  public Element body()
  {
    return findFirstElementByTagName("body", this);
  }
  




  public String title()
  {
    Element titleEl = getElementsByTag("title").first();
    return titleEl != null ? StringUtil.normaliseWhitespace(titleEl.text()).trim() : "";
  }
  




  public void title(String title)
  {
    Validate.notNull(title);
    Element titleEl = getElementsByTag("title").first();
    if (titleEl == null) {
      head().appendElement("title").text(title);
    } else {
      titleEl.text(title);
    }
  }
  




  public Element createElement(String tagName)
  {
    return new Element(Tag.valueOf(tagName, ParseSettings.preserveCase), baseUri());
  }
  




  public Document normalise()
  {
    Element htmlEl = findFirstElementByTagName("html", this);
    if (htmlEl == null)
      htmlEl = appendElement("html");
    if (head() == null)
      htmlEl.prependElement("head");
    if (body() == null) {
      htmlEl.appendElement("body");
    }
    

    normaliseTextNodes(head());
    normaliseTextNodes(htmlEl);
    normaliseTextNodes(this);
    
    normaliseStructure("head", htmlEl);
    normaliseStructure("body", htmlEl);
    
    ensureMetaCharsetElement();
    
    return this;
  }
  
  private void normaliseTextNodes(Element element)
  {
    List<Node> toMove = new ArrayList();
    for (Node node : childNodes) {
      if ((node instanceof TextNode)) {
        TextNode tn = (TextNode)node;
        if (!tn.isBlank()) {
          toMove.add(tn);
        }
      }
    }
    for (int i = toMove.size() - 1; i >= 0; i--) {
      Node node = (Node)toMove.get(i);
      element.removeChild(node);
      body().prependChild(new TextNode(" ", ""));
      body().prependChild(node);
    }
  }
  
  private void normaliseStructure(String tag, Element htmlEl)
  {
    Elements elements = getElementsByTag(tag);
    Element master = elements.first();
    int i; if (elements.size() > 1) {
      List<Node> toMove = new ArrayList();
      for (i = 1; i < elements.size(); i++) {
        Node dupe = (Node)elements.get(i);
        for (Node node : childNodes)
          toMove.add(node);
        dupe.remove();
      }
      
      for (Node dupe : toMove) {
        master.appendChild(dupe);
      }
    }
    if (!master.parent().equals(htmlEl)) {
      htmlEl.appendChild(master);
    }
  }
  
  private Element findFirstElementByTagName(String tag, Node node)
  {
    if (node.nodeName().equals(tag)) {
      return (Element)node;
    }
    for (Node child : childNodes) {
      Element found = findFirstElementByTagName(tag, child);
      if (found != null) {
        return found;
      }
    }
    return null;
  }
  
  public String outerHtml()
  {
    return super.html();
  }
  





  public Element text(String text)
  {
    body().text(text);
    return this;
  }
  
  public String nodeName()
  {
    return "#document";
  }
  























  public void charset(Charset charset)
  {
    updateMetaCharsetElement(true);
    outputSettings.charset(charset);
    ensureMetaCharsetElement();
  }
  







  public Charset charset()
  {
    return outputSettings.charset();
  }
  












  public void updateMetaCharsetElement(boolean update)
  {
    updateMetaCharset = update;
  }
  







  public boolean updateMetaCharsetElement()
  {
    return updateMetaCharset;
  }
  
  public Document clone()
  {
    Document clone = (Document)super.clone();
    outputSettings = outputSettings.clone();
    return clone;
  }
  


















  private void ensureMetaCharsetElement()
  {
    if (updateMetaCharset) {
      Document.OutputSettings.Syntax syntax = outputSettings().syntax();
      
      if (syntax == Document.OutputSettings.Syntax.html) {
        Element metaCharset = select("meta[charset]").first();
        
        if (metaCharset != null) {
          metaCharset.attr("charset", charset().displayName());
        } else {
          Element head = head();
          
          if (head != null) {
            head.appendElement("meta").attr("charset", charset().displayName());
          }
        }
        

        select("meta[name=charset]").remove();
      } else if (syntax == Document.OutputSettings.Syntax.xml) {
        Node node = (Node)childNodes().get(0);
        
        if ((node instanceof XmlDeclaration)) {
          XmlDeclaration decl = (XmlDeclaration)node;
          
          if (decl.name().equals("xml")) {
            decl.attr("encoding", charset().displayName());
            
            String version = decl.attr("version");
            
            if (version != null) {
              decl.attr("version", "1.0");
            }
          } else {
            decl = new XmlDeclaration("xml", baseUri, false);
            decl.attr("version", "1.0");
            decl.attr("encoding", charset().displayName());
            
            prependChild(decl);
          }
        } else {
          XmlDeclaration decl = new XmlDeclaration("xml", baseUri, false);
          decl.attr("version", "1.0");
          decl.attr("encoding", charset().displayName());
          
          prependChild(decl);
        }
      }
    }
  }
  

  public static class OutputSettings
    implements Cloneable
  {
    public OutputSettings() {}
    
    public static enum Syntax
    {
      html,  xml;
      private Syntax() {} }
    private Entities.EscapeMode escapeMode = Entities.EscapeMode.base;
    private Charset charset = Charset.forName("UTF-8");
    private boolean prettyPrint = true;
    private boolean outline = false;
    private int indentAmount = 1;
    private Syntax syntax = Syntax.html;
    









    public Entities.EscapeMode escapeMode()
    {
      return escapeMode;
    }
    





    public OutputSettings escapeMode(Entities.EscapeMode escapeMode)
    {
      this.escapeMode = escapeMode;
      return this;
    }
    







    public Charset charset()
    {
      return charset;
    }
    




    public OutputSettings charset(Charset charset)
    {
      this.charset = charset;
      return this;
    }
    




    public OutputSettings charset(String charset)
    {
      charset(Charset.forName(charset));
      return this;
    }
    
    CharsetEncoder encoder() {
      return charset.newEncoder();
    }
    



    public Syntax syntax()
    {
      return syntax;
    }
    





    public OutputSettings syntax(Syntax syntax)
    {
      this.syntax = syntax;
      return this;
    }
    




    public boolean prettyPrint()
    {
      return prettyPrint;
    }
    




    public OutputSettings prettyPrint(boolean pretty)
    {
      prettyPrint = pretty;
      return this;
    }
    




    public boolean outline()
    {
      return outline;
    }
    




    public OutputSettings outline(boolean outlineMode)
    {
      outline = outlineMode;
      return this;
    }
    



    public int indentAmount()
    {
      return indentAmount;
    }
    




    public OutputSettings indentAmount(int indentAmount)
    {
      Validate.isTrue(indentAmount >= 0);
      this.indentAmount = indentAmount;
      return this;
    }
    
    public OutputSettings clone()
    {
      try
      {
        clone = (OutputSettings)super.clone();
      } catch (CloneNotSupportedException e) { OutputSettings clone;
        throw new RuntimeException(e); }
      OutputSettings clone;
      clone.charset(charset.name());
      escapeMode = Entities.EscapeMode.valueOf(escapeMode.name());
      
      return clone;
    }
  }
  



  public OutputSettings outputSettings()
  {
    return outputSettings;
  }
  




  public Document outputSettings(OutputSettings outputSettings)
  {
    Validate.notNull(outputSettings);
    this.outputSettings = outputSettings;
    return this;
  }
  
  public static enum QuirksMode {
    noQuirks,  quirks,  limitedQuirks;
    
    private QuirksMode() {} }
  
  public QuirksMode quirksMode() { return quirksMode; }
  
  public Document quirksMode(QuirksMode quirksMode)
  {
    this.quirksMode = quirksMode;
    return this;
  }
}
