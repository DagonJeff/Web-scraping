package com.cleaner;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileCleaner {
	
	public static void cleanUp(Path dir) {
		
		try {
			Files.walk(dir).filter(Files::isRegularFile).forEach(path -> {
				try {	
					Files.delete(path);
					
				} catch (Exception e) {
					System.err.println("Falha ao tentar deletar: "+path.getFileName()+" "+ e.getMessage());
				}
			});
			Files.deleteIfExists(dir);
			
		} catch (Exception e) {
			System.err.println("Falha ao tentar limpar o diretórtio "+e.getMessage());
		}
	}

}
