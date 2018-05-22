package cn.wx.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
//    private static HashMap<String,
//                           HttpClient> sessionMap = new HashMap<String,
//            HttpClient>();
//
//    public HttpUtil() {
//    }
//    
//    /*
//     * 将形如“\u7cfb\u7edf\u7e41\u5fd9\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\u3002”的字符串解码
//     * */
//	public static String decode(String s) {
//		StringReader s1 = new StringReader(s);
//		try {
//			char[] chars = new char[s.length()];
//			s1.read(chars);
//			return new String(chars);
//		} catch (Exception ex) {
//		}
//		return null;
//	}
//
//    /**
//     * get  httpGet
//     *
//     * @param url String
//     * @param name String     连接名称，用以维护session,不需要维持连接时请置null，操作结束后请调用destroy方法
//     * @param charset String
//     * @return String
//     */
//    public static String get(String url, String name, String charset) {
//        if (charset == null)
//            charset = HTTP.UTF_8;
//
//        try {
//            HttpClient httpclient;
//
//            if (name == null) { //不需要维持连接
//                httpclient = createHttpClient();
//            } else if (sessionMap.containsKey(name)) {
//                httpclient = sessionMap.get(name);
//            } else {
//                httpclient = createHttpClient();
//                sessionMap.put(name, httpclient);
//            }
//            
//            httpclient.getParams().setParameter(
//            	    HttpMethodParams.USER_AGENT,
//            	    "Mozilla/5.0 (X11; U; Android i686; zh-CN; rv:1.9.1.2) Gecko/20090803");
//            try {
//            	 httpclient.getParams().setParameter("referer","http://m.95081.com");
//			} catch (Exception e) {}
//           
//            
//            HttpGet httpget1 = new HttpGet(url);
//            
//            
//            
//            HttpResponse response1 = httpclient.execute(httpget1);
//            HttpEntity entity1 = response1.getEntity();
//
//            return EntityUtils.toString(entity1, charset);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    
//    /**
//     * post
//     *
//     * @param url String
//     * @param map HashMap     提交表单的键值对
//     * @param name String     连接名称，用以维护session
//     * @param charset String
//     * @return String
//     */
//    public static String post(String url, HashMap<String, String> map,
//            String name, String charset) {
//        if (charset == null)
//            charset = HTTP.UTF_8;
//
//        try {
//            HttpClient httpclient;
//
//            if (name == null) { //不需要维持连接
//                httpclient = createHttpClient();
//            } else if (sessionMap.containsKey(name)) {
//                httpclient = sessionMap.get(name);
//            } else {
//                httpclient = createHttpClient();
//                sessionMap.put(name, httpclient);
//            }
//
//            HttpPost httpost = new HttpPost(url);
//
//            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//
//            if (map != null) {
//                Iterator it = map.keySet().iterator();
//
//                while (it.hasNext()) {
//                    String key = (String) it.next();
//                    nvps.add(new BasicNameValuePair(key, map.get(key)));
//                }
//            }
//
//            httpost.setEntity(new UrlEncodedFormEntity(nvps, charset));
//
//            HttpResponse response = httpclient.execute(httpost);
//            HttpEntity entity1 = response.getEntity();
////            entity1.consumeContent();
//            return EntityUtils.toString(entity1, charset);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//
//    public static String post(String url, String content,
//                              String name, String charset) {
//        if (charset == null)
//            charset = HTTP.UTF_8;
//
//        try {
//            HttpClient httpclient;
//
//            if (name == null) { //不需要维持连接
//                httpclient = createHttpClient();
//            } else if (sessionMap.containsKey(name)) {
//                httpclient = sessionMap.get(name);
//            } else {
//                httpclient = createHttpClient();
//                sessionMap.put(name, httpclient);
//            }
//
//            HttpPost httpost = new HttpPost(url);
//
//            if (content != null) {
//                httpost.setEntity(new StringEntity(content,charset));
//            }
//            
//            HttpResponse response = httpclient.execute(httpost);
//            
//            HttpEntity entity1 = response.getEntity();
//            //entity1.consumeContent();
//            return EntityUtils.toString(entity1, charset);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//	public static String postJson(String url, String content, String name, String charset) {
//		if (charset == null)
//			charset = HTTP.UTF_8;
//
//		try {
//			HttpClient httpclient;
//
//			if (name == null) { // 不需要维持连接
//				httpclient = createHttpClient();
//			} else if (sessionMap.containsKey(name)) {
//				httpclient = sessionMap.get(name);
//			} else {
//				httpclient = createHttpClient();
//				sessionMap.put(name, httpclient);
//			}
//
//			HttpPost httpost = new HttpPost(url);
//
//			if (content != null) {
//				httpost.setEntity(new StringEntity(content, charset));
//			}
//			httpost.addHeader("Content-Type", "application/json;charset=utf-8");
//
//			HttpResponse response = httpclient.execute(httpost);
//
//			HttpEntity entity1 = response.getEntity();
//			// entity1.consumeContent();
//			return EntityUtils.toString(entity1, charset);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//    
//
//    public static void destroy(String name) {
//        HttpClient httpclient = sessionMap.get(name);
//        httpclient.getConnectionManager().shutdown();
//        sessionMap.remove(name);
//    }
//
//    private static HttpClient createHttpClient() {
//        DefaultHttpClient httpclient = new DefaultHttpClient();
//        System.getProperties().setProperty("httpclient.useragent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; CIBA; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
//
//        try {
//
//            httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
//
//                public void process(
//                        final HttpRequest request,
//                        final HttpContext context) throws HttpException,
//                        IOException {
//                    if (!request.containsHeader("Accept-Encoding")) {
//                        request.addHeader("Accept-Encoding", "gzip");
//                    }
//                }
//
//            });
//
//            httpclient.addResponseInterceptor(new HttpResponseInterceptor() {
//
//                public void process(
//                        final HttpResponse response,
//                        final HttpContext context) throws HttpException,
//                        IOException {
//                    HttpEntity entity = response.getEntity();
//                    Header ceheader = entity.getContentEncoding();
//                    if (ceheader != null) {
//                        HeaderElement[] codecs = ceheader.getElements();
//                        for (int i = 0; i < codecs.length; i++) {
//                            if (codecs[i].getName().equalsIgnoreCase("gzip")) {
//                                response.setEntity(
//                                        new GzipDecompressingEntity(response.
//                                        getEntity()));
//                                return;
//                            }
//                        }
//                    }
//                }
//
//            });
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return httpclient;
//
//    }
//    
//    static class GzipDecompressingEntity extends HttpEntityWrapper {
//
//        public GzipDecompressingEntity(final HttpEntity entity) {
//            super(entity);
//        }
//
//        @Override
//        public InputStream getContent() throws IOException,
//                IllegalStateException {
//
//            // the wrapped entity's getContent() decides about repeatability
//            InputStream wrappedin = wrappedEntity.getContent();
//
//            return new GZIPInputStream(wrappedin);
//        }
//
//        @Override
//        public long getContentLength() {
//            // length of ungzipped content is not known
//            return -1;
//        }
//
//    }

 
}
