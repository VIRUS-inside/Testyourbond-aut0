package junit.framework;

public class ComparisonCompactor
{
  private static final String ELLIPSIS = "...";
  private static final String DELTA_END = "]";
  private static final String DELTA_START = "[";
  private int fContextLength;
  private String fExpected;
  private String fActual;
  private int fPrefix;
  private int fSuffix;
  
  public ComparisonCompactor(int contextLength, String expected, String actual)
  {
    fContextLength = contextLength;
    fExpected = expected;
    fActual = actual;
  }
  
  public String compact(String message) {
    if ((fExpected == null) || (fActual == null) || (areStringsEqual())) {
      return Assert.format(message, fExpected, fActual);
    }
    
    findCommonPrefix();
    findCommonSuffix();
    String expected = compactString(fExpected);
    String actual = compactString(fActual);
    return Assert.format(message, expected, actual);
  }
  
  private String compactString(String source) {
    String result = "[" + source.substring(fPrefix, source.length() - fSuffix + 1) + "]";
    if (fPrefix > 0) {
      result = computeCommonPrefix() + result;
    }
    if (fSuffix > 0) {
      result = result + computeCommonSuffix();
    }
    return result;
  }
  
  private void findCommonPrefix() {
    fPrefix = 0;
    int end = Math.min(fExpected.length(), fActual.length());
    while ((fPrefix < end) && 
      (fExpected.charAt(fPrefix) == fActual.charAt(fPrefix))) {
      fPrefix += 1;
    }
  }
  


  private void findCommonSuffix()
  {
    int expectedSuffix = fExpected.length() - 1;
    int actualSuffix = fActual.length() - 1;
    for (; (actualSuffix >= fPrefix) && (expectedSuffix >= fPrefix); expectedSuffix--) {
      if (fExpected.charAt(expectedSuffix) != fActual.charAt(actualSuffix)) {
        break;
      }
      actualSuffix--;
    }
    


    fSuffix = (fExpected.length() - expectedSuffix);
  }
  
  private String computeCommonPrefix() {
    return (fPrefix > fContextLength ? "..." : "") + fExpected.substring(Math.max(0, fPrefix - fContextLength), fPrefix);
  }
  
  private String computeCommonSuffix() {
    int end = Math.min(fExpected.length() - fSuffix + 1 + fContextLength, fExpected.length());
    return fExpected.substring(fExpected.length() - fSuffix + 1, end) + (fExpected.length() - fSuffix + 1 < fExpected.length() - fContextLength ? "..." : "");
  }
  
  private boolean areStringsEqual() {
    return fExpected.equals(fActual);
  }
}
