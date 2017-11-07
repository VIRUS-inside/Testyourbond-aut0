package com.steadystate.css.parser.selectors;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.ContentCondition;
import org.w3c.css.sac.LangCondition;
import org.w3c.css.sac.NegativeCondition;
import org.w3c.css.sac.PositionalCondition;















public class ConditionFactoryImpl
  implements ConditionFactory
{
  public ConditionFactoryImpl() {}
  
  public CombinatorCondition createAndCondition(Condition first, Condition second)
    throws CSSException
  {
    return new AndConditionImpl(first, second);
  }
  
  public CombinatorCondition createOrCondition(Condition first, Condition second) throws CSSException {
    throw new CSSException((short)1);
  }
  
  public NegativeCondition createNegativeCondition(Condition condition) throws CSSException {
    throw new CSSException((short)1);
  }
  

  public PositionalCondition createPositionalCondition(int position, boolean typeNode, boolean type)
    throws CSSException
  {
    throw new CSSException((short)1);
  }
  


  public AttributeCondition createAttributeCondition(String localName, String namespaceURI, boolean specified, String value)
    throws CSSException
  {
    return new AttributeConditionImpl(localName, value, specified);
  }
  
  public AttributeCondition createIdCondition(String value) throws CSSException {
    return new IdConditionImpl(value);
  }
  
  public LangCondition createLangCondition(String lang) throws CSSException {
    return new LangConditionImpl(lang);
  }
  


  public AttributeCondition createOneOfAttributeCondition(String localName, String namespaceURI, boolean specified, String value)
    throws CSSException
  {
    return new OneOfAttributeConditionImpl(localName, value, specified);
  }
  


  public AttributeCondition createBeginHyphenAttributeCondition(String localName, String namespaceURI, boolean specified, String value)
    throws CSSException
  {
    return new BeginHyphenAttributeConditionImpl(localName, value, specified);
  }
  


  public AttributeCondition createPrefixAttributeCondition(String localName, String namespaceURI, boolean specified, String value)
    throws CSSException
  {
    return new PrefixAttributeConditionImpl(localName, value, specified);
  }
  


  public AttributeCondition createSuffixAttributeCondition(String localName, String namespaceURI, boolean specified, String value)
    throws CSSException
  {
    return new SuffixAttributeConditionImpl(localName, value, specified);
  }
  


  public AttributeCondition createSubstringAttributeCondition(String localName, String namespaceURI, boolean specified, String value)
    throws CSSException
  {
    return new SubstringAttributeConditionImpl(localName, value, specified);
  }
  
  public AttributeCondition createClassCondition(String namespaceURI, String value)
    throws CSSException
  {
    return new ClassConditionImpl(value);
  }
  
  public AttributeCondition createPseudoClassCondition(String namespaceURI, String value)
    throws CSSException
  {
    return new PseudoClassConditionImpl(value);
  }
  
  public Condition createOnlyChildCondition() throws CSSException {
    throw new CSSException((short)1);
  }
  
  public Condition createOnlyTypeCondition() throws CSSException {
    throw new CSSException((short)1);
  }
  
  public ContentCondition createContentCondition(String data) throws CSSException
  {
    throw new CSSException((short)1);
  }
}
