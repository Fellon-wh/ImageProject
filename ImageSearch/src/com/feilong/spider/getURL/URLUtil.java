package com.feilong.spider.getURL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLUtil {
	public String SpiderURL(String url) {
		String relHref = null;
		String absHref = null;
		try {
			Document doc = Jsoup.connect(url).timeout(2000).get();
			Elements links = doc.select("a[href]");//带有href属性的a元素
			/*Elements pngs = doc.select("img[src$=.png]");
			  //扩展名为.png的图片*/
			// String regex = "(http|https)://[\\w+\\.?/?]+\\.[A-Za-z]+";  
			for (Element link : links) {
				absHref = link.attr("abs:href");//相对路径
				System.out.println("相对路径: "+absHref);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return absHref;
	}
	
	public static void main(String[] args) {
		String url = "http://www.nipic.com";
		URLUtil urlUtil = new URLUtil();
		String href = urlUtil.SpiderURL(url);
		System.out.println("dddd"+href);
	}
}
