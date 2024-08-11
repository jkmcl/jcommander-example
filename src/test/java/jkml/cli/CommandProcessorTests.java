package jkml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

class CommandProcessorTests {

	@Test
	void testRun_invalidArgs() {
		assertEquals(CommandProcessor.FAILURE, CommandProcessor.run(""));
	}

	@Test
	void testRun_copy() throws IOException {
		var outDir = TestUtils.createOutputDirectory();
		var source = outDir.resolve("source.txt");
		var target = outDir.resolve("target.txt");
		Files.deleteIfExists(source);
		Files.deleteIfExists(target);
		Files.createFile(source);

		assertEquals(CommandProcessor.SUCCESS,
				CommandProcessor.run("copy", "-s", source.toString(), "-t", target.toString()));
		assertTrue(Files.exists(source));
		assertTrue(Files.exists(target));
	}

	@Test
	void testRun_move() throws IOException {
		var outDir = TestUtils.createOutputDirectory();
		var source = outDir.resolve("source.txt");
		var target = outDir.resolve("target.txt");
		Files.deleteIfExists(source);
		Files.deleteIfExists(target);
		Files.createFile(source);

		assertEquals(CommandProcessor.SUCCESS,
				CommandProcessor.run("move", "-s", source.toString(), "-t", target.toString()));
		assertTrue(Files.notExists(source));
		assertTrue(Files.exists(target));
	}

	@Test
	void testRun_move_exception() throws IOException {
		var outDir = TestUtils.createOutputDirectory();
		var source = outDir.resolve("source.txt");
		var target = outDir.resolve("target.txt");
		Files.deleteIfExists(source);
		Files.deleteIfExists(target);

		assertEquals(CommandProcessor.FAILURE,
				CommandProcessor.run("move", "-s", source.toString(), "-t", target.toString()));
	}

	@Test
	void testParseCommand_invalid() {
		assertNull(CommandProcessor.parseCommand());
		assertNull(CommandProcessor.parseCommand(""));
		assertNull(CommandProcessor.parseCommand(" "));
		assertNull(CommandProcessor.parseCommand("unknown"));
	}

	@Test
	void testParseCommand_copy() {
		assertNull(CommandProcessor.parseCommand("copy", "-s", "a.txt"));
		assertInstanceOf(Commands.Copy.class, CommandProcessor.parseCommand("copy", "-s", "a.txt", "-t", "b.txt"));
	}

	@Test
	void testParseCommand_move() {
		assertNull(CommandProcessor.parseCommand("move", "-t", "b.txt"));
		assertInstanceOf(Commands.Move.class, CommandProcessor.parseCommand("move", "-s", "a.txt", "-t", "b.txt"));
	}

}
