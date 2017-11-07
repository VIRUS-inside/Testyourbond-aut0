package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import java.util.ArrayList;
import java.util.List;

























class LabelsHelper
  extends NodeList
{
  LabelsHelper(DomElement domeNode)
  {
    super(domeNode, false);
  }
  





  public List<Object> getElements()
  {
    List<Object> response = new ArrayList();
    DomElement domElement = (DomElement)getDomNodeOrDie();
    for (DomNode parent = domElement.getParentNode(); parent != null; parent = parent.getParentNode()) {
      if ((parent instanceof HtmlLabel)) {
        response.add(parent);
      }
    }
    String id = domElement.getId();
    if (id != DomElement.ATTRIBUTE_NOT_DEFINED) {
      for (DomElement label : domElement.getHtmlPageOrNull().getElementsByTagName("label")) {
        if (id.equals(label.getAttribute("for"))) {
          response.add(label);
        }
      }
    }
    
    return response;
  }
}
