package org.apache.xpath.objects;

import org.apache.xml.utils.XMLString;

abstract class Comparator
{
  Comparator() {}
  
  abstract boolean compareStrings(XMLString paramXMLString1, XMLString paramXMLString2);
  
  abstract boolean compareNumbers(double paramDouble1, double paramDouble2);
}
