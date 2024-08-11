package jkml.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

class CommandParserTests {

	private static String[] args(String str) {
		return str.split("\\s+");
	}

	private Object parseCommand(String commandLine) {
		return CommandParser.parseCommand(args(commandLine));
	}

	@Test
	void testRun_invalidArgs() {
		assertEquals(CommandParser.FAILURE, CommandParser.run(""));
	}

	@Test
	void testRun_copy() throws IOException {
		var outDir = TestUtils.createOutputDirectory();
		var source = outDir.resolve("source.txt");
		var target = outDir.resolve("target.txt");
		Files.deleteIfExists(source);
		Files.deleteIfExists(target);
		Files.createFile(source);

		assertEquals(CommandParser.SUCCESS,
				CommandParser.run("copy", "-s", source.toString(), "-t", target.toString()));
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

		assertEquals(CommandParser.SUCCESS,
				CommandParser.run("move", "-s", source.toString(), "-t", target.toString()));
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

		assertEquals(CommandParser.FAILURE,
				CommandParser.run("move", "-s", source.toString(), "-t", target.toString()));
	}

	@Test
	void testParseCommand() {
		assertNull(CommandParser.parseCommand());
		assertNull(CommandParser.parseCommand(""));
		assertNull(CommandParser.parseCommand(" "));
		assertNull(CommandParser.parseCommand("unknown a b"));

		var cmd = parseCommand("copy -s a.txt -t b.txt");
		assertTrue(cmd instanceof Commands.Copy);

		cmd = parseCommand("move -s a.txt -t b.txt");
		assertTrue(cmd instanceof Commands.Move);
	}

}
