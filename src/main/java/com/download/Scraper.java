package com.download;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

import java.net.HttpURLConnection;

import java.net.URL;

public class Scraper {
	
	public static void downloadAnexos(String url, String downDir) throws IOException{
		
		System.out.println("Acessando " + url);
		Document doc = Jsoup.connect(url).get();
		System.out.println("Baizando arquivos...");
		Elements links = doc.select("a[href$=.pdf]");
		
		for(Element link : links) {
			String pdfUrl = link.attr("href");
			String fileName = pdfUrl.substring(pdfUrl.lastIndexOf('/')+1);
			
			if(fileName.contains("Anexo I")|| fileName.contains("Anexo II")) {
				download(pdfUrl, downDir);
				System.out.println("Arquivo :" + fileName + " baixado com sucesso!");
			}
					
		}
		
	}
	private static void download(String pdfUrl, String downloadDir) throws IOException{
		
		URL url = new URL(pdfUrl);
		HttpURLConnection conect = (HttpURLConnection) url.openConnection();
		int responseCode = conect.getResponseCode();
		
		if(responseCode == HttpURLConnection.HTTP_OK) {
			try(InputStream inps = conect.getInputStream()){
				
				String fileName = pdfUrl.substring(pdfUrl.lastIndexOf('/')+1);
				Path otpPath = Paths.get(downloadDir, fileName);
				Files.createDirectories(otpPath.getParent());
				Files.copy(inps, otpPath);
				inps.close();
			}
			
		}else throw new IOException("Falha ao baixar arquivo. Código: "+ responseCode);
		
	}

}
