package com.extractor;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfExtractor {
	
	public static void extractTableToCSV(String pdfPath, String csvPath) throws IOException{
		
//		PdfExtractor.extractTableToCSV(DECOMPRESS_DIRECTORY+NAME_ANEXO_I, CSV_PATH);
//		
//		FileCompressor.compressFiles(CSV_PATH, COMPRESS_DIRECTORY, CSV_ZIP_NAME);
		
		System.out.println("Extraindo os dados da tabela do arquivo pdf e salvando em CSV.");
		
		
        PDDocument document = PDDocument.load(new File(pdfPath));
        PDFTextStripper stripper = new PDFTextStripper();
        String textoPDF = stripper.getText(document);
        document.close();

        // Lógica para extrair os dados da tabela do textoPDF
        StringBuilder dadosTabela = new StringBuilder();
        String[] linhas = textoPDF.split(System.lineSeparator());
        Pattern pattern = Pattern.compile("(\\S+\\s+)+\\S+"); // Padrão para detectar linhas de tabelas com colunas variáveis

        for (String linha : linhas) {
            Matcher matcher = pattern.matcher(linha);
            if (matcher.find()) {
                String[] colunas = linha.trim().split("\\s+");

                for (int i = 0; i < colunas.length; i++) {
                    // Substituir abreviações
                    colunas[i] = colunas[i].replace("OD", "Descrição OD").replace("AMB", "Descrição AMB");

                    // Adicionar ao resultado
                    dadosTabela.append(colunas[i]);

                    // Adicionar delimitador de coluna, exceto no último elemento
                    if (i < colunas.length - 1) {
                        dadosTabela.append(",");
                    }
                }
                dadosTabela.append("\n");
            }
        }
        String satan = dadosTabela.toString();
		Files.write(Paths.get(csvPath), satan.getBytes());
        
		
//		try(PDDocument doc = PDDocument.load(new File(pdfPath))){
//			
//			if(!doc.isEncrypted()) {
//				PDFTextStripperByArea stp = new PDFTextStripperByArea();
//				stp.setSortByPosition(true);
//				
//				PDFTextStripper txtStp = new PDFTextStripper();
//				txtStp.setSortByPosition(true);
//				txtStp.setStartPage(0);
//				txtStp.setEndPage(doc.getNumberOfPages());
//				
//				String pdfTxt = txtStp.getText(doc);
//				String[] lines = pdfTxt.split(System.lineSeparator());
//				
//				try(FileWriter wrt = new FileWriter(csvPath)){
//					
//					for (String line : lines) {
//						String csvLine = formatTextCSV(line);
//						wrt.write(csvLine + "\n");
//					}
//				}
//			}
			
			
//			PDFTextStripper stp = new PDFTextStripper() {
//				
//				@Override
//				protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
//					super.writeString(formatTextCSV(text), textPositions);
//					}
//				};
//				stp.setStartPage(0);
//				stp.setEndPage(doc.getNumberOfPages());
//				
//				try(FileWriter wrt = new FileWriter(csvPath)){
//					stp.writeText(doc, wrt);
//				
//			}
//		}
	}
	
//	private static String formatTextCSV(String text) {
//		
//		text = text.replace("OD", "Seg. Odontológica").replace("AMB", "Seg. Ambulatorial");
//		text = text.replaceAll("\\s{2,}", ",");
//		text = text.replace("\n", System.lineSeparator());
//        text = text.replace("\"", "\"\"");
//        text = "\"" + text + "\"";
//		
//		return text;
//	}

}
