package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;









































































public class BeiderMorseEncoder
  implements StringEncoder
{
  private PhoneticEngine engine = new PhoneticEngine(NameType.GENERIC, RuleType.APPROX, true);
  
  public BeiderMorseEncoder() {}
  
  public Object encode(Object source) throws EncoderException { if (!(source instanceof String)) {
      throw new EncoderException("BeiderMorseEncoder encode parameter is not of type String");
    }
    return encode((String)source);
  }
  
  public String encode(String source) throws EncoderException
  {
    if (source == null) {
      return null;
    }
    return engine.encode(source);
  }
  




  public NameType getNameType()
  {
    return engine.getNameType();
  }
  




  public RuleType getRuleType()
  {
    return engine.getRuleType();
  }
  




  public boolean isConcat()
  {
    return engine.isConcat();
  }
  






  public void setConcat(boolean concat)
  {
    engine = new PhoneticEngine(engine.getNameType(), engine.getRuleType(), concat, engine.getMaxPhonemes());
  }
  









  public void setNameType(NameType nameType)
  {
    engine = new PhoneticEngine(nameType, engine.getRuleType(), engine.isConcat(), engine.getMaxPhonemes());
  }
  








  public void setRuleType(RuleType ruleType)
  {
    engine = new PhoneticEngine(engine.getNameType(), ruleType, engine.isConcat(), engine.getMaxPhonemes());
  }
  









  public void setMaxPhonemes(int maxPhonemes)
  {
    engine = new PhoneticEngine(engine.getNameType(), engine.getRuleType(), engine.isConcat(), maxPhonemes);
  }
}
