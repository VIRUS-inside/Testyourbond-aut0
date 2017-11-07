package org.openqa.selenium.remote;

public abstract interface CapabilityType
{
  public static final String BROWSER_NAME = "browserName";
  public static final String PLATFORM = "platform";
  public static final String SUPPORTS_JAVASCRIPT = "javascriptEnabled";
  public static final String TAKES_SCREENSHOT = "takesScreenshot";
  public static final String VERSION = "version";
  public static final String BROWSER_VERSION = "browserVersion";
  public static final String SUPPORTS_ALERTS = "handlesAlerts";
  public static final String SUPPORTS_SQL_DATABASE = "databaseEnabled";
  public static final String SUPPORTS_LOCATION_CONTEXT = "locationContextEnabled";
  public static final String SUPPORTS_APPLICATION_CACHE = "applicationCacheEnabled";
  public static final String SUPPORTS_NETWORK_CONNECTION = "networkConnectionEnabled";
  public static final String SUPPORTS_FINDING_BY_CSS = "cssSelectorsEnabled";
  public static final String PROXY = "proxy";
  public static final String SUPPORTS_WEB_STORAGE = "webStorageEnabled";
  public static final String ROTATABLE = "rotatable";
  public static final String APPLICATION_NAME = "applicationName";
  public static final String ACCEPT_SSL_CERTS = "acceptSslCerts";
  public static final String ACCEPT_INSECURE_CERTS = "acceptInsecureCerts";
  public static final String HAS_NATIVE_EVENTS = "nativeEvents";
  public static final String UNEXPECTED_ALERT_BEHAVIOUR = "unexpectedAlertBehaviour";
  public static final String UNHANDLED_PROMPT_BEHAVIOUR = "unhandledPromptBehavior";
  public static final String ELEMENT_SCROLL_BEHAVIOR = "elementScrollBehavior";
  public static final String HAS_TOUCHSCREEN = "hasTouchScreen";
  public static final String OVERLAPPING_CHECK_DISABLED = "overlappingCheckDisabled";
  public static final String LOGGING_PREFS = "loggingPrefs";
  public static final String ENABLE_PROFILING_CAPABILITY = "webdriver.logging.profiler.enabled";
  @Deprecated
  public static final String PAGE_LOADING_STRATEGY = "pageLoadingStrategy";
  public static final String PAGE_LOAD_STRATEGY = "pageLoadStrategy";
  @Deprecated
  public static final String ENABLE_PERSISTENT_HOVERING = "enablePersistentHover";
  
  public static abstract interface ForSeleniumServer
  {
    public static final String AVOIDING_PROXY = "avoidProxy";
    public static final String ONLY_PROXYING_SELENIUM_TRAFFIC = "onlyProxySeleniumTraffic";
    public static final String PROXYING_EVERYTHING = "proxyEverything";
    public static final String PROXY_PAC = "proxy_pac";
    public static final String ENSURING_CLEAN_SESSION = "ensureCleanSession";
  }
}
