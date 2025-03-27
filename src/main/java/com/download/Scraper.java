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
	
	
	public static void downloadFiles(String url, String downDir, String fileUrl, String spcFileName) throws IOException{
		
		Document doc = Jsoup.connect(url).get();
		System.out.println("Buscando arquivos a serem baixados...");
		Elements links = doc.select("a[href$=.pdf]");
		
		for(Element link : links) {
			String pdfUrl = link.attr("href");
			String fileName = fileUrl.substring(fileUrl.lastIndexOf('/')+1);
						
			if(fileUrl != null && !fileUrl.isEmpty()) {
				if(pdfUrl.equals(fileUrl)) {
					download(pdfUrl, downDir, spcFileName);
					System.out.println("Arquivo: "+spcFileName+" baixado com sucesso!");
					break;
					}
				}else {
					download(pdfUrl, downDir, fileName);
					System.out.println("Arquivo: "+fileName+" baixado com sucesso!");
				}
					
		}
	}
	private static void download(String pdfUrl, String downloadDir, String newFileName) throws IOException{
		
		URL url = new URL(pdfUrl);
		HttpURLConnection conect = (HttpURLConnection) url.openConnection();
		int responseCode = conect.getResponseCode();
		
		if(responseCode == HttpURLConnection.HTTP_OK) {
			try(InputStream inps = conect.getInputStream()){
				
				Path otpPath = Paths.get(downloadDir, newFileName);
				Files.createDirectories(otpPath.getParent());
				Files.copy(inps, otpPath, StandardCopyOption.REPLACE_EXISTING);
				inps.close();
			}finally {
				conect.disconnect();
			}
			
		}else throw new IOException("Falha ao baixar arquivo. Código: "+ responseCode);
		
	}

}
