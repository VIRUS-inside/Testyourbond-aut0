package org.jsoup.helper;

import java.io.StringWriter;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.w3c.dom.Text;





public class W3CDom
{
  protected DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
  

  public W3CDom() {}
  

  public org.w3c.dom.Document fromJsoup(org.jsoup.nodes.Document in)
  {
    Validate.notNull(in);
    
    try
    {
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      org.w3c.dom.Document out = builder.newDocument();
      convert(in, out);
      return out;
    } catch (ParserConfigurationException e) {
      throw new IllegalStateException(e);
    }
  }
  






  public void convert(org.jsoup.nodes.Document in, org.w3c.dom.Document out)
  {
    if (!StringUtil.isBlank(in.location())) {
      out.setDocumentURI(in.location());
    }
    org.jsoup.nodes.Element rootEl = in.child(0);
    NodeTraversor traversor = new NodeTraversor(new W3CBuilder(out));
    traversor.traverse(rootEl);
  }
  

  protected static class W3CBuilder
    implements NodeVisitor
  {
    private static final String xmlnsKey = "xmlns";
    
    private static final String xmlnsPrefix = "xmlns:";
    private final org.w3c.dom.Document doc;
    private final HashMap<String, String> namespaces = new HashMap();
    private org.w3c.dom.Element dest;
    
    public W3CBuilder(org.w3c.dom.Document doc) {
      this.doc = doc;
    }
    
    public void head(Node source, int depth) {
      if ((source instanceof org.jsoup.nodes.Element)) {
        org.jsoup.nodes.Element sourceEl = (org.jsoup.nodes.Element)source;
        
        String prefix = updateNamespaces(sourceEl);
        String namespace = (String)namespaces.get(prefix);
        
        org.w3c.dom.Element el = doc.createElementNS(namespace, sourceEl.tagName());
        copyAttributes(sourceEl, el);
        if (dest == null) {
          doc.appendChild(el);
        } else {
          dest.appendChild(el);
        }
        dest = el;
      } else if ((source instanceof TextNode)) {
        TextNode sourceText = (TextNode)source;
        Text text = doc.createTextNode(sourceText.getWholeText());
        dest.appendChild(text);
      } else if ((source instanceof org.jsoup.nodes.Comment)) {
        org.jsoup.nodes.Comment sourceComment = (org.jsoup.nodes.Comment)source;
        org.w3c.dom.Comment comment = doc.createComment(sourceComment.getData());
        dest.appendChild(comment);
      } else if ((source instanceof DataNode)) {
        DataNode sourceData = (DataNode)source;
        Text node = doc.createTextNode(sourceData.getWholeData());
        dest.appendChild(node);
      }
    }
    

    public void tail(Node source, int depth)
    {
      if (((source instanceof org.jsoup.nodes.Element)) && ((dest.getParentNode() instanceof org.w3c.dom.Element))) {
        dest = ((org.w3c.dom.Element)dest.getParentNode());
      }
    }
    
    private void copyAttributes(Node source, org.w3c.dom.Element el) {
      for (Attribute attribute : source.attributes())
      {
        String key = attribute.getKey().replaceAll("[^-a-zA-Z0-9_:.]", "");
        if (key.matches("[a-zA-Z_:]{1}[-a-zA-Z0-9_:.]*")) {
          el.setAttribute(key, attribute.getValue());
        }
      }
    }
    



    private String updateNamespaces(org.jsoup.nodes.Element el)
    {
      Attributes attributes = el.attributes();
      for (Attribute attr : attributes) {
        String key = attr.getKey();
        String prefix;
        String prefix; if (key.equals("xmlns")) {
          prefix = "";
        } else { if (!key.startsWith("xmlns:")) continue;
          prefix = key.substring("xmlns:".length());
        }
        

        namespaces.put(prefix, attr.getValue());
      }
      

      int pos = el.tagName().indexOf(":");
      return pos > 0 ? el.tagName().substring(0, pos) : "";
    }
  }
  




  public String asString(org.w3c.dom.Document doc)
  {
    try
    {
      DOMSource domSource = new DOMSource(doc);
      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.transform(domSource, result);
      return writer.toString();
    } catch (TransformerException e) {
      throw new IllegalStateException(e);
    }
  }
}
