package jkml.cli;

import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class CommandProcessor {

	private static final Logger logger = LoggerFactory.getLogger(CommandProcessor.class);

	static final int SUCCESS = 0;

	static final int FAILURE = 1;

	public static void main(String[] args) {
		System.exit(run(args));
	}

	static int run(String... args) {
		try {
			var cmd = parseCommand(args);
			if (cmd != null) {
				return executeCommand(cmd);
			}
		} catch (Exception e) {
			logger.error("Exception occurred", e);
		}
		return FAILURE;
	}

	static Object parseCommand(String... args) {
		var jc = JCommander.newBuilder()
				.addCommand(new Commands.Copy())
				.addCommand(new Commands.Move())
				.build();

		if (args.length == 0) {
			jc.usage();
			return null;
		}

		try {
			jc.parse(args);
		} catch (ParameterException e) {
			System.err.println("Invalid argument: " + e.getMessage());
			return null;
		}
		return jc.getCommands().get(jc.getParsedCommand()).getObjects().get(0);
	}

	private static int executeCommand(Object command) throws IOException {
		if (command instanceof Commands.Copy copy) {
			logger.info("Copying file from {} to {}", copy.getSource(), copy.getTarget());
			Files.copy(copy.getSource(), copy.getTarget());
		} else if (command instanceof Commands.Move move) {
			logger.info("Moving file from {} to {}", move.getSource(), move.getTarget());
			Files.move(move.getSource(), move.getTarget());
		} else {
			throw new IllegalArgumentException("Unsupported command: {}" + command.getClass().getName());
		}
		return SUCCESS;
	}

}
