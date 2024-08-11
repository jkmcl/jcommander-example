package jkml.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtils {

	public static final Path OUTPUT_DIRECTORY = Path.of("target/testOutput");

	public static Path createOutputDirectory() throws IOException {
		return Files.createDirectories(OUTPUT_DIRECTORY);
	}

}
