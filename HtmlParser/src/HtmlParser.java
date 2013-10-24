import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.io.ObjectInputStream.GetField;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HtmlParser {

	private String fName;
	private String [] headers;
	private String [] info;
	private int levels = 3;

	public HtmlParser(String f, String [] headers) {
		fName = f;
		this.headers = headers;
		this.info = getInfoArray();
	}


	public String getInfo(Document doc, String title) {
		Elements els = doc.getElementsContainingOwnText(title);
		String info = "";
		if(!els.isEmpty()){
			Element e = els.get(0);
			for (int i = 0; i < levels ; i++) {
				info = getText(e.nextElementSibling());
				if(info != ""){
					break;
				}
				e = e.parent();
			}
		} 
		return info.replace("\"", "\\\"");
	}
	
		
	private String getText(Element e) {
		if(e!=null){
			return e.text();
		}
		return "";
	}

	private String[] getInfoArray() {
		String [] info = new String[headers.length];
		try {
			File input = new File(fName);
			Document doc = Jsoup.parse(input, "windows-1255", "http://example.com/");
			for (int i = 0; i < headers.length; i++) {
				info[i] = getInfo(doc, headers[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}
	
	private void info2Csv() {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("info.txt",true)));
			for (int i = 0; i < headers.length; i++) {
				writer.print("\""+headers[i]+"\",");
				
			}
			writer.print("\n");
			for (int i = 0; i < info.length; i++) {
				writer.print("\""+info[i]+"\",");
			}
			writer.print("\n");
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String [] headers = {"Day & Time & Location:","First Lecture:","No. of planned sessions:"};
		HtmlParser h = new HtmlParser("C:\\Users\\Jordan\\Downloads\\parse.htm", headers);
		h.info2Csv();
	}

}
