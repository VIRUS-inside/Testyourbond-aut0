package org.openqa.selenium.remote.server;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandCodec;
import org.openqa.selenium.remote.ErrorCodes;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.ResponseCodec;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.JsonHttpCommandCodec;
import org.openqa.selenium.remote.http.JsonHttpResponseCodec;
import org.openqa.selenium.remote.http.W3CHttpCommandCodec;
import org.openqa.selenium.remote.server.handler.AcceptAlert;
import org.openqa.selenium.remote.server.handler.AddConfig;
import org.openqa.selenium.remote.server.handler.AddCookie;
import org.openqa.selenium.remote.server.handler.CaptureScreenshot;
import org.openqa.selenium.remote.server.handler.ChangeUrl;
import org.openqa.selenium.remote.server.handler.ClearElement;
import org.openqa.selenium.remote.server.handler.ClickElement;
import org.openqa.selenium.remote.server.handler.CloseWindow;
import org.openqa.selenium.remote.server.handler.ConfigureTimeout;
import org.openqa.selenium.remote.server.handler.DeleteCookie;
import org.openqa.selenium.remote.server.handler.DeleteNamedCookie;
import org.openqa.selenium.remote.server.handler.DeleteSession;
import org.openqa.selenium.remote.server.handler.DismissAlert;
import org.openqa.selenium.remote.server.handler.ElementEquality;
import org.openqa.selenium.remote.server.handler.ExecuteAsyncScript;
import org.openqa.selenium.remote.server.handler.ExecuteScript;
import org.openqa.selenium.remote.server.handler.FindActiveElement;
import org.openqa.selenium.remote.server.handler.FindChildElement;
import org.openqa.selenium.remote.server.handler.FindChildElements;
import org.openqa.selenium.remote.server.handler.FindElement;
import org.openqa.selenium.remote.server.handler.FindElements;
import org.openqa.selenium.remote.server.handler.FullscreenWindow;
import org.openqa.selenium.remote.server.handler.GetAlertText;
import org.openqa.selenium.remote.server.handler.GetAllCookies;
import org.openqa.selenium.remote.server.handler.GetAllSessions;
import org.openqa.selenium.remote.server.handler.GetAllWindowHandles;
import org.openqa.selenium.remote.server.handler.GetAvailableLogTypesHandler;
import org.openqa.selenium.remote.server.handler.GetCookie;
import org.openqa.selenium.remote.server.handler.GetCssProperty;
import org.openqa.selenium.remote.server.handler.GetCurrentUrl;
import org.openqa.selenium.remote.server.handler.GetCurrentWindowHandle;
import org.openqa.selenium.remote.server.handler.GetElementAttribute;
import org.openqa.selenium.remote.server.handler.GetElementDisplayed;
import org.openqa.selenium.remote.server.handler.GetElementEnabled;
import org.openqa.selenium.remote.server.handler.GetElementLocation;
import org.openqa.selenium.remote.server.handler.GetElementLocationInView;
import org.openqa.selenium.remote.server.handler.GetElementRect;
import org.openqa.selenium.remote.server.handler.GetElementSelected;
import org.openqa.selenium.remote.server.handler.GetElementSize;
import org.openqa.selenium.remote.server.handler.GetElementText;
import org.openqa.selenium.remote.server.handler.GetLogHandler;
import org.openqa.selenium.remote.server.handler.GetPageSource;
import org.openqa.selenium.remote.server.handler.GetScreenOrientation;
import org.openqa.selenium.remote.server.handler.GetSessionCapabilities;
import org.openqa.selenium.remote.server.handler.GetSessionLogsHandler;
import org.openqa.selenium.remote.server.handler.GetTagName;
import org.openqa.selenium.remote.server.handler.GetTitle;
import org.openqa.selenium.remote.server.handler.GetWindowPosition;
import org.openqa.selenium.remote.server.handler.GetWindowSize;
import org.openqa.selenium.remote.server.handler.GoBack;
import org.openqa.selenium.remote.server.handler.GoForward;
import org.openqa.selenium.remote.server.handler.ImeActivateEngine;
import org.openqa.selenium.remote.server.handler.ImeDeactivate;
import org.openqa.selenium.remote.server.handler.ImeGetActiveEngine;
import org.openqa.selenium.remote.server.handler.ImeGetAvailableEngines;
import org.openqa.selenium.remote.server.handler.ImeIsActivated;
import org.openqa.selenium.remote.server.handler.ImplicitlyWait;
import org.openqa.selenium.remote.server.handler.MaximizeWindow;
import org.openqa.selenium.remote.server.handler.NewSession;
import org.openqa.selenium.remote.server.handler.RefreshPage;
import org.openqa.selenium.remote.server.handler.Rotate;
import org.openqa.selenium.remote.server.handler.SendKeys;
import org.openqa.selenium.remote.server.handler.SetAlertCredentials;
import org.openqa.selenium.remote.server.handler.SetAlertText;
import org.openqa.selenium.remote.server.handler.SetScriptTimeout;
import org.openqa.selenium.remote.server.handler.SetWindowPosition;
import org.openqa.selenium.remote.server.handler.SetWindowSize;
import org.openqa.selenium.remote.server.handler.Status;
import org.openqa.selenium.remote.server.handler.SubmitElement;
import org.openqa.selenium.remote.server.handler.SwitchToFrame;
import org.openqa.selenium.remote.server.handler.SwitchToParentFrame;
import org.openqa.selenium.remote.server.handler.SwitchToWindow;
import org.openqa.selenium.remote.server.handler.UploadFile;
import org.openqa.selenium.remote.server.handler.W3CActions;
import org.openqa.selenium.remote.server.handler.html5.ClearLocalStorage;
import org.openqa.selenium.remote.server.handler.html5.ClearSessionStorage;
import org.openqa.selenium.remote.server.handler.html5.GetAppCacheStatus;
import org.openqa.selenium.remote.server.handler.html5.GetLocalStorageItem;
import org.openqa.selenium.remote.server.handler.html5.GetLocalStorageKeys;
import org.openqa.selenium.remote.server.handler.html5.GetLocalStorageSize;
import org.openqa.selenium.remote.server.handler.html5.GetLocationContext;
import org.openqa.selenium.remote.server.handler.html5.GetSessionStorageItem;
import org.openqa.selenium.remote.server.handler.html5.GetSessionStorageKeys;
import org.openqa.selenium.remote.server.handler.html5.GetSessionStorageSize;
import org.openqa.selenium.remote.server.handler.html5.RemoveLocalStorageItem;
import org.openqa.selenium.remote.server.handler.html5.RemoveSessionStorageItem;
import org.openqa.selenium.remote.server.handler.html5.SetLocalStorageItem;
import org.openqa.selenium.remote.server.handler.html5.SetLocationContext;
import org.openqa.selenium.remote.server.handler.html5.SetSessionStorageItem;
import org.openqa.selenium.remote.server.handler.interactions.ClickInSession;
import org.openqa.selenium.remote.server.handler.interactions.DoubleClickInSession;
import org.openqa.selenium.remote.server.handler.interactions.MouseDown;
import org.openqa.selenium.remote.server.handler.interactions.MouseMoveToLocation;
import org.openqa.selenium.remote.server.handler.interactions.MouseUp;
import org.openqa.selenium.remote.server.handler.interactions.SendKeyToActiveElement;
import org.openqa.selenium.remote.server.handler.interactions.touch.DoubleTapOnElement;
import org.openqa.selenium.remote.server.handler.interactions.touch.Down;
import org.openqa.selenium.remote.server.handler.interactions.touch.Flick;
import org.openqa.selenium.remote.server.handler.interactions.touch.LongPressOnElement;
import org.openqa.selenium.remote.server.handler.interactions.touch.Move;
import org.openqa.selenium.remote.server.handler.interactions.touch.Scroll;
import org.openqa.selenium.remote.server.handler.interactions.touch.SingleTapOnElement;
import org.openqa.selenium.remote.server.handler.interactions.touch.Up;
import org.openqa.selenium.remote.server.handler.mobile.GetNetworkConnection;
import org.openqa.selenium.remote.server.handler.mobile.SetNetworkConnection;
import org.openqa.selenium.remote.server.log.LoggingManager;
import org.openqa.selenium.remote.server.log.PerSessionLogHandler;
import org.openqa.selenium.remote.server.rest.RestishHandler;
import org.openqa.selenium.remote.server.rest.ResultConfig;






















public class JsonHttpCommandHandler
{
  private static final String ADD_CONFIG_COMMAND_NAME = "-selenium-add-config";
  private final DriverSessions sessions;
  private final Logger log;
  private final Set<CommandCodec<HttpRequest>> commandCodecs;
  private final ResponseCodec<HttpResponse> responseCodec;
  private final Map<String, ResultConfig> configs = new LinkedHashMap();
  private final ErrorCodes errorCodes = new ErrorCodes();
  
  public JsonHttpCommandHandler(DriverSessions sessions, Logger log) {
    this.sessions = sessions;
    this.log = log;
    commandCodecs = new LinkedHashSet();
    commandCodecs.add(new JsonHttpCommandCodec());
    commandCodecs.add(new W3CHttpCommandCodec());
    responseCodec = new JsonHttpResponseCodec();
    setUpMappings();
  }
  
  public void addNewMapping(String commandName, Class<? extends RestishHandler<?>> implementationClass)
  {
    ResultConfig config = new ResultConfig(commandName, implementationClass, sessions, log);
    configs.put(commandName, config);
  }
  
  public HttpResponse handleRequest(HttpRequest request) {
    LoggingManager.perSessionLogHandler().clearThreadTempLogs();
    log.fine(String.format("Handling: %s %s", new Object[] { request.getMethod(), request.getUri() }));
    
    Command command = null;
    Response response;
    try {
      command = decode(request);
      ResultConfig config = (ResultConfig)configs.get(command.getName());
      if (config == null) {
        throw new UnsupportedCommandException();
      }
      Response response = config.handle(command);
      log.fine(String.format("Finished: %s %s", new Object[] { request.getMethod(), request.getUri() }));
    } catch (Exception e) {
      log.fine(String.format("Error on: %s %s", new Object[] { request.getMethod(), request.getUri() }));
      response = new Response();
      response.setStatus(Integer.valueOf(errorCodes.toStatusCode(e)));
      response.setState(errorCodes.toState(response.getStatus()));
      response.setValue(e);
      
      if ((command != null) && (command.getSessionId() != null)) {
        response.setSessionId(command.getSessionId().toString());
      }
    }
    
    PerSessionLogHandler handler = LoggingManager.perSessionLogHandler();
    if (response.getSessionId() != null) {
      handler.attachToCurrentThread(new SessionId(response.getSessionId()));
    }
    try {
      return (HttpResponse)responseCodec.encode(response);
    } finally {
      handler.detachFromCurrentThread();
    }
  }
  
  private Command decode(HttpRequest request) {
    UnsupportedCommandException lastException = null;
    for (CommandCodec<HttpRequest> codec : commandCodecs) {
      try {
        return codec.decode(request);
      } catch (UnsupportedCommandException e) {
        lastException = e;
      }
    }
    if (lastException != null) {
      throw lastException;
    }
    throw new UnsupportedOperationException("Cannot find command for: " + request.getUri());
  }
  
  private void setUpMappings() {
    for (CommandCodec<HttpRequest> codec : commandCodecs) {
      codec.defineCommand("-selenium-add-config", HttpMethod.POST, "/config/drivers");
    }
    
    addNewMapping("-selenium-add-config", AddConfig.class);
    
    addNewMapping("status", Status.class);
    addNewMapping("getAllSessions", GetAllSessions.class);
    addNewMapping("newSession", NewSession.class);
    addNewMapping("getCapabilities", GetSessionCapabilities.class);
    addNewMapping("quit", DeleteSession.class);
    
    addNewMapping("getCurrentWindowHandle", GetCurrentWindowHandle.class);
    addNewMapping("getWindowHandles", GetAllWindowHandles.class);
    
    addNewMapping("dismissAlert", DismissAlert.class);
    addNewMapping("acceptAlert", AcceptAlert.class);
    addNewMapping("getAlertText", GetAlertText.class);
    addNewMapping("setAlertValue", SetAlertText.class);
    addNewMapping("setAlertCredentials", SetAlertCredentials.class);
    
    addNewMapping("get", ChangeUrl.class);
    addNewMapping("getCurrentUrl", GetCurrentUrl.class);
    addNewMapping("goForward", GoForward.class);
    addNewMapping("goBack", GoBack.class);
    addNewMapping("refresh", RefreshPage.class);
    
    addNewMapping("executeScript", ExecuteScript.class);
    addNewMapping("executeAsyncScript", ExecuteAsyncScript.class);
    
    addNewMapping("getPageSource", GetPageSource.class);
    
    addNewMapping("screenshot", CaptureScreenshot.class);
    
    addNewMapping("getTitle", GetTitle.class);
    
    addNewMapping("findElement", FindElement.class);
    addNewMapping("findElements", FindElements.class);
    addNewMapping("getActiveElement", FindActiveElement.class);
    
    addNewMapping("findChildElement", FindChildElement.class);
    addNewMapping("findChildElements", FindChildElements.class);
    
    addNewMapping("clickElement", ClickElement.class);
    addNewMapping("getElementText", GetElementText.class);
    addNewMapping("submitElement", SubmitElement.class);
    
    addNewMapping("uploadFile", UploadFile.class);
    addNewMapping("sendKeysToElement", SendKeys.class);
    addNewMapping("getElementTagName", GetTagName.class);
    
    addNewMapping("clearElement", ClearElement.class);
    addNewMapping("isElementSelected", GetElementSelected.class);
    addNewMapping("isElementEnabled", GetElementEnabled.class);
    addNewMapping("isElementDisplayed", GetElementDisplayed.class);
    addNewMapping("getElementLocation", GetElementLocation.class);
    addNewMapping("getElementLocationOnceScrolledIntoView", GetElementLocationInView.class);
    addNewMapping("getElementSize", GetElementSize.class);
    addNewMapping("getElementValueOfCssProperty", GetCssProperty.class);
    addNewMapping("getElementRect", GetElementRect.class);
    
    addNewMapping("getElementAttribute", GetElementAttribute.class);
    addNewMapping("elementEquals", ElementEquality.class);
    
    addNewMapping("getCookies", GetAllCookies.class);
    addNewMapping("getCookie", GetCookie.class);
    addNewMapping("addCookie", AddCookie.class);
    addNewMapping("deleteAllCookies", DeleteCookie.class);
    addNewMapping("deleteCookie", DeleteNamedCookie.class);
    
    addNewMapping("switchToFrame", SwitchToFrame.class);
    addNewMapping("switchToParentFrame", SwitchToParentFrame.class);
    addNewMapping("switchToWindow", SwitchToWindow.class);
    addNewMapping("close", CloseWindow.class);
    
    addNewMapping("getCurrentWindowSize", GetWindowSize.class);
    addNewMapping("setCurrentWindowSize", SetWindowSize.class);
    addNewMapping("getWindowPosition", GetWindowPosition.class);
    addNewMapping("setWindowPosition", SetWindowPosition.class);
    addNewMapping("maximizeCurrentWindow", MaximizeWindow.class);
    addNewMapping("fullscreenCurrentWindow", FullscreenWindow.class);
    
    addNewMapping("setTimeout", ConfigureTimeout.class);
    addNewMapping("implicitlyWait", ImplicitlyWait.class);
    addNewMapping("setScriptTimeout", SetScriptTimeout.class);
    
    addNewMapping("getLocation", GetLocationContext.class);
    addNewMapping("setLocation", SetLocationContext.class);
    
    addNewMapping("getStatus", GetAppCacheStatus.class);
    
    addNewMapping("getLocalStorageItem", GetLocalStorageItem.class);
    addNewMapping("removeLocalStorageItem", RemoveLocalStorageItem.class);
    addNewMapping("getLocalStorageKeys", GetLocalStorageKeys.class);
    addNewMapping("setLocalStorageItem", SetLocalStorageItem.class);
    addNewMapping("clearLocalStorage", ClearLocalStorage.class);
    addNewMapping("getLocalStorageSize", GetLocalStorageSize.class);
    
    addNewMapping("getSessionStorageItem", GetSessionStorageItem.class);
    addNewMapping("removeSessionStorageItem", RemoveSessionStorageItem.class);
    addNewMapping("getSessionStorageKey", GetSessionStorageKeys.class);
    addNewMapping("setSessionStorageItem", SetSessionStorageItem.class);
    addNewMapping("clearSessionStorage", ClearSessionStorage.class);
    addNewMapping("getSessionStorageSize", GetSessionStorageSize.class);
    
    addNewMapping("getScreenOrientation", GetScreenOrientation.class);
    addNewMapping("setScreenOrientation", Rotate.class);
    
    addNewMapping("mouseMoveTo", MouseMoveToLocation.class);
    addNewMapping("mouseClick", ClickInSession.class);
    addNewMapping("mouseDoubleClick", DoubleClickInSession.class);
    addNewMapping("mouseButtonDown", MouseDown.class);
    addNewMapping("mouseButtonUp", MouseUp.class);
    addNewMapping("sendKeysToActiveElement", SendKeyToActiveElement.class);
    
    addNewMapping("imeGetAvailableEngines", ImeGetAvailableEngines.class);
    addNewMapping("imeGetActiveEngine", ImeGetActiveEngine.class);
    addNewMapping("imeIsActivated", ImeIsActivated.class);
    addNewMapping("imeDeactivate", ImeDeactivate.class);
    addNewMapping("imeActivateEngine", ImeActivateEngine.class);
    
    addNewMapping("actions", W3CActions.class);
    

    addNewMapping("touchSingleTap", SingleTapOnElement.class);
    addNewMapping("touchDown", Down.class);
    addNewMapping("touchUp", Up.class);
    addNewMapping("touchMove", Move.class);
    addNewMapping("touchScroll", Scroll.class);
    addNewMapping("touchDoubleTap", DoubleTapOnElement.class);
    addNewMapping("touchLongPress", LongPressOnElement.class);
    addNewMapping("touchFlick", Flick.class);
    
    addNewMapping("getAvailableLogTypes", GetAvailableLogTypesHandler.class);
    addNewMapping("getLog", GetLogHandler.class);
    addNewMapping("getSessionLogs", GetSessionLogsHandler.class);
    
    addNewMapping("getNetworkConnection", GetNetworkConnection.class);
    addNewMapping("setNetworkConnection", SetNetworkConnection.class);
    

    addNewMapping("getWindowSize", GetWindowSize.class);
    addNewMapping("setWindowSize", SetWindowSize.class);
    addNewMapping("maximizeWindow", MaximizeWindow.class);
  }
}
