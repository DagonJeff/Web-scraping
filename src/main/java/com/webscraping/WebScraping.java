package com.webscraping;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.cleaner.FileCleaner;
import com.compressor.FileCompressor;
import com.download.Scraper;
import com.extractor.PdfExtractor;

public class WebScraping {
	
	private static final String URL = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
	private static final String DOWNLOAD_DIRECTORY = "temp_downloads/";
	private static final String ZIP_NAME = "anexos.zip";
	private static final String COMPRESS_DIRECTORY = "arquivos_compactados/";
	private static final String URL_ANEXO_I = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos/Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf";
	private static final String URL_ANEXO_II = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos/Anexo_II_DUT_2021_RN_465.2021_RN628.2025_RN629.2025.pdf";
	private static final String NAME_ANEXO_I = "Anexo_I.pdf";
	private static final String NAME_ANEXO_II = "Anexo_II.pdf";
	private static final String DECOMPRESS_DIRECTORY = "arquivos_descompactados/";
	private static final String CSV_PATH = "Rol_de_Procedimentos_e_Eventos_em_Saude.csv";
	private static final String CSV_ZIP_NAME = "Teste_Jefferson.zip";
	
	
	
	
	public static void main(String[] args) {
		
		try {
			Path downloadDirPath = Paths.get(DOWNLOAD_DIRECTORY);
			if(!downloadDirPath.toFile().exists()) Files.createDirectories(downloadDirPath);
			
			Scraper.downloadFiles(URL, DOWNLOAD_DIRECTORY, URL_ANEXO_I, NAME_ANEXO_I);
			Scraper.downloadFiles(URL, DOWNLOAD_DIRECTORY, URL_ANEXO_II, NAME_ANEXO_II);

			FileCompressor.compressFiles(DOWNLOAD_DIRECTORY,COMPRESS_DIRECTORY, ZIP_NAME);
			
			FileCompressor.decompressFile(COMPRESS_DIRECTORY+ZIP_NAME, DECOMPRESS_DIRECTORY, NAME_ANEXO_I);
					
			PdfExtractor.extractTableToCSV(DECOMPRESS_DIRECTORY+NAME_ANEXO_I, CSV_PATH);
			
			FileCompressor.compressFiles(CSV_PATH, COMPRESS_DIRECTORY, CSV_ZIP_NAME);
			
			System.out.println("Processo concluído!");
			
		} catch (IOException e) {
			
			System.err.println("Erro ao tentar realizar operação: "+e.getMessage());
			e.printStackTrace();
			
		}catch (Exception e) {
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
