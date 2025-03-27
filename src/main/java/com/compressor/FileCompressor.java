package com.compressor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class FileCompressor {
	
	
//=======================Compactar===================================================================//	
	public static void compressFiles(String srcDirPath,String destDir, String zipFileName)throws IOException{
		
		System.out.println("Compactando arquivos");
		Path destDirPath = Paths.get(destDir);
		
		try {
			if(!Files.exists(destDirPath)) Files.createDirectories(destDirPath);
			
			Path zipFilePath = destDirPath.resolve(zipFileName);
//		Path zipPath = Paths.get(zipFilePath);
		
		try(ArchiveOutputStream arch = new ZipArchiveOutputStream(Files.newOutputStream(zipFilePath))){
			
			Path srcDir = Paths.get(srcDirPath);
			Files.walk(srcDir).filter(path -> !Files.isDirectory(path)).forEach(path -> {
				ArchiveEntry ent = new ZipArchiveEntry(srcDir.relativize(path).toString());
				
				try {
					arch.putArchiveEntry(ent);
					Files.copy(path, arch);
					arch.closeArchiveEntry();
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			});
			}
		}catch (IOException e) {
			throw new IOException("Compactação Falhou: "+e.getMessage(), e);
		}
	}
	
//=======================Descompactar===============================================================//	
	public static void decompressFile(String zipFilePath, String destDir, String spcFileName) throws IOException{
		
		System.out.println("Descompactando arquivos...");
		
		File dir = new File(destDir);
		if(!dir.exists()) dir.mkdirs();
		
		try(ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))){
			
			ZipEntry ent = zipIn.getNextEntry();
			
			while(ent != null) {
				
				String filePath = destDir+File.separator+ent.getName();
				if (!ent.isDirectory() && (spcFileName == null || spcFileName.isEmpty() || ent.getName().equals(spcFileName))) {
                    extractFile(zipIn, filePath);
                    if (spcFileName != null && !spcFileName.isEmpty()) {
                        break; 
                    }
                } else if (ent.isDirectory() && (spcFileName == null || spcFileName.isEmpty())) {
                    File dirFile = new File(filePath);
                    dirFile.mkdirs();
                }
                zipIn.closeEntry();
                ent = zipIn.getNextEntry();
				
			}
		}catch (IOException e) {
			throw new IOException("Descompactação Falhou: "+e.getMessage(), e);
		}
		
	}
	
	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException{
		
		try(BufferedOutputStream buf = new BufferedOutputStream(new FileOutputStream(filePath))){
			byte[] bt = new byte[4096];
			int read;
			
			while((read = zipIn.read(bt)) != -1) buf.write(bt, 0, read);
		}
		
	}

}
