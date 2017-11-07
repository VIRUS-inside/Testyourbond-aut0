package com.steadystate.css.parser;

import com.steadystate.css.parser.media.MediaQuery;
import java.util.ArrayList;
import java.util.List;
import org.w3c.css.sac.SACMediaList;





















public class SACMediaListImpl
  extends LocatableImpl
  implements SACMediaList
{
  private final List<MediaQuery> mediaQueries_;
  
  public SACMediaListImpl()
  {
    mediaQueries_ = new ArrayList();
  }
  
  public int getLength() {
    return mediaQueries_.size();
  }
  
  public String item(int index) {
    return mediaQuery(index).getMedia();
  }
  
  public MediaQuery mediaQuery(int index) {
    return (MediaQuery)mediaQueries_.get(index);
  }
  
  public void add(String s) {
    add(new MediaQuery(s));
  }
  
  public void add(MediaQuery mediaQuery) {
    mediaQueries_.add(mediaQuery);
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    int len = getLength();
    for (int i = 0; i < len; i++) {
      sb.append(item(i));
      if (i < len - 1) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }
}
