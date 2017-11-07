package com.gargoylesoftware.htmlunit.svg;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.ElementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.xml.sax.Attributes;




















public class SvgElementFactory
  implements ElementFactory
{
  private static Class<?>[] CLASSES_ = { SvgAltGlyph.class, SvgAltGlyphDef.class, SvgAltGlyphItem.class, 
    SvgAnchor.class, SvgAnimate.class, SvgAnimateColor.class, SvgAnimateMotion.class, SvgAnimateTransform.class, 
    SvgCircle.class, SvgClipPath.class, SvgColorProfile.class, SvgCursor.class, SvgDefs.class, SvgDesc.class, 
    SvgEllipse.class, SvgFeBlend.class, SvgFeColorMatrix.class, SvgFeComponentTransfer.class, 
    SvgFeComposite.class, SvgFeConvolveMatrix.class, SvgFeDiffuseLighting.class, SvgFeDisplacementMap.class, 
    SvgFeDistantLight.class, SvgFeFlood.class, SvgFeFuncA.class, SvgFeFuncB.class, SvgFeFuncG.class, 
    SvgFeFuncR.class, SvgFeGaussianBlur.class, SvgFeImage.class, SvgFeMerge.class, SvgFeMergeNode.class, 
    SvgFeMorphology.class, SvgFeOffset.class, SvgFePointLight.class, SvgFeSpecularLighting.class, 
    SvgFeSpotLight.class, SvgFeTile.class, SvgFeTurbulence.class, SvgFilter.class, SvgFont.class, 
    SvgFontFace.class, SvgFontFaceFormat.class, SvgFontFaceName.class, SvgFontFaceSrc.class, 
    SvgFontFaceURI.class, SvgForeignObject.class, SvgGlyph.class, SvgGlyphRef.class, SvgGroup.class, 
    SvgHKern.class, SvgImage.class, SvgLine.class, SvgLinearGradient.class, SvgMarker.class, SvgMask.class, 
    SvgMetadata.class, SvgMissingGlyph.class, SvgMPath.class, SvgPath.class, SvgPattern.class, SvgPolygon.class, 
    SvgPolyline.class, SvgRadialGradient.class, SvgRect.class, SvgScript.class, SvgSet.class, SvgStop.class, 
    SvgStyle.class, SvgSvg.class, SvgSwitch.class, SvgSymbol.class, SvgText.class, SvgTextPath.class, 
    SvgTitle.class, SvgTRef.class, SvgTSpan.class, SvgUse.class, SvgView.class, SvgVKern.class };
  

  private static Map<String, Class<?>> ELEMENTS_ = new HashMap();
  
  static {
    try {
      for (Class<?> klass : CLASSES_) {
        ELEMENTS_.put(klass.getField("TAG_NAME").get(null).toString().toLowerCase(), klass);
      }
    }
    catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  

  public SvgElementFactory() {}
  
  public DomElement createElement(SgmlPage page, String tagName, Attributes attributes)
  {
    throw new IllegalStateException("SVG.createElement not yet implemented!");
  }
  




  public DomElement createElementNS(SgmlPage page, String namespaceURI, String qualifiedName, Attributes attributes)
  {
    return createElementNS(page, namespaceURI, qualifiedName, attributes, false);
  }
  





  public DomElement createElementNS(SgmlPage page, String namespaceURI, String qualifiedName, Attributes attributes, boolean checkBrowserCompatibility)
  {
    Map<String, DomAttr> attributeMap = toMap(page, attributes);
    qualifiedName = qualifiedName.toLowerCase();
    String tagName = qualifiedName;
    if (tagName.indexOf(':') != -1) {
      tagName = tagName.substring(tagName.indexOf(':') + 1);
    }
    DomElement element = null;
    
    Class<?> klass = (Class)ELEMENTS_.get(tagName.toLowerCase());
    if (klass != null) {
      try {
        element = 
          (DomElement)klass.getDeclaredConstructors()[0].newInstance(new Object[] { namespaceURI, qualifiedName, page, attributeMap });
      }
      catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }
    if (element == null) {
      if (page.getWebClient().getBrowserVersion().hasFeature(BrowserVersionFeatures.SVG_UNKNOWN_ARE_DOM)) {
        element = new DomElement(namespaceURI, qualifiedName, page, attributeMap);
      }
      else {
        element = new SvgElement(namespaceURI, qualifiedName, page, attributeMap);
      }
    }
    return element;
  }
  
  private static Map<String, DomAttr> toMap(SgmlPage page, Attributes attributes) {
    Map<String, DomAttr> attributeMap = null;
    if (attributes != null) {
      attributeMap = new LinkedHashMap(attributes.getLength());
      for (int i = 0; i < attributes.getLength(); i++) {
        String qName = attributes.getQName(i);
        
        if (!attributeMap.containsKey(qName)) {
          String namespaceURI = attributes.getURI(i);
          if ((namespaceURI != null) && (namespaceURI.isEmpty())) {
            namespaceURI = null;
          }
          DomAttr newAttr = new DomAttr(page, namespaceURI, qName, attributes.getValue(i), true);
          attributeMap.put(qName, newAttr);
        }
      }
    }
    return attributeMap;
  }
  




  public boolean isSupported(String tagNameLowerCase)
  {
    return ELEMENTS_.containsKey(tagNameLowerCase);
  }
}
