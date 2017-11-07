package com.gargoylesoftware.htmlunit.util;

import com.gargoylesoftware.htmlunit.WebAssert;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;






























public final class UrlUtils
{
  private static final BitSet PATH_ALLOWED_CHARS = new BitSet(256);
  private static final BitSet QUERY_ALLOWED_CHARS = new BitSet(256);
  private static final BitSet ANCHOR_ALLOWED_CHARS = new BitSet(256);
  private static final BitSet HASH_ALLOWED_CHARS = new BitSet(256);
  private static final URLCreator URL_CREATOR = URLCreator.getCreator();
  


  static
  {
    BitSet reserved = new BitSet(256);
    reserved.set(59);
    reserved.set(47);
    reserved.set(63);
    reserved.set(58);
    reserved.set(64);
    reserved.set(38);
    reserved.set(61);
    reserved.set(43);
    reserved.set(36);
    reserved.set(44);
    
    BitSet mark = new BitSet(256);
    mark.set(45);
    mark.set(95);
    mark.set(46);
    mark.set(33);
    mark.set(126);
    mark.set(42);
    mark.set(39);
    mark.set(40);
    mark.set(41);
    
    BitSet alpha = new BitSet(256);
    for (int i = 97; i <= 122; i++) {
      alpha.set(i);
    }
    for (int i = 65; i <= 90; i++) {
      alpha.set(i);
    }
    
    BitSet digit = new BitSet(256);
    for (int i = 48; i <= 57; i++) {
      digit.set(i);
    }
    
    BitSet alphanumeric = new BitSet(256);
    alphanumeric.or(alpha);
    alphanumeric.or(digit);
    
    BitSet unreserved = new BitSet(256);
    unreserved.or(alphanumeric);
    unreserved.or(mark);
    
    BitSet hex = new BitSet(256);
    hex.or(digit);
    for (int i = 97; i <= 102; i++) {
      hex.set(i);
    }
    for (int i = 65; i <= 70; i++) {
      hex.set(i);
    }
    
    BitSet escaped = new BitSet(256);
    escaped.set(37);
    escaped.or(hex);
    
    BitSet uric = new BitSet(256);
    uric.or(reserved);
    uric.or(unreserved);
    uric.or(escaped);
    
    BitSet pchar = new BitSet(256);
    pchar.or(unreserved);
    pchar.or(escaped);
    pchar.set(58);
    pchar.set(64);
    pchar.set(38);
    pchar.set(61);
    pchar.set(43);
    pchar.set(36);
    pchar.set(44);
    
    BitSet param = pchar;
    
    BitSet segment = new BitSet(256);
    segment.or(pchar);
    segment.set(59);
    segment.or(param);
    
    BitSet pathSegments = new BitSet(256);
    pathSegments.set(47);
    pathSegments.or(segment);
    
    BitSet absPath = new BitSet(256);
    absPath.set(47);
    absPath.or(pathSegments);
    
    BitSet allowedAbsPath = new BitSet(256);
    allowedAbsPath.or(absPath);
    
    BitSet allowedFragment = new BitSet(256);
    allowedFragment.or(uric);
    

    BitSet allowedQuery = new BitSet(256);
    allowedQuery.or(uric);
    
    BitSet allowedHash = new BitSet(256);
    allowedHash.or(uric);
    allowedHash.clear(37);
    
    PATH_ALLOWED_CHARS.or(allowedAbsPath);
    QUERY_ALLOWED_CHARS.or(allowedQuery);
    ANCHOR_ALLOWED_CHARS.or(allowedFragment);
    HASH_ALLOWED_CHARS.or(allowedHash);
  }
  







  private UrlUtils() {}
  







  public static URL toUrlSafe(String url)
  {
    try
    {
      return toUrlUnsafe(url);
    }
    catch (MalformedURLException e)
    {
      throw new RuntimeException(e);
    }
  }
  










  public static URL toUrlUnsafe(String url)
    throws MalformedURLException
  {
    WebAssert.notNull("url", url);
    return URL_CREATOR.toUrlUnsafeClassic(url);
  }
  










  public static URL encodeUrl(URL url, boolean minimalQueryEncoding, Charset charset)
  {
    if (!isNormalUrlProtocol(URL_CREATOR.getProtocol(url))) {
      return url;
    }
    try
    {
      String path = url.getPath();
      if (path != null) {
        path = encode(path, PATH_ALLOWED_CHARS, StandardCharsets.UTF_8);
      }
      String query = url.getQuery();
      if (query != null) {
        if (minimalQueryEncoding) {
          query = org.apache.commons.lang3.StringUtils.replace(query, " ", "%20");
        }
        else {
          query = encode(query, QUERY_ALLOWED_CHARS, charset);
        }
      }
      String anchor = url.getRef();
      if (anchor != null) {
        anchor = encode(anchor, ANCHOR_ALLOWED_CHARS, StandardCharsets.UTF_8);
      }
      return createNewUrl(url.getProtocol(), url.getUserInfo(), url.getHost(), 
        url.getPort(), path, anchor, query);
    }
    catch (MalformedURLException e)
    {
      throw new RuntimeException(e);
    }
  }
  





  public static String encodeAnchor(String anchor)
  {
    if (anchor != null) {
      anchor = encode(anchor, ANCHOR_ALLOWED_CHARS, StandardCharsets.UTF_8);
    }
    return anchor;
  }
  





  public static String encodeHash(String hash)
  {
    if (hash != null) {
      hash = encode(hash, HASH_ALLOWED_CHARS, StandardCharsets.UTF_8);
    }
    return hash;
  }
  




  public static String decode(String escaped)
  {
    try
    {
      byte[] bytes = escaped.getBytes(StandardCharsets.US_ASCII);
      byte[] bytes2 = URLCodec.decodeUrl(bytes);
      return new String(bytes2, StandardCharsets.UTF_8);
    }
    catch (DecoderException e)
    {
      throw new RuntimeException(e);
    }
  }
  







  private static String encode(String unescaped, BitSet allowed, Charset charset)
  {
    byte[] bytes = unescaped.getBytes(charset);
    byte[] bytes2 = URLCodec.encodeUrl(allowed, bytes);
    return encodePercentSign(bytes2);
  }
  






  private static String encodePercentSign(byte[] input)
  {
    if (input == null) {
      return null;
    }
    
    StringBuilder result = new StringBuilder(new String(input, StandardCharsets.US_ASCII));
    int state = 0;
    int offset = 0;
    for (int i = 0; i < input.length; i++) {
      byte b = input[i];
      if (b == 37) {
        state = 1;
      }
      else if ((state == 1) || (state == 2)) {
        if (((48 <= b) && (b <= 57)) || 
          ((65 <= b) && (b <= 70)) || (
          (97 <= b) && (b <= 102))) {
          state++;
          if (state == 3) {
            state = 0;
          }
        }
        else {
          int st = i - state + offset;
          result.replace(st, st + 1, "%25");
          offset += 2;
          state = 0;
        }
      }
    }
    if ((state == 1) || (state == 2)) {
      int st = input.length - state + offset;
      result.replace(st, st + 1, "%25");
    }
    return result.toString();
  }
  





  public static URL getUrlWithNewProtocol(URL u, String newProtocol)
    throws MalformedURLException
  {
    return createNewUrl(newProtocol, u.getAuthority(), u.getPath(), u.getRef(), u.getQuery());
  }
  






  public static URL getUrlWithNewHost(URL u, String newHost)
    throws MalformedURLException
  {
    return createNewUrl(u.getProtocol(), u.getUserInfo(), newHost, 
      u.getPort(), u.getPath(), u.getRef(), u.getQuery());
  }
  







  public static URL getUrlWithNewHostAndPort(URL u, String newHost, int newPort)
    throws MalformedURLException
  {
    return createNewUrl(u.getProtocol(), u.getUserInfo(), newHost, newPort, u.getPath(), u.getRef(), u.getQuery());
  }
  





  public static URL getUrlWithNewPort(URL u, int newPort)
    throws MalformedURLException
  {
    return createNewUrl(u.getProtocol(), u.getUserInfo(), u.getHost(), 
      newPort, u.getPath(), u.getRef(), u.getQuery());
  }
  





  public static URL getUrlWithNewPath(URL u, String newPath)
    throws MalformedURLException
  {
    return createNewUrl(u.getProtocol(), u.getAuthority(), newPath, u.getRef(), u.getQuery());
  }
  





  public static URL getUrlWithNewRef(URL u, String newRef)
    throws MalformedURLException
  {
    return createNewUrl(u.getProtocol(), u.getAuthority(), u.getPath(), newRef, u.getQuery());
  }
  





  public static URL getUrlWithNewQuery(URL u, String newQuery)
    throws MalformedURLException
  {
    return createNewUrl(u.getProtocol(), u.getAuthority(), u.getPath(), u.getRef(), newQuery);
  }
  











  private static URL createNewUrl(String protocol, String userInfo, String host, int port, String path, String ref, String query)
    throws MalformedURLException
  {
    StringBuilder s = new StringBuilder();
    s.append(protocol);
    s.append("://");
    if (userInfo != null) {
      s.append(userInfo).append("@");
    }
    s.append(host);
    if (port != -1) {
      s.append(":").append(port);
    }
    if ((path != null) && (!path.isEmpty())) {
      if ('/' != path.charAt(0)) {
        s.append("/");
      }
      s.append(path);
    }
    if (query != null) {
      s.append("?").append(query);
    }
    if (ref != null) {
      if ((ref.isEmpty()) || (ref.charAt(0) != '#')) {
        s.append("#");
      }
      s.append(ref);
    }
    
    URL url = new URL(s.toString());
    return url;
  }
  











  private static URL createNewUrl(String protocol, String authority, String path, String ref, String query)
    throws MalformedURLException
  {
    int len = protocol.length() + 1;
    if ((authority != null) && (!authority.isEmpty())) {
      len += 2 + authority.length();
    }
    if (path != null) {
      len += path.length();
    }
    if (query != null) {
      len += 1 + query.length();
    }
    if (ref != null) {
      len += 1 + ref.length();
    }
    
    StringBuilder s = new StringBuilder(len);
    s.append(protocol);
    s.append(":");
    if ((authority != null) && (!authority.isEmpty())) {
      s.append("//");
      s.append(authority);
    }
    if (path != null) {
      s.append(path);
    }
    if (query != null) {
      s.append('?');
      s.append(query);
    }
    if (ref != null) {
      if ((ref.isEmpty()) || (ref.charAt(0) != '#')) {
        s.append("#");
      }
      s.append(ref);
    }
    
    return new URL(s.toString());
  }
  








  public static String resolveUrl(String baseUrl, String relativeUrl)
  {
    if (baseUrl == null) {
      throw new IllegalArgumentException("Base URL must not be null");
    }
    if (relativeUrl == null) {
      throw new IllegalArgumentException("Relative URL must not be null");
    }
    Url url = resolveUrl(parseUrl(baseUrl.trim()), relativeUrl.trim());
    
    return url.toString();
  }
  








  public static String resolveUrl(URL baseUrl, String relativeUrl)
  {
    if (baseUrl == null) {
      throw new IllegalArgumentException("Base URL must not be null");
    }
    return resolveUrl(baseUrl.toExternalForm(), relativeUrl);
  }
  

















  private static Url parseUrl(String spec)
  {
    Url url = new Url();
    int startIndex = 0;
    int endIndex = spec.length();
    














    int crosshatchIndex = StringUtils.indexOf(spec, '#', startIndex, endIndex);
    
    if (crosshatchIndex >= 0) {
      fragment_ = spec.substring(crosshatchIndex + 1, endIndex);
      endIndex = crosshatchIndex;
    }
    







    int colonIndex = StringUtils.indexOf(spec, ':', startIndex, endIndex);
    
    if (colonIndex > 0) {
      String scheme = spec.substring(startIndex, colonIndex);
      if (isValidScheme(scheme)) {
        scheme_ = scheme;
        startIndex = colonIndex + 1;
      }
    }
    




    int locationStartIndex;
    



    int locationEndIndex;
    



    if (spec.startsWith("//", startIndex)) {
      int locationStartIndex = startIndex + 2;
      int locationEndIndex = StringUtils.indexOf(spec, '/', locationStartIndex, endIndex);
      if (locationEndIndex >= 0) {
        startIndex = locationEndIndex;
      }
    }
    else {
      locationStartIndex = -1;
      locationEndIndex = -1;
    }
    








    int questionMarkIndex = StringUtils.indexOf(spec, '?', startIndex, endIndex);
    
    if (questionMarkIndex >= 0) {
      if ((locationStartIndex >= 0) && (locationEndIndex < 0))
      {


        locationEndIndex = questionMarkIndex;
        startIndex = questionMarkIndex;
      }
      query_ = spec.substring(questionMarkIndex + 1, endIndex);
      endIndex = questionMarkIndex;
    }
    







    int semicolonIndex = StringUtils.indexOf(spec, ';', startIndex, endIndex);
    
    if (semicolonIndex >= 0) {
      if ((locationStartIndex >= 0) && (locationEndIndex < 0))
      {


        locationEndIndex = semicolonIndex;
        startIndex = semicolonIndex;
      }
      parameters_ = spec.substring(semicolonIndex + 1, endIndex);
      endIndex = semicolonIndex;
    }
    







    if ((locationStartIndex >= 0) && (locationEndIndex < 0))
    {

      locationEndIndex = endIndex;
    }
    else if (startIndex < endIndex) {
      path_ = spec.substring(startIndex, endIndex);
    }
    
    if ((locationStartIndex >= 0) && (locationEndIndex >= 0)) {
      location_ = spec.substring(locationStartIndex, locationEndIndex);
    }
    return url;
  }
  


  private static boolean isValidScheme(String scheme)
  {
    int length = scheme.length();
    if (length < 1) {
      return false;
    }
    char c = scheme.charAt(0);
    if (!Character.isLetter(c)) {
      return false;
    }
    for (int i = 1; i < length; i++) {
      c = scheme.charAt(i);
      if ((!Character.isLetterOrDigit(c)) && (c != '.') && (c != '+') && (c != '-')) {
        return false;
      }
    }
    return true;
  }
  

















  private static Url resolveUrl(Url baseUrl, String relativeUrl)
  {
    Url url = parseUrl(relativeUrl);
    



    if (baseUrl == null) {
      return url;
    }
    




    if (relativeUrl.isEmpty()) {
      return new Url(baseUrl);
    }
    

    if (scheme_ != null) {
      return url;
    }
    

    scheme_ = scheme_;
    


    if (location_ != null) {
      return url;
    }
    location_ = location_;
    

    if ((path_ != null) && (!path_.isEmpty()) && (path_.charAt(0) == '/')) {
      path_ = removeLeadingSlashPoints(path_);
      return url;
    }
    


    if (path_ == null) {
      path_ = path_;
      


      if (parameters_ != null) {
        return url;
      }
      parameters_ = parameters_;
      


      if (query_ != null) {
        return url;
      }
      query_ = query_;
      return url;
    }
    




    String basePath = path_;
    String path = "";
    
    if (basePath != null) {
      int lastSlashIndex = basePath.lastIndexOf('/');
      
      if (lastSlashIndex >= 0) {
        path = basePath.substring(0, lastSlashIndex + 1);
      }
    }
    else {
      path = "/";
    }
    path = path.concat(path_);
    

    int pathSegmentIndex;
    
    while ((pathSegmentIndex = path.indexOf("/./")) >= 0) { int pathSegmentIndex;
      path = path.substring(0, pathSegmentIndex + 1).concat(path.substring(pathSegmentIndex + 3));
    }
    

    if (path.endsWith("/.")) {
      path = path.substring(0, path.length() - 1);
    }
    




    while ((pathSegmentIndex = path.indexOf("/../")) > 0) {
      String pathSegment = path.substring(0, pathSegmentIndex);
      int slashIndex = pathSegment.lastIndexOf('/');
      
      if (slashIndex >= 0) {
        if (!"..".equals(pathSegment.substring(slashIndex))) {
          path = path.substring(0, slashIndex + 1).concat(path.substring(pathSegmentIndex + 4));
        }
      }
      else {
        path = path.substring(pathSegmentIndex + 4);
      }
    }
    


    if (path.endsWith("/..")) {
      String pathSegment = path.substring(0, path.length() - 3);
      int slashIndex = pathSegment.lastIndexOf('/');
      
      if (slashIndex >= 0) {
        path = path.substring(0, slashIndex + 1);
      }
    }
    
    path = removeLeadingSlashPoints(path);
    
    path_ = path;
    


    return url;
  }
  


  private static String removeLeadingSlashPoints(String path)
  {
    while (path.startsWith("/..")) {
      path = path.substring(3);
    }
    
    return path;
  }
  


  private static class Url
  {
    private String scheme_;
    

    private String location_;
    

    private String path_;
    

    private String parameters_;
    

    private String query_;
    

    private String fragment_;
    

    Url() {}
    

    Url(Url url)
    {
      scheme_ = scheme_;
      location_ = location_;
      path_ = path_;
      parameters_ = parameters_;
      query_ = query_;
      fragment_ = fragment_;
    }
    





    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      
      if (scheme_ != null) {
        sb.append(scheme_);
        sb.append(':');
      }
      if (location_ != null) {
        sb.append("//");
        sb.append(location_);
      }
      if (path_ != null) {
        sb.append(path_);
      }
      if (parameters_ != null) {
        sb.append(';');
        sb.append(parameters_);
      }
      if (query_ != null) {
        sb.append('?');
        sb.append(query_);
      }
      if (fragment_ != null) {
        sb.append('#');
        sb.append(fragment_);
      }
      return sb.toString();
    }
  }
  
  static boolean isNormalUrlProtocol(String protocol) {
    if (("http".equals(protocol)) || ("https".equals(protocol)) || ("file".equals(protocol))) {
      return true;
    }
    return false;
  }
  








  public static boolean sameFile(URL u1, URL u2)
  {
    if (u1 == u2) {
      return true;
    }
    if ((u1 == null) || (u2 == null)) {
      return false;
    }
    

    String p1 = u1.getProtocol();
    String p2 = u2.getProtocol();
    if ((p1 != p2) && ((p1 == null) || (!p1.equalsIgnoreCase(p2)))) {
      return false;
    }
    

    int port1 = u1.getPort() != -1 ? u1.getPort() : u1.getDefaultPort();
    int port2 = u2.getPort() != -1 ? u2.getPort() : u2.getDefaultPort();
    if (port1 != port2) {
      return false;
    }
    

    String h1 = u1.getHost();
    String h2 = u2.getHost();
    if ((h1 != h2) && ((h1 == null) || (!h1.equalsIgnoreCase(h2)))) {
      return false;
    }
    

    String f1 = u1.getFile();
    if (f1.isEmpty()) {
      f1 = "/";
    }
    String f2 = u2.getFile();
    if (f2.isEmpty()) {
      f2 = "/";
    }
    if ((f1.indexOf('.') > 0) || (f2.indexOf('.') > 0)) {
      try {
        f1 = u1.toURI().normalize().toURL().getFile();
        f2 = u2.toURI().normalize().toURL().getFile();
      }
      catch (Exception localException) {}
    }
    

    if ((f1 != f2) && ((f1 == null) || (!f1.equals(f2)))) {
      return false;
    }
    
    return true;
  }
  






  public static String normalize(URL url)
  {
    StringBuilder result = new StringBuilder();
    
    result.append(url.getProtocol());
    result.append("://");
    result.append(url.getHost());
    result.append(':');
    result.append(url.getPort() != -1 ? url.getPort() : url.getDefaultPort());
    

    String f = url.getFile();
    if (f.isEmpty()) {
      result.append("/");
    }
    else {
      if (f.indexOf('.') > 0) {
        try {
          f = url.toURI().normalize().toURL().getFile();
        }
        catch (Exception localException) {}
      }
      

      result.append(f);
    }
    
    return result.toString();
  }
  












  public static URI toURI(URL url, String query)
    throws URISyntaxException
  {
    String scheme = url.getProtocol();
    String host = url.getHost();
    int port = url.getPort();
    String path = url.getPath();
    StringBuilder buffer = new StringBuilder();
    if (host != null) {
      if (scheme != null) {
        buffer.append(scheme);
        buffer.append("://");
      }
      buffer.append(host);
      if (port > 0) {
        buffer.append(':');
        buffer.append(port);
      }
    }
    if ((path == null) || (!path.startsWith("/"))) {
      buffer.append('/');
    }
    if (path != null) {
      buffer.append(path);
    }
    if (query != null) {
      buffer.append('?');
      buffer.append(query);
    }
    return new URI(buffer.toString());
  }
}
