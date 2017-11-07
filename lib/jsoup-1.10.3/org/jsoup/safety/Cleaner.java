package org.jsoup.safety;

import java.util.List;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;




















public class Cleaner
{
  private Whitelist whitelist;
  
  public Cleaner(Whitelist whitelist)
  {
    Validate.notNull(whitelist);
    this.whitelist = whitelist;
  }
  





  public Document clean(Document dirtyDocument)
  {
    Validate.notNull(dirtyDocument);
    
    Document clean = Document.createShell(dirtyDocument.baseUri());
    if (dirtyDocument.body() != null) {
      copySafeNodes(dirtyDocument.body(), clean.body());
    }
    return clean;
  }
  










  public boolean isValid(Document dirtyDocument)
  {
    Validate.notNull(dirtyDocument);
    
    Document clean = Document.createShell(dirtyDocument.baseUri());
    int numDiscarded = copySafeNodes(dirtyDocument.body(), clean.body());
    
    return (numDiscarded == 0) && (dirtyDocument.head().childNodes().size() == 0);
  }
  
  public boolean isValidBodyHtml(String bodyHtml) {
    Document clean = Document.createShell("");
    Document dirty = Document.createShell("");
    ParseErrorList errorList = ParseErrorList.tracking(1);
    List<Node> nodes = Parser.parseFragment(bodyHtml, dirty.body(), "", errorList);
    dirty.body().insertChildren(0, nodes);
    int numDiscarded = copySafeNodes(dirty.body(), clean.body());
    return (numDiscarded == 0) && (errorList.size() == 0);
  }
  

  private final class CleaningVisitor
    implements NodeVisitor
  {
    private int numDiscarded = 0;
    private final Element root;
    private Element destination;
    
    private CleaningVisitor(Element root, Element destination) {
      this.root = root;
      this.destination = destination;
    }
    
    public void head(Node source, int depth) {
      if ((source instanceof Element)) {
        Element sourceEl = (Element)source;
        
        if (whitelist.isSafeTag(sourceEl.tagName())) {
          Cleaner.ElementMeta meta = Cleaner.this.createSafeElement(sourceEl);
          Element destChild = el;
          destination.appendChild(destChild);
          
          numDiscarded += numAttribsDiscarded;
          destination = destChild;
        } else if (source != root) {
          numDiscarded += 1;
        }
      } else if ((source instanceof TextNode)) {
        TextNode sourceText = (TextNode)source;
        TextNode destText = new TextNode(sourceText.getWholeText(), source.baseUri());
        destination.appendChild(destText);
      } else if (((source instanceof DataNode)) && (whitelist.isSafeTag(source.parent().nodeName()))) {
        DataNode sourceData = (DataNode)source;
        DataNode destData = new DataNode(sourceData.getWholeData(), source.baseUri());
        destination.appendChild(destData);
      } else {
        numDiscarded += 1;
      }
    }
    
    public void tail(Node source, int depth) {
      if (((source instanceof Element)) && (whitelist.isSafeTag(source.nodeName()))) {
        destination = destination.parent();
      }
    }
  }
  
  private int copySafeNodes(Element source, Element dest) {
    CleaningVisitor cleaningVisitor = new CleaningVisitor(source, dest, null);
    NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
    traversor.traverse(source);
    return numDiscarded;
  }
  
  private ElementMeta createSafeElement(Element sourceEl) {
    String sourceTag = sourceEl.tagName();
    Attributes destAttrs = new Attributes();
    Element dest = new Element(Tag.valueOf(sourceTag), sourceEl.baseUri(), destAttrs);
    int numDiscarded = 0;
    
    Attributes sourceAttrs = sourceEl.attributes();
    for (Attribute sourceAttr : sourceAttrs) {
      if (whitelist.isSafeAttribute(sourceTag, sourceEl, sourceAttr)) {
        destAttrs.put(sourceAttr);
      } else
        numDiscarded++;
    }
    Attributes enforcedAttrs = whitelist.getEnforcedAttributes(sourceTag);
    destAttrs.addAll(enforcedAttrs);
    
    return new ElementMeta(dest, numDiscarded);
  }
  
  private static class ElementMeta {
    Element el;
    int numAttribsDiscarded;
    
    ElementMeta(Element el, int numAttribsDiscarded) {
      this.el = el;
      this.numAttribsDiscarded = numAttribsDiscarded;
    }
  }
}
