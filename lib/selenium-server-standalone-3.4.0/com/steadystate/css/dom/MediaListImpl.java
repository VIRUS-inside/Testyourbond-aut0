package com.steadystate.css.dom;

import com.steadystate.css.format.CSSFormat;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.Locatable;
import com.steadystate.css.parser.SACMediaListImpl;
import com.steadystate.css.parser.media.MediaQuery;
import com.steadystate.css.userdata.UserDataConstants;
import com.steadystate.css.util.LangUtils;
import com.steadystate.css.util.ThrowCssExceptionErrorHandler;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Locator;
import org.w3c.css.sac.SACMediaList;
import org.w3c.dom.DOMException;
import org.w3c.dom.stylesheets.MediaList;

























public class MediaListImpl
  extends CSSOMObjectImpl
  implements MediaList
{
  private static final long serialVersionUID = 6662784733573034870L;
  private List<MediaQuery> mediaQueries_;
  
  public MediaListImpl(SACMediaList mediaList)
  {
    this();
    
    setMediaList(mediaList);
    
    if ((mediaList instanceof Locatable)) {
      Locator locator = ((Locatable)mediaList).getLocator();
      if (locator != null) {
        setUserData(UserDataConstants.KEY_LOCATOR, locator);
      }
    }
  }
  



  public MediaListImpl()
  {
    mediaQueries_ = new ArrayList(10);
  }
  
  public String getMediaText() {
    return getMediaText(null);
  }
  






  public String getMediaText(CSSFormat format)
  {
    StringBuilder sb = new StringBuilder("");
    boolean isNotFirst = false;
    for (MediaQuery mediaQuery : mediaQueries_) {
      if (isNotFirst) {
        sb.append(", ");
      }
      else {
        isNotFirst = true;
      }
      sb.append(mediaQuery.getCssText(format));
    }
    return sb.toString();
  }
  
  public void setMediaText(String mediaText) throws DOMException {
    InputSource source = new InputSource(new StringReader(mediaText));
    try {
      CSSOMParser parser = new CSSOMParser();
      parser.setErrorHandler(ThrowCssExceptionErrorHandler.INSTANCE);
      SACMediaList sml = parser.parseMedia(source);
      setMediaList(sml);
    }
    catch (CSSParseException e) {
      throw new DOMException((short)12, e.getLocalizedMessage());
    }
    catch (IOException e) {
      throw new DOMException((short)8, e.getLocalizedMessage());
    }
  }
  
  public int getLength() {
    return mediaQueries_.size();
  }
  
  public String item(int index) {
    MediaQuery mq = mediaQuery(index);
    if (null == mq) {
      return null;
    }
    
    return mq.getMedia();
  }
  
  public MediaQuery mediaQuery(int index) {
    if ((index < 0) || (index >= mediaQueries_.size())) {
      return null;
    }
    return (MediaQuery)mediaQueries_.get(index);
  }
  
  public void deleteMedium(String oldMedium) throws DOMException {
    for (MediaQuery mediaQuery : mediaQueries_) {
      String str = mediaQuery.getMedia();
      if (str.equalsIgnoreCase(oldMedium)) {
        mediaQueries_.remove(mediaQuery);
        return;
      }
    }
    throw new DOMExceptionImpl((short)8, 18);
  }
  
  public void appendMedium(String newMedium) throws DOMException {
    mediaQueries_.add(new MediaQuery(newMedium));
  }
  
  public String toString()
  {
    return getMediaText(null);
  }
  
  public void setMedia(List<String> media) {
    mediaQueries_.clear();
    for (String medium : media) {
      mediaQueries_.add(new MediaQuery(medium));
    }
  }
  
  private void setMediaList(SACMediaList mediaList) {
    if ((mediaList instanceof SACMediaListImpl)) {
      SACMediaListImpl impl = (SACMediaListImpl)mediaList;
      for (int i = 0; i < mediaList.getLength(); i++) {
        mediaQueries_.add(impl.mediaQuery(i));
      }
      return;
    }
    
    for (int i = 0; i < mediaList.getLength(); i++) {
      mediaQueries_.add(new MediaQuery(mediaList.item(i)));
    }
  }
  
  private boolean equalsMedia(MediaList ml) {
    if ((ml == null) || (getLength() != ml.getLength())) {
      return false;
    }
    for (int i = 0; i < getLength(); i++) {
      String m1 = item(i);
      String m2 = ml.item(i);
      if (!LangUtils.equals(m1, m2)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof MediaList)) {
      return false;
    }
    MediaList ml = (MediaList)obj;
    return (super.equals(obj)) && (equalsMedia(ml));
  }
  
  public int hashCode()
  {
    int hash = super.hashCode();
    hash = LangUtils.hashCode(hash, mediaQueries_);
    return hash;
  }
}
