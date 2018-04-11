package com.feilong.spider.getURL;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.feilong.spider.getURL.vo.URL;

public class URLUtil {
	public static final Log log = LogFactory.getLog(URLUtil.class);
	List<URL> urls = new ArrayList<URL>();
	
	/**
	 * 
	 * @Description: TODO(爬取网页中的URL)
	 * @param url 地址
	 * @return List<URL> URL集合
	 */
	public List<URL> SpiderURL(String href) {
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
			if(state != HttpStatus.SC_OK) {  //判断连接状态-->200
				return null;
			}
		}else {
			return null;
		}
		try {
			Document doc = con.timeout(5000).get();
			Elements links = doc.select("a[href]");   //带有 href 属性的 a 元素
			for (Element link : links) {
				absHref = link.attr("abs:href");      //相对路径
				URL url = contentOfURL(absHref);
				if(url == null) {
					continue;
				}
				urls.add(url);
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
		return urls;
	}
	
	/**
	 * 
	 * @Description: TODO(抓取网页中的属性)
	 * @param href 地址
	 * @return URL对象
	 */
	public URL contentOfURL(String href) {
		if(href.endsWith("/")) {
			href = href.substring(0, href.length()-1);
		}
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
	
	/**
	 * 
	 * @Description: TODO(通过HashSet删除重复元素)
	 * @param urls Lis<URL> 集合
	 * @return Lis<URL> 集合
	 */
	public List<URL> getNewList(List<URL> urls){
		HashSet<URL> h = new HashSet<URL>(urls);
		urls.clear();
		urls.addAll(h);
		return urls;
	}
	
	public static void main(String[] args) {
		String href = "http://www.nipic.com";
		URLUtil urlUtil = new URLUtil();
		List<URL> urls = urlUtil.SpiderURL(href);
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		SessionFactory sessionFactory = (SessionFactory)context.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		for (URL url : urls) {  //first remove duplication
			String hql = "select u.id from URL u where u.url = ?";
			System.out.println(hql);
			Query query = session.createQuery(hql);
			query.setString(0, url.getUrl());
			
	        System.out.println(url.getUrl()+"\n"+url.getTitle()+"\n"+url.getDescription());
	        if(query.list().size()==0) {  //如果不存在 则 insert
	        	session.save(url);
	        }
		}
		tx.commit();
		session.close();
		sessionFactory.close();
	}
}
