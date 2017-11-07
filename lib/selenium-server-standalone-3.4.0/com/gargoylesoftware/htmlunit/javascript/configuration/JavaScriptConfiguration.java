package com.gargoylesoftware.htmlunit.javascript.configuration;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;
import com.gargoylesoftware.htmlunit.javascript.host.ApplicationCache;
import com.gargoylesoftware.htmlunit.javascript.host.AudioScheduledSourceNode;
import com.gargoylesoftware.htmlunit.javascript.host.BarProp;
import com.gargoylesoftware.htmlunit.javascript.host.BatteryManager;
import com.gargoylesoftware.htmlunit.javascript.host.BroadcastChannel;
import com.gargoylesoftware.htmlunit.javascript.host.CacheStorage;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRect;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRectList;
import com.gargoylesoftware.htmlunit.javascript.host.Console;
import com.gargoylesoftware.htmlunit.javascript.host.External;
import com.gargoylesoftware.htmlunit.javascript.host.Gamepad;
import com.gargoylesoftware.htmlunit.javascript.host.History;
import com.gargoylesoftware.htmlunit.javascript.host.MessageChannel;
import com.gargoylesoftware.htmlunit.javascript.host.MessagePort;
import com.gargoylesoftware.htmlunit.javascript.host.MimeType;
import com.gargoylesoftware.htmlunit.javascript.host.MimeTypeArray;
import com.gargoylesoftware.htmlunit.javascript.host.Namespace;
import com.gargoylesoftware.htmlunit.javascript.host.NamespaceCollection;
import com.gargoylesoftware.htmlunit.javascript.host.Navigator;
import com.gargoylesoftware.htmlunit.javascript.host.Notification;
import com.gargoylesoftware.htmlunit.javascript.host.PerformanceObserver;
import com.gargoylesoftware.htmlunit.javascript.host.PermissionStatus;
import com.gargoylesoftware.htmlunit.javascript.host.Permissions;
import com.gargoylesoftware.htmlunit.javascript.host.PushManager;
import com.gargoylesoftware.htmlunit.javascript.host.ReadableStream;
import com.gargoylesoftware.htmlunit.javascript.host.Screen;
import com.gargoylesoftware.htmlunit.javascript.host.ScreenOrientation;
import com.gargoylesoftware.htmlunit.javascript.host.SharedWorker;
import com.gargoylesoftware.htmlunit.javascript.host.SimpleArray;
import com.gargoylesoftware.htmlunit.javascript.host.Storage;
import com.gargoylesoftware.htmlunit.javascript.host.StorageManager;
import com.gargoylesoftware.htmlunit.javascript.host.Symbol;
import com.gargoylesoftware.htmlunit.javascript.host.TextDecoder;
import com.gargoylesoftware.htmlunit.javascript.host.TextEncoder;
import com.gargoylesoftware.htmlunit.javascript.host.Touch;
import com.gargoylesoftware.htmlunit.javascript.host.URL;
import com.gargoylesoftware.htmlunit.javascript.host.URLSearchParams;
import com.gargoylesoftware.htmlunit.javascript.host.WeakSet;
import com.gargoylesoftware.htmlunit.javascript.host.WebSocket;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.XPathExpression;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferView;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.DataView;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Float32Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Float64Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Int16Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Int32Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Int8Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint16Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint32Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8Array;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasCaptureMediaStream;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasGradient;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasPattern;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasRenderingContext2D;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ImageBitmapRenderingContext;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.IntersectionObserver;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.IntersectionObserverEntry;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.Path2D;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.TextMetrics;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGL2RenderingContext;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLFramebuffer;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLProgram;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLRenderbuffer;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLShader;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLShaderPrecisionFormat;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.ANGLE_instanced_arrays;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.EXT_texture_filter_anisotropic;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.OES_standard_derivatives;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.OES_texture_float;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.WEBGL_compressed_texture_s3tc;
import com.gargoylesoftware.htmlunit.javascript.host.crypto.CryptoKey;
import com.gargoylesoftware.htmlunit.javascript.host.crypto.SubtleCrypto;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSS;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSS2Properties;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSConditionRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSCounterStyleRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSGroupingRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSImportRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSKeyframeRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSMediaRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSNamespaceRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSPageRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSValueList;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSViewportRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.MediaQueryList;
import com.gargoylesoftware.htmlunit.javascript.host.css.MozCSSKeyframesRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleMedia;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.dom.CDATASection;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Comment;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMCursor;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMException;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMMatrix;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMMatrixReadOnly;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMParser;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMPointReadOnly;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringMap;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentFragment;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentType;
import com.gargoylesoftware.htmlunit.javascript.host.dom.EventNode;
import com.gargoylesoftware.htmlunit.javascript.host.dom.IdleDeadline;
import com.gargoylesoftware.htmlunit.javascript.host.dom.MutationRecord;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.dom.ProcessingInstruction;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Range;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Selection;
import com.gargoylesoftware.htmlunit.javascript.host.dom.ShadowRoot;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Text;
import com.gargoylesoftware.htmlunit.javascript.host.dom.TreeWalker;
import com.gargoylesoftware.htmlunit.javascript.host.dom.WebKitMutationObserver;
import com.gargoylesoftware.htmlunit.javascript.host.dom.XPathEvaluator;
import com.gargoylesoftware.htmlunit.javascript.host.dom.XPathNSResolver;
import com.gargoylesoftware.htmlunit.javascript.host.event.ApplicationCacheErrorEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.BeforeInstallPromptEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.BeforeUnloadEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.BlobEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.ClipboardEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.CloseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.CompositionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.DeviceLightEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.DeviceMotionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.DeviceOrientationEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.DeviceProximityEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.DragEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.ErrorEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventSource;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.FocusEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.HashChangeEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.IDBVersionChangeEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.InputEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MIDIConnectionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MSGestureEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MediaEncryptedEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MediaKeyMessageEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MediaQueryListEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MediaStreamEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MozContactChangeEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MozSettingsEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PresentationConnectionAvailableEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PresentationConnectionCloseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PromiseRejectionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.RTCPeerConnectionIceEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.SVGZoomEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.SecurityPolicyViolationEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.SpeechSynthesisEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.TextEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.TouchEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.TrackEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.TransitionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.UIEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.UserProximityEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.WebGLContextEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.WebKitAnimationEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.WebKitTransitionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.webkitSpeechRecognitionError;
import com.gargoylesoftware.htmlunit.javascript.host.event.webkitSpeechRecognitionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.fetch.Headers;
import com.gargoylesoftware.htmlunit.javascript.host.fetch.Response;
import com.gargoylesoftware.htmlunit.javascript.host.file.Blob;
import com.gargoylesoftware.htmlunit.javascript.host.file.DataTransferItemList;
import com.gargoylesoftware.htmlunit.javascript.host.file.FileList;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Geolocation;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Position;
import com.gargoylesoftware.htmlunit.javascript.host.geo.PositionError;
import com.gargoylesoftware.htmlunit.javascript.host.html.DataTransfer;
import com.gargoylesoftware.htmlunit.javascript.host.html.Enumerator;
import com.gargoylesoftware.htmlunit.javascript.host.html.FormField;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAllCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAreaElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAudioElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLContentElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDDElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDataElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDetailsElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDirectoryElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLEmbedElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFieldSetElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormControlsCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameSetElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadingElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIFrameElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInlineQuotationElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIsIndexElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLIElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLegendElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLinkElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMarqueeElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMediaElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMenuItemElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMeterElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLModElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLNextIdElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLObjectElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionsCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParamElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPhraseElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPictureElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLProgressElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLQuoteElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLScriptElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSelectElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLShadowElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSlotElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSourceElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSpanElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCaptionElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCellElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableColElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableDataCellElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableRowElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableSectionElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTemplateElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTextAreaElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTimeElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTrackElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUnknownElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLVideoElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.Image;
import com.gargoylesoftware.htmlunit.javascript.host.html.RowContainer;
import com.gargoylesoftware.htmlunit.javascript.host.html.ValidityState;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBCursorWithValue;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBFactory;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBIndex;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBMutableFile;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBOpenDBRequest;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBTransaction;
import com.gargoylesoftware.htmlunit.javascript.host.media.AnalyserNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioBufferSourceNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioDestinationNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioListener;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioParam;
import com.gargoylesoftware.htmlunit.javascript.host.media.BaseAudioContext;
import com.gargoylesoftware.htmlunit.javascript.host.media.ChannelSplitterNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.ConstantSourceNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.ConvolverNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.DynamicsCompressorNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.IIRFilterNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.LocalMediaStream;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaDevices;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaElementAudioSourceNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaError;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaKeyError;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaKeySession;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaKeys;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaStreamAudioDestinationNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaStreamAudioSourceNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaStreamTrack;
import com.gargoylesoftware.htmlunit.javascript.host.media.OfflineAudioContext;
import com.gargoylesoftware.htmlunit.javascript.host.media.OscillatorNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.PannerNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.PeriodicWave;
import com.gargoylesoftware.htmlunit.javascript.host.media.ScriptProcessorNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.SourceBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.media.SourceBufferList;
import com.gargoylesoftware.htmlunit.javascript.host.media.TextTrack;
import com.gargoylesoftware.htmlunit.javascript.host.media.TextTrackCue;
import com.gargoylesoftware.htmlunit.javascript.host.media.TextTrackCueList;
import com.gargoylesoftware.htmlunit.javascript.host.media.TextTrackList;
import com.gargoylesoftware.htmlunit.javascript.host.media.TimeRanges;
import com.gargoylesoftware.htmlunit.javascript.host.media.VTTCue;
import com.gargoylesoftware.htmlunit.javascript.host.media.VideoPlaybackQuality;
import com.gargoylesoftware.htmlunit.javascript.host.media.midi.MIDIAccess;
import com.gargoylesoftware.htmlunit.javascript.host.media.midi.MIDIInput;
import com.gargoylesoftware.htmlunit.javascript.host.media.midi.MIDIInputMap;
import com.gargoylesoftware.htmlunit.javascript.host.media.midi.MIDIOutput;
import com.gargoylesoftware.htmlunit.javascript.host.media.midi.MIDIOutputMap;
import com.gargoylesoftware.htmlunit.javascript.host.media.midi.MIDIPort;
import com.gargoylesoftware.htmlunit.javascript.host.media.presentation.PresentationAvailability;
import com.gargoylesoftware.htmlunit.javascript.host.media.presentation.PresentationConnection;
import com.gargoylesoftware.htmlunit.javascript.host.media.presentation.PresentationRequest;
import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.RTCCertificate;
import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.RTCPeerConnection;
import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.RTCStatsReport;
import com.gargoylesoftware.htmlunit.javascript.host.media.webkitMediaStream;
import com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceEntry;
import com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceMark;
import com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceMeasure;
import com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceNavigation;
import com.gargoylesoftware.htmlunit.javascript.host.security.CredentialsContainer;
import com.gargoylesoftware.htmlunit.javascript.host.security.PasswordCredential;
import com.gargoylesoftware.htmlunit.javascript.host.speech.SpeechSynthesis;
import com.gargoylesoftware.htmlunit.javascript.host.speech.webkitSpeechGrammarList;
import com.gargoylesoftware.htmlunit.javascript.host.speech.webkitSpeechRecognition;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAltGlyphElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAngle;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateMotionElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateTransformElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedAngle;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedBoolean;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedEnumeration;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedInteger;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedLength;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedLengthList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedNumber;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedRect;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedTransformList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimationElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGComponentTransferFunctionElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDescElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDiscardElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGEllipseElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEBlendElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEComponentTransferElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFECompositeElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEConvolveMatrixElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDiffuseLightingElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDistantLightElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDropShadowElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncAElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncGElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncRElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEGaussianBlurElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEImageElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMergeElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMorphologyElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEOffsetElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEPointLightElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFESpecularLightingElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFETurbulenceElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGGElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGGeometryElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGGradientElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLength;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLineElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLinearGradientElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMPathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGNumber;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGNumberList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSeg;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegCurvetoCubicAbs;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegCurvetoQuadraticSmoothAbs;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegCurvetoQuadraticSmoothRel;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegLinetoHorizontalRel;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegLinetoVerticalAbs;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegLinetoVerticalRel;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegMovetoAbs;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegMovetoRel;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPatternElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPointList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolygonElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolylineElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPreserveAspectRatio;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRectElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSVGElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGScriptElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStopElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStyleElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSymbolElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTSpanElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextContentElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextPathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextPositioningElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTitleElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTransform;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTransformList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGUseElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGViewElement;
import com.gargoylesoftware.htmlunit.javascript.host.webkitURL;
import com.gargoylesoftware.htmlunit.javascript.host.worker.ServiceWorker;
import com.gargoylesoftware.htmlunit.javascript.host.worker.ServiceWorkerContainer;
import com.gargoylesoftware.htmlunit.javascript.host.worker.ServiceWorkerRegistration;
import com.gargoylesoftware.htmlunit.javascript.host.worker.SyncManager;
import com.gargoylesoftware.htmlunit.javascript.host.worker.Worker;
import com.gargoylesoftware.htmlunit.javascript.host.xml.FormData;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequest;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequestEventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequestUpload;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLSerializer;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XSLTProcessor;

public final class JavaScriptConfiguration extends AbstractJavaScriptConfiguration
{
  static final Class<? extends SimpleScriptable>[] CLASSES_ = {
    AbstractList.class, ActiveXObject.class, AnalyserNode.class, ANGLE_instanced_arrays.class, com.gargoylesoftware.htmlunit.javascript.host.event.AnimationEvent.class, 
    com.gargoylesoftware.htmlunit.javascript.host.AppBannerPromptResult.class, ApplicationCache.class, ApplicationCacheErrorEvent.class, ArrayBuffer.class, 
    ArrayBufferView.class, com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferViewBase.class, Attr.class, AudioBuffer.class, 
    AudioBufferSourceNode.class, com.gargoylesoftware.htmlunit.javascript.host.media.AudioContext.class, AudioDestinationNode.class, AudioListener.class, 
    com.gargoylesoftware.htmlunit.javascript.host.media.AudioNode.class, AudioParam.class, com.gargoylesoftware.htmlunit.javascript.host.event.AudioProcessingEvent.class, AudioScheduledSourceNode.class, 
    BarProp.class, BaseAudioContext.class, 
    BatteryManager.class, BeforeInstallPromptEvent.class, BeforeUnloadEvent.class, com.gargoylesoftware.htmlunit.javascript.host.media.BiquadFilterNode.class, 
    Blob.class, BlobEvent.class, BroadcastChannel.class, com.gargoylesoftware.htmlunit.javascript.host.Cache.class, CacheStorage.class, 
    CanvasCaptureMediaStream.class, com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasCaptureMediaStreamTrack.class, 
    CanvasGradient.class, CanvasPattern.class, CanvasRenderingContext2D.class, com.gargoylesoftware.htmlunit.javascript.host.css.CaretPosition.class, 
    CDATASection.class, com.gargoylesoftware.htmlunit.javascript.host.media.ChannelMergerNode.class, ChannelSplitterNode.class, com.gargoylesoftware.htmlunit.javascript.host.dom.CharacterData.class, ClientRect.class, 
    ClientRectList.class, ClipboardEvent.class, 
    CloseEvent.class, Comment.class, CompositionEvent.class, com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration.class, Console.class, 
    ConstantSourceNode.class, 
    ConvolverNode.class, com.gargoylesoftware.htmlunit.javascript.host.geo.Coordinates.class, com.gargoylesoftware.htmlunit.javascript.host.security.Credential.class, CredentialsContainer.class, com.gargoylesoftware.htmlunit.javascript.host.crypto.Crypto.class, 
    CryptoKey.class, CSS.class, CSS2Properties.class, CSSConditionRule.class, 
    CSSCounterStyleRule.class, com.gargoylesoftware.htmlunit.javascript.host.css.CSSFontFaceRule.class, CSSGroupingRule.class, CSSImportRule.class, 
    CSSKeyframeRule.class, com.gargoylesoftware.htmlunit.javascript.host.css.CSSKeyframesRule.class, CSSMediaRule.class, CSSNamespaceRule.class, CSSPageRule.class, 
    com.gargoylesoftware.htmlunit.javascript.host.css.CSSPrimitiveValue.class, CSSRule.class, CSSRuleList.class, CSSStyleDeclaration.class, CSSStyleRule.class, 
    com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet.class, 
    com.gargoylesoftware.htmlunit.javascript.host.css.CSSSupportsRule.class, com.gargoylesoftware.htmlunit.javascript.host.css.CSSValue.class, CSSValueList.class, CSSViewportRule.class, com.gargoylesoftware.htmlunit.javascript.host.dom.CustomElementRegistry.class, 
    com.gargoylesoftware.htmlunit.javascript.host.event.CustomEvent.class, DataTransfer.class, 
    com.gargoylesoftware.htmlunit.javascript.host.file.DataTransferItem.class, DataTransferItemList.class, DataView.class, com.gargoylesoftware.htmlunit.javascript.host.media.DelayNode.class, DeviceLightEvent.class, 
    DeviceMotionEvent.class, DeviceOrientationEvent.class, DeviceProximityEvent.class, 
    Document.class, DocumentFragment.class, DocumentType.class, DOMCursor.class, com.gargoylesoftware.htmlunit.javascript.host.dom.DOMError.class, DOMException.class, 
    com.gargoylesoftware.htmlunit.javascript.host.dom.DOMImplementation.class, DOMMatrix.class, DOMMatrixReadOnly.class, DOMParser.class, com.gargoylesoftware.htmlunit.javascript.host.dom.DOMPoint.class, 
    DOMPointReadOnly.class, com.gargoylesoftware.htmlunit.javascript.host.dom.DOMRectReadOnly.class, com.gargoylesoftware.htmlunit.javascript.host.dom.DOMRequest.class, 
    com.gargoylesoftware.htmlunit.javascript.host.dom.DOMSettableTokenList.class, DOMStringList.class, DOMStringMap.class, com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList.class, 
    DragEvent.class, DynamicsCompressorNode.class, 
    com.gargoylesoftware.htmlunit.javascript.host.Element.class, Enumerator.class, ErrorEvent.class, Event.class, EventNode.class, EventSource.class, 
    EventTarget.class, EXT_texture_filter_anisotropic.class, External.class, com.gargoylesoftware.htmlunit.javascript.host.security.FederatedCredential.class, 
    com.gargoylesoftware.htmlunit.javascript.host.file.File.class, FileList.class, 
    com.gargoylesoftware.htmlunit.javascript.host.file.FileReader.class, Float32Array.class, Float64Array.class, FocusEvent.class, com.gargoylesoftware.htmlunit.javascript.host.FontFace.class, 
    com.gargoylesoftware.htmlunit.javascript.host.FontFaceSet.class, FormData.class, FormField.class, com.gargoylesoftware.htmlunit.javascript.host.media.GainNode.class, Gamepad.class, 
    com.gargoylesoftware.htmlunit.javascript.host.GamepadButton.class, com.gargoylesoftware.htmlunit.javascript.host.event.GamepadEvent.class, Geolocation.class, HashChangeEvent.class, Headers.class, History.class, 
    HTMLAllCollection.class, 
    com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAnchorElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAppletElement.class, HTMLAreaElement.class, HTMLAudioElement.class, 
    HTMLBaseElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseFontElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBGSoundElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBlockElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBRElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement.class, 
    HTMLCanvasElement.class, HTMLCollection.class, HTMLContentElement.class, 
    HTMLDataElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDataListElement.class, 
    HTMLDDElement.class, HTMLDetailsElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDialogElement.class, HTMLDirectoryElement.class, 
    HTMLDivElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDListElement.class, HTMLDocument.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDTElement.class, HTMLElement.class, 
    HTMLEmbedElement.class, HTMLFieldSetElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFontElement.class, HTMLFormControlsCollection.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameElement.class, 
    HTMLFrameSetElement.class, 
    HTMLHeadElement.class, HTMLHeadingElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHRElement.class, HTMLHtmlElement.class, 
    HTMLIFrameElement.class, HTMLImageElement.class, HTMLInlineQuotationElement.class, HTMLInputElement.class, 
    HTMLIsIndexElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLabelElement.class, 
    HTMLLegendElement.class, HTMLLIElement.class, HTMLLinkElement.class, HTMLListElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMapElement.class, HTMLMarqueeElement.class, 
    HTMLMediaElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMenuElement.class, HTMLMenuItemElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMetaElement.class, 
    HTMLMeterElement.class, HTMLModElement.class, HTMLNextIdElement.class, 
    HTMLObjectElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOListElement.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptGroupElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionElement.class, HTMLOptionsCollection.class, com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOutputElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParagraphElement.class, HTMLParamElement.class, HTMLPhraseElement.class, HTMLPictureElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPreElement.class, HTMLProgressElement.class, HTMLQuoteElement.class, HTMLScriptElement.class, 
    HTMLSelectElement.class, HTMLShadowElement.class, HTMLSlotElement.class, HTMLSourceElement.class, 
    HTMLSpanElement.class, 
    HTMLStyleElement.class, HTMLTableCaptionElement.class, HTMLTableCellElement.class, HTMLTableColElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableComponent.class, HTMLTableDataCellElement.class, HTMLTableElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableHeaderCellElement.class, HTMLTableRowElement.class, HTMLTableSectionElement.class, 
    HTMLTemplateElement.class, HTMLTextAreaElement.class, HTMLTimeElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTitleElement.class, HTMLTrackElement.class, HTMLUListElement.class, HTMLUnknownElement.class, 
    HTMLVideoElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.idb.IDBCursor.class, IDBCursorWithValue.class, com.gargoylesoftware.htmlunit.javascript.host.idb.IDBDatabase.class, IDBFactory.class, IDBIndex.class, 
    com.gargoylesoftware.htmlunit.javascript.host.idb.IDBKeyRange.class, IDBMutableFile.class, com.gargoylesoftware.htmlunit.javascript.host.idb.IDBObjectStore.class, IDBOpenDBRequest.class, com.gargoylesoftware.htmlunit.javascript.host.idb.IDBRequest.class, 
    IDBTransaction.class, IDBVersionChangeEvent.class, IdleDeadline.class, IIRFilterNode.class, 
    Image.class, com.gargoylesoftware.htmlunit.javascript.host.ImageBitmap.class, ImageBitmapRenderingContext.class, com.gargoylesoftware.htmlunit.javascript.host.canvas.ImageData.class, 
    com.gargoylesoftware.htmlunit.javascript.host.media.InputDeviceCapabilities.class, InputEvent.class, 
    com.gargoylesoftware.htmlunit.javascript.host.InstallTrigger.class, Int16Array.class, Int32Array.class, Int8Array.class, IntersectionObserver.class, 
    IntersectionObserverEntry.class, 
    KeyboardEvent.class, LocalMediaStream.class, 
    com.gargoylesoftware.htmlunit.javascript.host.Location.class, com.gargoylesoftware.htmlunit.javascript.host.Map.class, 
    com.gargoylesoftware.htmlunit.javascript.host.media.MediaDeviceInfo.class, 
    MediaDevices.class, MediaElementAudioSourceNode.class, MediaEncryptedEvent.class, MediaError.class, 
    MediaKeyError.class, MediaKeyMessageEvent.class, MediaKeys.class, MediaKeySession.class, 
    com.gargoylesoftware.htmlunit.javascript.host.media.MediaKeyStatusMap.class, com.gargoylesoftware.htmlunit.javascript.host.media.MediaKeySystemAccess.class, com.gargoylesoftware.htmlunit.javascript.host.dom.MediaList.class, MediaQueryList.class, 
    MediaQueryListEvent.class, com.gargoylesoftware.htmlunit.javascript.host.media.MediaRecorder.class, 
    com.gargoylesoftware.htmlunit.javascript.host.media.MediaSource.class, com.gargoylesoftware.htmlunit.javascript.host.media.MediaStream.class, MediaStreamAudioDestinationNode.class, MediaStreamAudioSourceNode.class, 
    MediaStreamEvent.class, MediaStreamTrack.class, com.gargoylesoftware.htmlunit.javascript.host.event.MediaStreamTrackEvent.class, MessageChannel.class, 
    com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent.class, MessagePort.class, MIDIAccess.class, MIDIConnectionEvent.class, MIDIInput.class, 
    MIDIInputMap.class, com.gargoylesoftware.htmlunit.javascript.host.event.MIDIMessageEvent.class, MIDIOutput.class, MIDIOutputMap.class, MIDIPort.class, 
    MimeType.class, MimeTypeArray.class, MouseEvent.class, com.gargoylesoftware.htmlunit.javascript.host.event.MouseScrollEvent.class, 
    com.gargoylesoftware.htmlunit.javascript.host.event.MouseWheelEvent.class, MozContactChangeEvent.class, MozCSSKeyframesRule.class, 
    com.gargoylesoftware.htmlunit.javascript.host.moz.MozPowerManager.class, com.gargoylesoftware.htmlunit.javascript.host.media.rtc.mozRTCIceCandidate.class, 
    com.gargoylesoftware.htmlunit.javascript.host.media.rtc.mozRTCPeerConnection.class, com.gargoylesoftware.htmlunit.javascript.host.media.rtc.mozRTCSessionDescription.class, MozSettingsEvent.class, 
    MSGestureEvent.class, 
    com.gargoylesoftware.htmlunit.javascript.host.event.MutationEvent.class, com.gargoylesoftware.htmlunit.javascript.host.dom.MutationObserver.class, MutationRecord.class, NamedNodeMap.class, Namespace.class, 
    NamespaceCollection.class, Navigator.class, Node.class, com.gargoylesoftware.htmlunit.javascript.host.dom.NodeFilter.class, com.gargoylesoftware.htmlunit.javascript.host.dom.NodeIterator.class, 
    com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList.class, Notification.class, com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.OES_element_index_uint.class, OES_standard_derivatives.class, 
    OES_texture_float.class, com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.OES_texture_float_linear.class, com.gargoylesoftware.htmlunit.javascript.host.event.OfflineAudioCompletionEvent.class, 
    OfflineAudioContext.class, com.gargoylesoftware.htmlunit.javascript.host.html.Option.class, OscillatorNode.class, com.gargoylesoftware.htmlunit.javascript.host.event.PageTransitionEvent.class, PannerNode.class, 
    PasswordCredential.class, 
    Path2D.class, com.gargoylesoftware.htmlunit.javascript.host.performance.Performance.class, PerformanceEntry.class, PerformanceMark.class, 
    PerformanceMeasure.class, PerformanceNavigation.class, com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceNavigationTiming.class, 
    PerformanceObserver.class, com.gargoylesoftware.htmlunit.javascript.host.PerformanceObserverEntryList.class, 
    com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceResourceTiming.class, com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceTiming.class, 
    PeriodicWave.class, Permissions.class, PermissionStatus.class, com.gargoylesoftware.htmlunit.javascript.host.Plugin.class, com.gargoylesoftware.htmlunit.javascript.host.PluginArray.class, 
    com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent.class, com.gargoylesoftware.htmlunit.javascript.host.event.PopStateEvent.class, Position.class, PositionError.class, com.gargoylesoftware.htmlunit.javascript.host.media.presentation.Presentation.class, 
    PresentationAvailability.class, PresentationConnection.class, PresentationConnectionAvailableEvent.class, 
    PresentationConnectionCloseEvent.class, PresentationRequest.class, 
    ProcessingInstruction.class, com.gargoylesoftware.htmlunit.javascript.host.event.ProgressEvent.class, com.gargoylesoftware.htmlunit.javascript.host.Promise.class, PromiseRejectionEvent.class, 
    com.gargoylesoftware.htmlunit.javascript.host.Proxy.class, PushManager.class, 
    com.gargoylesoftware.htmlunit.javascript.host.PushSubscription.class, com.gargoylesoftware.htmlunit.javascript.host.PushSubscriptionOptions.class, com.gargoylesoftware.htmlunit.javascript.host.dom.RadioNodeList.class, Range.class, ReadableStream.class, 
    com.gargoylesoftware.htmlunit.javascript.host.media.RemotePlayback.class, com.gargoylesoftware.htmlunit.javascript.host.fetch.Request.class, Response.class, RowContainer.class, RTCCertificate.class, 
    com.gargoylesoftware.htmlunit.javascript.host.event.RTCDataChannelEvent.class, com.gargoylesoftware.htmlunit.javascript.host.media.rtc.RTCIceCandidate.class, RTCPeerConnection.class, RTCPeerConnectionIceEvent.class, 
    com.gargoylesoftware.htmlunit.javascript.host.media.rtc.RTCSessionDescription.class, RTCStatsReport.class, Screen.class, ScreenOrientation.class, 
    ScriptProcessorNode.class, 
    SecurityPolicyViolationEvent.class, Selection.class, ServiceWorker.class, ServiceWorkerContainer.class, 
    ServiceWorkerRegistration.class, com.gargoylesoftware.htmlunit.javascript.host.Set.class, ShadowRoot.class, 
    SharedWorker.class, SimpleArray.class, com.gargoylesoftware.htmlunit.javascript.host.security.SiteBoundCredential.class, SourceBuffer.class, SourceBufferList.class, 
    SpeechSynthesis.class, SpeechSynthesisEvent.class, 
    com.gargoylesoftware.htmlunit.javascript.host.speech.SpeechSynthesisUtterance.class, com.gargoylesoftware.htmlunit.javascript.host.media.StereoPannerNode.class, Storage.class, com.gargoylesoftware.htmlunit.javascript.host.event.StorageEvent.class, StorageManager.class, 
    StyleMedia.class, StyleSheet.class, StyleSheetList.class, SubtleCrypto.class, 
    SVGAElement.class, SVGAltGlyphElement.class, SVGAngle.class, SVGAnimatedAngle.class, 
    SVGAnimatedBoolean.class, SVGAnimatedEnumeration.class, SVGAnimatedInteger.class, 
    SVGAnimatedLength.class, SVGAnimatedLengthList.class, SVGAnimatedNumber.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedNumberList.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedPreserveAspectRatio.class, SVGAnimatedRect.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedString.class, 
    SVGAnimatedTransformList.class, SVGAnimateElement.class, 
    SVGAnimateMotionElement.class, SVGAnimateTransformElement.class, SVGAnimationElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGCircleElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGClipPathElement.class, SVGComponentTransferFunctionElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDefsElement.class, SVGDescElement.class, SVGDiscardElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDocument.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGElement.class, 
    SVGEllipseElement.class, SVGFEBlendElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEColorMatrixElement.class, 
    SVGFEComponentTransferElement.class, SVGFECompositeElement.class, SVGFEConvolveMatrixElement.class, 
    SVGFEDiffuseLightingElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDisplacementMapElement.class, SVGFEDistantLightElement.class, 
    SVGFEDropShadowElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFloodElement.class, SVGFEFuncAElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncBElement.class, SVGFEFuncGElement.class, 
    SVGFEFuncRElement.class, SVGFEGaussianBlurElement.class, SVGFEImageElement.class, SVGFEMergeElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMergeNodeElement.class, SVGFEMorphologyElement.class, SVGFEOffsetElement.class, 
    SVGFEPointLightElement.class, SVGFESpecularLightingElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFESpotLightElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFETileElement.class, SVGFETurbulenceElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFilterElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGForeignObjectElement.class, 
    SVGGElement.class, SVGGeometryElement.class, SVGGradientElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGGraphicsElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGImageElement.class, SVGLength.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLengthList.class, SVGLinearGradientElement.class, 
    SVGLineElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMarkerElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMaskElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMatrix.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMetadataElement.class, SVGMPathElement.class, SVGNumber.class, SVGNumberList.class, 
    SVGPathElement.class, SVGPathSeg.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegArcAbs.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegArcRel.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegClosePath.class, SVGPathSegCurvetoCubicAbs.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegCurvetoCubicRel.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegCurvetoCubicSmoothAbs.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegCurvetoCubicSmoothRel.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegCurvetoQuadraticAbs.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegCurvetoQuadraticRel.class, 
    SVGPathSegCurvetoQuadraticSmoothAbs.class, SVGPathSegCurvetoQuadraticSmoothRel.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegLinetoAbs.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegLinetoHorizontalAbs.class, SVGPathSegLinetoHorizontalRel.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathSegLinetoRel.class, SVGPathSegLinetoVerticalAbs.class, SVGPathSegLinetoVerticalRel.class, 
    SVGPathSegList.class, SVGPathSegMovetoAbs.class, SVGPathSegMovetoRel.class, SVGPatternElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPoint.class, SVGPointList.class, SVGPolygonElement.class, SVGPolylineElement.class, 
    SVGPreserveAspectRatio.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRadialGradientElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRect.class, SVGRectElement.class, 
    SVGScriptElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSetElement.class, SVGStopElement.class, 
    com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStringList.class, SVGStyleElement.class, SVGSVGElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSwitchElement.class, 
    SVGSymbolElement.class, SVGTextContentElement.class, SVGTextElement.class, 
    SVGTextPathElement.class, SVGTextPositioningElement.class, SVGTitleElement.class, SVGTransform.class, 
    SVGTransformList.class, SVGTSpanElement.class, com.gargoylesoftware.htmlunit.javascript.host.svg.SVGUnitTypes.class, SVGUseElement.class, SVGViewElement.class, 
    SVGZoomEvent.class, Symbol.class, SyncManager.class, Text.class, TextDecoder.class, 
    TextEncoder.class, TextEvent.class, TextMetrics.class, com.gargoylesoftware.htmlunit.javascript.host.dom.TextRange.class, TextTrack.class, TextTrackCue.class, 
    TextTrackCueList.class, TextTrackList.class, com.gargoylesoftware.htmlunit.javascript.host.event.TimeEvent.class, TimeRanges.class, 
    Touch.class, TouchEvent.class, com.gargoylesoftware.htmlunit.javascript.host.TouchList.class, TrackEvent.class, TransitionEvent.class, TreeWalker.class, 
    UIEvent.class, Uint16Array.class, Uint32Array.class, Uint8Array.class, com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8ClampedArray.class, URL.class, 
    URLSearchParams.class, UserProximityEvent.class, ValidityState.class, VideoPlaybackQuality.class, 
    VTTCue.class, com.gargoylesoftware.htmlunit.javascript.host.media.WaveShaperNode.class, com.gargoylesoftware.htmlunit.javascript.host.WeakMap.class, WeakSet.class, WebGL2RenderingContext.class, 
    WEBGL_compressed_texture_s3tc.class, com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.WEBGL_debug_renderer_info.class, com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLActiveInfo.class, WebGLBuffer.class, 
    WebGLContextEvent.class, WebGLFramebuffer.class, WebGLProgram.class, WebGLRenderbuffer.class, 
    com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLRenderingContext.class, WebGLShader.class, WebGLShaderPrecisionFormat.class, com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLTexture.class, 
    com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLUniformLocation.class, WebKitAnimationEvent.class, 
    com.gargoylesoftware.htmlunit.javascript.host.css.WebKitCSSMatrix.class, webkitMediaStream.class, WebKitMutationObserver.class, 
    com.gargoylesoftware.htmlunit.javascript.host.media.rtc.webkitRTCPeerConnection.class, com.gargoylesoftware.htmlunit.javascript.host.speech.webkitSpeechGrammar.class, 
    webkitSpeechGrammarList.class, webkitSpeechRecognition.class, webkitSpeechRecognitionError.class, 
    webkitSpeechRecognitionEvent.class, WebKitTransitionEvent.class, webkitURL.class, 
    WebSocket.class, com.gargoylesoftware.htmlunit.javascript.host.event.WheelEvent.class, Window.class, Worker.class, com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument.class, 
    XMLHttpRequest.class, XMLHttpRequestEventTarget.class, XMLHttpRequestUpload.class, XMLSerializer.class, 
    XPathEvaluator.class, XPathExpression.class, 
    XPathNSResolver.class, com.gargoylesoftware.htmlunit.javascript.host.dom.XPathResult.class, XSLTProcessor.class };
  


  private static final java.util.Map<BrowserVersion, JavaScriptConfiguration> CONFIGURATION_MAP_ = new java.util.WeakHashMap();
  



  protected JavaScriptConfiguration(BrowserVersion browser)
  {
    super(browser);
  }
  





  public static synchronized JavaScriptConfiguration getInstance(BrowserVersion browserVersion)
  {
    if (browserVersion == null) {
      throw new IllegalStateException("BrowserVersion must be defined");
    }
    JavaScriptConfiguration configuration = (JavaScriptConfiguration)CONFIGURATION_MAP_.get(browserVersion);
    
    if (configuration == null) {
      configuration = new JavaScriptConfiguration(browserVersion);
      CONFIGURATION_MAP_.put(browserVersion, configuration);
    }
    return configuration;
  }
  
  protected Class<? extends SimpleScriptable>[] getClasses()
  {
    return CLASSES_;
  }
}
