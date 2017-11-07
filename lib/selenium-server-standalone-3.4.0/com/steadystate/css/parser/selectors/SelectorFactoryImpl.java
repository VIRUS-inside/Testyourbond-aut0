package com.steadystate.css.parser.selectors;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CharacterDataSelector;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.ProcessingInstructionSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorFactory;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

















public class SelectorFactoryImpl
  implements SelectorFactory
{
  public SelectorFactoryImpl() {}
  
  public ConditionalSelector createConditionalSelector(SimpleSelector selector, Condition condition)
    throws CSSException
  {
    return new ConditionalSelectorImpl(selector, condition);
  }
  
  public SimpleSelector createAnyNodeSelector() throws CSSException {
    throw new CSSException((short)1);
  }
  
  public SimpleSelector createRootNodeSelector() throws CSSException {
    throw new CSSException((short)1);
  }
  
  public NegativeSelector createNegativeSelector(SimpleSelector selector) throws CSSException {
    throw new CSSException((short)1);
  }
  
  public ElementSelector createElementSelector(String namespaceURI, String localName) throws CSSException
  {
    if (namespaceURI != null) {
      throw new CSSException((short)1);
    }
    return new ElementSelectorImpl(localName);
  }
  
  public ElementSelector createSyntheticElementSelector() throws CSSException {
    return new SyntheticElementSelectorImpl();
  }
  
  public CharacterDataSelector createTextNodeSelector(String data) throws CSSException {
    throw new CSSException((short)1);
  }
  
  public CharacterDataSelector createCDataSectionSelector(String data) throws CSSException
  {
    throw new CSSException((short)1);
  }
  
  public ProcessingInstructionSelector createProcessingInstructionSelector(String target, String data)
    throws CSSException
  {
    throw new CSSException((short)1);
  }
  
  public CharacterDataSelector createCommentSelector(String data) throws CSSException {
    throw new CSSException((short)1);
  }
  
  public ElementSelector createPseudoElementSelector(String namespaceURI, String pseudoName)
    throws CSSException
  {
    if (namespaceURI != null) {
      throw new CSSException((short)1);
    }
    return new PseudoElementSelectorImpl(pseudoName);
  }
  
  public DescendantSelector createDescendantSelector(Selector parent, SimpleSelector descendant)
    throws CSSException
  {
    return new DescendantSelectorImpl(parent, descendant);
  }
  
  public DescendantSelector createChildSelector(Selector parent, SimpleSelector child)
    throws CSSException
  {
    return new ChildSelectorImpl(parent, child);
  }
  

  public SiblingSelector createDirectAdjacentSelector(short nodeType, Selector child, SimpleSelector directAdjacent)
    throws CSSException
  {
    return new DirectAdjacentSelectorImpl(nodeType, child, directAdjacent);
  }
  

  public SiblingSelector createGeneralAdjacentSelector(short nodeType, Selector child, SimpleSelector directAdjacent)
    throws CSSException
  {
    return new GeneralAdjacentSelectorImpl(nodeType, child, directAdjacent);
  }
}
