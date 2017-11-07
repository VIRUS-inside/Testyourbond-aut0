package com.steadystate.css.parser.media;

import com.steadystate.css.dom.Property;
import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.format.CSSFormatable;
import com.steadystate.css.parser.LocatableImpl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


















public class MediaQuery
  extends LocatableImpl
  implements CSSFormatable, Serializable
{
  private static final long serialVersionUID = 456776383828897471L;
  private String media_;
  private List<Property> properties_;
  private boolean isOnly_;
  private boolean isNot_;
  
  public MediaQuery(String media)
  {
    this(media, false, false);
  }
  
  public MediaQuery(String media, boolean isOnly, boolean isNot) {
    setMedia(media);
    properties_ = new ArrayList(10);
    isOnly_ = isOnly;
    isNot_ = isNot;
  }
  
  public String getMedia() {
    return media_;
  }
  
  public void setMedia(String media) {
    media_ = media;
  }
  
  public List<Property> getProperties() {
    return properties_;
  }
  
  public void addMediaProperty(Property mp) {
    properties_.add(mp);
  }
  
  public boolean isOnly() {
    return isOnly_;
  }
  
  public boolean isNot() {
    return isNot_;
  }
  


  public String getCssText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder();
    
    if (isOnly_) {
      sb.append("only ");
    }
    if (isNot_) {
      sb.append("not ");
    }
    
    sb.append(getMedia());
    
    for (Property prop : properties_)
    {

      sb.append(" and (").append(prop.getCssText(format)).append(')');
    }
    return sb.toString();
  }
  
  public String toString()
  {
    return getCssText(null);
  }
}
