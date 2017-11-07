package org.w3c.css.sac;

public abstract interface SelectorFactory
{
  public abstract ConditionalSelector createConditionalSelector(SimpleSelector paramSimpleSelector, Condition paramCondition)
    throws CSSException;
  
  public abstract SimpleSelector createAnyNodeSelector()
    throws CSSException;
  
  public abstract SimpleSelector createRootNodeSelector()
    throws CSSException;
  
  public abstract NegativeSelector createNegativeSelector(SimpleSelector paramSimpleSelector)
    throws CSSException;
  
  public abstract ElementSelector createElementSelector(String paramString1, String paramString2)
    throws CSSException;
  
  public abstract CharacterDataSelector createTextNodeSelector(String paramString)
    throws CSSException;
  
  public abstract CharacterDataSelector createCDataSectionSelector(String paramString)
    throws CSSException;
  
  public abstract ProcessingInstructionSelector createProcessingInstructionSelector(String paramString1, String paramString2)
    throws CSSException;
  
  public abstract CharacterDataSelector createCommentSelector(String paramString)
    throws CSSException;
  
  public abstract ElementSelector createPseudoElementSelector(String paramString1, String paramString2)
    throws CSSException;
  
  public abstract DescendantSelector createDescendantSelector(Selector paramSelector, SimpleSelector paramSimpleSelector)
    throws CSSException;
  
  public abstract DescendantSelector createChildSelector(Selector paramSelector, SimpleSelector paramSimpleSelector)
    throws CSSException;
  
  public abstract SiblingSelector createDirectAdjacentSelector(short paramShort, Selector paramSelector, SimpleSelector paramSimpleSelector)
    throws CSSException;
}
