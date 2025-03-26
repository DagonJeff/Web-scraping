package com.webscraping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.cleaner.FileCleaner;
import com.compressor.FileCompressor;
import com.download.Scraper;

public class WebScraping {
	
	private static final String URL = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
	private static final String DOWNLOAD_DIRECTORY = "temp_downloads/";
	private static final String ZIP_PATH = "arquivos_anexados.zip";
	
	
	
	public static void main(String[] args) {
		
		try {
			Path downloadDirPath = Paths.get(DOWNLOAD_DIRECTORY);
			if(!downloadDirPath.toFile().exists()) Files.createDirectories(downloadDirPath);
			
			Scraper.downloadAnexos(URL, DOWNLOAD_DIRECTORY);
			FileCompressor.compressFiles(DOWNLOAD_DIRECTORY, ZIP_PATH);
			
			System.out.println("Processo concluído com sucesso!");
			
		} catch (Exception e) {
			
			System.err.println("Erro ao tentar realizar operação: "+e.getMessage());
			e.printStackTrace();
			
		}
		finally {
			try {
				FileCleaner.cleanUp(Paths.get(DOWNLOAD_DIRECTORY));
				System.out.println("Limpeza finalizada!");
			} catch (IOException e) {
				System.err.println("Falha ao limpar diretório. "+e.getMessage());
				e.printStackTrace();
			}
		}
		
	}

}
