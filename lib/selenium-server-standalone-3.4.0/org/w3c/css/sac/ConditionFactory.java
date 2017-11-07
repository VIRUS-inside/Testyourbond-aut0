package org.w3c.css.sac;

public abstract interface ConditionFactory
{
  public abstract CombinatorCondition createAndCondition(Condition paramCondition1, Condition paramCondition2)
    throws CSSException;
  
  public abstract CombinatorCondition createOrCondition(Condition paramCondition1, Condition paramCondition2)
    throws CSSException;
  
  public abstract NegativeCondition createNegativeCondition(Condition paramCondition)
    throws CSSException;
  
  public abstract PositionalCondition createPositionalCondition(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    throws CSSException;
  
  public abstract AttributeCondition createAttributeCondition(String paramString1, String paramString2, boolean paramBoolean, String paramString3)
    throws CSSException;
  
  public abstract AttributeCondition createIdCondition(String paramString)
    throws CSSException;
  
  public abstract LangCondition createLangCondition(String paramString)
    throws CSSException;
  
  public abstract AttributeCondition createOneOfAttributeCondition(String paramString1, String paramString2, boolean paramBoolean, String paramString3)
    throws CSSException;
  
  public abstract AttributeCondition createBeginHyphenAttributeCondition(String paramString1, String paramString2, boolean paramBoolean, String paramString3)
    throws CSSException;
  
  public abstract AttributeCondition createClassCondition(String paramString1, String paramString2)
    throws CSSException;
  
  public abstract AttributeCondition createPseudoClassCondition(String paramString1, String paramString2)
    throws CSSException;
  
  public abstract Condition createOnlyChildCondition()
    throws CSSException;
  
  public abstract Condition createOnlyTypeCondition()
    throws CSSException;
  
  public abstract ContentCondition createContentCondition(String paramString)
    throws CSSException;
}
