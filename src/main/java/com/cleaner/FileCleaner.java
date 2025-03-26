package com.cleaner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileCleaner {
	
	public static void cleanUp(Path dir) throws IOException {
		
		try(Stream<Path> paths = Files.walk(dir)) {
			paths.filter(Files::isRegularFile).forEach(path -> {
				try {	
					Files.delete(path);
					
				} catch (Exception e) {
					System.err.println("Falha ao tentar deletar: "+path.getFileName()+" "+ e.getMessage());
				}
			});
			Files.deleteIfExists(dir);
			
		} catch (Exception e) {
			System.err.println("Falha ao tentar limpar o diretórtio "+e.getMessage());
			throw e;
		}
	}

}
