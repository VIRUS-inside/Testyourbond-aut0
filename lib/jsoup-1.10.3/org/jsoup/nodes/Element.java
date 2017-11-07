package org.jsoup.nodes;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Evaluator.AllElements;
import org.jsoup.select.Evaluator.Attribute;
import org.jsoup.select.Evaluator.AttributeStarting;
import org.jsoup.select.Evaluator.AttributeWithValueContaining;
import org.jsoup.select.Evaluator.AttributeWithValueNot;
import org.jsoup.select.Evaluator.AttributeWithValueStarting;
import org.jsoup.select.Evaluator.Class;
import org.jsoup.select.Evaluator.ContainsOwnText;
import org.jsoup.select.Evaluator.ContainsText;
import org.jsoup.select.Evaluator.Id;
import org.jsoup.select.Evaluator.IndexGreaterThan;
import org.jsoup.select.Evaluator.IndexLessThan;
import org.jsoup.select.Evaluator.MatchesOwn;
import org.jsoup.select.Evaluator.Tag;
import org.jsoup.select.NodeTraversor;

public class Element extends Node
{
  private Tag tag;
  private WeakReference<List<Element>> shadowChildrenRef;
  private static final Pattern classSplit = Pattern.compile("\\s+");
  



  public Element(String tag)
  {
    this(Tag.valueOf(tag), "", new Attributes());
  }
  








  public Element(Tag tag, String baseUri, Attributes attributes)
  {
    super(baseUri, attributes);
    
    Validate.notNull(tag);
    this.tag = tag;
  }
  







  public Element(Tag tag, String baseUri)
  {
    this(tag, baseUri, new Attributes());
  }
  
  public String nodeName()
  {
    return tag.getName();
  }
  




  public String tagName()
  {
    return tag.getName();
  }
  






  public Element tagName(String tagName)
  {
    Validate.notEmpty(tagName, "Tag name must not be empty.");
    tag = Tag.valueOf(tagName, ParseSettings.preserveCase);
    return this;
  }
  




  public Tag tag()
  {
    return tag;
  }
  





  public boolean isBlock()
  {
    return tag.isBlock();
  }
  




  public String id()
  {
    return attributes.getIgnoreCase("id");
  }
  





  public Element attr(String attributeKey, String attributeValue)
  {
    super.attr(attributeKey, attributeValue);
    return this;
  }
  









  public Element attr(String attributeKey, boolean attributeValue)
  {
    attributes.put(attributeKey, attributeValue);
    return this;
  }
  












  public Map<String, String> dataset()
  {
    return attributes.dataset();
  }
  
  public final Element parent()
  {
    return (Element)parentNode;
  }
  



  public Elements parents()
  {
    Elements parents = new Elements();
    accumulateParents(this, parents);
    return parents;
  }
  
  private static void accumulateParents(Element el, Elements parents) {
    Element parent = el.parent();
    if ((parent != null) && (!parent.tagName().equals("#root"))) {
      parents.add(parent);
      accumulateParents(parent, parents);
    }
  }
  










  public Element child(int index)
  {
    return (Element)childElementsList().get(index);
  }
  







  public Elements children()
  {
    return new Elements(childElementsList());
  }
  

  private List<Element> childElementsList()
  {
    List<Element> children;
    
    List<Element> children;
    
    if ((shadowChildrenRef == null) || ((children = (List)shadowChildrenRef.get()) == null)) {
      int size = childNodes.size();
      children = new ArrayList(size);
      for (int i = 0; i < size; i++) {
        Node node = (Node)childNodes.get(i);
        if ((node instanceof Element))
          children.add((Element)node);
      }
      shadowChildrenRef = new WeakReference(children);
    }
    return children;
  }
  



  void nodelistChanged()
  {
    super.nodelistChanged();
    shadowChildrenRef = null;
  }
  















  public List<TextNode> textNodes()
  {
    List<TextNode> textNodes = new ArrayList();
    for (Node node : childNodes) {
      if ((node instanceof TextNode))
        textNodes.add((TextNode)node);
    }
    return Collections.unmodifiableList(textNodes);
  }
  








  public List<DataNode> dataNodes()
  {
    List<DataNode> dataNodes = new ArrayList();
    for (Node node : childNodes) {
      if ((node instanceof DataNode))
        dataNodes.add((DataNode)node);
    }
    return Collections.unmodifiableList(dataNodes);
  }
  



















  public Elements select(String cssQuery)
  {
    return org.jsoup.select.Selector.select(cssQuery, this);
  }
  




  public boolean is(String cssQuery)
  {
    return is(org.jsoup.select.QueryParser.parse(cssQuery));
  }
  




  public boolean is(Evaluator evaluator)
  {
    return evaluator.matches((Element)root(), this);
  }
  





  public Element appendChild(Node child)
  {
    Validate.notNull(child);
    

    reparentChild(child);
    ensureChildNodes();
    childNodes.add(child);
    child.setSiblingIndex(childNodes.size() - 1);
    return this;
  }
  





  public Element prependChild(Node child)
  {
    Validate.notNull(child);
    
    addChildren(0, new Node[] { child });
    return this;
  }
  









  public Element insertChildren(int index, Collection<? extends Node> children)
  {
    Validate.notNull(children, "Children collection to be inserted must not be null.");
    int currentSize = childNodeSize();
    if (index < 0) index += currentSize + 1;
    Validate.isTrue((index >= 0) && (index <= currentSize), "Insert position out of bounds.");
    
    ArrayList<Node> nodes = new ArrayList(children);
    Node[] nodeArray = (Node[])nodes.toArray(new Node[nodes.size()]);
    addChildren(index, nodeArray);
    return this;
  }
  








  public Element insertChildren(int index, Node... children)
  {
    Validate.notNull(children, "Children collection to be inserted must not be null.");
    int currentSize = childNodeSize();
    if (index < 0) index += currentSize + 1;
    Validate.isTrue((index >= 0) && (index <= currentSize), "Insert position out of bounds.");
    
    addChildren(index, children);
    return this;
  }
  






  public Element appendElement(String tagName)
  {
    Element child = new Element(Tag.valueOf(tagName), baseUri());
    appendChild(child);
    return child;
  }
  






  public Element prependElement(String tagName)
  {
    Element child = new Element(Tag.valueOf(tagName), baseUri());
    prependChild(child);
    return child;
  }
  





  public Element appendText(String text)
  {
    Validate.notNull(text);
    TextNode node = new TextNode(text, baseUri());
    appendChild(node);
    return this;
  }
  





  public Element prependText(String text)
  {
    Validate.notNull(text);
    TextNode node = new TextNode(text, baseUri());
    prependChild(node);
    return this;
  }
  





  public Element append(String html)
  {
    Validate.notNull(html);
    
    List<Node> nodes = Parser.parseFragment(html, this, baseUri());
    addChildren((Node[])nodes.toArray(new Node[nodes.size()]));
    return this;
  }
  





  public Element prepend(String html)
  {
    Validate.notNull(html);
    
    List<Node> nodes = Parser.parseFragment(html, this, baseUri());
    addChildren(0, (Node[])nodes.toArray(new Node[nodes.size()]));
    return this;
  }
  







  public Element before(String html)
  {
    return (Element)super.before(html);
  }
  






  public Element before(Node node)
  {
    return (Element)super.before(node);
  }
  







  public Element after(String html)
  {
    return (Element)super.after(html);
  }
  






  public Element after(Node node)
  {
    return (Element)super.after(node);
  }
  



  public Element empty()
  {
    childNodes.clear();
    return this;
  }
  






  public Element wrap(String html)
  {
    return (Element)super.wrap(html);
  }
  









  public String cssSelector()
  {
    if (id().length() > 0) {
      return "#" + id();
    }
    
    String tagName = tagName().replace(':', '|');
    StringBuilder selector = new StringBuilder(tagName);
    String classes = StringUtil.join(classNames(), ".");
    if (classes.length() > 0) {
      selector.append('.').append(classes);
    }
    if ((parent() == null) || ((parent() instanceof Document))) {
      return selector.toString();
    }
    selector.insert(0, " > ");
    if (parent().select(selector.toString()).size() > 1) {
      selector.append(String.format(":nth-child(%d)", new Object[] {
        Integer.valueOf(elementSiblingIndex() + 1) }));
    }
    return parent().cssSelector() + selector.toString();
  }
  




  public Elements siblingElements()
  {
    if (parentNode == null) {
      return new Elements(0);
    }
    List<Element> elements = parent().childElementsList();
    Elements siblings = new Elements(elements.size() - 1);
    for (Element el : elements)
      if (el != this)
        siblings.add(el);
    return siblings;
  }
  








  public Element nextElementSibling()
  {
    if (parentNode == null) return null;
    List<Element> siblings = parent().childElementsList();
    Integer index = Integer.valueOf(indexInList(this, siblings));
    Validate.notNull(index);
    if (siblings.size() > index.intValue() + 1) {
      return (Element)siblings.get(index.intValue() + 1);
    }
    return null;
  }
  




  public Element previousElementSibling()
  {
    if (parentNode == null) return null;
    List<Element> siblings = parent().childElementsList();
    Integer index = Integer.valueOf(indexInList(this, siblings));
    Validate.notNull(index);
    if (index.intValue() > 0) {
      return (Element)siblings.get(index.intValue() - 1);
    }
    return null;
  }
  




  public Element firstElementSibling()
  {
    List<Element> siblings = parent().childElementsList();
    return siblings.size() > 1 ? (Element)siblings.get(0) : null;
  }
  




  public int elementSiblingIndex()
  {
    if (parent() == null) return 0;
    return indexInList(this, parent().childElementsList());
  }
  



  public Element lastElementSibling()
  {
    List<Element> siblings = parent().childElementsList();
    return siblings.size() > 1 ? (Element)siblings.get(siblings.size() - 1) : null;
  }
  
  private static <E extends Element> int indexInList(Element search, List<E> elements) {
    for (int i = 0; i < elements.size(); i++) {
      if (elements.get(i) == search)
        return i;
    }
    return 0;
  }
  






  public Elements getElementsByTag(String tagName)
  {
    Validate.notEmpty(tagName);
    tagName = org.jsoup.internal.Normalizer.normalize(tagName);
    
    return Collector.collect(new Evaluator.Tag(tagName), this);
  }
  








  public Element getElementById(String id)
  {
    Validate.notEmpty(id);
    
    Elements elements = Collector.collect(new Evaluator.Id(id), this);
    if (elements.size() > 0) {
      return (Element)elements.get(0);
    }
    return null;
  }
  










  public Elements getElementsByClass(String className)
  {
    Validate.notEmpty(className);
    
    return Collector.collect(new Evaluator.Class(className), this);
  }
  





  public Elements getElementsByAttribute(String key)
  {
    Validate.notEmpty(key);
    key = key.trim();
    
    return Collector.collect(new Evaluator.Attribute(key), this);
  }
  





  public Elements getElementsByAttributeStarting(String keyPrefix)
  {
    Validate.notEmpty(keyPrefix);
    keyPrefix = keyPrefix.trim();
    
    return Collector.collect(new Evaluator.AttributeStarting(keyPrefix), this);
  }
  






  public Elements getElementsByAttributeValue(String key, String value)
  {
    return Collector.collect(new org.jsoup.select.Evaluator.AttributeWithValue(key, value), this);
  }
  






  public Elements getElementsByAttributeValueNot(String key, String value)
  {
    return Collector.collect(new Evaluator.AttributeWithValueNot(key, value), this);
  }
  






  public Elements getElementsByAttributeValueStarting(String key, String valuePrefix)
  {
    return Collector.collect(new Evaluator.AttributeWithValueStarting(key, valuePrefix), this);
  }
  






  public Elements getElementsByAttributeValueEnding(String key, String valueSuffix)
  {
    return Collector.collect(new org.jsoup.select.Evaluator.AttributeWithValueEnding(key, valueSuffix), this);
  }
  






  public Elements getElementsByAttributeValueContaining(String key, String match)
  {
    return Collector.collect(new Evaluator.AttributeWithValueContaining(key, match), this);
  }
  





  public Elements getElementsByAttributeValueMatching(String key, Pattern pattern)
  {
    return Collector.collect(new org.jsoup.select.Evaluator.AttributeWithValueMatching(key, pattern), this);
  }
  






  public Elements getElementsByAttributeValueMatching(String key, String regex)
  {
    try
    {
      pattern = Pattern.compile(regex);
    } catch (PatternSyntaxException e) { Pattern pattern;
      throw new IllegalArgumentException("Pattern syntax error: " + regex, e); }
    Pattern pattern;
    return getElementsByAttributeValueMatching(key, pattern);
  }
  




  public Elements getElementsByIndexLessThan(int index)
  {
    return Collector.collect(new Evaluator.IndexLessThan(index), this);
  }
  




  public Elements getElementsByIndexGreaterThan(int index)
  {
    return Collector.collect(new Evaluator.IndexGreaterThan(index), this);
  }
  




  public Elements getElementsByIndexEquals(int index)
  {
    return Collector.collect(new org.jsoup.select.Evaluator.IndexEquals(index), this);
  }
  






  public Elements getElementsContainingText(String searchText)
  {
    return Collector.collect(new Evaluator.ContainsText(searchText), this);
  }
  






  public Elements getElementsContainingOwnText(String searchText)
  {
    return Collector.collect(new Evaluator.ContainsOwnText(searchText), this);
  }
  





  public Elements getElementsMatchingText(Pattern pattern)
  {
    return Collector.collect(new org.jsoup.select.Evaluator.Matches(pattern), this);
  }
  





  public Elements getElementsMatchingText(String regex)
  {
    try
    {
      pattern = Pattern.compile(regex);
    } catch (PatternSyntaxException e) { Pattern pattern;
      throw new IllegalArgumentException("Pattern syntax error: " + regex, e); }
    Pattern pattern;
    return getElementsMatchingText(pattern);
  }
  





  public Elements getElementsMatchingOwnText(Pattern pattern)
  {
    return Collector.collect(new Evaluator.MatchesOwn(pattern), this);
  }
  





  public Elements getElementsMatchingOwnText(String regex)
  {
    try
    {
      pattern = Pattern.compile(regex);
    } catch (PatternSyntaxException e) { Pattern pattern;
      throw new IllegalArgumentException("Pattern syntax error: " + regex, e); }
    Pattern pattern;
    return getElementsMatchingOwnText(pattern);
  }
  




  public Elements getAllElements()
  {
    return Collector.collect(new Evaluator.AllElements(), this);
  }
  








  public String text()
  {
    final StringBuilder accum = new StringBuilder();
    new NodeTraversor(new org.jsoup.select.NodeVisitor() {
      public void head(Node node, int depth) {
        if ((node instanceof TextNode)) {
          TextNode textNode = (TextNode)node;
          Element.appendNormalisedText(accum, textNode);
        } else if ((node instanceof Element)) {
          Element element = (Element)node;
          if ((accum.length() > 0) && 
            ((element.isBlock()) || (tag.getName().equals("br"))) && 
            (!TextNode.lastCharIsWhitespace(accum)))
            accum.append(" "); } } public void tail(Node node, int depth) {} })
    




      .traverse(this);
    return accum.toString().trim();
  }
  










  public String ownText()
  {
    StringBuilder sb = new StringBuilder();
    ownText(sb);
    return sb.toString().trim();
  }
  
  private void ownText(StringBuilder accum) {
    for (Node child : childNodes) {
      if ((child instanceof TextNode)) {
        TextNode textNode = (TextNode)child;
        appendNormalisedText(accum, textNode);
      } else if ((child instanceof Element)) {
        appendWhitespaceIfBr((Element)child, accum);
      }
    }
  }
  
  private static void appendNormalisedText(StringBuilder accum, TextNode textNode) {
    String text = textNode.getWholeText();
    
    if (preserveWhitespace(parentNode)) {
      accum.append(text);
    } else
      StringUtil.appendNormalisedWhitespace(accum, text, TextNode.lastCharIsWhitespace(accum));
  }
  
  private static void appendWhitespaceIfBr(Element element, StringBuilder accum) {
    if ((tag.getName().equals("br")) && (!TextNode.lastCharIsWhitespace(accum))) {
      accum.append(" ");
    }
  }
  
  static boolean preserveWhitespace(Node node) {
    if ((node != null) && ((node instanceof Element))) {
      Element element = (Element)node;
      
      return (tag.preserveWhitespace()) || ((element.parent() != null) && (parenttag.preserveWhitespace()));
    }
    return false;
  }
  




  public Element text(String text)
  {
    Validate.notNull(text);
    
    empty();
    TextNode textNode = new TextNode(text, baseUri);
    appendChild(textNode);
    
    return this;
  }
  



  public boolean hasText()
  {
    for (Node child : childNodes) {
      if ((child instanceof TextNode)) {
        TextNode textNode = (TextNode)child;
        if (!textNode.isBlank())
          return true;
      } else if ((child instanceof Element)) {
        Element el = (Element)child;
        if (el.hasText())
          return true;
      }
    }
    return false;
  }
  








  public String data()
  {
    StringBuilder sb = new StringBuilder();
    
    for (Node childNode : childNodes) {
      if ((childNode instanceof DataNode)) {
        DataNode data = (DataNode)childNode;
        sb.append(data.getWholeData());
      } else if ((childNode instanceof Comment)) {
        Comment comment = (Comment)childNode;
        sb.append(comment.getData());
      } else if ((childNode instanceof Element)) {
        Element element = (Element)childNode;
        String elementData = element.data();
        sb.append(elementData);
      }
    }
    return sb.toString();
  }
  




  public String className()
  {
    return attr("class").trim();
  }
  





  public Set<String> classNames()
  {
    String[] names = classSplit.split(className());
    Set<String> classNames = new LinkedHashSet(Arrays.asList(names));
    classNames.remove("");
    
    return classNames;
  }
  




  public Element classNames(Set<String> classNames)
  {
    Validate.notNull(classNames);
    attributes.put("class", StringUtil.join(classNames, " "));
    return this;
  }
  





  public boolean hasClass(String className)
  {
    String classAttr = attributes.getIgnoreCase("class");
    int len = classAttr.length();
    int wantLen = className.length();
    
    if ((len == 0) || (len < wantLen)) {
      return false;
    }
    

    if (len == wantLen) {
      return className.equalsIgnoreCase(classAttr);
    }
    

    boolean inClass = false;
    int start = 0;
    for (int i = 0; i < len; i++) {
      if (Character.isWhitespace(classAttr.charAt(i))) {
        if (inClass)
        {
          if ((i - start == wantLen) && (classAttr.regionMatches(true, start, className, 0, wantLen))) {
            return true;
          }
          inClass = false;
        }
      }
      else if (!inClass)
      {
        inClass = true;
        start = i;
      }
    }
    


    if ((inClass) && (len - start == wantLen)) {
      return classAttr.regionMatches(true, start, className, 0, wantLen);
    }
    
    return false;
  }
  




  public Element addClass(String className)
  {
    Validate.notNull(className);
    
    Set<String> classes = classNames();
    classes.add(className);
    classNames(classes);
    
    return this;
  }
  




  public Element removeClass(String className)
  {
    Validate.notNull(className);
    
    Set<String> classes = classNames();
    classes.remove(className);
    classNames(classes);
    
    return this;
  }
  




  public Element toggleClass(String className)
  {
    Validate.notNull(className);
    
    Set<String> classes = classNames();
    if (classes.contains(className)) {
      classes.remove(className);
    } else
      classes.add(className);
    classNames(classes);
    
    return this;
  }
  



  public String val()
  {
    if (tagName().equals("textarea")) {
      return text();
    }
    return attr("value");
  }
  




  public Element val(String value)
  {
    if (tagName().equals("textarea")) {
      text(value);
    } else
      attr("value", value);
    return this;
  }
  
  void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
    if ((out.prettyPrint()) && ((tag.formatAsBlock()) || ((parent() != null) && (parent().tag().formatAsBlock())) || (out.outline()))) {
      if ((accum instanceof StringBuilder)) {
        if (((StringBuilder)accum).length() > 0)
          indent(accum, depth, out);
      } else {
        indent(accum, depth, out);
      }
    }
    

    accum.append("<").append(tagName());
    attributes.html(accum, out);
    

    if ((childNodes.isEmpty()) && (tag.isSelfClosing())) {
      if ((out.syntax() == Document.OutputSettings.Syntax.html) && (tag.isEmpty())) {
        accum.append('>');
      } else {
        accum.append(" />");
      }
    } else
      accum.append(">");
  }
  
  void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
    if ((!childNodes.isEmpty()) || (!tag.isSelfClosing())) {
      if ((out.prettyPrint()) && (!childNodes.isEmpty()) && (
        (tag.formatAsBlock()) || ((out.outline()) && ((childNodes.size() > 1) || ((childNodes.size() == 1) && (!(childNodes.get(0) instanceof TextNode)))))))
      {
        indent(accum, depth, out); }
      accum.append("</").append(tagName()).append(">");
    }
  }
  






  public String html()
  {
    StringBuilder accum = new StringBuilder();
    html(accum);
    return getOutputSettings().prettyPrint() ? accum.toString().trim() : accum.toString();
  }
  
  private void html(StringBuilder accum) {
    for (Node node : childNodes) {
      node.outerHtml(accum);
    }
  }
  


  public <T extends Appendable> T html(T appendable)
  {
    for (Node node : childNodes) {
      node.outerHtml(appendable);
    }
    return appendable;
  }
  





  public Element html(String html)
  {
    empty();
    append(html);
    return this;
  }
  
  public String toString() {
    return outerHtml();
  }
  
  public Element clone()
  {
    return (Element)super.clone();
  }
}
