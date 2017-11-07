package net.sourceforge.htmlunit.cyberneko.parsers;

import net.sourceforge.htmlunit.cyberneko.HTMLConfiguration;
import org.apache.xerces.parsers.AbstractSAXParser;





























public class SAXParser
  extends AbstractSAXParser
{
  public SAXParser()
  {
    super(new HTMLConfiguration());
  }
}
