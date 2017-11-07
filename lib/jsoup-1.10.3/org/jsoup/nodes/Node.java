package org.jsoup.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.SerializationException;
import org.jsoup.helper.ChangeNotifyingArrayList;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.internal.Normalizer;
import org.jsoup.parser.Parser;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;





public abstract class Node
  implements Cloneable
{
  private static final List<Node> EMPTY_NODES = ;
  
  Node parentNode;
  
  List<Node> childNodes;
  
  Attributes attributes;
  
  String baseUri;
  int siblingIndex;
  
  protected Node(String baseUri, Attributes attributes)
  {
    Validate.notNull(baseUri);
    Validate.notNull(attributes);
    
    childNodes = EMPTY_NODES;
    this.baseUri = baseUri.trim();
    this.attributes = attributes;
  }
  
  protected Node(String baseUri) {
    this(baseUri, new Attributes());
  }
  


  protected Node()
  {
    childNodes = EMPTY_NODES;
    attributes = null;
  }
  









  public abstract String nodeName();
  









  public String attr(String attributeKey)
  {
    Validate.notNull(attributeKey);
    
    String val = attributes.getIgnoreCase(attributeKey);
    if (val.length() > 0)
      return val;
    if (Normalizer.lowerCase(attributeKey).startsWith("abs:"))
      return absUrl(attributeKey.substring("abs:".length()));
    return "";
  }
  



  public Attributes attributes()
  {
    return attributes;
  }
  





  public Node attr(String attributeKey, String attributeValue)
  {
    attributes.put(attributeKey, attributeValue);
    return this;
  }
  




  public boolean hasAttr(String attributeKey)
  {
    Validate.notNull(attributeKey);
    
    if (attributeKey.startsWith("abs:")) {
      String key = attributeKey.substring("abs:".length());
      if ((attributes.hasKeyIgnoreCase(key)) && (!absUrl(key).equals("")))
        return true;
    }
    return attributes.hasKeyIgnoreCase(attributeKey);
  }
  




  public Node removeAttr(String attributeKey)
  {
    Validate.notNull(attributeKey);
    attributes.removeIgnoreCase(attributeKey);
    return this;
  }
  



  public Node clearAttributes()
  {
    Iterator<Attribute> it = attributes.iterator();
    while (it.hasNext()) {
      it.next();
      it.remove();
    }
    return this;
  }
  



  public String baseUri()
  {
    return baseUri;
  }
  



  public void setBaseUri(final String baseUri)
  {
    Validate.notNull(baseUri);
    
    traverse(new NodeVisitor() {
      public void head(Node node, int depth) {
        baseUri = baseUri;
      }
      












      public void tail(Node node, int depth) {}
    });
  }
  











  public String absUrl(String attributeKey)
  {
    Validate.notEmpty(attributeKey);
    
    if (!hasAttr(attributeKey)) {
      return "";
    }
    return StringUtil.resolve(baseUri, attr(attributeKey));
  }
  





  public Node childNode(int index)
  {
    return (Node)childNodes.get(index);
  }
  




  public List<Node> childNodes()
  {
    return Collections.unmodifiableList(childNodes);
  }
  




  public List<Node> childNodesCopy()
  {
    List<Node> children = new ArrayList(childNodes.size());
    for (Node node : childNodes) {
      children.add(node.clone());
    }
    return children;
  }
  



  public final int childNodeSize()
  {
    return childNodes.size();
  }
  
  protected Node[] childNodesAsArray() {
    return (Node[])childNodes.toArray(new Node[childNodeSize()]);
  }
  



  public Node parent()
  {
    return parentNode;
  }
  



  public final Node parentNode()
  {
    return parentNode;
  }
  



  public Node root()
  {
    Node node = this;
    while (parentNode != null)
      node = parentNode;
    return node;
  }
  



  public Document ownerDocument()
  {
    Node root = root();
    return (root instanceof Document) ? (Document)root : null;
  }
  


  public void remove()
  {
    Validate.notNull(parentNode);
    parentNode.removeChild(this);
  }
  





  public Node before(String html)
  {
    addSiblingHtml(siblingIndex, html);
    return this;
  }
  





  public Node before(Node node)
  {
    Validate.notNull(node);
    Validate.notNull(parentNode);
    
    parentNode.addChildren(siblingIndex, new Node[] { node });
    return this;
  }
  





  public Node after(String html)
  {
    addSiblingHtml(siblingIndex + 1, html);
    return this;
  }
  





  public Node after(Node node)
  {
    Validate.notNull(node);
    Validate.notNull(parentNode);
    
    parentNode.addChildren(siblingIndex + 1, new Node[] { node });
    return this;
  }
  
  private void addSiblingHtml(int index, String html) {
    Validate.notNull(html);
    Validate.notNull(parentNode);
    
    Element context = (parent() instanceof Element) ? (Element)parent() : null;
    List<Node> nodes = Parser.parseFragment(html, context, baseUri());
    parentNode.addChildren(index, (Node[])nodes.toArray(new Node[nodes.size()]));
  }
  




  public Node wrap(String html)
  {
    Validate.notEmpty(html);
    
    Element context = (parent() instanceof Element) ? (Element)parent() : null;
    List<Node> wrapChildren = Parser.parseFragment(html, context, baseUri());
    Node wrapNode = (Node)wrapChildren.get(0);
    if ((wrapNode == null) || (!(wrapNode instanceof Element))) {
      return null;
    }
    Element wrap = (Element)wrapNode;
    Element deepest = getDeepChild(wrap);
    parentNode.replaceChild(this, wrap);
    deepest.addChildren(new Node[] { this });
    

    if (wrapChildren.size() > 0) {
      for (int i = 0; i < wrapChildren.size(); i++) {
        Node remainder = (Node)wrapChildren.get(i);
        parentNode.removeChild(remainder);
        wrap.appendChild(remainder);
      }
    }
    return this;
  }
  














  public Node unwrap()
  {
    Validate.notNull(parentNode);
    
    Node firstChild = childNodes.size() > 0 ? (Node)childNodes.get(0) : null;
    parentNode.addChildren(siblingIndex, childNodesAsArray());
    remove();
    
    return firstChild;
  }
  
  private Element getDeepChild(Element el) {
    List<Element> children = el.children();
    if (children.size() > 0) {
      return getDeepChild((Element)children.get(0));
    }
    return el;
  }
  



  void nodelistChanged() {}
  


  public void replaceWith(Node in)
  {
    Validate.notNull(in);
    Validate.notNull(parentNode);
    parentNode.replaceChild(this, in);
  }
  
  protected void setParentNode(Node parentNode) {
    Validate.notNull(parentNode);
    if (this.parentNode != null)
      this.parentNode.removeChild(this);
    this.parentNode = parentNode;
  }
  
  protected void replaceChild(Node out, Node in) {
    Validate.isTrue(parentNode == this);
    Validate.notNull(in);
    if (parentNode != null) {
      parentNode.removeChild(in);
    }
    int index = siblingIndex;
    childNodes.set(index, in);
    parentNode = this;
    in.setSiblingIndex(index);
    parentNode = null;
  }
  
  protected void removeChild(Node out) {
    Validate.isTrue(parentNode == this);
    int index = siblingIndex;
    childNodes.remove(index);
    reindexChildren(index);
    parentNode = null;
  }
  
  protected void addChildren(Node... children)
  {
    for (Node child : children) {
      reparentChild(child);
      ensureChildNodes();
      childNodes.add(child);
      child.setSiblingIndex(childNodes.size() - 1);
    }
  }
  
  protected void addChildren(int index, Node... children) {
    Validate.noNullElements(children);
    ensureChildNodes();
    for (int i = children.length - 1; i >= 0; i--) {
      Node in = children[i];
      reparentChild(in);
      childNodes.add(index, in);
      reindexChildren(index);
    }
  }
  
  protected void ensureChildNodes() {
    if (childNodes == EMPTY_NODES) {
      childNodes = new NodeList(4);
    }
  }
  
  protected void reparentChild(Node child) {
    if (parentNode != null)
      parentNode.removeChild(child);
    child.setParentNode(this);
  }
  
  private void reindexChildren(int start) {
    for (int i = start; i < childNodes.size(); i++) {
      ((Node)childNodes.get(i)).setSiblingIndex(i);
    }
  }
  




  public List<Node> siblingNodes()
  {
    if (parentNode == null) {
      return Collections.emptyList();
    }
    List<Node> nodes = parentNode.childNodes;
    List<Node> siblings = new ArrayList(nodes.size() - 1);
    for (Node node : nodes)
      if (node != this)
        siblings.add(node);
    return siblings;
  }
  



  public Node nextSibling()
  {
    if (parentNode == null) {
      return null;
    }
    List<Node> siblings = parentNode.childNodes;
    int index = siblingIndex + 1;
    if (siblings.size() > index) {
      return (Node)siblings.get(index);
    }
    return null;
  }
  



  public Node previousSibling()
  {
    if (parentNode == null) {
      return null;
    }
    if (siblingIndex > 0) {
      return (Node)parentNode.childNodes.get(siblingIndex - 1);
    }
    return null;
  }
  





  public int siblingIndex()
  {
    return siblingIndex;
  }
  
  protected void setSiblingIndex(int siblingIndex) {
    this.siblingIndex = siblingIndex;
  }
  




  public Node traverse(NodeVisitor nodeVisitor)
  {
    Validate.notNull(nodeVisitor);
    NodeTraversor traversor = new NodeTraversor(nodeVisitor);
    traversor.traverse(this);
    return this;
  }
  



  public String outerHtml()
  {
    StringBuilder accum = new StringBuilder(128);
    outerHtml(accum);
    return accum.toString();
  }
  
  protected void outerHtml(Appendable accum) {
    new NodeTraversor(new OuterHtmlVisitor(accum, getOutputSettings())).traverse(this);
  }
  
  Document.OutputSettings getOutputSettings()
  {
    Document owner = ownerDocument();
    return owner != null ? owner.outputSettings() : new Document("").outputSettings();
  }
  



  abstract void outerHtmlHead(Appendable paramAppendable, int paramInt, Document.OutputSettings paramOutputSettings)
    throws IOException;
  



  abstract void outerHtmlTail(Appendable paramAppendable, int paramInt, Document.OutputSettings paramOutputSettings)
    throws IOException;
  


  public <T extends Appendable> T html(T appendable)
  {
    outerHtml(appendable);
    return appendable;
  }
  
  public String toString() {
    return outerHtml();
  }
  
  protected void indent(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
    accum.append("\n").append(StringUtil.padding(depth * out.indentAmount()));
  }
  







  public boolean equals(Object o)
  {
    return this == o;
  }
  






  public boolean hasSameValue(Object o)
  {
    if (this == o) return true;
    if ((o == null) || (getClass() != o.getClass())) { return false;
    }
    return outerHtml().equals(((Node)o).outerHtml());
  }
  








  public Node clone()
  {
    Node thisClone = doClone(null);
    

    LinkedList<Node> nodesToProcess = new LinkedList();
    nodesToProcess.add(thisClone);
    
    while (!nodesToProcess.isEmpty()) {
      Node currParent = (Node)nodesToProcess.remove();
      
      for (int i = 0; i < childNodes.size(); i++) {
        Node childClone = ((Node)childNodes.get(i)).doClone(currParent);
        childNodes.set(i, childClone);
        nodesToProcess.add(childClone);
      }
    }
    
    return thisClone;
  }
  




  protected Node doClone(Node parent)
  {
    try
    {
      clone = (Node)super.clone();
    } catch (CloneNotSupportedException e) { Node clone;
      throw new RuntimeException(e);
    }
    Node clone;
    parentNode = parent;
    siblingIndex = (parent == null ? 0 : siblingIndex);
    attributes = (attributes != null ? attributes.clone() : null);
    baseUri = baseUri;
    childNodes = new NodeList(childNodes.size());
    
    for (Node child : childNodes) {
      childNodes.add(child);
    }
    return clone;
  }
  
  private static class OuterHtmlVisitor implements NodeVisitor {
    private Appendable accum;
    private Document.OutputSettings out;
    
    OuterHtmlVisitor(Appendable accum, Document.OutputSettings out) {
      this.accum = accum;
      this.out = out;
    }
    
    public void head(Node node, int depth) {
      try {
        node.outerHtmlHead(accum, depth, out);
      } catch (IOException exception) {
        throw new SerializationException(exception);
      }
    }
    
    public void tail(Node node, int depth) {
      if (!node.nodeName().equals("#text")) {
        try {
          node.outerHtmlTail(accum, depth, out);
        } catch (IOException exception) {
          throw new SerializationException(exception);
        }
      }
    }
  }
  
  private final class NodeList extends ChangeNotifyingArrayList<Node> {
    NodeList(int initialCapacity) {
      super();
    }
    
    public void onContentsChanged() {
      nodelistChanged();
    }
  }
}
