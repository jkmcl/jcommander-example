package jkml.cli;

import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

public class CommandParser {

	private static final Logger logger = LoggerFactory.getLogger(CommandParser.class);

	static final int SUCCESS = 0;

	static final int FAILURE = 1;

	public static void main(String... args) {
		System.exit(run(args));
	}

	public static int run(String... args) {
		try {
			return runInternal(args);
		} catch (Exception e) {
			logger.error("Exception occurred", e);
			return FAILURE;
		}
	}

	static Object parseCommand(String... args) {
		var jc = JCommander.newBuilder()
				.addCommand(new Commands.Copy())
				.addCommand(new Commands.Move())
				.build();
		try {
			jc.parse(args);
			return jc.getCommands().get(jc.getParsedCommand()).getObjects().get(0);
		} catch (Exception e) {
			jc.usage();
			return null;
		}
	}

	public static int runInternal(String... args) throws IOException {
		var command = parseCommand(args);

		if (command == null) {
			return FAILURE;
		} else if (command instanceof Commands.Copy copy) {
			Files.copy(copy.getSource(), copy.getTarget());
		} else if (command instanceof Commands.Move move) {
			Files.move(move.getSource(), move.getTarget());
		} else {
			throw new IllegalArgumentException("Unsupported command: {}" + command.getClass().getName());
		}

		return SUCCESS;
	}

}
