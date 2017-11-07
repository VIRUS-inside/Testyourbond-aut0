package org.apache.xalan.transformer;








public class DecimalToRoman
{
  public long m_postValue;
  






  public String m_postLetter;
  






  public long m_preValue;
  





  public String m_preLetter;
  






  public DecimalToRoman(long postValue, String postLetter, long preValue, String preLetter)
  {
    m_postValue = postValue;
    m_postLetter = postLetter;
    m_preValue = preValue;
    m_preLetter = preLetter;
  }
}
