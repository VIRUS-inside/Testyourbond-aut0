package com.gargoylesoftware.htmlunit.html;

import java.util.List;
import org.w3c.dom.NodeList;

public abstract interface DomNodeList<E extends DomNode>
  extends NodeList, List<E>
{}
