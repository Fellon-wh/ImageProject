package com.feilong.spider.getURL;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLUtil {
	public static final Log log = LogFactory.getLog(URLUtil.class);
	List<URL> urls = new ArrayList<URL>();
	
	/**
	 * 
	 * @Description: TODO(爬取网页中的URL)
	 * @param url 地址
	 * @return
	 */
	public String SpiderURL(String href) {
		String absHref = null;
		Connection con;
		if(href!=null&&href!="") {    //判断 href 地址是否有效
			con = Jsoup.connect(href);
			int state = -1;
			try {
				state = con.execute().statusCode();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("state..."+state);
			if(state != HttpStatus.SC_OK) {
				return null;
			}
		}else {
			return "kk";
		}
		try {
			Document doc = con.timeout(5000).get();
			Elements links = doc.select("a[href]");   //带有 href 属性的 a 元素
			for (Element link : links) {
				absHref = link.attr("abs:href");      //相对路径
				System.out.println("相对路径: "+absHref);
				URL url = contentOfURL(absHref);
				if(url == null) {
					continue;
				}
				urls.add(url);
				System.out.println(urls.size()+"\n"+url.getUrl()
						+"\n"+url.getTitle()+"\n"+url.getDescription());
			}
		}catch (MalformedURLException e) {
			log.error(e);
		}catch (IOException e) {
			if (e instanceof SocketTimeoutException) {
				log.error(e);
				return null;
			}
	        if(e instanceof UnknownHostException){
	        	log.error(e);    
	        	return null;    
	        }
	        log.error(e);    
	        return null;
		}
		return absHref;
	}
	
	/**
	 * 
	 * @Description: TODO(抓取网页中的属性)
	 * @param href 地址
	 * @return URL对象
	 */
	public URL contentOfURL(String href) {
		URL url = new URL();
		Connection con;
		if(href!=null && href!="") {    //判断 href 地址是否有效
			con = Jsoup.connect(href);
			int state = -1;
			try {
				state = con.execute().statusCode();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("state....."+state);
			if(state != HttpStatus.SC_OK) {
				return null;
			}
		}else {
			return null;
		}
		try {
			Document doc = con.timeout(5000).get();
			Element meta = doc.head().select("meta[name=description]").first();//获取 description 的 meta 标签
			String description = "";
			if(meta!=null) {
				description = meta.attr("content");
			}
			url.setDescription(description);
			String title = doc.title();//获取title
			url.setTitle(title);
			url.setUrl(href);
		}catch (MalformedURLException e) {
			log.error(e);
		}catch (IOException e) {
			if (e instanceof SocketTimeoutException) {
				log.error(e);
				return null;
			}
	        if(e instanceof UnknownHostException){
	        	log.error(e);    
	        	return null;    
	        }
	        log.error(e);    
	        return null;
		}
		return url;
	}
	
	public static void main(String[] args) {
		String url = "http://www.nipic.com/topic/show_27055_1.html";
		URLUtil urlUtil = new URLUtil();
		String href = urlUtil.SpiderURL(url);
		System.out.println(href);
	}
}
