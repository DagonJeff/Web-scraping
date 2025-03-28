package com.extractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.opencsv.CSVWriter;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;


public class PdfExtractor {
	
	public static void extractTableToCSV(String pdfPath, String csvPath) throws IOException{
		
		System.out.println("Extraindo os dados da tabela do arquivo pdf e salvando em CSV...");

		PDDocument doc = PDDocument.load(new File(pdfPath));
		ObjectExtractor extractor = new ObjectExtractor(doc);
		SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
		
		try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(csvPath), StandardCharsets.UTF_8)) {
			osw.write("\uFEFF");
			try (CSVWriter csvWriter = new CSVWriter(osw, ';', CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
				extractTablesToCSV(extractor, sea, csvWriter);
				
				System.out.println("Arquivo CSV criado com sucesso!");
			}
		} finally {
			doc.close();
			extractor.close();
		}
	}
	
	private static void extractTablesToCSV(ObjectExtractor extractor, SpreadsheetExtractionAlgorithm sea, CSVWriter csvWriter) throws IOException {
		PageIterator pages = extractor.extract();
		
		while (pages.hasNext()) {
			
			Page page = pages.next();
			List<Table> tables = sea.extract(page);
			
			for (Table table : tables) {
				boolean isHeader = true;
				
				for (List<RectangularTextContainer> row : table.getRows()) {
					String[] csvRow = new String[row.size()];
					
					for (int i = 0; i < row.size(); i++) {
						String cellText = row.get(i).getText().trim();
						cellText = convertCell(cellText, isHeader);
						csvRow[i] = cellText;
					}
					csvWriter.writeNext(csvRow);
					if (isHeader) {
						isHeader = false;
					}
					
					
				}
			}
			
		}
		System.out.println("Dados extraídos!");
	}

	private static String convertCell(String cellText, boolean isHeader) {
		if ("OD".equals(cellText)) {
			cellText = "Seg. Odontológica";
		} else if ("AMB".equals(cellText)) {
			cellText = "Seg. Ambulatorial";
		}
		if (isHeader) {
			cellText = ">> " + cellText + " <<";
		}
		return cellText;
	}
}