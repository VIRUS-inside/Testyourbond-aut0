package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


























public final class StyleAttributes
{
  private static final Map<String, Definition> styles_ = new HashMap();
  
  static {
    for (Definition definition : Definition.values()) {
      styles_.put(definition.getPropertyName(), definition);
    }
  }
  




  private StyleAttributes() {}
  



  public static Definition getDefinition(String propertyName, BrowserVersion browserVersion)
  {
    if (browserVersion == null) {
      return null;
    }
    
    Definition definition = (Definition)styles_.get(propertyName);
    if (definition == null) {
      return null;
    }
    if (!definition.isAvailable(browserVersion, false)) {
      return null;
    }
    return definition;
  }
  




  public static List<Definition> getDefinitions(BrowserVersion browserVersion)
  {
    List<Definition> list = new ArrayList();
    for (Definition definition : Definition.values()) {
      if (definition.isAvailable(browserVersion, true)) {
        list.add(definition);
      }
    }
    
    return list;
  }
  



  public static enum Definition
  {
    ACCELERATOR(
      "accelerator", "accelerator", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    ALIGN_CONTENT(
      "alignContent", "align-content", new BrowserConfiguration[] { BrowserConfiguration.ie("stretch"), 
      BrowserConfiguration.ff("auto"), BrowserConfiguration.chrome("normal") }), 
    
    ALIGN_CONTENT_(
      "align-content", "align-content", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    ALIGN_ITEMS(
      "alignItems", "align-items", new BrowserConfiguration[] { BrowserConfiguration.ff("start"), 
      BrowserConfiguration.ie("stretch"), BrowserConfiguration.chrome("normal") }), 
    
    ALIGN_ITEMS_(
      "align-items", "align-items", new BrowserConfiguration[] { BrowserConfiguration.ff("start") }), 
    
    ALIGN_SELF(
      "alignSelf", "align-self", new BrowserConfiguration[] { BrowserConfiguration.ff("start"), 
      BrowserConfiguration.ie("auto"), BrowserConfiguration.chrome("normal") }), 
    
    ALIGN_SELF_(
      "align-self", "align-self", new BrowserConfiguration[] { BrowserConfiguration.ff("start") }), 
    
    ALIGNMENT_BASELINE(
      "alignmentBaseline", "alignment-baseline", new BrowserConfiguration[] { BrowserConfiguration.ie("auto"), BrowserConfiguration.chrome("auto") }), 
    
    ALL(
      "all", "all", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.chrome("") }), 
    
    ANIMATION(
      "animation", "animation", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.ie(""), BrowserConfiguration.chrome("none 0s ease 0s 1 normal none running") }), 
    
    ANIMATION_DELAY(
      "animationDelay", "animation-delay", new BrowserConfiguration[] { BrowserConfiguration.ff("0s"), BrowserConfiguration.ie("0s"), BrowserConfiguration.chrome("0s") }), 
    
    ANIMATION_DELAY_(
      "animation-delay", "animation-delay", new BrowserConfiguration[] { BrowserConfiguration.ff("0s") }), 
    
    ANIMATION_DIRECTION(
      "animationDirection", "animation-direction", new BrowserConfiguration[] {
      BrowserConfiguration.ff("normal"), BrowserConfiguration.ie("normal"), BrowserConfiguration.chrome("normal") }), 
    
    ANIMATION_DIRECTION_(
      "animation-direction", "animation-direction", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    ANIMATION_DURATION(
      "animationDuration", "animation-duration", new BrowserConfiguration[] { BrowserConfiguration.ff("0s"), BrowserConfiguration.ie("0s"), BrowserConfiguration.chrome("0s") }), 
    
    ANIMATION_DURATION_(
      "animation-duration", "animation-duration", new BrowserConfiguration[] { BrowserConfiguration.ff("0s") }), 
    
    ANIMATION_FILL_MODE(
      "animationFillMode", "animation-fill-mode", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    ANIMATION_FILL_MODE_(
      "animation-fill-mode", "animation-fill-mode", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    ANIMATION_ITERATION_COUNT(
      "animationIterationCount", "animation-iteration-count", new BrowserConfiguration[] {
      BrowserConfiguration.ff("1"), BrowserConfiguration.ie("1"), BrowserConfiguration.chrome("1") }), 
    
    ANIMATION_ITERATION_COUNT_(
      "animation-iteration-count", "animation-iteration-count", new BrowserConfiguration[] { BrowserConfiguration.ff("1") }), 
    
    ANIMATION_NAME(
      "animationName", "animation-name", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    ANIMATION_NAME_(
      "animation-name", "animation-name", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    ANIMATION_PLAY_STATE(
      "animationPlayState", "animation-play-state", new BrowserConfiguration[] {
      BrowserConfiguration.ff("running"), BrowserConfiguration.ie("running"), BrowserConfiguration.chrome("running") }), 
    
    ANIMATION_PLAY_STATE_(
      "animation-play-state", "animation-play-state", new BrowserConfiguration[] { BrowserConfiguration.ff("running") }), 
    
    ANIMATION_TIMING_FUNCTION(
      "animationTimingFunction", 
      "animation-timing-function", new BrowserConfiguration[] {
      BrowserConfiguration.ff("ease"), 
      BrowserConfiguration.ie("cubic-bezier(0.25, 0.1, 0.25, 1)"), BrowserConfiguration.chrome("ease") }), 
    
    ANIMATION_TIMING_FUNCTION_(
      "animation-timing-function", "animation-timing-function", new BrowserConfiguration[] {
      BrowserConfiguration.ff("ease") }), 
    
    AZIMUTH(
      "azimuth", "azimuth", new BrowserConfiguration[0]), 
    
    BACKFACE_VISIBILITY(
      "backfaceVisibility", "backface-visibility", new BrowserConfiguration[] {
      BrowserConfiguration.ff("visible"), BrowserConfiguration.ie("visible"), BrowserConfiguration.chrome("visible") }), 
    
    BACKFACE_VISIBILITY_(
      "backface-visibility", "backface-visibility", new BrowserConfiguration[] { BrowserConfiguration.ff("visible") }), 
    
    BACKGROUND(
      "background", "background", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.ie(""), 
      BrowserConfiguration.chrome("rgba(0, 0, 0, 0) none repeat scroll 0% 0% / auto padding-box border-box") }), 
    
    BACKGROUND_ATTACHMENT(
      "backgroundAttachment", "background-attachment", new BrowserConfiguration[] { BrowserConfiguration.chrome("scroll"), 
      BrowserConfiguration.ff("scroll"), BrowserConfiguration.ie("scroll") }), 
    
    BACKGROUND_ATTACHMENT_(
      "background-attachment", "background-attachment", new BrowserConfiguration[] { BrowserConfiguration.ff("scroll") }), 
    
    BACKGROUND_BLEND_MODE(
      "backgroundBlendMode", "background-blend-mode", new BrowserConfiguration[] { BrowserConfiguration.ff("normal"), BrowserConfiguration.chrome("normal") }), 
    
    BACKGROUND_BLEND_MODE_(
      "background-blend-mode", "background-blend-mode", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    BACKGROUND_CLIP(
      "backgroundClip", "background-clip", new BrowserConfiguration[] {
      BrowserConfiguration.ff("border-box"), BrowserConfiguration.ie("border-box"), BrowserConfiguration.chrome("border-box") }), 
    
    BACKGROUND_CLIP_(
      "background-clip", "background-clip", new BrowserConfiguration[] { BrowserConfiguration.ff("border-box") }), 
    
    BACKGROUND_COLOR(
      "backgroundColor", "background-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgba(0, 0, 0, 0)"), BrowserConfiguration.ff("transparent"), 
      BrowserConfiguration.ie("transparent") }), 
    
    BACKGROUND_COLOR_(
      "background-color", "background-color", new BrowserConfiguration[] { BrowserConfiguration.ff("transparent") }), 
    
    BACKGROUND_IMAGE(
      "backgroundImage", "background-image", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    BACKGROUND_IMAGE_(
      "background-image", "background-image", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    BACKGROUND_ORIGIN(
      "backgroundOrigin", "background-origin", new BrowserConfiguration[] {
      BrowserConfiguration.ff("padding-box"), BrowserConfiguration.ie("padding-box"), BrowserConfiguration.chrome("padding-box") }), 
    
    BACKGROUND_ORIGIN_(
      "background-origin", "background-origin", new BrowserConfiguration[] { BrowserConfiguration.ff("padding-box") }), 
    
    BACKGROUND_POSITION(
      "backgroundPosition", "background-position", new BrowserConfiguration[] { BrowserConfiguration.chrome("0% 0%"), BrowserConfiguration.ff("0% 0%"), BrowserConfiguration.ie("0% 0%") }), 
    
    BACKGROUND_POSITION_(
      "background-position", "background-position", new BrowserConfiguration[] { BrowserConfiguration.ff("0% 0%") }), 
    
    BACKGROUND_POSITION_X(
      "backgroundPositionX", "background-position-x", new BrowserConfiguration[] {
      BrowserConfiguration.ie("undefined"), BrowserConfiguration.chrome("0%") }), 
    
    BACKGROUND_POSITION_Y(
      "backgroundPositionY", "background-position-y", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined"), BrowserConfiguration.chrome("0%") }), 
    
    BACKGROUND_REPEAT(
      "backgroundRepeat", "background-repeat", new BrowserConfiguration[] { BrowserConfiguration.chrome("repeat"), BrowserConfiguration.ff("repeat"), BrowserConfiguration.ie("repeat") }), 
    
    BACKGROUND_REPEAT_(
      "background-repeat", "background-repeat", new BrowserConfiguration[] { BrowserConfiguration.ff("repeat") }), 
    
    BACKGROUND_REPEAT_X(
      "backgroundRepeatX", "background-repeat-x", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    BACKGROUND_REPEAT_Y(
      "backgroundRepeatY", "background-repeat-y", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    BACKGROUND_SIZE(
      "backgroundSize", "background-size", new BrowserConfiguration[] { BrowserConfiguration.ff("auto auto"), BrowserConfiguration.ie("auto"), BrowserConfiguration.chrome("auto") }), 
    
    BACKGROUND_SIZE_(
      "background-size", "background-size", new BrowserConfiguration[] { BrowserConfiguration.ff("auto auto") }), 
    
    BASELINE_SHIFT(
      "baselineShift", "baseline-shift", new BrowserConfiguration[] { BrowserConfiguration.ie("baseline"), BrowserConfiguration.chrome("0px") }), 
    
    BEHAVIOR(
      "behavior", "behavior", new BrowserConfiguration[0]), 
    
    BLOCK_SIZE(
      "blockSize", "block-size", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.chrome("0px") }), 
    
    BLOCK_SIZE_(
      "block-size", "block-size", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER(
      "border", "border", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px none rgb(0, 0, 0)"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_BLOCK_END(
      "borderBlockEnd", "border-block-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_END_(
      "border-block-end", "border-block-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_END_COLOR(
      "borderBlockEndColor", "border-block-end-color", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_END_COLOR_(
      "border-block-end-color", "border-block-end-color", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_END_STYLE(
      "borderBlockEndStyle", "border-block-end-style", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_END_STYLE_(
      "border-block-end-style", "border-block-end-style", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_END_WIDTH(
      "borderBlockEndWidth", "border-block-end-width", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_END_WIDTH_(
      "border-block-end-width", "border-block-end-width", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_START(
      "borderBlockStart", "border-block-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_START_(
      "border-block-start", "border-block-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_START_COLOR(
      "borderBlockStartColor", "border-block-start-color", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_START_COLOR_(
      "border-block-start-color", "border-block-start-color", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_START_STYLE(
      "borderBlockStartStyle", "border-block-start-style", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_START_STYLE_(
      "border-block-start-style", "border-block-start-style", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_START_WIDTH(
      "borderBlockStartWidth", "border-block-start-width", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BLOCK_START_WIDTH_(
      "border-block-start-width", "border-block-start-width", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BOTTOM(
      "borderBottom", "border-bottom", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px none rgb(0, 0, 0)"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_BOTTOM_(
      "border-bottom", "border-bottom", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_BOTTOM_COLOR(
      "borderBottomColor", "border-bottom-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)"), BrowserConfiguration.ff("rgb(0, 0, 0)"), 
      BrowserConfiguration.ie("rgb(0, 0, 0)") }), 
    
    BORDER_BOTTOM_COLOR_(
      "border-bottom-color", "border-bottom-color", new BrowserConfiguration[] { BrowserConfiguration.ff("rgb(0, 0, 0)") }), 
    
    BORDER_BOTTOM_LEFT_RADIUS(
      "borderBottomLeftRadius", "border-bottom-left-radius", new BrowserConfiguration[] {
      BrowserConfiguration.ff("0px"), BrowserConfiguration.ie("0px"), BrowserConfiguration.chrome("0px") }), 
    
    BORDER_BOTTOM_LEFT_RADIUS_(
      "border-bottom-left-radius", "border-bottom-left-radius", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    BORDER_BOTTOM_RIGHT_RADIUS(
      "borderBottomRightRadius", "border-bottom-right-radius", new BrowserConfiguration[] {
      BrowserConfiguration.ff("0px"), BrowserConfiguration.ie("0px"), BrowserConfiguration.chrome("0px") }), 
    
    BORDER_BOTTOM_RIGHT_RADIUS_(
      "border-bottom-right-radius", "border-bottom-right-radius", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    BORDER_BOTTOM_STYLE(
      "borderBottomStyle", "border-bottom-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    BORDER_BOTTOM_STYLE_(
      "border-bottom-style", "border-bottom-style", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    BORDER_BOTTOM_WIDTH(
      "borderBottomWidth", "border-bottom-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff("0px"), BrowserConfiguration.ie("0px") }), 
    
    BORDER_BOTTOM_WIDTH_(
      "border-bottom-width", "border-bottom-width", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    BORDER_COLLAPSE(
      "borderCollapse", "border-collapse", new BrowserConfiguration[] { BrowserConfiguration.chrome("separate"), BrowserConfiguration.ff("separate"), BrowserConfiguration.ie("separate") }), 
    
    BORDER_COLLAPSE_(
      "border-collapse", "border-collapse", new BrowserConfiguration[] { BrowserConfiguration.ff("separate") }), 
    
    BORDER_COLOR(
      "borderColor", "border-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_COLOR_(
      "border-color", "border-color", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_IMAGE(
      "borderImage", "border-image", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.ie(""), BrowserConfiguration.chrome("none") }), 
    
    BORDER_IMAGE_(
      "border-image", "border-image", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_IMAGE_OUTSET(
      "borderImageOutset", "border-image-outset", new BrowserConfiguration[] { BrowserConfiguration.ff("0 0 0 0"), BrowserConfiguration.ie("0"), BrowserConfiguration.chrome("0px") }), 
    
    BORDER_IMAGE_OUTSET_(
      "border-image-outset", "border-image-outset", new BrowserConfiguration[] { BrowserConfiguration.ff("0 0 0 0") }), 
    
    BORDER_IMAGE_REPEAT(
      "borderImageRepeat", "border-image-repeat", new BrowserConfiguration[] {
      BrowserConfiguration.ff("stretch stretch"), BrowserConfiguration.ie("stretch"), BrowserConfiguration.chrome("stretch") }), 
    
    BORDER_IMAGE_REPEAT_(
      "border-image-repeat", "border-image-repeat", new BrowserConfiguration[] { BrowserConfiguration.ff("stretch stretch") }), 
    
    BORDER_IMAGE_SLICE(
      "borderImageSlice", "border-image-slice", new BrowserConfiguration[] {
      BrowserConfiguration.ff("100% 100% 100% 100%"), BrowserConfiguration.ie("100%"), BrowserConfiguration.chrome("100%") }), 
    
    BORDER_IMAGE_SLICE_(
      "border-image-slice", "border-image-slice", new BrowserConfiguration[] { BrowserConfiguration.ff("100% 100% 100% 100%") }), 
    
    BORDER_IMAGE_SOURCE(
      "borderImageSource", "border-image-source", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    BORDER_IMAGE_SOURCE_(
      "border-image-source", "border-image-source", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    BORDER_IMAGE_WIDTH(
      "borderImageWidth", "border-image-width", new BrowserConfiguration[] { BrowserConfiguration.ff("1 1 1 1"), BrowserConfiguration.ie("1"), BrowserConfiguration.chrome("1") }), 
    
    BORDER_IMAGE_WIDTH_(
      "border-image-width", "border-image-width", new BrowserConfiguration[] { BrowserConfiguration.ff("1 1 1 1") }), 
    
    BORDER_INLINE_END(
      "borderInlineEnd", "border-inline-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_END_(
      "border-inline-end", "border-inline-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_END_COLOR(
      "borderInlineEndColor", "border-inline-end-color", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_END_COLOR_(
      "border-inline-end-color", "border-inline-end-color", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_END_STYLE(
      "borderInlineEndStyle", "border-inline-end-style", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_END_STYLE_(
      "border-inline-end-style", "border-inline-end-style", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_END_WIDTH(
      "borderInlineEndWidth", "border-inline-end-width", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_END_WIDTH_(
      "border-inline-end-width", "border-inline-end-width", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_START(
      "borderInlineStart", "border-inline-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_START_(
      "border-inline-start", "border-inline-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_START_COLOR(
      "borderInlineStartColor", "border-inline-start-color", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_START_COLOR_(
      "border-inline-start-color", "border-inline-start-color", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_START_STYLE(
      "borderInlineStartStyle", "border-inline-start-style", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_START_STYLE_(
      "border-inline-start-style", "border-inline-start-style", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_START_WIDTH(
      "borderInlineStartWidth", "border-inline-start-width", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_INLINE_START_WIDTH_(
      "border-inline-start-width", "border-inline-start-width", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_LEFT(
      "borderLeft", "border-left", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px none rgb(0, 0, 0)"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_LEFT_(
      "border-left", "border-left", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_LEFT_COLOR(
      "borderLeftColor", "border-left-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)"), BrowserConfiguration.ff("rgb(0, 0, 0)"), 
      BrowserConfiguration.ie("rgb(0, 0, 0)") }), 
    
    BORDER_LEFT_COLOR_(
      "border-left-color", "border-left-color", new BrowserConfiguration[] { BrowserConfiguration.ff("rgb(0, 0, 0)") }), 
    
    BORDER_LEFT_STYLE(
      "borderLeftStyle", "border-left-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    BORDER_LEFT_STYLE_(
      "border-left-style", "border-left-style", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    BORDER_LEFT_WIDTH(
      "borderLeftWidth", "border-left-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_LEFT_WIDTH_(
      "border-left-width", "border-left-width", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    BORDER_RADIUS(
      "borderRadius", "border-radius", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.ie(""), BrowserConfiguration.chrome("0px") }), 
    
    BORDER_RADIUS_(
      "border-radius", "border-radius", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_RIGHT(
      "borderRight", "border-right", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px none rgb(0, 0, 0)"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_RIGHT_(
      "border-right", "border-right", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_RIGHT_COLOR(
      "borderRightColor", "border-right-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_RIGHT_COLOR_(
      "border-right-color", "border-right-color", new BrowserConfiguration[] { BrowserConfiguration.ff("rgb(0, 0, 0)") }), 
    
    BORDER_RIGHT_STYLE(
      "borderRightStyle", "border-right-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_RIGHT_STYLE_(
      "border-right-style", "border-right-style", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    BORDER_RIGHT_WIDTH(
      "borderRightWidth", "border-right-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_RIGHT_WIDTH_(
      "border-right-width", "border-right-width", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    BORDER_SPACING(
      "borderSpacing", "border-spacing", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px 0px"), BrowserConfiguration.ff("0px 0px"), BrowserConfiguration.ie("0px 0px") }), 
    
    BORDER_SPACING_(
      "border-spacing", "border-spacing", new BrowserConfiguration[] { BrowserConfiguration.ff("0px 0px") }), 
    
    BORDER_STYLE(
      "borderStyle", "border-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_STYLE_(
      "border-style", "border-style", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_TOP(
      "borderTop", "border-top", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px none rgb(0, 0, 0)"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_TOP_(
      "border-top", "border-top", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BORDER_TOP_COLOR(
      "borderTopColor", "border-top-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_TOP_COLOR_(
      "border-top-color", "border-top-color", new BrowserConfiguration[] { BrowserConfiguration.ff("rgb(0, 0, 0)") }), 
    
    BORDER_TOP_LEFT_RADIUS(
      "borderTopLeftRadius", "border-top-left-radius", new BrowserConfiguration[] {
      BrowserConfiguration.ff("0px"), BrowserConfiguration.ie("0px"), BrowserConfiguration.chrome("0px") }), 
    
    BORDER_TOP_LEFT_RADIUS_(
      "border-top-left-radius", "border-top-left-radius", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    BORDER_TOP_RIGHT_RADIUS(
      "borderTopRightRadius", "border-top-right-radius", new BrowserConfiguration[] {
      BrowserConfiguration.ff("0px"), BrowserConfiguration.ie("0px"), BrowserConfiguration.chrome("0px") }), 
    
    BORDER_TOP_RIGHT_RADIUS_(
      "border-top-right-radius", "border-top-right-radius", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    BORDER_TOP_STYLE(
      "borderTopStyle", "border-top-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_TOP_STYLE_(
      "border-top-style", "border-top-style", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    BORDER_TOP_WIDTH(
      "borderTopWidth", "border-top-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_TOP_WIDTH_(
      "border-top-width", "border-top-width", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    BORDER_WIDTH(
      "borderWidth", "border-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BORDER_WIDTH_(
      "border-width", "border-width", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    BOTTOM(
      "bottom", "bottom", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    BOX_DECORATION_BREAK(
      "boxDecorationBreak", "box-decoration-break", new BrowserConfiguration[] { BrowserConfiguration.ff("slice") }), 
    
    BOX_DECORATION_BREAK_(
      "box-decoration-break", "box-decoration-break", new BrowserConfiguration[] { BrowserConfiguration.ff("slice") }), 
    
    BOX_SHADOW(
      "boxShadow", "box-shadow", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    BOX_SHADOW_(
      "box-shadow", "box-shadow", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    BOX_SIZING(
      "boxSizing", "box-sizing", new BrowserConfiguration[] { BrowserConfiguration.ff("content-box"), BrowserConfiguration.ie("content-box"), BrowserConfiguration.chrome("content-box") }), 
    
    BOX_SIZING_(
      "box-sizing", "box-sizing", new BrowserConfiguration[] { BrowserConfiguration.ff("content-box") }), 
    
    BREAK_AFTER(
      "breakAfter", "break-after", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ie("auto") }), 
    
    BREAK_BEFORE(
      "breakBefore", "break-before", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ie("auto") }), 
    
    BREAK_INSIDE(
      "breakInside", "break-inside", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ie("auto") }), 
    
    BUFFERED_RENDERING(
      "bufferedRendering", "buffered-rendering", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    CAPTION_SIDE(
      "captionSide", "caption-side", new BrowserConfiguration[] { BrowserConfiguration.chrome("top"), BrowserConfiguration.ff("top"), BrowserConfiguration.ie("top") }), 
    
    CAPTION_SIDE_(
      "caption-side", "caption-side", new BrowserConfiguration[] { BrowserConfiguration.ff("top") }), 
    
    CARET_COLOR(
      "caretColor", "caret-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)") }), 
    
    CLEAR(
      "clear", "clear", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    CLIP(
      "clip", "clip", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ff("auto"), BrowserConfiguration.ie("auto") }), 
    
    CLIP_PATH(
      "clipPath", "clip-path", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    CLIP_PATH_(
      "clip-path", "clip-path", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    CLIP_RULE(
      "clipRule", "clip-rule", new BrowserConfiguration[] { BrowserConfiguration.ff("nonzero"), BrowserConfiguration.ie("nonzero"), BrowserConfiguration.chrome("nonzero") }), 
    
    CLIP_RULE_(
      "clip-rule", "clip-rule", new BrowserConfiguration[] { BrowserConfiguration.ff("nonzero") }), 
    
    COLOR(
      "color", "color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    COLOR_INTERPOLATION(
      "colorInterpolation", "color-interpolation", new BrowserConfiguration[] { BrowserConfiguration.ff("srgb"), BrowserConfiguration.chrome("sRGB") }), 
    
    COLOR_INTERPOLATION_(
      "color-interpolation", "color-interpolation", new BrowserConfiguration[] { BrowserConfiguration.ff("srgb") }), 
    
    COLOR_INTERPOLATION_FILTERS(
      "colorInterpolationFilters", 
      "color-interpolation-filters", new BrowserConfiguration[] { BrowserConfiguration.ff("linearrgb"), BrowserConfiguration.ie(""), BrowserConfiguration.chrome("linearRGB") }), 
    
    COLOR_INTERPOLATION_FILTERS_(
      "color-interpolation-filters", "color-interpolation-filters", new BrowserConfiguration[] { BrowserConfiguration.ff("linearrgb") }), 
    
    COLOR_RENDERING(
      "colorRendering", "color-rendering", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    COLUMN_COUNT(
      "columnCount", "column-count", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ie("auto") }), 
    
    COLUMN_FILL(
      "columnFill", "column-fill", new BrowserConfiguration[] { BrowserConfiguration.chrome("balance"), BrowserConfiguration.ie("balance") }), 
    
    COLUMN_GAP(
      "columnGap", "column-gap", new BrowserConfiguration[] { BrowserConfiguration.chrome("normal"), BrowserConfiguration.ie("normal") }), 
    
    COLUMN_RULE(
      "columnRule", "column-rule", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px none rgb(0, 0, 0)"), BrowserConfiguration.ie("") }), 
    
    COLUMN_RULE_COLOR(
      "columnRuleColor", "column-rule-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)"), BrowserConfiguration.ie("rgb(0, 0, 0)") }), 
    
    COLUMN_RULE_STYLE(
      "columnRuleStyle", "column-rule-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ie("none") }), 
    
    COLUMN_RULE_WIDTH(
      "columnRuleWidth", "column-rule-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ie("medium") }), 
    
    COLUMN_SPAN(
      "columnSpan", "column-span", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ie("1") }), 
    
    COLUMN_WIDTH(
      "columnWidth", "column-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ie("auto") }), 
    
    COLUMNS(
      "columns", "columns", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto auto"), BrowserConfiguration.ie("") }), 
    
    CONTAIN(
      "contain", "contain", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    CONTENT(
      "content", "content", new BrowserConfiguration[] { BrowserConfiguration.ie("normal"), BrowserConfiguration.chrome(""), BrowserConfiguration.ff("none") }), 
    
    COUNTER_INCREMENT(
      "counterIncrement", "counter-increment", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    COUNTER_INCREMENT_(
      "counter-increment", "counter-increment", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    COUNTER_RESET(
      "counterReset", "counter-reset", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    COUNTER_RESET_(
      "counter-reset", "counter-reset", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    CSS_FLOAT(
      "cssFloat", "css-float", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    CSS_TEXT(
    
      "cssText", "css-text", new BrowserConfiguration[] { BrowserConfiguration.chrome(""), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    CUE(
      "cue", "cue", new BrowserConfiguration[0]), 
    
    CUE_AFTER(
      "cueAfter", "cue-after", new BrowserConfiguration[0]), 
    
    CUE_BEFORE(
      "cueBefore", "cue-before", new BrowserConfiguration[0]), 
    
    CURSOR(
      "cursor", "cursor", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ff("auto"), BrowserConfiguration.ie("auto") }), 
    
    CX(
      "cx", "cx", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    CY(
      "cy", "cy", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    D(
      "d", "d", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    DIRECTION(
      "direction", "direction", new BrowserConfiguration[] { BrowserConfiguration.chrome("ltr"), BrowserConfiguration.ff("ltr"), BrowserConfiguration.ie("ltr") }), 
    
    DISPLAY(
      "display", "display", new BrowserConfiguration[] { BrowserConfiguration.chrome("block"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    DOMINANT_BASELINE(
      "dominantBaseline", "dominant-baseline", new BrowserConfiguration[] { BrowserConfiguration.ff("auto"), BrowserConfiguration.ie("auto"), BrowserConfiguration.chrome("auto") }), 
    
    DOMINANT_BASELINE_(
      "dominant-baseline", "dominant-baseline", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    ELEVATION(
      "elevation", "elevation", new BrowserConfiguration[0]), 
    
    EMPTY_CELLS(
      "emptyCells", "empty-cells", new BrowserConfiguration[] { BrowserConfiguration.ie("show"), BrowserConfiguration.ff("show"), 
      BrowserConfiguration.chrome("show") }), 
    
    EMPTY_CELLS_(
      "empty-cells", "empty-cells", new BrowserConfiguration[] { BrowserConfiguration.ff("show") }), 
    
    ENABLE_BACKGROUND(
      "enableBackground", "enable-background", new BrowserConfiguration[] { BrowserConfiguration.ie("accumulate") }), 
    
    FILL(
      "fill", "fill", new BrowserConfiguration[] { BrowserConfiguration.ff("rgb(0, 0, 0)"), BrowserConfiguration.ie("black"), BrowserConfiguration.chrome("rgb(0, 0, 0)") }), 
    
    FILL_OPACITY(
      "fillOpacity", "fill-opacity", new BrowserConfiguration[] { BrowserConfiguration.ff("1"), BrowserConfiguration.ie("1"), BrowserConfiguration.chrome("1") }), 
    
    FILL_OPACITY_(
      "fill-opacity", "fill-opacity", new BrowserConfiguration[] { BrowserConfiguration.ff("1") }), 
    
    FILL_RULE(
      "fillRule", "fill-rule", new BrowserConfiguration[] { BrowserConfiguration.ff("nonzero"), BrowserConfiguration.ie("nonzero"), BrowserConfiguration.chrome("nonzero") }), 
    
    FILL_RULE_(
      "fill-rule", "fill-rule", new BrowserConfiguration[] { BrowserConfiguration.ff("nonzero") }), 
    
    FILTER(
      "filter", "filter", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    FLEX(
      "flex", "flex", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.ie("0 1 auto"), BrowserConfiguration.chrome("0 1 auto") }), 
    
    FLEX_BASIS(
      "flexBasis", "flex-basis", new BrowserConfiguration[] { BrowserConfiguration.ff("auto"), BrowserConfiguration.ie("auto"), BrowserConfiguration.chrome("auto") }), 
    
    FLEX_BASIS_(
      "flex-basis", "flex-basis", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    FLEX_DIRECTION(
      "flexDirection", "flex-direction", new BrowserConfiguration[] { BrowserConfiguration.ff("row"), BrowserConfiguration.ie("row"), BrowserConfiguration.chrome("row") }), 
    
    FLEX_DIRECTION_(
      "flex-direction", "flex-direction", new BrowserConfiguration[] { BrowserConfiguration.ff("row") }), 
    
    FLEX_FLOW(
      "flexFlow", "flex-flow", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.ie("row nowrap"), BrowserConfiguration.chrome("row nowrap") }), 
    
    FLEX_FLOW_(
      "flex-flow", "flex-flow", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    FLEX_GROW(
      "flexGrow", "flex-grow", new BrowserConfiguration[] { BrowserConfiguration.ff("0"), BrowserConfiguration.ie("0"), BrowserConfiguration.chrome("0") }), 
    
    FLEX_GROW_(
      "flex-grow", "flex-grow", new BrowserConfiguration[] { BrowserConfiguration.ff("0") }), 
    
    FLEX_SHRINK(
      "flexShrink", "flex-shrink", new BrowserConfiguration[] { BrowserConfiguration.ff("1"), BrowserConfiguration.ie("1"), BrowserConfiguration.chrome("1") }), 
    
    FLEX_SHRINK_(
      "flex-shrink", "flex-shrink", new BrowserConfiguration[] { BrowserConfiguration.ff("1") }), 
    
    FLEX_WRAP(
      "flexWrap", "flex-wrap", new BrowserConfiguration[] { BrowserConfiguration.ff("nowrap"), BrowserConfiguration.ie("nowrap"), BrowserConfiguration.chrome("nowrap") }), 
    
    FLEX_WRAP_(
      "flex-wrap", "flex-wrap", new BrowserConfiguration[] { BrowserConfiguration.ff("nowrap") }), 
    
    FLOAT(
      "float", "float", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.chrome("none") }), 
    
    FLOOD_COLOR(
      "floodColor", "flood-color", new BrowserConfiguration[] { BrowserConfiguration.ff("rgb(0, 0, 0)"), BrowserConfiguration.ie(""), BrowserConfiguration.chrome("rgb(0, 0, 0)") }), 
    
    FLOOD_COLOR_(
      "flood-color", "flood-color", new BrowserConfiguration[] { BrowserConfiguration.ff("rgb(0, 0, 0)") }), 
    
    FLOOD_OPACITY(
      "floodOpacity", "flood-opacity", new BrowserConfiguration[] { BrowserConfiguration.ff("1"), BrowserConfiguration.ie("1"), BrowserConfiguration.chrome("1") }), 
    
    FLOOD_OPACITY_(
      "flood-opacity", "flood-opacity", new BrowserConfiguration[] { BrowserConfiguration.ff("1") }), 
    
    FONT(
      "font", "font", new BrowserConfiguration[] { BrowserConfiguration.chrome("normal normal normal normal 16px / normal \"Times New Roman\""), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    FONT_FAMILY(
      "fontFamily", "font-family", new BrowserConfiguration[] { BrowserConfiguration.chrome("\"Times New Roman\""), BrowserConfiguration.ie("Times New Roman"), BrowserConfiguration.ff("serif") }), 
    
    FONT_FAMILY_(
      "font-family", "font-family", new BrowserConfiguration[] { BrowserConfiguration.ff("serif") }), 
    
    FONT_FEATURE_SETTINGS(
      "fontFeatureSettings", "font-feature-settings", new BrowserConfiguration[] {
      BrowserConfiguration.ie("normal"), BrowserConfiguration.ff("normal"), BrowserConfiguration.chrome("normal") }), 
    
    FONT_FEATURE_SETTINGS_(
      "font-feature-settings", "font-feature-settings", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_KERNING(
      "fontKerning", "font-kerning", new BrowserConfiguration[] { BrowserConfiguration.ff("auto"), BrowserConfiguration.chrome("auto") }), 
    
    FONT_KERNING_(
      "font-kerning", "font-kerning", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    FONT_LANGUAGE_OVERRIDE(
      "fontLanguageOverride", "font-language-override", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_LANGUAGE_OVERRIDE_(
      "font-language-override", "font-language-override", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_SIZE(
      "fontSize", "font-size", new BrowserConfiguration[] { BrowserConfiguration.chrome("16px"), BrowserConfiguration.ff("16px"), BrowserConfiguration.ie("16px") }), 
    
    FONT_SIZE_(
      "font-size", "font-size", new BrowserConfiguration[] { BrowserConfiguration.ff("16px") }), 
    
    FONT_SIZE_ADJUST(
      "fontSizeAdjust", "font-size-adjust", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    FONT_SIZE_ADJUST_(
      "font-size-adjust", "font-size-adjust", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    FONT_STRETCH(
      "fontStretch", "font-stretch", new BrowserConfiguration[] { BrowserConfiguration.chrome("normal"), BrowserConfiguration.ff("normal"), BrowserConfiguration.ie("normal") }), 
    
    FONT_STRETCH_(
      "font-stretch", "font-stretch", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_STYLE(
      "fontStyle", "font-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("normal"), BrowserConfiguration.ff("normal"), BrowserConfiguration.ie("normal") }), 
    
    FONT_STYLE_(
      "font-style", "font-style", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_SYNTHESIS(
      "fontSynthesis", "font-synthesis", new BrowserConfiguration[] { BrowserConfiguration.ff("weight style") }), 
    
    FONT_SYNTHESIS_(
      "font-synthesis", "font-synthesis", new BrowserConfiguration[] { BrowserConfiguration.ff("weight style") }), 
    
    FONT_VARIANT(
      "fontVariant", "font-variant", new BrowserConfiguration[] { BrowserConfiguration.chrome("normal"), BrowserConfiguration.ff("normal"), BrowserConfiguration.ie("normal") }), 
    
    FONT_VARIANT_(
      "font-variant", "font-variant", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_VARIANT_ALTERNATES(
      "fontVariantAlternates", "font-variant-alternates", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_VARIANT_ALTERNATES_(
      "font-variant-alternates", "font-variant-alternates", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_VARIANT_CAPS(
      "fontVariantCaps", "font-variant-caps", new BrowserConfiguration[] { BrowserConfiguration.ff("normal"), BrowserConfiguration.chrome("normal") }), 
    
    FONT_VARIANT_CAPS_(
      "font-variant-caps", "font-variant-caps", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_VARIANT_EAST_ASIAN(
      "fontVariantEastAsian", "font-variant-east-asian", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_VARIANT_EAST_ASIAN_(
      "font-variant-east-asian", "font-variant-east-asian", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_VARIANT_LIGATURES(
      "fontVariantLigatures", "font-variant-ligatures", new BrowserConfiguration[] { BrowserConfiguration.ff("normal"), BrowserConfiguration.chrome("normal") }), 
    
    FONT_VARIANT_LIGATURES_(
      "font-variant-ligatures", "font-variant-ligatures", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_VARIANT_NUMERIC(
      "fontVariantNumeric", "font-variant-numeric", new BrowserConfiguration[] { BrowserConfiguration.ff("normal"), BrowserConfiguration.chrome("normal") }), 
    
    FONT_VARIANT_NUMERIC_(
      "font-variant-numeric", "font-variant-numeric", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_VARIANT_POSITION(
      "fontVariantPosition", "font-variant-position", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_VARIANT_POSITION_(
      "font-variant-position", "font-variant-position", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    FONT_WEIGHT(
      "fontWeight", "font-weight", new BrowserConfiguration[] { BrowserConfiguration.chrome("normal"), BrowserConfiguration.ff("400"), BrowserConfiguration.ie("400") }), 
    
    FONT_WEIGHT_(
      "font-weight", "font-weight", new BrowserConfiguration[] { BrowserConfiguration.ff("400") }), 
    
    GLYPH_ORIENTATION_HORIZONTAL(
      "glyphOrientationHorizontal", "glyph-orientation-horizontal", new BrowserConfiguration[] {
      BrowserConfiguration.ie("0deg") }), 
    
    GLYPH_ORIENTATION_VERTICAL(
      "glyphOrientationVertical", "glyph-orientation-vertical", new BrowserConfiguration[] {
      BrowserConfiguration.ie("auto") }), 
    
    GRID(
      "grid", "grid", new BrowserConfiguration[] { BrowserConfiguration.chrome("none / none / none / row / auto / auto / 0px / 0px") }), 
    
    GRID_AREA(
      "gridArea", "grid-area", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto / auto / auto / auto") }), 
    
    GRID_AUTO_COLUMNS(
      "gridAutoColumns", "grid-auto-columns", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    GRID_AUTO_FLOW(
      "gridAutoFlow", "grid-auto-flow", new BrowserConfiguration[] { BrowserConfiguration.chrome("row") }), 
    
    GRID_AUTO_ROWS(
      "gridAutoRows", "grid-auto-rows", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    GRID_COLUMN(
      "gridColumn", "grid-column", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto / auto") }), 
    
    GRID_COLUMN_END(
      "gridColumnEnd", "grid-column-end", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    GRID_COLUMN_GAP(
      "gridColumnGap", "grid-column-gap", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    GRID_COLUMN_START(
      "gridColumnStart", "grid-column-start", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    GRID_GAP(
      "gridGap", "grid-gap", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px 0px") }), 
    
    GRID_ROW(
      "gridRow", "grid-row", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto / auto") }), 
    
    GRID_ROW_END(
      "gridRowEnd", "grid-row-end", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    GRID_ROW_GAP(
      "gridRowGap", "grid-row-gap", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    GRID_ROW_START(
      "gridRowStart", "grid-row-start", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    GRID_TEMPLATE(
      "gridTemplate", "grid-template", new BrowserConfiguration[] { BrowserConfiguration.chrome("none / none / none") }), 
    
    GRID_TEMPLATE_AREAS(
      "gridTemplateAreas", "grid-template-areas", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    GRID_TEMPLATE_COLUMNS(
      "gridTemplateColumns", "grid-template-columns", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    GRID_TEMPLATE_ROWS(
      "gridTemplateRows", "grid-template-rows", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    HEIGHT(
      "height", "height", new BrowserConfiguration[] { BrowserConfiguration.chrome(""), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    HYPHENS(
      "hyphens", "hyphens", new BrowserConfiguration[] { BrowserConfiguration.ff("manual"), BrowserConfiguration.chrome("manual") }), 
    
    IMAGE_ORIENTATION(
      "imageOrientation", "image-orientation", new BrowserConfiguration[] { BrowserConfiguration.ff("0deg") }), 
    
    IMAGE_ORIENTATION_(
      "image-orientation", "image-orientation", new BrowserConfiguration[] { BrowserConfiguration.ff("0deg") }), 
    
    IMAGE_RENDERING(
      "imageRendering", "image-rendering", new BrowserConfiguration[] { BrowserConfiguration.ff("auto"), BrowserConfiguration.chrome("auto") }), 
    
    IMAGE_RENDERING_(
      "image-rendering", "image-rendering", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    IME_MODE(
      "imeMode", "ime-mode", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined"), BrowserConfiguration.ff("auto") }), 
    
    IME_MODE_(
      "ime-mode", "ime-mode", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    INLINE_SIZE(
      "inlineSize", "inline-size", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.chrome("913px") }), 
    
    INLINE_SIZE_(
      "inline-size", "inline-size", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    ISOLATION(
      "isolation", "isolation", new BrowserConfiguration[] { BrowserConfiguration.ff("auto"), BrowserConfiguration.chrome("auto") }), 
    
    JUSTIFY_CONTENT(
      "justifyContent", "justify-content", new BrowserConfiguration[] {
      BrowserConfiguration.ff("auto"), 
      BrowserConfiguration.ie("flex-start"), BrowserConfiguration.chrome("normal") }), 
    
    JUSTIFY_CONTENT_(
      "justify-content", "justify-content", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    JUSTIFY_ITEMS(
      "justifyItems", "justify-items", new BrowserConfiguration[] { BrowserConfiguration.ff("start"), BrowserConfiguration.chrome("normal") }), 
    
    JUSTIFY_ITEMS_(
      "justify-items", "justify-items", new BrowserConfiguration[] { BrowserConfiguration.ff("start") }), 
    
    JUSTIFY_SELF(
      "justifySelf", "justify-self", new BrowserConfiguration[] { BrowserConfiguration.ff("start"), BrowserConfiguration.chrome("normal") }), 
    
    JUSTIFY_SELF_(
      "justify-self", "justify-self", new BrowserConfiguration[] { BrowserConfiguration.ff("start") }), 
    
    KERNING(
      "kerning", "kerning", new BrowserConfiguration[] { BrowserConfiguration.ie("auto") }), 
    
    LAYOUT_FLOW(
      "layoutFlow", "layout-flow", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    LAYOUT_GRID(
      "layoutGrid", "layout-grid", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    LAYOUT_GRID_CHAR(
      "layoutGridChar", "layout-grid-char", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    LAYOUT_GRID_LINE(
      "layoutGridLine", "layout-grid-line", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    LAYOUT_GRID_MODE(
      "layoutGridMode", "layout-grid-mode", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    LAYOUT_GRID_TYPE(
      "layoutGridType", "layout-grid-type", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    LEFT(
      "left", "left", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    LETTER_SPACING(
      "letterSpacing", "letter-spacing", new BrowserConfiguration[] { BrowserConfiguration.chrome("normal"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    LETTER_SPACING_(
      "letter-spacing", "letter-spacing", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    LIGHTING_COLOR(
      "lightingColor", "lighting-color", new BrowserConfiguration[] {
      BrowserConfiguration.ff("rgb(255, 255, 255)"), BrowserConfiguration.ie(""), BrowserConfiguration.chrome("rgb(255, 255, 255)") }), 
    
    LIGHTING_COLOR_(
      "lighting-color", "lighting-color", new BrowserConfiguration[] { BrowserConfiguration.ff("rgb(255, 255, 255)") }), 
    
    LINE_BREAK(
      "lineBreak", "line-break", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    LINE_HEIGHT(
      "lineHeight", "line-height", new BrowserConfiguration[] { BrowserConfiguration.ff("20px"), BrowserConfiguration.ie("normal"), BrowserConfiguration.chrome("normal") }), 
    
    LINE_HEIGHT_(
      "line-height", "line-height", new BrowserConfiguration[] { BrowserConfiguration.ff("20px") }), 
    
    LIST_STYLE(
      "listStyle", "list-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("disc outside none"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    LIST_STYLE_(
      "list-style", "list-style", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    LIST_STYLE_IMAGE(
      "listStyleImage", "list-style-image", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    LIST_STYLE_IMAGE_(
      "list-style-image", "list-style-image", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    LIST_STYLE_POSITION(
      "listStylePosition", "list-style-position", new BrowserConfiguration[] {
      BrowserConfiguration.chrome("outside"), BrowserConfiguration.ff("outside"), BrowserConfiguration.ie("outside") }), 
    
    LIST_STYLE_POSITION_(
      "list-style-position", "list-style-position", new BrowserConfiguration[] { BrowserConfiguration.ff("outside") }), 
    
    LIST_STYLE_TYPE(
      "listStyleType", "list-style-type", new BrowserConfiguration[] { BrowserConfiguration.chrome("disc"), BrowserConfiguration.ff("disc"), BrowserConfiguration.ie("disc") }), 
    
    LIST_STYLE_TYPE_(
      "list-style-type", "list-style-type", new BrowserConfiguration[] { BrowserConfiguration.ff("disc") }), 
    
    MARGIN(
      "margin", "margin", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    MARGIN_BLOCK_END(
      "marginBlockEnd", "margin-block-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MARGIN_BLOCK_END_(
      "margin-block-end", "margin-block-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MARGIN_BLOCK_START(
      "marginBlockStart", "margin-block-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MARGIN_BLOCK_START_(
      "margin-block-start", "margin-block-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MARGIN_BOTTOM(
      "marginBottom", "margin-bottom", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    MARGIN_BOTTOM_(
      "margin-bottom", "margin-bottom", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    MARGIN_INLINE_END(
      "marginInlineEnd", "margin-inline-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MARGIN_INLINE_END_(
      "margin-inline-end", "margin-inline-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MARGIN_INLINE_START(
      "marginInlineStart", "margin-inline-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MARGIN_INLINE_START_(
      "margin-inline-start", "margin-inline-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MARGIN_LEFT(
      "marginLeft", "margin-left", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    MARGIN_LEFT_(
      "margin-left", "margin-left", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    MARGIN_RIGHT(
      "marginRight", "margin-right", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    MARGIN_RIGHT_(
      "margin-right", "margin-right", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    MARGIN_TOP(
      "marginTop", "margin-top", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    MARGIN_TOP_(
      "margin-top", "margin-top", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    MARKER(
      "marker", "marker", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("") }), 
    
    MARKER_END(
      "markerEnd", "marker-end", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    MARKER_END_(
      "marker-end", "marker-end", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MARKER_MID(
      "markerMid", "marker-mid", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    MARKER_MID_(
      "marker-mid", "marker-mid", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MARKER_OFFSET(
      "markerOffset", "marker-offset", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    MARKER_OFFSET_(
      "marker-offset", "marker-offset", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    MARKER_START(
      "markerStart", "marker-start", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    MARKER_START_(
      "marker-start", "marker-start", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MASK(
      "mask", "mask", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    MASK_TYPE(
      "maskType", "mask-type", new BrowserConfiguration[] { BrowserConfiguration.ff("luminance"), BrowserConfiguration.chrome("luminance") }), 
    
    MASK_TYPE_(
      "mask-type", "mask-type", new BrowserConfiguration[] { BrowserConfiguration.ff("luminance") }), 
    
    MAX_BLOCK_SIZE(
      "maxBlockSize", "max-block-size", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.chrome("none") }), 
    
    MAX_BLOCK_SIZE_(
      "max-block-size", "max-block-size", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MAX_HEIGHT(
      "maxHeight", "max-height", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    MAX_HEIGHT_(
      "max-height", "max-height", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MAX_INLINE_SIZE(
      "maxInlineSize", "max-inline-size", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.chrome("none") }), 
    
    MAX_INLINE_SIZE_(
      "max-inline-size", "max-inline-size", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MAX_WIDTH(
      "maxWidth", "max-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    MAX_WIDTH_(
      "max-width", "max-width", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MAX_ZOOM(
      "maxZoom", "max-zoom", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    MIN_BLOCK_SIZE(
      "minBlockSize", "min-block-size", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.chrome("0px") }), 
    
    MIN_BLOCK_SIZE_(
      "min-block-size", "min-block-size", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MIN_HEIGHT(
      "minHeight", "min-height", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    MIN_HEIGHT_(
      "min-height", "min-height", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    MIN_INLINE_SIZE(
      "minInlineSize", "min-inline-size", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.chrome("0px") }), 
    
    MIN_INLINE_SIZE_(
      "min-inline-size", "min-inline-size", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MIN_WIDTH(
      "minWidth", "min-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    MIN_WIDTH_(
      "min-width", "min-width", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    MIN_ZOOM(
      "minZoom", "min-zoom", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    MIX_BLEND_MODE(
      "mixBlendMode", "mix-blend-mode", new BrowserConfiguration[] { BrowserConfiguration.ff("normal"), BrowserConfiguration.chrome("normal") }), 
    
    MIX_BLEND_MODE_(
      "mix-blend-mode", "mix-blend-mode", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    MOTION(
      "motion", "motion", new BrowserConfiguration[] { BrowserConfiguration.chrome("none 0px auto 0deg") }), 
    
    MOZ_ANIMATION(
      "MozAnimation", "-moz-animation", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_ANIMATION_DELAY(
      "MozAnimationDelay", "-moz-animation-delay", new BrowserConfiguration[] {
      BrowserConfiguration.ff("0s") }), 
    
    MOZ_ANIMATION_DIRECTION(
      "MozAnimationDirection", "-moz-animation-direction", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    MOZ_ANIMATION_DURATION(
      "MozAnimationDuration", 
      "-moz-animation-duration", new BrowserConfiguration[] { BrowserConfiguration.ff("0s") }), 
    
    MOZ_ANIMATION_FILL_MODE(
      "MozAnimationFillMode", 
      "moz-animation-fill-mode", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MOZ_ANIMATION_ITERATION_COUNT(
      "MozAnimationIterationCount", 
      "-moz-animation-iteration-count", new BrowserConfiguration[] { BrowserConfiguration.ff("1") }), 
    
    MOZ_ANIMATION_NAME(
      "MozAnimationName", "moz-annimation-name", new BrowserConfiguration[] {
      BrowserConfiguration.ff("none") }), 
    
    MOZ_ANIMATION_PLAY_STATE(
      "MozAnimationPlayState", 
      "moz-annimation-play-state", new BrowserConfiguration[] { BrowserConfiguration.ff("running") }), 
    
    MOZ_ANIMATION_TIMING_FUNCTION(
      "MozAnimationTimingFunction", 
      "moz-annimation-timing-function", new BrowserConfiguration[] {
      BrowserConfiguration.ff("ease") }), 
    
    MOZ_APPEARANCE(
      "MozAppearance", "-moz-appearance", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MOZ_BACKFACE_VISIBILITY(
      "MozBackfaceVisibility", 
      "-moz-backface-visibility", new BrowserConfiguration[] { BrowserConfiguration.ff("visible") }), 
    
    MOZ_BACKGROUND_CLIP(
      "MozBackgroundClip", "-moz-background-clip", new BrowserConfiguration[0]), 
    
    MOZ_BACKGROUND_ORIGIN(
      "MozBackgroundOrigin", "-moz-background-origin", new BrowserConfiguration[0]), 
    
    MOZ_BACKGROUND_SIZE(
      "MozBackgroundSize", "-moz-background-size", new BrowserConfiguration[0]), 
    
    MOZ_BINDING(
      "MozBinding", "-moz-binding", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MOZ_BORDER_BOTTOM_COLORS(
      "MozBorderBottomColors", 
      "-moz-border-bottom-colors", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MOZ_BORDER_END(
      "MozBorderEnd", "-moz-border-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_BORDER_END_COLOR(
      "MozBorderEndColor", "-moz-border-end-color", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_BORDER_END_STYLE(
      "MozBorderEndStyle", "-moz-border-end-style", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_BORDER_END_WIDTH(
      "MozBorderEndWidth", "-moz-border-end-width", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_BORDER_IMAGE(
      "MozBorderImage", "-moz-border-image", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_BORDER_LEFT_COLORS(
      "MozBorderLeftColors", "-moz-border-left-colors", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MOZ_BORDER_RADIUS(
      "MozBorderRadius", "-moz-border-radius", new BrowserConfiguration[0]), 
    
    MOZ_BORDER_RADIUS_BOTTOMLEFT(
      "MozBorderRadiusBottomleft", "-moz-border-radius-bottomleft", new BrowserConfiguration[0]), 
    
    MOZ_BORDER_RADIUS_BOTTOMRIGHT(
      "MozBorderRadiusBottomright", "-moz-border-radius-bottomright", new BrowserConfiguration[0]), 
    
    MOZ_BORDER_RADIUS_TOPLEFT(
      "MozBorderRadiusTopleft", "-moz-border-radius-topleft", new BrowserConfiguration[0]), 
    
    MOZ_BORDER_RADIUS_TOPRIGHT(
      "MozBorderRadiusTopright", "-moz-border-radius-topright", new BrowserConfiguration[0]), 
    
    MOZ_BORDER_RIGHT_COLORS(
      "MozBorderRightColors", "-moz-border-right-colors", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MOZ_BORDER_START(
      "MozBorderStart", "-moz-border-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_BORDER_START_COLOR(
      "MozBorderStartColor", "-moz-border-start-color", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_BORDER_START_STYLE(
      "MozBorderStartStyle", "-moz-border-start-style", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_BORDER_START_WIDTH(
      "MozBorderStartWidth", "-moz-border-start-width", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_BORDER_TOP_COLORS(
      "MozBorderTopColors", "-moz-border-top-colors", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MOZ_BOX_ALIGN(
      "MozBoxAlign", "-moz-box-align", new BrowserConfiguration[] { BrowserConfiguration.ff("stretch") }), 
    
    MOZ_BOX_DIRECTION(
      "MozBoxDirection", "-moz-box-direction", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    MOZ_BOX_FLEX(
      "MozBoxFlex", "-moz-box-flex", new BrowserConfiguration[] { BrowserConfiguration.ff("0") }), 
    
    MOZ_BOX_ORDINAL_GROUP(
      "MozBoxOrdinalGroup", "-moz-box-ordinal-group", new BrowserConfiguration[] { BrowserConfiguration.ff("1") }), 
    
    MOZ_BOX_ORIENT(
      "MozBoxOrient", "-moz-box-orient", new BrowserConfiguration[] { BrowserConfiguration.ff("horizontal") }), 
    
    MOZ_BOX_PACK(
      "MozBoxPack", "-moz-box-pack", new BrowserConfiguration[] { BrowserConfiguration.ff("start") }), 
    
    MOZ_BOX_SHADOW(
      "MozBoxShadow", "-moz-box-shadow", new BrowserConfiguration[0]), 
    
    MOZ_BOX_SIZING(
      "MozBoxSizing", "-moz-box-sizing", new BrowserConfiguration[] { BrowserConfiguration.ff("content-box") }), 
    
    MOZ_COLUMN_COUNT(
      "MozColumnCount", "-moz-column-count", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    MOZ_COLUMN_FILL(
      "MozColumnFill", "-moz-column-fill", new BrowserConfiguration[] { BrowserConfiguration.ff("balance") }), 
    
    MOZ_COLUMN_GAP(
      "MozColumnGap", "-moz-column-gap", new BrowserConfiguration[] { BrowserConfiguration.ff("16px") }), 
    
    MOZ_COLUMN_RULE(
      "MozColumnRule", "-moz-column-rule", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_COLUMN_RULE_COLOR(
      "MozColumnRuleColor", "-moz-column-rule-color", new BrowserConfiguration[] {
      BrowserConfiguration.ff("rgb(0, 0, 0)") }), 
    
    MOZ_COLUMN_RULE_STYLE(
      "MozColumnRuleStyle", "-moz-column-rule-style", new BrowserConfiguration[] {
      BrowserConfiguration.ff("none") }), 
    
    MOZ_COLUMN_RULE_WIDTH(
      "MozColumnRuleWidth", "-moz-column-rule-width", new BrowserConfiguration[] {
      BrowserConfiguration.ff("0px") }), 
    
    MOZ_COLUMN_WIDTH(
      "MozColumnWidth", "-moz-column-width", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    MOZ_COLUMNS(
      "MozColumns", "-moz-columns", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_FLOAT_EDGE(
      "MozFloatEdge", "-moz-float-edge", new BrowserConfiguration[] { BrowserConfiguration.ff("content-box") }), 
    
    MOZ_FONT_FEATURE_SETTINGS(
      "MozFontFeatureSettings", 
      "-moz-font-feature-settings", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    MOZ_FONT_LANGUAGE_OVERRIDE(
      "MozFontLanguageOverride", 
      "-moz-font-language-override", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    MOZ_FORCE_BROKEN_IMAGE_ICON(
      "MozForceBrokenImageIcon", 
      "-moz-force-broken-image-icon", new BrowserConfiguration[] { BrowserConfiguration.ff("0") }), 
    
    MOZ_HYPHENS(
      "MozHyphens", "-moz-hyphens", new BrowserConfiguration[] { BrowserConfiguration.ff("manual") }), 
    
    MOZ_IMAGE_REGION(
      "MozImageRegion", "-moz-image-region", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    MOZ_MARGIN_END(
      "MozMarginEnd", "-moz-margin-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_MARGIN_START(
      "MozMarginStart", "-moz-margin-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_OPACITY(
      "MozOpacity", "-moz-opacity", new BrowserConfiguration[0]), 
    
    MOZ_ORIENT(
      "MozOrient", "-moz-orient", new BrowserConfiguration[] { BrowserConfiguration.ff("inline") }), 
    
    MOZ_OUTLINE(
      "MozOutline", "-moz-outline", new BrowserConfiguration[0]), 
    
    MOZ_OUTLINE_COLOR(
      "MozOutlineColor", "-moz-outline-color", new BrowserConfiguration[0]), 
    
    MOZ_OUTLINE_OFFSET(
      "MozOutlineOffset", "-moz-outline-offset", new BrowserConfiguration[0]), 
    
    MOZ_OUTLINE_RADIUS(
      "MozOutlineRadius", "-mz-outline-radius", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_OUTLINE_RADIUS_BOTTOMLEFT(
      "MozOutlineRadiusBottomleft", 
      "-moz-outline-radius-bottomleft", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    MOZ_OUTLINE_RADIUS_BOTTOMRIGHT(
      "MozOutlineRadiusBottomright", 
      "-moz-outline-radius-bottomright", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    MOZ_OUTLINE_RADIUS_TOPLEFT(
      "MozOutlineRadiusTopleft", 
      "-moz-outline-radius-topleft", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    MOZ_OUTLINE_RADIUS_TOPRIGHT(
      "MozOutlineRadiusTopright", 
      "-moz-outline-radius-topright", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    MOZ_OUTLINE_STYLE(
      "MozOutlineStyle", "-moz-outline-style", new BrowserConfiguration[0]), 
    
    MOZ_OUTLINE_WIDTH(
      "MozOutlineWidth", "-moz-outline-width", new BrowserConfiguration[0]), 
    
    MOZ_PADDING_END(
      "MozPaddingEnd", "-moz-padding-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_PADDING_START(
      "MozPaddingStart", "-moz-padding-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_PERSPECTIVE(
      "MozPerspective", "-moz-perspective", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MOZ_PERSPECTIVE_ORIGIN(
      "MozPerspectiveOrigin", 
      "-moz-perspective-origin", new BrowserConfiguration[] { BrowserConfiguration.ff("621px 172.5px") }), 
    
    MOZ_STACK_SIZING(
      "MozStackSizing", "-moz-stack-sizing", new BrowserConfiguration[] {
      BrowserConfiguration.ff("stretch-to-fit") }), 
    
    MOZ_TAB_SIZE(
      "MozTabSize", "-moz-tab-size", new BrowserConfiguration[] { BrowserConfiguration.ff("8") }), 
    
    MOZ_TEXT_ALIGN_LAST(
      "MozTextAlignLast", "-moz-text-align-last", new BrowserConfiguration[] {
      BrowserConfiguration.ff("auto") }), 
    
    MOZ_TEXT_SIZE_ADJUST(
      "MozTextSizeAdjust", "-moz-text-size-adjust", new BrowserConfiguration[] {
      BrowserConfiguration.ff("auto") }), 
    
    MOZ_TRANSFORM(
      "MozTransform", "-moz-transform", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MOZ_TRANSFORM_ORIGIN(
      "MozTransformOrigin", "-moz-transform-origin", new BrowserConfiguration[] { BrowserConfiguration.ff("621px 172.5px") }), 
    
    MOZ_TRANSFORM_STYLE(
      "MozTransformStyle", "-moz-transform-style", new BrowserConfiguration[] {
      BrowserConfiguration.ff("flat") }), 
    
    MOZ_TRANSITION(
      "MozTransition", "-moz-transition", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    MOZ_TRANSITION_DELAY(
      "MozTransitionDelay", "-moz-transition-delay", new BrowserConfiguration[] {
      BrowserConfiguration.ff("0s") }), 
    
    MOZ_TRANSITION_DURATION(
      "MozTransitionDuration", 
      "-moz-transition-duration", new BrowserConfiguration[] { BrowserConfiguration.ff("0s") }), 
    
    MOZ_TRANSITION_PROPERTY(
      "MozTransitionProperty", 
      "-moz-transition-property", new BrowserConfiguration[] { BrowserConfiguration.ff("all") }), 
    
    MOZ_TRANSITION_TIMING_FUNCTION(
      "MozTransitionTimingFunction", 
      "-moz-transition-timing-function", new BrowserConfiguration[] {
      BrowserConfiguration.ff("ease") }), 
    
    MOZ_USER_FOCUS(
      "MozUserFocus", "-moz-user-focus", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    MOZ_USER_INPUT(
      "MozUserInput", "-moz-user-input", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    MOZ_USER_MODIFY(
      "MozUserModify", "-moz-user-modify", new BrowserConfiguration[] { BrowserConfiguration.ff("read-only") }), 
    
    MOZ_USER_SELECT(
      "MozUserSelect", "-moz-user-select", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    MOZ_WINDOW_DRAGGING(
      "MozWindowDragging", "-moz-window-dragging", new BrowserConfiguration[] { BrowserConfiguration.ff("no-drag") }), 
    
    MS_ANIMATION(
      "msAnimation", "-ms-animation", new BrowserConfiguration[] { BrowserConfiguration.ie("") }), 
    
    MS_ANIMATION_DELAY(
      "msAnimationDelay", "-ms-animation-delay", new BrowserConfiguration[] {
      BrowserConfiguration.ie("0s") }), 
    
    MS_ANIMATION_DIRECTION(
      "msAnimationDirection", 
      "-ms-animation-direction", new BrowserConfiguration[] { BrowserConfiguration.ie("normal") }), 
    
    MS_ANIMATION_DURATION(
      "msAnimationDuration", 
      "-ms-animation-duration", new BrowserConfiguration[] { BrowserConfiguration.ie("0s") }), 
    
    MS_ANIMATION_FILL_MODE(
      "msAnimationFillMode", 
      "-ms-animation-fill-mode", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_ANIMATION_ITERATION_COUNT(
      "msAnimationIterationCount", 
      "-ms-animation-iteration-count", new BrowserConfiguration[] { BrowserConfiguration.ie("1") }), 
    
    MS_ANIMATION_NAME(
      "msAnimationName", "-ms-annimation-name", new BrowserConfiguration[] {
      BrowserConfiguration.ie("none") }), 
    
    MS_ANIMATION_PLAY_STATE(
      "msAnimationPlayState", 
      "-ms-animation-play-state", new BrowserConfiguration[] { BrowserConfiguration.ie("running") }), 
    
    MS_ANIMATION_TIMING_FUNCTION(
      "msAnimationTimingFunction", 
      "-ms-animation-timing-function", new BrowserConfiguration[] {
      BrowserConfiguration.ie("cubic-bezier(0.25, 0.1, 0.25, 1)") }), 
    
    MS_BACKFACE_VISIBILITY(
      "msBackfaceVisibility", 
      "-ms-backface-visibility", new BrowserConfiguration[] { BrowserConfiguration.ie("visible") }), 
    
    MS_BLOCK_PROGRESSION(
      "msBlockProgression", "-ms-block-progression", new BrowserConfiguration[] {
      BrowserConfiguration.ie("undefined") }), 
    
    MS_CONTENT_ZOOM_CHAINING(
      "msContentZoomChaining", 
      "-ms-content-zoom-chaining", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_CONTENT_ZOOM_LIMIT(
      "msContentZoomLimit", "-ms-content-zoom-limit", new BrowserConfiguration[] {
      BrowserConfiguration.ie("") }), 
    
    MS_CONTENT_ZOOM_LIMIT_MAX(
      "msContentZoomLimitMax", "-ms-content-zoom-limit-max", new BrowserConfiguration[] {
      BrowserConfiguration.ie("400%") }), 
    
    MS_CONTENT_ZOOM_LIMIT_MIN(
      "msContentZoomLimitMin", "-ms-content-zoom-limit-min", new BrowserConfiguration[] {
      BrowserConfiguration.ie("100%") }), 
    
    MS_CONTENT_ZOOM_SNAP(
      "msContentZoomSnap", 
      "-ms-content-zoom-snap", new BrowserConfiguration[] { BrowserConfiguration.ie("none snapInterval(0%, 100%)") }), 
    
    MS_CONTENT_ZOOM_SNAP_POINTS(
      "msContentZoomSnapPoints", 
      "-ms-content-zoom-snap-points", new BrowserConfiguration[] { BrowserConfiguration.ie("snapInterval(0%, 100%)") }), 
    
    MS_CONTENT_ZOOM_SNAP_TYPE(
      "msContentZoomSnapType", "-ms-content-zoom-snap-type", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_CONTENT_ZOOMING(
      "msContentZooming", "-ms-content-zooming", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_FLEX(
      "msFlex", "-ms-flex", new BrowserConfiguration[] { BrowserConfiguration.ie("0 1 auto") }), 
    
    MS_FLEX_ALIGN(
      "msFlexAlign", "-ms-flex-align", new BrowserConfiguration[] { BrowserConfiguration.ie("stretch") }), 
    
    MS_FLEX_DIRECTION(
      "msFlexDirection", "-ms-flex-direction", new BrowserConfiguration[] { BrowserConfiguration.ie("row") }), 
    
    MS_FLEX_FLOW(
      "msFlexFlow", "-ms-flex-flow", new BrowserConfiguration[] { BrowserConfiguration.ie("row nowrap") }), 
    
    MS_FLEX_ITEM_ALIGN(
      "msFlexItemAlign", "-ms-flex-item-align", new BrowserConfiguration[] { BrowserConfiguration.ie("auto") }), 
    
    MS_FLEX_LINE_PACK(
      "msFlexLinePack", "-ms-flex-line-pack", new BrowserConfiguration[] { BrowserConfiguration.ie("stretch") }), 
    
    MS_FLEX_NEGATIVE(
      "msFlexNegative", "-ms-flex-negative", new BrowserConfiguration[] { BrowserConfiguration.ie("1") }), 
    
    MS_FLEX_ORDER(
      "msFlexOrder", "-ms-flex-order", new BrowserConfiguration[] { BrowserConfiguration.ie("0") }), 
    
    MS_FLEX_PACK(
      "msFlexPack", "-ms-flex-pack", new BrowserConfiguration[] { BrowserConfiguration.ie("start") }), 
    
    MS_FLEX_POSITIVE(
      "msFlexPositive", "-ms-flex-positive", new BrowserConfiguration[] { BrowserConfiguration.ie("0") }), 
    
    MS_FLEX_PREFERRED_SIZE(
      "msFlexPreferredSize", "-ms-flex-preferred-size", new BrowserConfiguration[] { BrowserConfiguration.ie("auto") }), 
    
    MS_FLEX_WRAP(
      "msFlexWrap", "-ms-flex-wrap", new BrowserConfiguration[] { BrowserConfiguration.ie("nowrap") }), 
    
    MS_FLOW_FROM(
      "msFlowFrom", "-ms-flow-from", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_FLOW_INTO(
      "msFlowInto", "-ms-flow-into", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_FONT_FEATURE_SETTINGS(
      "msFontFeatureSettings", "-ms-font-feature-settings", new BrowserConfiguration[] { BrowserConfiguration.ie("normal") }), 
    
    MS_GRID_COLUMN(
      "msGridColumn", "-ms-grid-column", new BrowserConfiguration[] { BrowserConfiguration.ie("1") }), 
    
    MS_GRID_COLUMN_ALIGN(
      "msGridColumnAlign", "-ms-grid-column-align", new BrowserConfiguration[] { BrowserConfiguration.ie("stretch") }), 
    
    MS_GRID_COLUMN_SPAN(
      "msGridColumnSpan", "-ms-grid-column-span", new BrowserConfiguration[] { BrowserConfiguration.ie("1") }), 
    
    MS_GRID_COLUMNS(
      "msGridColumns", "-ms-grid-columns", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_GRID_ROW(
      "msGridRow", "-ms-grid-row", new BrowserConfiguration[] { BrowserConfiguration.ie("1") }), 
    
    MS_GRID_ROW_ALIGN(
      "msGridRowAlign", "-ms-grid-row-align", new BrowserConfiguration[] { BrowserConfiguration.ie("stretch") }), 
    
    MS_GRID_ROW_SPAN(
      "msGridRowSpan", "-ms-grid-row-span", new BrowserConfiguration[] { BrowserConfiguration.ie("1") }), 
    
    MS_GRID_ROWS(
      "msGridRows", "-ms-grid-rows", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_HIGH_CONTRAST_ADJUST(
      "msHighContrastAdjust", "-ms-high-contrast-adjust", new BrowserConfiguration[] { BrowserConfiguration.ie("auto") }), 
    
    MS_HYPHENATE_LIMIT_CHARS(
      "msHyphenateLimitChars", "-ms-hyphenate-limit-chars", new BrowserConfiguration[] { BrowserConfiguration.ie("5 2 2") }), 
    
    MS_HYPHENATE_LIMIT_LINES(
      "msHyphenateLimitLines", "-ms-hyphenate-limit-lines", new BrowserConfiguration[] { BrowserConfiguration.ie("no-limit") }), 
    
    MS_HYPHENATE_LIMIT_ZONE(
      "msHyphenateLimitZone", "-ms-hyphenate-limit-zone", new BrowserConfiguration[] { BrowserConfiguration.ie("0px") }), 
    
    MS_HYPHENS(
      "msHyphens", "-ms-hyphens", new BrowserConfiguration[] { BrowserConfiguration.ie("manual") }), 
    
    MS_IME_ALIGN(
      "msImeAlign", "-ms-ime-align", new BrowserConfiguration[] { BrowserConfiguration.ie("") }), 
    
    MS_INTERPOLATION_MODE(
      "msInterpolationMode", "-ms-interpolation-mode", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    MS_OVERFLOW_STYLE(
      "msOverflowStyle", "-ms-overflow-style", new BrowserConfiguration[] { BrowserConfiguration.ie("scrollbar") }), 
    
    MS_PERSPECTIVE(
      "msPerspective", "-ms-perspective", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_PERSPECTIVE_ORIGIN(
      "msPerspectiveOrigin", "-ms-perspective-origin", new BrowserConfiguration[] { BrowserConfiguration.ie("620px 163.2px") }), 
    
    MS_SCROLL_CHAINING(
      "msScrollChaining", "-ms-scroll-chaining", new BrowserConfiguration[] { BrowserConfiguration.ie("chained") }), 
    
    MS_SCROLL_LIMIT(
      "msScrollLimit", "-ms-scroll-limit", new BrowserConfiguration[] { BrowserConfiguration.ie("") }), 
    
    MS_SCROLL_LIMIT_X_MAX(
      "msScrollLimitXMax", "-ms-scroll-limit-x-max", new BrowserConfiguration[] { BrowserConfiguration.ie("0px") }), 
    
    MS_SCROLL_LIMIT_X_MIN(
      "msScrollLimitXMin", "-ms-scroll-limit-x-min", new BrowserConfiguration[] { BrowserConfiguration.ie("0px") }), 
    
    MS_SCROLL_LIMIT_Y_MAX(
      "msScrollLimitYMax", "-ms-scroll-limit-y-max", new BrowserConfiguration[] { BrowserConfiguration.ie("0px") }), 
    
    MS_SCROLL_LIMIT_Y_MIN(
      "msScrollLimitYMin", "-ms-scroll-limit-y-min", new BrowserConfiguration[] { BrowserConfiguration.ie("0px") }), 
    
    MS_SCROLL_RAILS(
      "msScrollRails", "-ms-scroll-rails", new BrowserConfiguration[] { BrowserConfiguration.ie("railed") }), 
    
    MS_SCROLL_SNAP_POINTS_X(
      "msScrollSnapPointsX", "-ms-scroll-snap-points-x", new BrowserConfiguration[] { BrowserConfiguration.ie("snapInterval(0%, 100%)") }), 
    
    MS_SCROLL_SNAP_POINTS_Y(
      "msScrollSnapPointsY", "-ms-scroll-snap-points-y", new BrowserConfiguration[] { BrowserConfiguration.ie("snapInterval(0%, 100%)") }), 
    
    MS_SCROLL_SNAP_TYPE(
      "msScrollSnapType", "-ms-scroll-snap-type", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_SCROLL_SNAP_X(
      "msScrollSnapX", "-ms-scroll-snap-x", new BrowserConfiguration[] { BrowserConfiguration.ie("none snapInterval(0%, 100%)") }), 
    
    MS_SCROLL_SNAP_Y(
      "msScrollSnapY", "-ms-scroll-snap-y", new BrowserConfiguration[] { BrowserConfiguration.ie("none snapInterval(0%, 100%)") }), 
    
    MS_SCROLL_TRANSLATION(
      "msScrollTranslation", "-ms-scroll-translation", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_TEXT_COMBINE_HORIZONTAL(
      "msTextCombineHorizontal", "-ms-text-combine-horizontal", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_TOUCH_ACTION(
      "msTouchAction", "-ms-touch-action", new BrowserConfiguration[] { BrowserConfiguration.ie("auto") }), 
    
    MS_TOUCH_SELECT(
      "msTouchSelect", "-ms-touch-select", new BrowserConfiguration[] { BrowserConfiguration.ie("") }), 
    
    MS_TRANSFORM(
      "msTransform", "-ms-transform", new BrowserConfiguration[] { BrowserConfiguration.ie("none") }), 
    
    MS_TRANSFORM_ORIGIN(
      "msTransformOrigin", "-ms-transform-origin", new BrowserConfiguration[] { BrowserConfiguration.ie("620px 163.2px") }), 
    
    MS_TRANSFORM_STYLE(
      "msTransformStyle", "-ms-transform-style", new BrowserConfiguration[] { BrowserConfiguration.ie("flat") }), 
    
    MS_TRANSITION(
      "msTransition", "-ms-transition", new BrowserConfiguration[] { BrowserConfiguration.ie("") }), 
    
    MS_TRANSITION_DELAY(
      "msTransitionDelay", "-ms-transition-delay", new BrowserConfiguration[] { BrowserConfiguration.ie("0s") }), 
    
    MS_TRANSITION_DURATION(
      "msTransitionDuration", 
      "-ms-transition-duration", new BrowserConfiguration[] { BrowserConfiguration.ie("0s") }), 
    
    MS_TRANSITION_PROPERTY(
      "msTransitionProperty", 
      "-ms-transition-property", new BrowserConfiguration[] { BrowserConfiguration.ie("all") }), 
    
    MS_TRANSITION_TIMING_FUNCTION(
      "msTransitionTimingFunction", 
      "-ms-transition-timing-function", new BrowserConfiguration[] {
      BrowserConfiguration.ie("cubic-bezier(0.25, 0.1, 0.25, 1)") }), 
    
    MS_USER_SELECT(
      "msUserSelect", "-ms-user-select", new BrowserConfiguration[] { BrowserConfiguration.ie("text") }), 
    
    MS_WRAP_FLOW(
      "msWrapFlow", "-ms-wrap-flow", new BrowserConfiguration[] { BrowserConfiguration.ie("auto") }), 
    
    MS_WRAP_MARGIN(
      "msWrapMargin", "-ms-wrap-margin", new BrowserConfiguration[] { BrowserConfiguration.ie("auto") }), 
    
    MS_WRAP_THROUGH(
      "msWrapThrough", "-ms-wrap-through", new BrowserConfiguration[] { BrowserConfiguration.ie("wrap") }), 
    
    OBJECT_FIT(
      "objectFit", "object-fit", new BrowserConfiguration[] { BrowserConfiguration.ff("fill"), BrowserConfiguration.chrome("fill") }), 
    
    OBJECT_FIT_(
      "object-fit", "object-fit", new BrowserConfiguration[] { BrowserConfiguration.ff("fill") }), 
    
    OBJECT_POSITION(
      "objectPosition", "object-position", new BrowserConfiguration[] { BrowserConfiguration.ff("50% 50%"), BrowserConfiguration.chrome("50% 50%") }), 
    
    OBJECT_POSITION_(
      "object-position", "object-position", new BrowserConfiguration[] { BrowserConfiguration.ff("50% 50%") }), 
    
    OFFSET(
      "offset", "offset", new BrowserConfiguration[] { BrowserConfiguration.chrome("none 0px auto 0deg") }), 
    
    OFFSET_BLOCK_END(
      "offsetBlockEnd", "offset-block-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    OFFSET_BLOCK_END_(
      "offset-block-end", "offset-block-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    OFFSET_BLOCK_START(
      "offsetBlockStart", "offset-block-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    OFFSET_BLOCK_START_(
      "offset-block-start", "offset-block-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    OFFSET_DISTANCE(
      "offsetDistance", "offsetDistance", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    OFFSET_INLINE_END(
      "offsetInlineEnd", "offset-inline-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    OFFSET_INLINE_END_(
      "offset-inline-end", "offset-inline-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    OFFSET_INLINE_START(
      "offsetInlineStart", "offset-inline-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    OFFSET_INLINE_START_(
      "offset-inline-start", "offset-inline-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    OFFSET_PATH(
      "offsetPath", "offsetPath", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    OFFSET_ROTATE(
      "offsetRotate", "offsetRotate", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto 0deg") }), 
    
    OFFSET_ROTATION(
      "offsetRotation", "offsetRotation", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto 0deg") }), 
    
    OPACITY(
      "opacity", "opacity", new BrowserConfiguration[] { BrowserConfiguration.chrome("1"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    ORDER(
      "order", "order", new BrowserConfiguration[] { BrowserConfiguration.ff("0"), BrowserConfiguration.ie("0"), BrowserConfiguration.chrome("0") }), 
    
    ORIENTATION(
      "orientation", "orientation", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    ORPHANS(
      "orphans", "orphans", new BrowserConfiguration[] { BrowserConfiguration.ie("2"), BrowserConfiguration.chrome("2") }), 
    
    OUTLINE(
      "outline", "outline", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0) none 0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    OUTLINE_COLOR(
      "outlineColor", "outline-color", new BrowserConfiguration[] { BrowserConfiguration.ie("transparent"), BrowserConfiguration.chrome("rgb(0, 0, 0)"), 
      BrowserConfiguration.ff("rgb(0, 0, 0)") }), 
    
    OUTLINE_COLOR_(
      "outline-color", "outline-color", new BrowserConfiguration[] { BrowserConfiguration.ff("rgb(0, 0, 0)") }), 
    
    OUTLINE_OFFSET(
      "outlineOffset", "outline-offset", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff("0px") }), 
    
    OUTLINE_OFFSET_(
      "outline-offset", "outline-offset", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    OUTLINE_STYLE(
      "outlineStyle", "outline-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    OUTLINE_STYLE_(
      "outline-style", "outline-style", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    OUTLINE_WIDTH(
      "outlineWidth", "outline-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    OUTLINE_WIDTH_(
      "outline-width", "outline-width", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    OVERFLOW(
      "overflow", "overflow", new BrowserConfiguration[] { BrowserConfiguration.chrome("visible"), BrowserConfiguration.ff("visible"), BrowserConfiguration.ie("visible") }), 
    
    OVERFLOW_ANCHOR(
      "overflowAnchor", "overflow-anchor", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    OVERFLOW_WRAP(
      "overflowWrap", "overflow-wrap", new BrowserConfiguration[] { BrowserConfiguration.chrome("normal") }), 
    
    OVERFLOW_X(
      "overflowX", "overflow-x", new BrowserConfiguration[] { BrowserConfiguration.chrome("visible"), BrowserConfiguration.ff("visible"), BrowserConfiguration.ie("visible") }), 
    
    OVERFLOW_X_(
      "overflow-x", "overflow-x", new BrowserConfiguration[] { BrowserConfiguration.ff("visible") }), 
    
    OVERFLOW_Y(
      "overflowY", "overflow-y", new BrowserConfiguration[] { BrowserConfiguration.chrome("visible"), BrowserConfiguration.ff("visible"), BrowserConfiguration.ie("visible") }), 
    
    OVERFLOW_Y_(
      "overflow-y", "overflow-y", new BrowserConfiguration[] { BrowserConfiguration.ff("visible") }), 
    
    PADDING(
      "padding", "padding", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    PADDING_BLOCK_END(
      "paddingBlockEnd", "padding-block-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    PADDING_BLOCK_END_(
      "padding-block-end", "padding-block-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    PADDING_BLOCK_START(
      "paddingBlockStart", "padding-block-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    PADDING_BLOCK_START_(
      "padding-block-start", "padding-block-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    PADDING_BOTTOM(
      "paddingBottom", "padding-bottom", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    PADDING_BOTTOM_(
      "padding-bottom", "padding-bottom", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    PADDING_INLINE_END(
      "paddingInlineEnd", "padding-inline-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    PADDING_INLINE_END_(
      "padding-inline-end", "padding-inline-end", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    PADDING_INLINE_START(
      "paddingInlineStart", "padding-inline-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    PADDING_INLINE_START_(
      "padding-inline-start", "padding-inline-start", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    PADDING_LEFT(
      "paddingLeft", "padding-left", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    PADDING_LEFT_(
      "padding-left", "padding-left", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    PADDING_RIGHT(
      "paddingRight", "padding-right", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    PADDING_RIGHT_(
      "padding-right", "padding-right", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    PADDING_TOP(
      "paddingTop", "padding-top", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    PADDING_TOP_(
      "padding-top", "padding-top", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    PAGE(
      "page", "page", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    PAGE_BREAK_AFTER(
      "pageBreakAfter", "page-break-after", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ff("auto"), BrowserConfiguration.ie("auto") }), 
    
    PAGE_BREAK_AFTER_(
      "page-break-after", "page-break-after", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    PAGE_BREAK_BEFORE(
      "pageBreakBefore", "page-break-before", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ff("auto"), BrowserConfiguration.ie("auto") }), 
    
    PAGE_BREAK_BEFORE_(
      "page-break-before", "page-break-before", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    PAGE_BREAK_INSIDE(
      "pageBreakInside", "page-break-inside", new BrowserConfiguration[] { BrowserConfiguration.ff("auto"), BrowserConfiguration.ie("auto"), BrowserConfiguration.chrome("auto") }), 
    
    PAGE_BREAK_INSIDE_(
      "page-break-inside", "page-break-inside", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    PAINT_ORDER(
      "paintOrder", "paint-order", new BrowserConfiguration[] { BrowserConfiguration.ff("normal"), BrowserConfiguration.chrome("fill stroke markers") }), 
    
    PAINT_ORDER_(
      "paint-order", "paint-order", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    PAUSE(
      "pause", "pause", new BrowserConfiguration[0]), 
    
    PAUSE_AFTER(
      "pauseAfter", "pause-after", new BrowserConfiguration[0]), 
    
    PAUSE_BEFORE(
      "pauseBefore", "pause-before", new BrowserConfiguration[0]), 
    
    PERSPECTIVE(
      "perspective", "perspective", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    PERSPECTIVE_ORIGIN(
      "perspectiveOrigin", "perspective-origin", new BrowserConfiguration[] {
      BrowserConfiguration.ff("621px 172.5px"), BrowserConfiguration.ie("620px 163.2px"), BrowserConfiguration.chrome("456.5px 161px") }), 
    
    PERSPECTIVE_ORIGIN_(
      "perspective-origin", "perspective-origin", new BrowserConfiguration[] { BrowserConfiguration.ff("621px 172.5px") }), 
    
    PITCH(
      "pitch", "pitch", new BrowserConfiguration[0]), 
    
    PITCH_RANGE(
      "pitchRange", "pitch-range", new BrowserConfiguration[0]), 
    
    PIXEL_BOTTOM(
      "pixelBottom", "pixel-bottom", new BrowserConfiguration[] { BrowserConfiguration.ie("").setIteratable(false) }), 
    
    PIXEL_HEIGHT(
      "pixelHeight", "pixel-height", new BrowserConfiguration[] { BrowserConfiguration.ie("").setIteratable(false) }), 
    
    PIXEL_LEFT(
      "pixelLeft", "pixel-left", new BrowserConfiguration[] { BrowserConfiguration.ie("").setIteratable(false) }), 
    
    PIXEL_RIGHT(
      "pixelRight", "pixel-right", new BrowserConfiguration[] { BrowserConfiguration.ie("").setIteratable(false) }), 
    
    PIXEL_TOP(
      "pixelTop", "pixel-top", new BrowserConfiguration[] { BrowserConfiguration.ie("").setIteratable(false) }), 
    
    PIXEL_WIDTH(
      "pixelWidth", "pixel-width", new BrowserConfiguration[] { BrowserConfiguration.ie("").setIteratable(false) }), 
    
    POINTER_EVENTS(
      "pointerEvents", "pointer-events", new BrowserConfiguration[] { BrowserConfiguration.ie("visiblePainted"), BrowserConfiguration.chrome("auto"), BrowserConfiguration.ff("auto") }), 
    
    POINTER_EVENTS_(
      "pointer-events", "pointer-events", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    POS_BOTTOM(
      "posBottom", "pos-bottom", new BrowserConfiguration[] { BrowserConfiguration.ie("").setIteratable(false) }), 
    
    POS_HEIGHT(
      "posHeight", "pos-height", new BrowserConfiguration[] { BrowserConfiguration.ie("").setIteratable(false) }), 
    
    POS_LEFT(
      "posLeft", "pos-left", new BrowserConfiguration[] { BrowserConfiguration.ie("").setIteratable(false) }), 
    
    POS_RIGHT(
      "posRight", "pos-right", new BrowserConfiguration[] { BrowserConfiguration.ie("").setIteratable(false) }), 
    
    POS_TOP(
      "posTop", "pos-top", new BrowserConfiguration[] { BrowserConfiguration.ie("").setIteratable(false) }), 
    
    POS_WIDTH(
      "posWidth", "pos-width", new BrowserConfiguration[] { BrowserConfiguration.ie("").setIteratable(false) }), 
    
    POSITION(
      "position", "position", new BrowserConfiguration[] { BrowserConfiguration.chrome("static"), BrowserConfiguration.ff("static"), BrowserConfiguration.ie("static") }), 
    
    QUOTES(
      "quotes", "quotes", new BrowserConfiguration[] { BrowserConfiguration.ff("\"“\" \"”\" \"‘\" \"’\""), BrowserConfiguration.ie(""), BrowserConfiguration.chrome("") }), 
    
    R(
      "r", "r", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    RESIZE(
      "resize", "resize", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.chrome("none") }), 
    
    RICHNESS(
      "richness", "richness", new BrowserConfiguration[0]), 
    
    RIGHT(
      "right", "right", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    RUBY_ALIGN(
      "rubyAlign", "ruby-align", new BrowserConfiguration[] { BrowserConfiguration.ff("space-around"), BrowserConfiguration.ie("") }), 
    
    RUBY_ALIGN_(
      "ruby-align", "ruby-align", new BrowserConfiguration[] { BrowserConfiguration.ff("space-around") }), 
    
    RUBY_OVERHANG(
      "rubyOverhang", "ruby-overhang", new BrowserConfiguration[] { BrowserConfiguration.ie("auto") }), 
    
    RUBY_POSITION(
      "rubyPosition", "ruby-position", new BrowserConfiguration[] { BrowserConfiguration.ie("above"), BrowserConfiguration.ff("over") }), 
    
    RUBY_POSITION_(
      "ruby-position", "ruby-position", new BrowserConfiguration[] { BrowserConfiguration.ff("over") }), 
    
    RX(
      "rx", "rx", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    RY(
      "ry", "ry", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    SCROLL_BEHAVIOR(
      "scrollBehavior", "scroll-behavior", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    SCROLL_BEHAVIOR_(
      "scroll-behavior", "scroll-behavior", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    SCROLL_SNAP_COORDINATE(
      "scrollSnapCoordinate", "scroll-snap-coordinate", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    SCROLL_SNAP_COORDINATE_(
      "scroll-snap-coordinate", "scroll-snap-coordinate", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    SCROLL_SNAP_DESTINATION(
      "scrollSnapDestination", "scroll-snap-destination", new BrowserConfiguration[] { BrowserConfiguration.ff("0px 0px") }), 
    
    SCROLL_SNAP_DESTINATION_(
      "scroll-snap-destination", "scroll-snap-destination", new BrowserConfiguration[] { BrowserConfiguration.ff("0px 0px") }), 
    
    SCROLL_SNAP_POINTS_X(
      "scrollSnapPointsX", "scroll-snap-points-x", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    SCROLL_SNAP_POINTS_X_(
      "scroll-snap-points-x", "scroll-snap-points-x", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    SCROLL_SNAP_POINTS_Y(
      "scrollSnapPointsY", "scroll-snap-points-y", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    SCROLL_SNAP_POINTS_Y_(
      "scroll-snap-points-y", "scroll-snap-points-y", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    SCROLL_SNAP_TYPE(
      "scrollSnapType", "scroll-snap-type", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    SCROLL_SNAP_TYPE_(
      "scroll-snap-type", "scroll-snap-type", new BrowserConfiguration[] { BrowserConfiguration.ff("") }), 
    
    SCROLL_SNAP_TYPE_X(
      "scrollSnapTypeX", "scroll-snap-type-x", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    SCROLL_SNAP_TYPE_X_(
      "scroll-snap-type-x", "scroll-snap-type-x", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    SCROLL_SNAP_TYPE_Y(
      "scrollSnapTypeY", "scroll-snap-type-y", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    SCROLL_SNAP_TYPE_Y_(
      "scroll-snap-type-y", "scroll-snap-type-y", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    SCROLLBAR_3DLIGHT_COLOR(
      "scrollbar3dLightColor", "scrollbar-3dlight-color", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    SCROLLBAR_ARROW_COLOR(
      "scrollbarArrowColor", "scrollbar-arrow-color", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    SCROLLBAR_BASE_COLOR(
      "scrollbarBaseColor", "scrollbar-base-color", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    SCROLLBAR_DARKSHADOW_COLOR(
      "scrollbarDarkShadowColor", "scrollbar-darkshadow-color", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    SCROLLBAR_FACE_COLOR(
      "scrollbarFaceColor", "scrollbar-face-color", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    SCROLLBAR_HIGHLIGHT_COLOR(
      "scrollbarHighlightColor", "scrollbar-highlight-color", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    SCROLLBAR_SHADOW_COLOR(
      "scrollbarShadowColor", "scrollbar-shadow-color", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    SCROLLBAR_TRACK_COLOR(
      "scrollbarTrackColor", "scrollbar-track-color", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    SHAPE_IMAGE_THRESHOLD(
      "shapeImageThreshold", "shape-image-threshold", new BrowserConfiguration[] { BrowserConfiguration.chrome("0") }), 
    
    SHAPE_MARGIN(
      "shapeMargin", "shape-margin", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    SHAPE_OUTSIDE(
      "shapeOutside", "shape-outside", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    SHAPE_RENDERING(
      "shapeRendering", "shape-rendering", new BrowserConfiguration[] { BrowserConfiguration.ff("auto"), BrowserConfiguration.chrome("auto") }), 
    
    SHAPE_RENDERING_(
      "shape-rendering", "shape-rendering", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    SIZE(
      "size", "size", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    SPEAK(
      "speak", "speak", new BrowserConfiguration[] { BrowserConfiguration.chrome("normal") }), 
    
    SPEAK_HEADER(
      "speakHeader", "speak-header", new BrowserConfiguration[0]), 
    
    SPEAK_NUMERAL(
      "speakNumeral", "speak-numeral", new BrowserConfiguration[0]), 
    
    SPEAK_PUNCTUATION(
      "speakPunctuation", "speak-punctuation", new BrowserConfiguration[0]), 
    
    SPEECH_RATE(
      "speechRate", "speech-rate", new BrowserConfiguration[0]), 
    
    SRC(
      "src", "src", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    STOP_COLOR(
      "stopColor", "stop-color", new BrowserConfiguration[] { BrowserConfiguration.ff("rgb(0, 0, 0)"), BrowserConfiguration.ie(""), BrowserConfiguration.chrome("rgb(0, 0, 0)") }), 
    
    STOP_COLOR_(
      "stop-color", "stop-color", new BrowserConfiguration[] { BrowserConfiguration.ff("rgb(0, 0, 0)") }), 
    
    STOP_OPACITY(
      "stopOpacity", "stop-opacity", new BrowserConfiguration[] { BrowserConfiguration.ff("1"), BrowserConfiguration.ie("1"), BrowserConfiguration.chrome("1") }), 
    
    STOP_OPACITY_(
      "stop-opacity", "stop-opacity", new BrowserConfiguration[] { BrowserConfiguration.ff("1") }), 
    
    STRESS(
      "stress", "stress", new BrowserConfiguration[0]), 
    
    STROKE(
      "stroke", "stroke", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie(""), BrowserConfiguration.chrome("none") }), 
    
    STROKE_DASHARRAY(
      "strokeDasharray", "stroke-dasharray", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    STROKE_DASHARRAY_(
      "stroke-dasharray", "stroke-dasharray", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    STROKE_DASHOFFSET(
      "strokeDashoffset", "stroke-dashoffset", new BrowserConfiguration[] { BrowserConfiguration.ff("0px"), BrowserConfiguration.ie("0px"), BrowserConfiguration.chrome("0px") }), 
    
    STROKE_DASHOFFSET_(
      "stroke-dashoffset", "stroke-dashoffset", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    STROKE_LINECAP(
      "strokeLinecap", "stroke-linecap", new BrowserConfiguration[] { BrowserConfiguration.ff("butt"), BrowserConfiguration.ie("butt"), BrowserConfiguration.chrome("butt") }), 
    
    STROKE_LINECAP_(
      "stroke-linecap", "stroke-linecap", new BrowserConfiguration[] { BrowserConfiguration.ff("butt") }), 
    
    STROKE_LINEJOIN(
      "strokeLinejoin", "stroke-linejoin", new BrowserConfiguration[] { BrowserConfiguration.ff("miter"), BrowserConfiguration.ie("miter"), BrowserConfiguration.chrome("miter") }), 
    
    STROKE_LINEJOIN_(
      "stroke-linejoin", "stroke-linejoin", new BrowserConfiguration[] { BrowserConfiguration.ff("miter") }), 
    
    STROKE_MITERLIMIT(
      "strokeMiterlimit", "stroke-miterlimit", new BrowserConfiguration[] { BrowserConfiguration.ff("4"), BrowserConfiguration.ie("4"), BrowserConfiguration.chrome("4") }), 
    
    STROKE_MITERLIMIT_(
      "stroke-miterlimit", "stroke-miterlimit", new BrowserConfiguration[] { BrowserConfiguration.ff("4") }), 
    
    STROKE_OPACITY(
      "strokeOpacity", "stroke-opacity", new BrowserConfiguration[] { BrowserConfiguration.ff("1"), BrowserConfiguration.ie("1"), BrowserConfiguration.chrome("1") }), 
    
    STROKE_OPACITY_(
      "stroke-opacity", "stroke-opacity", new BrowserConfiguration[] { BrowserConfiguration.ff("1") }), 
    
    STROKE_WIDTH(
      "strokeWidth", "stroke-width", new BrowserConfiguration[] { BrowserConfiguration.ff("1px"), BrowserConfiguration.ie("0.01px"), BrowserConfiguration.chrome("1px") }), 
    
    STROKE_WIDTH_(
      "stroke-width", "stroke-width", new BrowserConfiguration[] { BrowserConfiguration.ff("1px") }), 
    
    STYLE_FLOAT(
      "styleFloat", "style-float", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    TAB_SIZE(
      "tabSize", "tab-size", new BrowserConfiguration[] { BrowserConfiguration.chrome("8") }), 
    
    TABLE_LAYOUT(
      "tableLayout", "table-layout", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ff("auto"), BrowserConfiguration.ie("auto") }), 
    
    TABLE_LAYOUT_(
      "table-layout", "table-layout", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    TEXT_ALIGN(
      "textAlign", "text-align", new BrowserConfiguration[] { BrowserConfiguration.ie("left"), BrowserConfiguration.chrome("start"), BrowserConfiguration.ff("start") }), 
    
    TEXT_ALIGN_(
      "text-align", "text-align", new BrowserConfiguration[] { BrowserConfiguration.ff("start") }), 
    
    TEXT_ALIGN_LAST(
      "textAlignLast", "text-align-last", new BrowserConfiguration[] { BrowserConfiguration.ie("auto"), BrowserConfiguration.chrome("auto") }), 
    
    TEXT_ANCHOR(
      "textAnchor", "text-anchor", new BrowserConfiguration[] { BrowserConfiguration.ff("start"), BrowserConfiguration.ie("start"), BrowserConfiguration.chrome("start") }), 
    
    TEXT_ANCHOR_(
      "text-anchor", "text-anchor", new BrowserConfiguration[] { BrowserConfiguration.ff("start") }), 
    
    TEXT_AUTOSPACE(
      "textAutospace", "text-autospace", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    TEXT_COMBINE_UPRIGHT(
      "textCombineUpright", "text-combine-upright", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    TEXT_DECORATION(
      "textDecoration", "text-decoration", new BrowserConfiguration[] { BrowserConfiguration.chrome("none solid rgb(0, 0, 0)"), BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    TEXT_DECORATION_(
      "text-decoration", "text-decoration", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    TEXT_DECORATION_BLINK(
      "textDecorationBlink", "text-decoration-blink", new BrowserConfiguration[] { BrowserConfiguration.ie("false").setIteratable(false) }), 
    
    TEXT_DECORATION_COLOR(
      "textDecorationColor", "text-decoration-color", new BrowserConfiguration[] {
      BrowserConfiguration.chrome("rgb(0, 0, 0)"), BrowserConfiguration.ff("rgb(0, 0, 0)") }), 
    
    TEXT_DECORATION_COLOR_(
      "text-decoration-color", "text-decoration-color", new BrowserConfiguration[] { BrowserConfiguration.ff("rgb(0, 0, 0)") }), 
    
    TEXT_DECORATION_LINE(
      "textDecorationLine", "text-decoration-line", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff("none") }), 
    
    TEXT_DECORATION_LINE_(
      "text-decoration-line", "text-decoration-line", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    TEXT_DECORATION_LINE_THROUGH(
      "textDecorationLineThrough", "text-decoration-line-through", new BrowserConfiguration[] {
      BrowserConfiguration.ie("false").setIteratable(false) }), 
    
    TEXT_DECORATION_NONE(
      "textDecorationNone", "text-decoration-none", new BrowserConfiguration[] { BrowserConfiguration.ie("false").setIteratable(false) }), 
    
    TEXT_DECORATION_OVERLINE(
      "textDecorationOverline", "text-decoration-overline", new BrowserConfiguration[] {
      BrowserConfiguration.ie("false").setIteratable(false) }), 
    
    TEXT_DECORATION_SKIP(
      "textDecorationSkip", "text-decoration-sskip", new BrowserConfiguration[] { BrowserConfiguration.chrome("objects") }), 
    
    TEXT_DECORATION_STYLE(
      "textDecorationStyle", "text-decoration-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("solid"), BrowserConfiguration.ff("solid") }), 
    
    TEXT_DECORATION_STYLE_(
      "text-decoration-style", "text-decoration-style", new BrowserConfiguration[] { BrowserConfiguration.ff("solid") }), 
    
    TEXT_DECORATION_UNDERLINE(
      "textDecorationUnderline", "text-decoration-underline", new BrowserConfiguration[] {
      BrowserConfiguration.ie("false").setIteratable(false) }), 
    
    TEXT_INDENT(
      "textIndent", "text-indent", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    TEXT_INDENT_(
      "text-indent", "text-indent", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    TEXT_JUSTIFY(
      "textJustify", "text-justify", new BrowserConfiguration[] { BrowserConfiguration.ie("auto") }), 
    
    TEXT_JUSTIFY_TRIM(
      "textJustifyTrim", "text-justify-trim", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    TEXT_KASHIDA(
      "textKashida", "text-kashida", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    TEXT_KASHIDA_SPACE(
      "textKashidaSpace", "text-kashida-space", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined") }), 
    
    TEXT_ORIENTATION(
      "textOrientation", "text-orientation", new BrowserConfiguration[] { BrowserConfiguration.chrome("mixed"), BrowserConfiguration.ff("mixed") }), 
    
    TEXT_ORIENTATION_(
      "text-orientation", "text-orientation", new BrowserConfiguration[] { BrowserConfiguration.ff("mixed") }), 
    
    TEXT_OVERFLOW(
      "textOverflow", "text-overflow", new BrowserConfiguration[] { BrowserConfiguration.ff("clip"), BrowserConfiguration.ie("clip"), BrowserConfiguration.chrome("clip") }), 
    
    TEXT_OVERFLOW_(
      "text-overflow", "text-overflow", new BrowserConfiguration[] { BrowserConfiguration.ff("clip") }), 
    
    TEXT_RENDERING(
      "textRendering", "text-rendering", new BrowserConfiguration[] { BrowserConfiguration.ff("auto"), BrowserConfiguration.chrome("auto") }), 
    
    TEXT_RENDERING_(
      "text-rendering", "text-rendering", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    TEXT_SHADOW(
      "textShadow", "text-shadow", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    TEXT_SHADOW_(
      "text-shadow", "text-shadow", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    TEXT_SIZE_ADJUST(
      "textSizeAdjust", "text-size-adjust", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    TEXT_TRANSFORM(
      "textTransform", "text-transform", new BrowserConfiguration[] { BrowserConfiguration.chrome("none"), BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none") }), 
    
    TEXT_TRANSFORM_(
      "text-transform", "text-transform", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    TEXT_UNDERLINE_POSITION(
      "textUnderlinePosition", "text-underline-position", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto"), BrowserConfiguration.ie("auto") }), 
    
    TOP(
      "top", "top", new BrowserConfiguration[] { BrowserConfiguration.ff("auto"), BrowserConfiguration.ie("auto"), BrowserConfiguration.chrome("auto") }), 
    
    TOUCH_ACTION(
      "touchAction", "touch-action", new BrowserConfiguration[] { BrowserConfiguration.ie("auto"), BrowserConfiguration.chrome("auto") }), 
    
    TRANSFORM(
      "transform", "transform", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.ie("none"), BrowserConfiguration.chrome("none") }), 
    
    TRANSFORM_ORIGIN(
      "transformOrigin", "transform-origin", new BrowserConfiguration[] {
      BrowserConfiguration.ff("621px 172.5px"), BrowserConfiguration.ie("620px 163.2px"), BrowserConfiguration.chrome("456.5px 161px") }), 
    
    TRANSFORM_ORIGIN_(
      "transform-origin", "transform-origin", new BrowserConfiguration[] { BrowserConfiguration.ff("621px 172.5px") }), 
    
    TRANSFORM_STYLE(
      "transformStyle", "transform-style", new BrowserConfiguration[] { BrowserConfiguration.ff("flat"), BrowserConfiguration.ie("flat"), BrowserConfiguration.chrome("flat") }), 
    
    TRANSFORM_STYLE_(
      "transform-style", "transform-style", new BrowserConfiguration[] { BrowserConfiguration.ff("flat") }), 
    
    TRANSITION(
      "transition", "transition", new BrowserConfiguration[] { BrowserConfiguration.ff(""), BrowserConfiguration.ie(""), BrowserConfiguration.chrome("all 0s ease 0s") }), 
    
    TRANSITION_DELAY(
      "transitionDelay", "transition-delay", new BrowserConfiguration[] { BrowserConfiguration.ff("0s"), BrowserConfiguration.ie("0s"), BrowserConfiguration.chrome("0s") }), 
    
    TRANSITION_DELAY_(
      "transition-delay", "transition-delay", new BrowserConfiguration[] { BrowserConfiguration.ff("0s") }), 
    
    TRANSITION_DURATION(
      "transitionDuration", "transition-duration", new BrowserConfiguration[] { BrowserConfiguration.ff("0s"), BrowserConfiguration.ie("0s"), BrowserConfiguration.chrome("0s") }), 
    
    TRANSITION_DURATION_(
      "transition-duration", "transition-duration", new BrowserConfiguration[] { BrowserConfiguration.ff("0s") }), 
    
    TRANSITION_PROPERTY(
      "transitionProperty", "transition-property", new BrowserConfiguration[] { BrowserConfiguration.ff("all"), BrowserConfiguration.ie("all"), BrowserConfiguration.chrome("all") }), 
    
    TRANSITION_PROPERTY_(
      "transition-property", "transition-property", new BrowserConfiguration[] { BrowserConfiguration.ff("all") }), 
    
    TRANSITION_TIMING_FUNCTION(
      "transitionTimingFunction", 
      "transition-timing-function", new BrowserConfiguration[] {
      BrowserConfiguration.ff("ease"), 
      BrowserConfiguration.ie("cubic-bezier(0.25, 0.1, 0.25, 1)"), 
      BrowserConfiguration.chrome("ease") }), 
    
    TRANSITION_TIMING_FUNCTION_(
      "transition-timing-function", "transition-timing-function", new BrowserConfiguration[] {
      BrowserConfiguration.ff("ease") }), 
    
    UNICODE_BIDI(
      "unicodeBidi", "unicode-bidi", new BrowserConfiguration[] {
      BrowserConfiguration.ff("-moz-isolate"), BrowserConfiguration.ie("normal"), BrowserConfiguration.chrome("normal") }), 
    
    UNICODE_BIDI_(
      "unicode-bidi", "unicode-bidi", new BrowserConfiguration[] { BrowserConfiguration.ff("-moz-isolate") }), 
    
    UNICODE_RANGE(
      "unicodeRange", "unicode-range", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    USER_SELECT(
      "userSelect", "user-select", new BrowserConfiguration[] { BrowserConfiguration.chrome("text") }), 
    
    USER_ZOOM(
      "userZoom", "user-zoom", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    VECTOR_EFFECT(
      "vectorEffect", "vector-effect", new BrowserConfiguration[] { BrowserConfiguration.ff("none"), BrowserConfiguration.chrome("none") }), 
    
    VECTOR_EFFECT_(
      "vector-effect", "vector-effect", new BrowserConfiguration[] { BrowserConfiguration.ff("none") }), 
    
    VERTICAL_ALIGN(
      "verticalAlign", "vertical-align", new BrowserConfiguration[] { BrowserConfiguration.chrome("baseline"), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    VERTICAL_ALIGN_(
      "vertical-align", "vertical-align", new BrowserConfiguration[] { BrowserConfiguration.ff("baseline") }), 
    
    VISIBILITY(
      "visibility", "visibility", new BrowserConfiguration[] { BrowserConfiguration.chrome("visible"), BrowserConfiguration.ff("visible"), BrowserConfiguration.ie("visible") }), 
    
    VOICE_FAMILY(
      "voiceFamily", "voice-family", new BrowserConfiguration[0]), 
    
    VOLUME(
      "volume", "volume", new BrowserConfiguration[0]), 
    
    WEBKIT_APP_REGION(
      "webkitAppRegion", "webkit-app-region", new BrowserConfiguration[] { BrowserConfiguration.chrome("no-drag") }), 
    
    WEBKIT_APPEARANCE(
      "webkitAppearance", "webkit-appearance", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_BACKGROUND_CLIP(
      "webkitBackgroundClip", "webkit-background-clip", new BrowserConfiguration[] { BrowserConfiguration.chrome("border-box") }), 
    
    WEBKIT_BACKGROUND_ORIGIN(
      "webkitBackgroundOrigin", "webkit-background-origin", new BrowserConfiguration[] { BrowserConfiguration.chrome("padding-box") }), 
    
    WEBKIT_BORDER_AFTER(
      "webkitBorderAfter", "webkit-border-after", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px none rgb(0, 0, 0)") }), 
    
    WEBKIT_BORDER_AFTER_COLOR(
      "webkitBorderAfterColor", "webkit-border-after-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)") }), 
    
    WEBKIT_BORDER_AFTER_STYLE(
      "webkitBorderAfterStyle", "webkit-border-after-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_BORDER_AFTER_WIDTH(
      "webkitBorderAfterWidth", "webkit-border-after-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_BORDER_BEFORE(
      "webkitBorderBefore", "webkit-border-before", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px none rgb(0, 0, 0)") }), 
    
    WEBKIT_BORDER_BEFORE_COLOR(
      "webkitBorderBeforeColor", "webkit-border-before-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)") }), 
    
    WEBKIT_BORDER_BEFORE_STYLE(
      "webkitBorderBeforeStyle", "webkit-border-before-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_BORDER_BEFORE_WIDTH(
      "webkitBorderBeforeWidth", "webkit-border-before-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_BORDER_END(
      "webkitBorderEnd", "webkit-border-end", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px none rgb(0, 0, 0)") }), 
    
    WEBKIT_BORDER_END_COLOR(
      "webkitBorderEndColor", "webkit-border-end-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)") }), 
    
    WEBKIT_BORDER_END_STYLE(
      "webkitBorderEndStyle", "webkit-border-end-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_BORDER_END_WIDTH(
      "webkitBorderEndWidth", "webkit-border-end-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_BORDER_HORIZONTAL_SPACING(
      "webkitBorderHorizontalSpacing", "webkit-border-horizontal-spacing", new BrowserConfiguration[] {
      BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_BORDER_IMAGE(
      "webkitBorderImage", "webkit-border-image", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_BORDER_START(
      "webkitBorderStart", "webkit-border-start", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px none rgb(0, 0, 0)") }), 
    
    WEBKIT_BORDER_START_COLOR(
      "webkitBorderStartColor", "webkit-border-start-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)") }), 
    
    WEBKIT_BORDER_START_STYLE(
      "webkitBorderStartStyle", "webkit-border-start-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_BORDER_START_WIDTH(
      "webkitBorderStartWidth", "webkit-border-start-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_BORDER_VERTICAL_SPACING(
      "webkitBorderVerticalSpacing", "webkit-border-vertical-spacing", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_BOX_ALIGN(
      "webkitBoxAlign", "webkit-box-align", new BrowserConfiguration[] { BrowserConfiguration.chrome("stretch") }), 
    
    WEBKIT_BOX_DECORATION_BREAK(
      "webkitBoxDecorationBreak", "webkit-box-decoration-break", new BrowserConfiguration[] { BrowserConfiguration.chrome("slice") }), 
    
    WEBKIT_BOX_DIRECTION(
      "webkitBoxDirection", "webkit-box-direction", new BrowserConfiguration[] { BrowserConfiguration.chrome("normal") }), 
    
    WEBKIT_BOX_FLEX(
      "webkitBoxFlex", "webkit-box-flex", new BrowserConfiguration[] { BrowserConfiguration.chrome("0") }), 
    
    WEBKIT_BOX_FLEX_GROUP(
      "webkitBoxFlexGroup", "webkit-box-flex-group", new BrowserConfiguration[] { BrowserConfiguration.chrome("1") }), 
    
    WEBKIT_BOX_LINES(
      "webkitBoxLines", "webkit-box-lines", new BrowserConfiguration[] { BrowserConfiguration.chrome("single") }), 
    
    WEBKIT_BOX_ORDINAL_GROUP(
      "webkitBoxOrdinalGroup", "webkit-box-ordinal-group", new BrowserConfiguration[] { BrowserConfiguration.chrome("1") }), 
    
    WEBKIT_BOX_ORIENT(
      "webkitBoxOrient", "webkit-box-orient", new BrowserConfiguration[] { BrowserConfiguration.chrome("horizontal") }), 
    
    WEBKIT_BOX_PACK(
      "webkitBoxPack", "webkit-box-pack", new BrowserConfiguration[] { BrowserConfiguration.chrome("start") }), 
    
    WEBKIT_BOX_REFLECT(
      "webkitBoxReflect", "webkit-box-reflect", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_COLUMN_BREAK_AFTER(
      "webkitColumnBreakAfter", "webkit-column-break-after", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    WEBKIT_COLUMN_BREAK_BEFORE(
      "webkitColumnBreakBefore", "webkit-column-break-before", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    WEBKIT_COLUMN_BREAK_INSIDE(
      "webkitColumnBreakInside", "webkit-column-break-inside", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    WEBKIT_FONT_SIZE_DELTA(
      "webkitFontSizeDelta", "webkit-font-size-delta", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    WEBKIT_FONT_SMOOTHING(
      "webkitFontSmoothing", "webkit-font-smoothing", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    WEBKIT_HIGHLIGHT(
      "webkitHighlight", "webkit-highlight", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_HYPHENATE_CHARACTER(
      "webkitHyphenateCharacter", "webkit-hyphenate-character", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    WEBKIT_LINE_BREAK(
      "webkitLineBreak", "webkit-line-break", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    WEBKIT_LINE_CLAMP(
      "webkitLineClamp", "webkit-line-clamp", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_LOCALE(
      "webkitLocale", "webkit-locale", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    WEBKIT_LOGICAL_HEIGHT(
      "webkitLogicalHeight", "webkit-logical-height", new BrowserConfiguration[] { BrowserConfiguration.chrome("322px") }), 
    
    WEBKIT_LOGICAL_WIDTH(
      "webkitLogicalWidth", "webkit-logical-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("913px") }), 
    
    WEBKIT_MARGIN_AFTER(
      "webkitMarginAfter", "webkit-margin-after", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_MARGIN_AFTER_COLLAPSE(
      "webkitMarginAfterCollapse", "webkit-margin-after-collapse", new BrowserConfiguration[] { BrowserConfiguration.chrome("collapse") }), 
    
    WEBKIT_MARGIN_BEFORE(
      "webkitMarginBefore", "webkit-margin-before", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_MARGIN_BEFORE_COLLAPSE(
      "webkitMarginBeforeCollapse", "webkit-margin-before-collapse", new BrowserConfiguration[] {
      BrowserConfiguration.chrome("collapse") }), 
    
    WEBKIT_MARGIN_BOTTOM_COLLAPSE(
      "webkitMarginBottomCollapse", "webkit-margin-bottom-collapse", new BrowserConfiguration[] {
      BrowserConfiguration.chrome("collapse") }), 
    
    WEBKIT_MARGIN_COLLAPSE(
      "webkitMarginCollapse", "webkit-margin-collapse", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    WEBKIT_MARGIN_END(
      "webkitMarginEnd", "webkit-margin-end", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_MARGIN_START(
      "webkitMarginStart", "webkit-margin-start", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_MARGIN_TOP_COLLAPSE(
      "webkitMarginTopCollapse", "webkit-margin-top-collapse", new BrowserConfiguration[] { BrowserConfiguration.chrome("collapse") }), 
    
    WEBKIT_MASK(
      "webkitMask", "webkit-mask", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    WEBKIT_MASK_BOX_IMAGE(
      "webkitMaskBoxImage", "webkit-mask-box-image", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_MASK_BOX_IMAGE_OUTSET(
      "webkitMaskBoxImageOutset", "webkit-mask-box-image-outset", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_MASK_BOX_IMAGE_REPEAT(
      "webkitMaskBoxImageRepeat", "webkit-mask-box-image-repeat", new BrowserConfiguration[] { BrowserConfiguration.chrome("stretch") }), 
    
    WEBKIT_MASK_BOX_IMAGE_SLICE(
      "webkitMaskBoxImageSlice", "webkit-mask-box-image-slice", new BrowserConfiguration[] { BrowserConfiguration.chrome("0 fill") }), 
    
    WEBKIT_MASK_BOX_IMAGE_SOURCE(
      "webkitMaskBoxImageSource", "webkit-mask-box-image-source", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_MASK_BOX_IMAGE_WIDTH(
      "webkitMaskBoxImageWidth", "webkit-mask-box-image-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    WEBKIT_MASK_CLIP(
      "webkitMaskClip", "webkit-mask-clip", new BrowserConfiguration[] { BrowserConfiguration.chrome("border-box") }), 
    
    WEBKIT_MASK_COMPOSITE(
      "webkitMaskComposite", "webkit-mask-composite", new BrowserConfiguration[] { BrowserConfiguration.chrome("source-over") }), 
    
    WEBKIT_MASK_IMAGE(
      "webkitMaskImage", "webkit-mask-image", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_MASK_ORIGIN(
      "webkitMaskOrigin", "webkit-mask-origin", new BrowserConfiguration[] { BrowserConfiguration.chrome("border-box") }), 
    
    WEBKIT_MASK_POSITION(
      "webkitMaskPosition", "webkit-mask-position", new BrowserConfiguration[] { BrowserConfiguration.chrome("0% 0%") }), 
    
    WEBKIT_MASK_POSITION_X(
      "webkitMaskPositionX", "webkit-mask-position-x", new BrowserConfiguration[] { BrowserConfiguration.chrome("0%") }), 
    
    WEBKIT_MASK_POSITION_Y(
      "webkitMaskPositionY", "webkit-mask-position-y", new BrowserConfiguration[] { BrowserConfiguration.chrome("0%") }), 
    
    WEBKIT_MASK_REPEAT(
      "webkitMaskRepeat", "webkit-mask-repeat", new BrowserConfiguration[] { BrowserConfiguration.chrome("repeat") }), 
    
    WEBKIT_MASK_REPEAT_X(
      "webkitMaskRepeatX", "webkit-mask-repeat-x", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    WEBKIT_MASK_REPEAT_Y(
      "webkitMaskRepeatY", "webkit-mask-repeat-y", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    WEBKIT_MASK_SIZE(
      "webkitMaskSize", "webkit-mask-size", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    WEBKIT_MAX_LOGICAL_HEIGHT(
      "webkitMaxLogicalHeight", "webkit-max-logical-height", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_MAX_LOGICAL_WIDTH(
      "webkitMaxLogicalWidth", "webkit-max-logical-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_MIN_LOGICAL_HEIGHT(
      "webkitMinLogicalHeight", "webkit-min-logical-height", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_MIN_LOGICAL_WIDTH(
      "webkitMinLogicalWidth", "webkit-min-logical-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_PADDING_AFTER(
      "webkitPaddingAfter", "webkit-padding-after", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_PADDING_BEFORE(
      "webkitPaddingBefore", "webkit-padding-before", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_PADDING_END(
      "webkitPaddingEnd", "webkit-padding-end", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_PADDING_START(
      "webkitPaddingStart", "webkit-padding-start", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_PERSPECTIVE_ORIGIN_X(
      "webkitPerspectiveOriginX", "webkit-perspective-origin-x", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    WEBKIT_PERSPECTIVE_ORIGIN_Y(
      "webkitPerspectiveOriginY", "webkit-perspective-origin-y", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    WEBKIT_PRINT_COLOR_ADJUST(
      "webkitPrintColorAdjust", "webkit-print-color-adjust", new BrowserConfiguration[] { BrowserConfiguration.chrome("economy") }), 
    
    WEBKIT_RTL_ORDERING(
      "webkitRtlOrdering", "webkit-rtl-ordering", new BrowserConfiguration[] { BrowserConfiguration.chrome("logical") }), 
    
    WEBKIT_RUBY_POSITION(
      "webkitRubyPosition", "webkit-ruby-position", new BrowserConfiguration[] { BrowserConfiguration.chrome("before") }), 
    
    WEBKIT_TAP_HIGHLIGHT_COLOR(
      "webkitTapHighlightColor", "webkit-tap-highlight-color", new BrowserConfiguration[] {
      BrowserConfiguration.chrome("rgba(0, 0, 0, 0.180392)") }), 
    
    WEBKIT_TEXT_COMBINE(
      "webkitTextCombine", "webkit-text-combine", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_TEXT_DECORATIONS_IN_EFFECT(
      "webkitTextDecorationsInEffect", "webkit-text-decorations-in-effect", new BrowserConfiguration[] {
      BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_TEXT_EMPHASIS(
      "webkitTextEmphasis", "webkit-text-emphasis", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    WEBKIT_TEXT_EMPHASIS_COLOR(
      "webkitTextEmphasisColor", "webkit-text-emphasis-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)") }), 
    
    WEBKIT_TEXT_EMPHASIS_POSITION(
      "webkitTextEmphasisPosition", "webkit-text-emphasis-position", new BrowserConfiguration[] { BrowserConfiguration.chrome("over") }), 
    
    WEBKIT_TEXT_EMPHASIS_STYLE(
      "webkitTextEmphasisStyle", "webkit-text-emphasis-style", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_TEXT_FILL_COLOR(
      "webkitTextFillColor", "webkit-text-fill-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)") }), 
    
    WEBKIT_TEXT_ORIENTATION(
      "webkitTextOrientation", "webkit-text-orientation", new BrowserConfiguration[] { BrowserConfiguration.chrome("vertical-right") }), 
    
    WEBKIT_TEXT_SECURITY(
      "webkitTextSecurity", "webkit-text-security", new BrowserConfiguration[] { BrowserConfiguration.chrome("none") }), 
    
    WEBKIT_TEXT_STROKE(
      "webkitTextStroke", "webkit-text-stroke", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    WEBKIT_TEXT_STROKE_COLOR(
      "webkitTextStrokeColor", "webkit-text-stroke-color", new BrowserConfiguration[] { BrowserConfiguration.chrome("rgb(0, 0, 0)") }), 
    
    WEBKIT_TEXT_STROKE_WIDTH(
      "webkitTextStrokeWidth", "webkit-text-stroke-width", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    WEBKIT_TRANSFORM_ORIGIN_X(
      "webkitTransformOriginX", "webkit-transform-origin-x", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    WEBKIT_TRANSFORM_ORIGIN_Y(
      "webkitTransformOriginY", "webkit-transform-origin-y", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    WEBKIT_TRANSFORM_ORIGIN_Z(
      "webkitTransformOriginZ", "webkit-transform-origin-z", new BrowserConfiguration[] { BrowserConfiguration.chrome("") }), 
    
    WEBKIT_USER_DRAG(
      "webkitUserDrag", "webkit-user-drag", new BrowserConfiguration[] { BrowserConfiguration.chrome("auto") }), 
    
    WEBKIT_USER_MODIFY(
      "webkitUserModify", "webkit-user-modify", new BrowserConfiguration[] { BrowserConfiguration.chrome("read-only") }), 
    
    WEBKIT_WRITING_MODE(
      "webkitWritingMode", "webkit-writing-mode", new BrowserConfiguration[] { BrowserConfiguration.chrome("horizontal-tb") }), 
    
    WHITE_SPACE(
      "whiteSpace", "white-space", new BrowserConfiguration[] { BrowserConfiguration.chrome("normal"), BrowserConfiguration.ff("normal"), BrowserConfiguration.ie("normal") }), 
    
    WHITE_SPACE_(
      "white-space", "white-space", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    WIDOWS(
      "widows", "widows", new BrowserConfiguration[] { BrowserConfiguration.ie("2"), BrowserConfiguration.chrome("2") }), 
    
    WIDTH(
      "width", "width", new BrowserConfiguration[] { BrowserConfiguration.chrome(""), BrowserConfiguration.ff(""), BrowserConfiguration.ie("") }), 
    
    WILL_CHANGE(
      "willChange", "will-change", new BrowserConfiguration[] { BrowserConfiguration.ff("auto"), BrowserConfiguration.chrome("auto") }), 
    
    WILL_CHANGE_(
      "will-change", "will-change", new BrowserConfiguration[] { BrowserConfiguration.ff("auto") }), 
    
    WORD_BREAK(
      "wordBreak", "word-break", new BrowserConfiguration[] { BrowserConfiguration.ff("normal"), BrowserConfiguration.ie("normal"), BrowserConfiguration.chrome("normal") }), 
    
    WORD_BREAK_(
      "word-break", "word-break", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    WORD_SPACING(
      "wordSpacing", "word-spacing", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px"), BrowserConfiguration.ff("0px"), BrowserConfiguration.ie("0px") }), 
    
    WORD_SPACING_(
      "word-spacing", "word-spacing", new BrowserConfiguration[] { BrowserConfiguration.ff("0px") }), 
    
    WORD_WRAP(
      "wordWrap", "word-wrap", new BrowserConfiguration[] { BrowserConfiguration.ie(""), BrowserConfiguration.chrome("normal"), BrowserConfiguration.ff("normal") }), 
    
    WORD_WRAP_(
      "word-wrap", "word-wrap", new BrowserConfiguration[] { BrowserConfiguration.ff("normal") }), 
    
    WRITING_MODE(
      "writingMode", "writing-mode", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined"), BrowserConfiguration.chrome("horizontal-tb"), BrowserConfiguration.ff("horizontal-tb") }), 
    
    WRITING_MODE_(
      "writing-mode", "writing-mode", new BrowserConfiguration[] { BrowserConfiguration.ff("horizontal-tb") }), 
    
    X(
      "x", "x", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    Y(
      "y", "y", new BrowserConfiguration[] { BrowserConfiguration.chrome("0px") }), 
    
    Z_INDEX(
      "zIndex", "z-index", new BrowserConfiguration[] { BrowserConfiguration.ff("auto"), BrowserConfiguration.chrome("auto"), BrowserConfiguration.ie("auto") }), 
    
    Z_INDEX_(
      "z-index", "z-index", new BrowserConfiguration[] { BrowserConfiguration.ff("auto"), BrowserConfiguration.chrome("auto").setIteratable(false), 
      BrowserConfiguration.ie("auto").setIteratable(false) }), 
    
    ZOOM(
      "zoom", "zoom", new BrowserConfiguration[] { BrowserConfiguration.ie("undefined"), BrowserConfiguration.chrome("1") });
    
    private final String propertyName_;
    private final String attributeName_;
    private final BrowserConfiguration[] browserConfigurations_;
    
    private Definition(String propertyName, String attributeName, BrowserConfiguration... browserConfigurations)
    {
      propertyName_ = propertyName;
      attributeName_ = attributeName;
      browserConfigurations_ = browserConfigurations;
    }
    
    boolean isAvailable(BrowserVersion browserVersion, boolean onlyIfIteratable) {
      if (browserConfigurations_ == null) {
        return true;
      }
      
      BrowserConfiguration config = 
        BrowserConfiguration.getMatchingConfiguration(browserVersion, browserConfigurations_);
      return (config != null) && ((!onlyIfIteratable) || (config.isIteratable()));
    }
    



    public String getPropertyName()
    {
      return propertyName_;
    }
    



    public String getAttributeName()
    {
      return attributeName_;
    }
    



    public String getDefaultComputedValue(BrowserVersion browserVersion)
    {
      BrowserConfiguration config = 
        BrowserConfiguration.getMatchingConfiguration(browserVersion, browserConfigurations_);
      if (config == null) {
        return "";
      }
      return config.getDefaultValue();
    }
  }
}
