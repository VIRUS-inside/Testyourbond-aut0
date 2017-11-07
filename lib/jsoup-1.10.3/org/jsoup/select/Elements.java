package org.jsoup.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;



public class Elements
  extends ArrayList<Element>
{
  public Elements() {}
  
  public Elements(int initialCapacity)
  {
    super(initialCapacity);
  }
  
  public Elements(Collection<Element> elements) {
    super(elements);
  }
  
  public Elements(List<Element> elements) {
    super(elements);
  }
  
  public Elements(Element... elements) {
    super(Arrays.asList(elements));
  }
  




  public Elements clone()
  {
    Elements clone = new Elements(size());
    
    for (Element e : this) {
      clone.add(e.clone());
    }
    return clone;
  }
  







  public String attr(String attributeKey)
  {
    for (Element element : this) {
      if (element.hasAttr(attributeKey))
        return element.attr(attributeKey);
    }
    return "";
  }
  




  public boolean hasAttr(String attributeKey)
  {
    for (Element element : this) {
      if (element.hasAttr(attributeKey))
        return true;
    }
    return false;
  }
  






  public List<String> eachAttr(String attributeKey)
  {
    List<String> attrs = new ArrayList(size());
    for (Element element : this) {
      if (element.hasAttr(attributeKey))
        attrs.add(element.attr(attributeKey));
    }
    return attrs;
  }
  





  public Elements attr(String attributeKey, String attributeValue)
  {
    for (Element element : this) {
      element.attr(attributeKey, attributeValue);
    }
    return this;
  }
  




  public Elements removeAttr(String attributeKey)
  {
    for (Element element : this) {
      element.removeAttr(attributeKey);
    }
    return this;
  }
  




  public Elements addClass(String className)
  {
    for (Element element : this) {
      element.addClass(className);
    }
    return this;
  }
  




  public Elements removeClass(String className)
  {
    for (Element element : this) {
      element.removeClass(className);
    }
    return this;
  }
  




  public Elements toggleClass(String className)
  {
    for (Element element : this) {
      element.toggleClass(className);
    }
    return this;
  }
  




  public boolean hasClass(String className)
  {
    for (Element element : this) {
      if (element.hasClass(className))
        return true;
    }
    return false;
  }
  




  public String val()
  {
    if (size() > 0) {
      return first().val();
    }
    return "";
  }
  




  public Elements val(String value)
  {
    for (Element element : this)
      element.val(value);
    return this;
  }
  








  public String text()
  {
    StringBuilder sb = new StringBuilder();
    for (Element element : this) {
      if (sb.length() != 0)
        sb.append(" ");
      sb.append(element.text());
    }
    return sb.toString();
  }
  




  public boolean hasText()
  {
    for (Element element : this) {
      if (element.hasText())
        return true;
    }
    return false;
  }
  







  public List<String> eachText()
  {
    ArrayList<String> texts = new ArrayList(size());
    for (Element el : this) {
      if (el.hasText())
        texts.add(el.text());
    }
    return texts;
  }
  





  public String html()
  {
    StringBuilder sb = new StringBuilder();
    for (Element element : this) {
      if (sb.length() != 0)
        sb.append("\n");
      sb.append(element.html());
    }
    return sb.toString();
  }
  





  public String outerHtml()
  {
    StringBuilder sb = new StringBuilder();
    for (Element element : this) {
      if (sb.length() != 0)
        sb.append("\n");
      sb.append(element.outerHtml());
    }
    return sb.toString();
  }
  






  public String toString()
  {
    return outerHtml();
  }
  






  public Elements tagName(String tagName)
  {
    for (Element element : this) {
      element.tagName(tagName);
    }
    return this;
  }
  





  public Elements html(String html)
  {
    for (Element element : this) {
      element.html(html);
    }
    return this;
  }
  





  public Elements prepend(String html)
  {
    for (Element element : this) {
      element.prepend(html);
    }
    return this;
  }
  





  public Elements append(String html)
  {
    for (Element element : this) {
      element.append(html);
    }
    return this;
  }
  





  public Elements before(String html)
  {
    for (Element element : this) {
      element.before(html);
    }
    return this;
  }
  





  public Elements after(String html)
  {
    for (Element element : this) {
      element.after(html);
    }
    return this;
  }
  








  public Elements wrap(String html)
  {
    Validate.notEmpty(html);
    for (Element element : this) {
      element.wrap(html);
    }
    return this;
  }
  













  public Elements unwrap()
  {
    for (Element element : this) {
      element.unwrap();
    }
    return this;
  }
  










  public Elements empty()
  {
    for (Element element : this) {
      element.empty();
    }
    return this;
  }
  











  public Elements remove()
  {
    for (Element element : this) {
      element.remove();
    }
    return this;
  }
  






  public Elements select(String query)
  {
    return Selector.select(query, this);
  }
  









  public Elements not(String query)
  {
    Elements out = Selector.select(query, this);
    return Selector.filterOut(this, out);
  }
  






  public Elements eq(int index)
  {
    return size() > index ? new Elements(new Element[] { (Element)get(index) }) : new Elements();
  }
  




  public boolean is(String query)
  {
    Evaluator eval = QueryParser.parse(query);
    for (Element e : this) {
      if (e.is(eval))
        return true;
    }
    return false;
  }
  



  public Elements next()
  {
    return siblings(null, true, false);
  }
  




  public Elements next(String query)
  {
    return siblings(query, true, false);
  }
  



  public Elements nextAll()
  {
    return siblings(null, true, true);
  }
  




  public Elements nextAll(String query)
  {
    return siblings(query, true, true);
  }
  



  public Elements prev()
  {
    return siblings(null, false, false);
  }
  




  public Elements prev(String query)
  {
    return siblings(query, false, false);
  }
  



  public Elements prevAll()
  {
    return siblings(null, false, true);
  }
  




  public Elements prevAll(String query)
  {
    return siblings(query, false, true);
  }
  
  private Elements siblings(String query, boolean next, boolean all) {
    Elements els = new Elements();
    Evaluator eval = query != null ? QueryParser.parse(query) : null;
    for (Element e : this) {
      do {
        Element sib = next ? e.nextElementSibling() : e.previousElementSibling();
        if (sib == null) break;
        if (eval == null) {
          els.add(sib);
        } else if (sib.is(eval))
          els.add(sib);
        e = sib;
      } while (all);
    }
    return els;
  }
  



  public Elements parents()
  {
    HashSet<Element> combo = new LinkedHashSet();
    for (Element e : this) {
      combo.addAll(e.parents());
    }
    return new Elements(combo);
  }
  




  public Element first()
  {
    return isEmpty() ? null : (Element)get(0);
  }
  



  public Element last()
  {
    return isEmpty() ? null : (Element)get(size() - 1);
  }
  




  public Elements traverse(NodeVisitor nodeVisitor)
  {
    Validate.notNull(nodeVisitor);
    NodeTraversor traversor = new NodeTraversor(nodeVisitor);
    for (Element el : this) {
      traversor.traverse(el);
    }
    return this;
  }
  




  public List<FormElement> forms()
  {
    ArrayList<FormElement> forms = new ArrayList();
    for (Element el : this)
      if ((el instanceof FormElement))
        forms.add((FormElement)el);
    return forms;
  }
}
