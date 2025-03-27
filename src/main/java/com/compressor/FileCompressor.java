package com.compressor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class FileCompressor {
	
	
//=======================Compactar===================================================================//	
	public static void compressFiles(String srcDirPath, String zipFilePath)throws IOException{
		
		System.out.println("Compactando arquivos");
		
		Path zipPath = Paths.get(zipFilePath);
		
		try(ArchiveOutputStream arch = new ZipArchiveOutputStream(Files.newOutputStream(zipPath))){
			
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
	}
	
//=======================Descompactar===============================================================//	
	public static void decompressFile() {
		
	}

}
