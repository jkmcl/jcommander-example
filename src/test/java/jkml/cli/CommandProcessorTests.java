package jkml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CommandProcessorTests {

	@TempDir
	static Path tempDir;

	@Test
	void testRun_noArg() {
		assertEquals(CommandProcessor.FAILURE, new CommandProcessor().run());
	}

	@Test
	void testRun_invalidArgs() {
		assertEquals(CommandProcessor.FAILURE, new CommandProcessor().run(""));
		assertEquals(CommandProcessor.FAILURE, new CommandProcessor().run(" "));
		assertEquals(CommandProcessor.FAILURE, new CommandProcessor().run("unknown"));
		assertEquals(CommandProcessor.FAILURE, new CommandProcessor().run("copy"));
	}

	@Test
	void testRun_copy() throws IOException {
		var source = tempDir.resolve("source.txt");
		var target = tempDir.resolve("target.txt");
		Files.deleteIfExists(source);
		Files.deleteIfExists(target);
		Files.createFile(source);

		assertEquals(CommandProcessor.SUCCESS,
				new CommandProcessor().run("copy", "-s", source.toString(), "-t", target.toString()));
		assertTrue(Files.exists(source));
		assertTrue(Files.exists(target));
	}

	@Test
	void testRun_move() throws IOException {
		var source = tempDir.resolve("source.txt");
		var target = tempDir.resolve("target.txt");
		Files.deleteIfExists(source);
		Files.deleteIfExists(target);
		Files.createFile(source);

		assertEquals(CommandProcessor.SUCCESS,
				new CommandProcessor().run("move", "-s", source.toString(), "-t", target.toString()));
		assertTrue(Files.notExists(source));
		assertTrue(Files.exists(target));
	}

	@Test
	void testRun_move_exception() throws IOException {
		var source = tempDir.resolve("source.txt");
		var target = tempDir.resolve("target.txt");
		Files.deleteIfExists(source);
		Files.deleteIfExists(target);

		assertEquals(CommandProcessor.FAILURE,
				new CommandProcessor().run("move", "-s", source.toString(), "-t", target.toString()));
	}

}
