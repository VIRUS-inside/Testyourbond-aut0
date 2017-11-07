package org.openqa.selenium.firefox;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Map;
import org.openqa.selenium.Beta;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.internal.ClasspathExtension;
import org.openqa.selenium.firefox.internal.Extension;
import org.openqa.selenium.firefox.internal.FileExtension;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.io.TemporaryFilesystem;
import org.openqa.selenium.io.Zip;























public class FirefoxProfile
{
  public static final String PORT_PREFERENCE = "webdriver_firefox_port";
  public static final String ALLOWED_HOSTS_PREFERENCE = "webdriver_firefox_allowed_hosts";
  private static final String defaultPrefs = "/org/openqa/selenium/firefox/webdriver_prefs.json";
  private Preferences additionalPrefs;
  private Map<String, Extension> extensions = Maps.newHashMap();
  private boolean loadNoFocusLib;
  private boolean acceptUntrustedCerts;
  private boolean untrustedCertIssuer;
  private File model;
  private static final String ACCEPT_UNTRUSTED_CERTS_PREF = "webdriver_accept_untrusted_certs";
  private static final String ASSUME_UNTRUSTED_ISSUER_PREF = "webdriver_assume_untrusted_issuer";
  
  public FirefoxProfile() {
    this(null);
  }
  






  public FirefoxProfile(File profileDir)
  {
    this(null, profileDir);
  }
  
  @Beta
  @VisibleForTesting
  protected FirefoxProfile(Reader defaultsReader, File profileDir) {
    if (defaultsReader == null) {
      defaultsReader = onlyOverrideThisIfYouKnowWhatYouAreDoing();
    }
    
    additionalPrefs = new Preferences(defaultsReader);
    
    model = profileDir;
    verifyModel(model);
    
    File prefsInModel = new File(model, "user.js");
    if (prefsInModel.exists()) {
      StringReader reader = new StringReader("{\"frozen\": {}, \"mutable\": {}}");
      Preferences existingPrefs = new Preferences(reader, prefsInModel);
      acceptUntrustedCerts = getBooleanPreference(existingPrefs, "webdriver_accept_untrusted_certs", true);
      untrustedCertIssuer = getBooleanPreference(existingPrefs, "webdriver_assume_untrusted_issuer", true);
      existingPrefs.addTo(additionalPrefs);
    } else {
      acceptUntrustedCerts = true;
      untrustedCertIssuer = true;
    }
    


    loadNoFocusLib = false;
    try
    {
      defaultsReader.close();
    } catch (IOException e) {
      throw new WebDriverException(e);
    }
  }
  




  @Beta
  protected Reader onlyOverrideThisIfYouKnowWhatYouAreDoing()
  {
    URL resource = Resources.getResource(FirefoxProfile.class, "/org/openqa/selenium/firefox/webdriver_prefs.json");
    try {
      return new InputStreamReader(resource.openStream());
    } catch (IOException e) {
      throw new WebDriverException(e);
    }
  }
  
  private boolean getBooleanPreference(Preferences prefs, String key, boolean defaultValue) {
    Object value = prefs.getPreference(key);
    if (value == null) {
      return defaultValue;
    }
    
    if ((value instanceof Boolean)) {
      return ((Boolean)value).booleanValue();
    }
    
    throw new WebDriverException("Expected boolean value is not a boolean. It is: " + value);
  }
  
  public String getStringPreference(String key, String defaultValue) {
    Object preference = additionalPrefs.getPreference(key);
    if ((preference != null) && ((preference instanceof String))) {
      return (String)preference;
    }
    return defaultValue;
  }
  
  public int getIntegerPreference(String key, int defaultValue) {
    Object preference = additionalPrefs.getPreference(key);
    if ((preference != null) && ((preference instanceof Integer))) {
      return ((Integer)preference).intValue();
    }
    return defaultValue;
  }
  
  public boolean getBooleanPreference(String key, boolean defaultValue) {
    Object preference = additionalPrefs.getPreference(key);
    if ((preference != null) && ((preference instanceof Boolean))) {
      return ((Boolean)preference).booleanValue();
    }
    return defaultValue;
  }
  
  private void verifyModel(File model) {
    if (model == null) {
      return;
    }
    
    if (!model.exists())
    {
      throw new UnableToCreateProfileException("Given model profile directory does not exist: " + model.getPath());
    }
    
    if (!model.isDirectory())
    {
      throw new UnableToCreateProfileException("Given model profile directory is not a directory: " + model.getAbsolutePath());
    }
  }
  
  public boolean containsWebDriverExtension() {
    return extensions.containsKey("webdriver");
  }
  
  public void addExtension(Class<?> loadResourcesUsing, String loadFrom)
  {
    File file = new File(loadFrom);
    if (file.exists()) {
      addExtension(file);
      return;
    }
    
    addExtension(loadFrom, new ClasspathExtension(loadResourcesUsing, loadFrom));
  }
  




  public void addExtension(File extensionToInstall)
  {
    addExtension(extensionToInstall.getName(), new FileExtension(extensionToInstall));
  }
  
  public void addExtension(String key, Extension extension) {
    String name = deriveExtensionName(key);
    extensions.put(name, extension);
  }
  
  private String deriveExtensionName(String originalName) {
    String[] pieces = originalName.replace('\\', '/').split("/");
    
    String name = pieces[(pieces.length - 1)];
    name = name.replaceAll("\\..*?$", "");
    return name;
  }
  








  public void setPreference(String key, String value)
  {
    additionalPrefs.setPreference(key, value);
  }
  





  public void setPreference(String key, boolean value)
  {
    additionalPrefs.setPreference(key, value);
  }
  





  public void setPreference(String key, int value)
  {
    additionalPrefs.setPreference(key, value);
  }
  
  protected Preferences getAdditionalPreferences() {
    return additionalPrefs;
  }
  
  public void updateUserPrefs(File userPrefs) {
    Preferences prefs = new Preferences(onlyOverrideThisIfYouKnowWhatYouAreDoing());
    

    prefs.setPreference("browser.startup.homepage", "about:blank");
    

    prefs.setPreference("browser.startup.page", 0);
    
    if (userPrefs.exists()) {
      prefs = new Preferences(onlyOverrideThisIfYouKnowWhatYouAreDoing(), userPrefs);
      if (!userPrefs.delete()) {
        throw new WebDriverException("Cannot delete existing user preferences");
      }
    }
    
    additionalPrefs.addTo(prefs);
    

    prefs.setPreference("webdriver_accept_untrusted_certs", acceptUntrustedCerts);
    
    prefs.setPreference("webdriver_assume_untrusted_issuer", untrustedCertIssuer);
    

    Object homePage = prefs.getPreference("browser.startup.homepage");
    if ((homePage != null) && ((homePage instanceof String))) {
      prefs.setPreference("startup.homepage_welcome_url", "");
    }
    
    if (!"about:blank".equals(prefs.getPreference("browser.startup.homepage"))) {
      prefs.setPreference("browser.startup.page", 1);
    }
    try {
      FileWriter writer = new FileWriter(userPrefs);Throwable localThrowable3 = null;
      try { prefs.writeTo(writer);
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;
      } finally {
        if (writer != null) if (localThrowable3 != null) try { writer.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else writer.close();
      } } catch (IOException e) { throw new WebDriverException(e);
    }
  }
  
  protected void deleteLockFiles(File profileDir) {
    File macAndLinuxLockFile = new File(profileDir, ".parentlock");
    File windowsLockFile = new File(profileDir, "parent.lock");
    
    macAndLinuxLockFile.delete();
    windowsLockFile.delete();
  }
  
  public void deleteExtensionsCacheIfItExists(File profileDir) {
    File cacheFile = new File(profileDir, "extensions.cache");
    if (cacheFile.exists()) {
      cacheFile.delete();
    }
  }
  


  @Deprecated
  public boolean areNativeEventsEnabled()
  {
    return false;
  }
  





  @Deprecated
  public void setEnableNativeEvents(boolean enableNativeEvents) {}
  




  public boolean shouldLoadNoFocusLib()
  {
    return loadNoFocusLib;
  }
  




  public void setAlwaysLoadNoFocusLib(boolean loadNoFocusLib)
  {
    this.loadNoFocusLib = loadNoFocusLib;
  }
  






  public void setAcceptUntrustedCertificates(boolean acceptUntrustedSsl)
  {
    acceptUntrustedCerts = acceptUntrustedSsl;
  }
  













  public void setAssumeUntrustedCertificateIssuer(boolean untrustedIssuer)
  {
    untrustedCertIssuer = untrustedIssuer;
  }
  
  public void clean(File profileDir) {
    TemporaryFilesystem.getDefaultTmpFS().deleteTempDir(profileDir);
  }
  
  public String toJson() throws IOException {
    return Zip.zip(layoutOnDisk());
  }
  
  public static FirefoxProfile fromJson(String json) throws IOException {
    return new FirefoxProfile(Zip.unzipToTempDir(json, "webdriver", "duplicated"));
  }
  
  protected void cleanTemporaryModel() {
    clean(model);
  }
  









  public File layoutOnDisk()
  {
    try
    {
      File profileDir = TemporaryFilesystem.getDefaultTmpFS().createTempDir("anonymous", "webdriver-profile");
      File userPrefs = new File(profileDir, "user.js");
      
      copyModel(model, profileDir);
      installExtensions(profileDir);
      deleteLockFiles(profileDir);
      deleteExtensionsCacheIfItExists(profileDir);
      updateUserPrefs(userPrefs);
      return profileDir;
    } catch (IOException e) {
      throw new UnableToCreateProfileException(e);
    }
  }
  
  protected void copyModel(File sourceDir, File profileDir) throws IOException {
    if ((sourceDir == null) || (!sourceDir.exists())) {
      return;
    }
    
    FileHandler.copy(sourceDir, profileDir);
  }
  
  protected void installExtensions(File parentDir) throws IOException {
    File extensionsDir = new File(parentDir, "extensions");
    
    for (Extension extension : extensions.values()) {
      extension.writeTo(extensionsDir);
    }
  }
}
