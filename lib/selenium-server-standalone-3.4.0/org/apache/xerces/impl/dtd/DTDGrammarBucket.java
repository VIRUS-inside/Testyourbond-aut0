package org.apache.xerces.impl.dtd;

import java.util.Hashtable;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;

public class DTDGrammarBucket
{
  protected final Hashtable fGrammars = new Hashtable();
  protected DTDGrammar fActiveGrammar;
  protected boolean fIsStandalone;
  
  public DTDGrammarBucket() {}
  
  public void putGrammar(DTDGrammar paramDTDGrammar)
  {
    XMLDTDDescription localXMLDTDDescription = (XMLDTDDescription)paramDTDGrammar.getGrammarDescription();
    fGrammars.put(localXMLDTDDescription, paramDTDGrammar);
  }
  
  public DTDGrammar getGrammar(XMLGrammarDescription paramXMLGrammarDescription)
  {
    return (DTDGrammar)fGrammars.get((XMLDTDDescription)paramXMLGrammarDescription);
  }
  
  public void clear()
  {
    fGrammars.clear();
    fActiveGrammar = null;
    fIsStandalone = false;
  }
  
  void setStandalone(boolean paramBoolean)
  {
    fIsStandalone = paramBoolean;
  }
  
  boolean getStandalone()
  {
    return fIsStandalone;
  }
  
  void setActiveGrammar(DTDGrammar paramDTDGrammar)
  {
    fActiveGrammar = paramDTDGrammar;
  }
  
  DTDGrammar getActiveGrammar()
  {
    return fActiveGrammar;
  }
}
