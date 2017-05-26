package com.github.wycm.hpp.proxy.task;

import com.github.wycm.hpp.http.entity.Page;
import com.github.wycm.hpp.proxy.entity.Proxy;
import com.github.wycm.hpp.http.parser.ListPageParser;
import com.github.wycm.hpp.proxy.ProxyHttpClient;
import com.github.wycm.hpp.proxy.ProxyPool;
import com.github.wycm.hpp.proxy.entity.Direct;
import com.github.wycm.hpp.proxy.site.ProxyListPageParserFactory;
import com.github.wycm.hpp.proxy.util.Constants;
import com.github.wycm.hpp.http.util.HttpClientUtil;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

import static com.github.wycm.hpp.proxy.ProxyPool.proxyQueue;
import static com.github.wycm.hpp.proxy.util.Constants.PROXY_TEST_URL;


/**
 * 下载代理网页并解析
 * 若下载失败，通过代理去下载代理网页
 */
public class ProxyPageTask implements Runnable{
	private static Logger logger = Logger.getLogger(ProxyPageTask.class);
	protected String url;
	private boolean proxyFlag;//是否通过代理下载
	private Proxy currentProxy;//当前线程使用的代理
	private String pageCharset = "utf-8";
	protected static ProxyHttpClient proxyHttpClient = ProxyHttpClient.getInstance();
	private ProxyPageTask(){

	}
	public ProxyPageTask(String url, boolean proxyFlag){
		this.url = url;
		this.proxyFlag = proxyFlag;
	}
	public ProxyPageTask(String url, boolean proxyFlag, String pageCharset){
		this.url = url;
		this.proxyFlag = proxyFlag;
		this.pageCharset = pageCharset;
	}
	public void run(){
		long requestStartTime = System.currentTimeMillis();
		HttpGet tempRequest = null;
		try {
			Page page = null;
			if (proxyFlag){
				tempRequest = new HttpGet(url);
				currentProxy = proxyQueue.take();
				if(!(currentProxy instanceof Direct)){
					HttpHost proxy = new HttpHost(currentProxy.getIp(), currentProxy.getPort());
					tempRequest.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
				}
				page = proxyHttpClient.getWebPage(tempRequest, pageCharset);
			}else {
				page = proxyHttpClient.getWebPage(url, pageCharset);
			}
			int status = page.getStatusCode();
			long requestEndTime = System.currentTimeMillis();
			String logStr = Thread.currentThread().getName() + " " + getProxyStr(currentProxy) +
					"  executing request " + page.getUrl()  + " response statusCode:" + status +
					"  request cost time:" + (requestEndTime - requestStartTime) + "ms";
			if(status == HttpStatus.SC_OK){
				logger.debug(logStr);
				handle(page);
			} else {
				logger.error(logStr);
				Thread.sleep(100);
				retry();
			}
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		} catch (IOException e) {
			retry();
		} finally {
			if(currentProxy != null){
				currentProxy.setTimeInterval(Constants.TIME_INTERVAL);
				proxyQueue.add(currentProxy);
			}
			if (tempRequest != null){
				tempRequest.releaseConnection();
			}
		}
	}

	/**
	 * retry
	 */
	public void retry(){
		proxyHttpClient.getProxyDownloadThreadExecutor().execute(new ProxyPageTask(url, true, pageCharset));
	}

	public void handle(Page page){
		ListPageParser<Proxy> parser = ProxyListPageParserFactory.
				getProxyListPageParser(ProxyPool.proxyMap.get(url));
		List<Proxy> proxyList = parser.parseListPage(page);
		for(Proxy p : proxyList){
			if (!ProxyPool.proxySet.contains(p.getProxyStr())){
				proxyHttpClient.getProxyTestThreadExecutor().execute(new ProxyTestTask(p, proxyHttpClient, PROXY_TEST_URL));
			}
		}
	}

	private String getProxyStr(Proxy proxy){
		if (proxy == null){
			return "";
		}
		return proxy.getIp() + ":" + proxy.getPort();
	}
}