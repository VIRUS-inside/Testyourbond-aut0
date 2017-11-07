package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.util.EncodingSniffer;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;




































public class HtmlForm
  extends HtmlElement
{
  public static final String TAG_NAME = "form";
  private static final Collection<String> SUBMITTABLE_ELEMENT_NAMES = Arrays.asList(new String[] { "input", "button", "select", "textarea", "isindex" });
  
  private static final Pattern SUBMIT_CHARSET_PATTERN = Pattern.compile("[ ,].*");
  
  private final List<HtmlElement> lostChildren_ = new ArrayList();
  



  private boolean isPreventDefault_;
  




  HtmlForm(String qualifiedName, SgmlPage htmlPage, Map<String, DomAttr> attributes)
  {
    super(qualifiedName, htmlPage, attributes);
  }
  













  Page submit(SubmittableElement submitElement)
  {
    HtmlPage htmlPage = (HtmlPage)getPage();
    WebClient webClient = htmlPage.getWebClient();
    if (webClient.getOptions().isJavaScriptEnabled()) {
      if (submitElement != null) {
        isPreventDefault_ = false;
        if (!areChildrenValid()) {
          return htmlPage;
        }
        ScriptResult scriptResult = fireEvent("submit");
        if (isPreventDefault_)
        {
          if (scriptResult == null) {
            return htmlPage;
          }
          return scriptResult.getNewPage();
        }
      }
      
      String action = getActionAttribute().trim();
      if (StringUtils.startsWithIgnoreCase(action, "javascript:")) {
        return htmlPage.executeJavaScriptIfPossible(action, "Form action", getStartLineNumber()).getNewPage();
      }
      
    }
    else if (StringUtils.startsWithIgnoreCase(getActionAttribute(), "javascript:"))
    {

      return htmlPage;
    }
    

    WebRequest request = getWebRequest(submitElement);
    String target = htmlPage.getResolvedTarget(getTargetAttribute());
    
    WebWindow webWindow = htmlPage.getEnclosingWindow();
    
    boolean checkHash = 
      !webClient.getBrowserVersion().hasFeature(BrowserVersionFeatures.FORM_SUBMISSION_DOWNLOWDS_ALSO_IF_ONLY_HASH_CHANGED);
    webClient.download(webWindow, target, request, checkHash, false, "JS form.submit()");
    return htmlPage;
  }
  
  private boolean areChildrenValid() {
    boolean valid = true;
    for (HtmlElement element : getFormHtmlElementDescendants()) {
      if (((element instanceof HtmlInput)) && (!((HtmlInput)element).isValid())) {
        valid = false;
        break;
      }
    }
    return valid;
  }
  






  public WebRequest getWebRequest(SubmittableElement submitElement)
  {
    HtmlPage htmlPage = (HtmlPage)getPage();
    List<NameValuePair> parameters = getParameterListForSubmit(submitElement);
    
    String methodAttribute = getMethodAttribute();
    HttpMethod method; HttpMethod method; if ("post".equalsIgnoreCase(methodAttribute)) {
      method = HttpMethod.POST;
    }
    else {
      if ((!"get".equalsIgnoreCase(methodAttribute)) && (StringUtils.isNotBlank(methodAttribute))) {
        notifyIncorrectness("Incorrect submit method >" + getMethodAttribute() + "<. Using >GET<.");
      }
      method = HttpMethod.GET;
    }
    
    BrowserVersion browser = getPage().getWebClient().getBrowserVersion();
    String actionUrl = getActionAttribute();
    String anchor = null;
    String queryFromFields = "";
    if (HttpMethod.GET == method) {
      if (actionUrl.contains("#")) {
        anchor = StringUtils.substringAfter(actionUrl, "#");
      }
      Charset enc = getPage().getCharset();
      queryFromFields = 
        URLEncodedUtils.format(Arrays.asList(NameValuePair.toHttpClient(parameters)), enc);
      

      actionUrl = StringUtils.substringBefore(actionUrl, "#");
      actionUrl = StringUtils.substringBefore(actionUrl, "?");
      parameters.clear();
    }
    try { URL url;
      URL url;
      if (actionUrl.isEmpty()) {
        url = WebClient.expandUrl(htmlPage.getUrl(), actionUrl);
      }
      else {
        url = htmlPage.getFullyQualifiedUrl(actionUrl);
      }
      
      if (!queryFromFields.isEmpty()) {
        url = UrlUtils.getUrlWithNewQuery(url, queryFromFields);
      }
      
      if ((HttpMethod.GET == method) && (browser.hasFeature(BrowserVersionFeatures.FORM_SUBMISSION_URL_WITHOUT_HASH)) && 
        (WebClient.URL_ABOUT_BLANK != url)) {
        url = UrlUtils.getUrlWithNewRef(url, null);
      }
      else if ((HttpMethod.POST == method) && 
        (browser.hasFeature(BrowserVersionFeatures.FORM_SUBMISSION_URL_WITHOUT_HASH)) && 
        (WebClient.URL_ABOUT_BLANK != url) && 
        (StringUtils.isEmpty(actionUrl))) {
        url = UrlUtils.getUrlWithNewRef(url, null);
      }
      else if ((anchor != null) && 
        (WebClient.URL_ABOUT_BLANK != url)) {
        url = UrlUtils.getUrlWithNewRef(url, anchor);
      }
    }
    catch (MalformedURLException e) {
      throw new IllegalArgumentException("Not a valid url: " + actionUrl);
    }
    URL url;
    WebRequest request = new WebRequest(url, method);
    request.setAdditionalHeader("Accept", browser.getHtmlAcceptHeader());
    request.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
    request.setRequestParameters(parameters);
    if (HttpMethod.POST == method) {
      request.setEncodingType(FormEncodingType.getInstance(getEnctypeAttribute()));
    }
    request.setCharset(getSubmitCharset());
    request.setAdditionalHeader("Referer", htmlPage.getUrl()
      .toExternalForm());
    return request;
  }
  





  private Charset getSubmitCharset()
  {
    String charset = getAcceptCharsetAttribute();
    if (!charset.isEmpty()) {
      charset = charset.trim();
      return EncodingSniffer.toCharset(
        SUBMIT_CHARSET_PATTERN.matcher(charset).replaceAll("").toUpperCase(Locale.ROOT));
    }
    return getPage().getCharset();
  }
  











  public List<NameValuePair> getParameterListForSubmit(SubmittableElement submitElement)
  {
    Collection<SubmittableElement> submittableElements = getSubmittableElements(submitElement);
    
    List<NameValuePair> parameterList = new ArrayList(submittableElements.size());
    int j; int i; for (Iterator localIterator = submittableElements.iterator(); localIterator.hasNext(); 
        i < j)
    {
      SubmittableElement element = (SubmittableElement)localIterator.next();
      NameValuePair[] arrayOfNameValuePair; j = (arrayOfNameValuePair = element.getSubmitNameValuePairs()).length;i = 0; continue;NameValuePair pair = arrayOfNameValuePair[i];
      parameterList.add(pair);i++;
    }
    


    return parameterList;
  }
  






  public Page reset()
  {
    SgmlPage htmlPage = getPage();
    ScriptResult scriptResult = fireEvent("reset");
    if (ScriptResult.isFalse(scriptResult)) {
      return scriptResult.getNewPage();
    }
    
    for (HtmlElement next : getHtmlElementDescendants()) {
      if ((next instanceof SubmittableElement)) {
        ((SubmittableElement)next).reset();
      }
    }
    
    return htmlPage;
  }
  







  Collection<SubmittableElement> getSubmittableElements(SubmittableElement submitElement)
  {
    List<SubmittableElement> submittableElements = new ArrayList();
    
    for (HtmlElement element : getFormHtmlElementDescendants()) {
      if (isSubmittable(element, submitElement)) {
        submittableElements.add((SubmittableElement)element);
      }
    }
    
    for (HtmlElement element : lostChildren_) {
      if (isSubmittable(element, submitElement)) {
        submittableElements.add((SubmittableElement)element);
      }
    }
    
    return submittableElements;
  }
  
  private static boolean isValidForSubmission(HtmlElement element, SubmittableElement submitElement) {
    String tagName = element.getTagName();
    if (!SUBMITTABLE_ELEMENT_NAMES.contains(tagName)) {
      return false;
    }
    if (element.hasAttribute("disabled")) {
      return false;
    }
    
    if ((element == submitElement) && ((element instanceof HtmlImageInput))) {
      return true;
    }
    
    if ((!"isindex".equals(tagName)) && (!element.hasAttribute("name"))) {
      return false;
    }
    
    if ((!"isindex".equals(tagName)) && ("".equals(element.getAttribute("name")))) {
      return false;
    }
    
    if ((element instanceof HtmlInput)) {
      String type = element.getAttribute("type").toLowerCase(Locale.ROOT);
      if (("radio".equals(type)) || ("checkbox".equals(type))) {
        return ((HtmlInput)element).isChecked();
      }
    }
    if ("select".equals(tagName)) {
      return ((HtmlSelect)element).isValidForSubmission();
    }
    return true;
  }
  








  private static boolean isSubmittable(HtmlElement element, SubmittableElement submitElement)
  {
    String tagName = element.getTagName();
    if (!isValidForSubmission(element, submitElement)) {
      return false;
    }
    

    if (element == submitElement) {
      return true;
    }
    if ((element instanceof HtmlInput)) {
      HtmlInput input = (HtmlInput)element;
      String type = input.getTypeAttribute().toLowerCase(Locale.ROOT);
      if (("submit".equals(type)) || ("image".equals(type)) || ("reset".equals(type)) || ("button".equals(type))) {
        return false;
      }
    }
    if ("button".equals(tagName)) {
      return false;
    }
    
    return true;
  }
  





  public List<HtmlInput> getInputsByName(String name)
  {
    List<HtmlInput> list = getFormElementsByAttribute("input", "name", name);
    

    for (HtmlElement elt : getLostChildren()) {
      if (((elt instanceof HtmlInput)) && (name.equals(elt.getAttribute("name")))) {
        list.add((HtmlInput)elt);
      }
    }
    return list;
  }
  








  private <E extends HtmlElement> List<E> getFormElementsByAttribute(String elementName, String attributeName, String attributeValue)
  {
    List<E> list = new ArrayList();
    String lowerCaseTagName = elementName.toLowerCase(Locale.ROOT);
    
    for (HtmlElement next : getFormHtmlElementDescendants()) {
      if (next.getTagName().equals(lowerCaseTagName)) {
        String attValue = next.getAttribute(attributeName);
        if ((attValue != null) && (attValue.equals(attributeValue))) {
          list.add(next);
        }
      }
    }
    return list;
  }
  



  private Iterable<HtmlElement> getFormHtmlElementDescendants()
  {
    final Iterator<HtmlElement> iter = new DomNode.DescendantElementsIterator(this, HtmlElement.class)
    {
      private boolean filterChildrenOfNestedForms_;
      
      protected boolean isAccepted(DomNode node) {
        if ((node instanceof HtmlForm)) {
          filterChildrenOfNestedForms_ = true;
          return false;
        }
        
        boolean accepted = super.isAccepted(node);
        if ((accepted) && (filterChildrenOfNestedForms_)) {
          return ((HtmlElement)node).getEnclosingForm() == HtmlForm.this;
        }
        return accepted;
      }
    };
    new Iterable()
    {
      public Iterator<HtmlElement> iterator() {
        return iter;
      }
    };
  }
  







  public final <I extends HtmlInput> I getInputByName(String name)
    throws ElementNotFoundException
  {
    List<HtmlInput> inputs = getInputsByName(name);
    
    if (inputs.isEmpty()) {
      throw new ElementNotFoundException("input", "name", name);
    }
    return (HtmlInput)inputs.get(0);
  }
  





  public List<HtmlSelect> getSelectsByName(String name)
  {
    List<HtmlSelect> list = getFormElementsByAttribute("select", "name", name);
    

    for (HtmlElement elt : getLostChildren()) {
      if (((elt instanceof HtmlSelect)) && (name.equals(elt.getAttribute("name")))) {
        list.add((HtmlSelect)elt);
      }
    }
    return list;
  }
  






  public HtmlSelect getSelectByName(String name)
    throws ElementNotFoundException
  {
    List<HtmlSelect> list = getSelectsByName(name);
    if (list.isEmpty()) {
      throw new ElementNotFoundException("select", "name", name);
    }
    return (HtmlSelect)list.get(0);
  }
  





  public List<HtmlButton> getButtonsByName(String name)
  {
    List<HtmlButton> list = getFormElementsByAttribute("button", "name", name);
    

    for (HtmlElement elt : getLostChildren()) {
      if (((elt instanceof HtmlButton)) && (name.equals(elt.getAttribute("name")))) {
        list.add((HtmlButton)elt);
      }
    }
    return list;
  }
  






  public HtmlButton getButtonByName(String name)
    throws ElementNotFoundException
  {
    List<HtmlButton> list = getButtonsByName(name);
    if (list.isEmpty()) {
      throw new ElementNotFoundException("button", "name", name);
    }
    return (HtmlButton)list.get(0);
  }
  





  public List<HtmlTextArea> getTextAreasByName(String name)
  {
    List<HtmlTextArea> list = getFormElementsByAttribute("textarea", "name", name);
    

    for (HtmlElement elt : getLostChildren()) {
      if (((elt instanceof HtmlTextArea)) && (name.equals(elt.getAttribute("name")))) {
        list.add((HtmlTextArea)elt);
      }
    }
    return list;
  }
  






  public HtmlTextArea getTextAreaByName(String name)
    throws ElementNotFoundException
  {
    List<HtmlTextArea> list = getTextAreasByName(name);
    if (list.isEmpty()) {
      throw new ElementNotFoundException("textarea", "name", name);
    }
    return (HtmlTextArea)list.get(0);
  }
  





  public List<HtmlRadioButtonInput> getRadioButtonsByName(String name)
  {
    WebAssert.notNull("name", name);
    
    List<HtmlRadioButtonInput> results = new ArrayList();
    
    for (HtmlElement element : getInputsByName(name)) {
      if ((element instanceof HtmlRadioButtonInput)) {
        results.add((HtmlRadioButtonInput)element);
      }
    }
    
    return results;
  }
  





  void setCheckedRadioButton(HtmlRadioButtonInput radioButtonInput)
  {
    if ((!isAncestorOf(radioButtonInput)) && (!lostChildren_.contains(radioButtonInput))) {
      throw new IllegalArgumentException("HtmlRadioButtonInput is not child of this HtmlForm");
    }
    List<HtmlRadioButtonInput> radios = getRadioButtonsByName(radioButtonInput.getNameAttribute());
    
    for (HtmlRadioButtonInput input : radios) {
      input.setCheckedInternal(input == radioButtonInput);
    }
  }
  






  public HtmlRadioButtonInput getCheckedRadioButton(String name)
  {
    WebAssert.notNull("name", name);
    
    for (HtmlRadioButtonInput input : getRadioButtonsByName(name)) {
      if (input.isChecked()) {
        return input;
      }
    }
    return null;
  }
  






  public final String getActionAttribute()
  {
    return getAttribute("action");
  }
  






  public final void setActionAttribute(String action)
  {
    setAttribute("action", action);
  }
  






  public final String getMethodAttribute()
  {
    return getAttribute("method");
  }
  






  public final void setMethodAttribute(String method)
  {
    setAttribute("method", method);
  }
  






  public final String getNameAttribute()
  {
    return getAttribute("name");
  }
  






  public final void setNameAttribute(String name)
  {
    setAttribute("name", name);
  }
  







  public final String getEnctypeAttribute()
  {
    return getAttribute("enctype");
  }
  







  public final void setEnctypeAttribute(String encoding)
  {
    setAttribute("enctype", encoding);
  }
  






  public final String getOnSubmitAttribute()
  {
    return getAttribute("onsubmit");
  }
  






  public final String getOnResetAttribute()
  {
    return getAttribute("onreset");
  }
  






  public final String getAcceptAttribute()
  {
    return getAttribute("accept");
  }
  






  public final String getAcceptCharsetAttribute()
  {
    return getAttribute("accept-charset");
  }
  






  public final String getTargetAttribute()
  {
    return getAttribute("target");
  }
  






  public final void setTargetAttribute(String target)
  {
    setAttribute("target", target);
  }
  






  public <I extends HtmlInput> I getInputByValue(String value)
    throws ElementNotFoundException
  {
    List<HtmlInput> list = getInputsByValue(value);
    if (list.isEmpty()) {
      throw new ElementNotFoundException("input", "value", value);
    }
    return (HtmlInput)list.get(0);
  }
  




  public List<HtmlInput> getInputsByValue(String value)
  {
    List<HtmlInput> results = getFormElementsByAttribute("input", "value", value);
    
    for (HtmlElement element : getLostChildren()) {
      if (((element instanceof HtmlInput)) && (value.equals(element.getAttribute("value")))) {
        results.add((HtmlInput)element);
      }
    }
    
    return results;
  }
  




  void addLostChild(HtmlElement field)
  {
    lostChildren_.add(field);
    field.setOwningForm(this);
  }
  




  public List<HtmlElement> getLostChildren()
  {
    return lostChildren_;
  }
  



  protected void preventDefault()
  {
    isPreventDefault_ = true;
  }
  



  protected boolean isEmptyXmlTagExpanded()
  {
    return true;
  }
}
