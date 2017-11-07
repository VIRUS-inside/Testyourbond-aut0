package org.apache.xml.utils;

import java.text.CollationElementIterator;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Locale;





























public class StringComparable
  implements Comparable
{
  public static final int UNKNOWN_CASE = -1;
  public static final int UPPER_CASE = 1;
  public static final int LOWER_CASE = 2;
  private String m_text;
  private Locale m_locale;
  private RuleBasedCollator m_collator;
  private String m_caseOrder;
  private int m_mask = -1;
  
  public StringComparable(String text, Locale locale, Collator collator, String caseOrder) {
    m_text = text;
    m_locale = locale;
    m_collator = ((RuleBasedCollator)collator);
    m_caseOrder = caseOrder;
    m_mask = getMask(m_collator.getStrength());
  }
  
  public static final Comparable getComparator(String text, Locale locale, Collator collator, String caseOrder) {
    if ((caseOrder == null) || (caseOrder.length() == 0)) {
      return ((RuleBasedCollator)collator).getCollationKey(text);
    }
    return new StringComparable(text, locale, collator, caseOrder);
  }
  

  public final String toString() { return m_text; }
  
  public int compareTo(Object o) {
    String pattern = ((StringComparable)o).toString();
    if (m_text.equals(pattern)) {
      return 0;
    }
    int savedStrength = m_collator.getStrength();
    int comp = 0;
    
    if ((savedStrength == 0) || (savedStrength == 1)) {
      comp = m_collator.compare(m_text, pattern);
    } else {
      m_collator.setStrength(1);
      comp = m_collator.compare(m_text, pattern);
      m_collator.setStrength(savedStrength);
    }
    if (comp != 0) {
      return comp;
    }
    


    comp = getCaseDiff(m_text, pattern);
    if (comp != 0) {
      return comp;
    }
    return m_collator.compare(m_text, pattern);
  }
  

  private final int getCaseDiff(String text, String pattern)
  {
    int savedStrength = m_collator.getStrength();
    int savedDecomposition = m_collator.getDecomposition();
    m_collator.setStrength(2);
    m_collator.setDecomposition(1);
    
    int[] diff = getFirstCaseDiff(text, pattern, m_locale);
    m_collator.setStrength(savedStrength);
    m_collator.setDecomposition(savedDecomposition);
    if (diff != null) {
      if (m_caseOrder.equals("upper-first")) {
        if (diff[0] == 1) {
          return -1;
        }
        return 1;
      }
      
      if (diff[0] == 2) {
        return -1;
      }
      return 1;
    }
    

    return 0;
  }
  




  private final int[] getFirstCaseDiff(String text, String pattern, Locale locale)
  {
    CollationElementIterator targIter = m_collator.getCollationElementIterator(text);
    CollationElementIterator patIter = m_collator.getCollationElementIterator(pattern);
    int startTarg = -1;
    int endTarg = -1;
    int startPatt = -1;
    int endPatt = -1;
    int done = getElement(-1);
    int patternElement = 0;int targetElement = 0;
    boolean getPattern = true;boolean getTarget = true;
    int[] diff;
    do { String subText;
      String subPatt; String subTextUp; String subPattUp; do { do { for (;;) { if (getPattern) {
              startPatt = patIter.getOffset();
              patternElement = getElement(patIter.next());
              endPatt = patIter.getOffset();
            }
            if (getTarget) {
              startTarg = targIter.getOffset();
              targetElement = getElement(targIter.next());
              endTarg = targIter.getOffset();
            }
            getTarget = getPattern = 1;
            if ((patternElement == done) || (targetElement == done))
              return null;
            if (targetElement == 0) {
              getPattern = false;
            } else { if (patternElement != 0) break;
              getTarget = false;
            } } } while ((targetElement == patternElement) || 
          (startPatt >= endPatt) || (startTarg >= endTarg));
        subText = text.substring(startTarg, endTarg);
        subPatt = pattern.substring(startPatt, endPatt);
        subTextUp = subText.toUpperCase(locale);
        subPattUp = subPatt.toUpperCase(locale);
      } while (m_collator.compare(subTextUp, subPattUp) != 0);
      


      diff = new int[] { -1, -1 };
      if (m_collator.compare(subText, subTextUp) == 0) {
        diff[0] = 1;
      } else if (m_collator.compare(subText, subText.toLowerCase(locale)) == 0) {
        diff[0] = 2;
      }
      if (m_collator.compare(subPatt, subPattUp) == 0) {
        diff[1] = 1;
      } else if (m_collator.compare(subPatt, subPatt.toLowerCase(locale)) == 0) {
        diff[1] = 2;
      }
      
    } while (((diff[0] != 1) || (diff[1] != 2)) && ((diff[0] != 2) || (diff[1] != 1)));
    
    return diff;
  }
  











  private static final int getMask(int strength)
  {
    switch (strength) {
    case 0: 
      return -65536;
    case 1: 
      return 65280;
    }
    return -1;
  }
  


  private final int getElement(int maxStrengthElement)
  {
    return maxStrengthElement & m_mask;
  }
}
