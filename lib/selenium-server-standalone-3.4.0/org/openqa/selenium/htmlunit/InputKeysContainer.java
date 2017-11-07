package org.openqa.selenium.htmlunit;

import org.openqa.selenium.Keys;





















public class InputKeysContainer
{
  private final StringBuilder builder = new StringBuilder();
  private final boolean submitKeyFound;
  private boolean capitalize = false;
  
  public InputKeysContainer(CharSequence... sequences) {
    this(false, sequences);
  }
  
  public InputKeysContainer(boolean trimPastEnterKey, CharSequence... sequences) {
    for (CharSequence seq : sequences) {
      builder.append(seq);
    }
    
    int indexOfSubmitKey = indexOfSubmitKey();
    submitKeyFound = (indexOfSubmitKey != -1);
    


    if ((trimPastEnterKey) && (indexOfSubmitKey != -1)) {
      builder.delete(indexOfSubmitKey, builder.length());
    }
  }
  
  private int indexOfSubmitKey() {
    CharSequence[] terminators = { "\n", Keys.ENTER, Keys.RETURN };
    for (CharSequence terminator : terminators) {
      String needle = String.valueOf(terminator);
      int index = builder.indexOf(needle);
      if (index != -1) {
        return index;
      }
    }
    
    return -1;
  }
  
  public String toString()
  {
    String toReturn = builder.toString();
    toReturn = toReturn.replaceAll(Keys.ENTER.toString(), "\r");
    toReturn = toReturn.replaceAll(Keys.RETURN.toString(), "\r");
    if (capitalize) {
      return toReturn.toUpperCase();
    }
    return toReturn;
  }
  
  public boolean wasSubmitKeyFound() {
    return submitKeyFound;
  }
  
  public void setCapitalization(boolean capitalize) {
    this.capitalize = capitalize;
  }
}
