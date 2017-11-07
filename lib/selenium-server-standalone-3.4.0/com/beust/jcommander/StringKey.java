package com.beust.jcommander;

public class StringKey
  implements FuzzyMap.IKey
{
  private String m_name;
  
  public StringKey(String name)
  {
    m_name = name;
  }
  
  public String getName()
  {
    return m_name;
  }
  
  public String toString()
  {
    return m_name;
  }
  
  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (m_name == null ? 0 : m_name.hashCode());
    return result;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    StringKey other = (StringKey)obj;
    if (m_name == null) {
      if (m_name != null)
        return false;
    } else if (!m_name.equals(m_name))
      return false;
    return true;
  }
}
