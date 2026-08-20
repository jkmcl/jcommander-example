package jkml.cli;

import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class CommandProcessor {

	static final int SUCCESS = 0;

	static final int FAILURE = 1;

	private static final Logger logger = LoggerFactory.getLogger(CommandProcessor.class);

	public static void main(String[] args) {
		System.exit(new CommandProcessor().run(args));
	}

	int run(String... args) {
		try {
			return doRun(args);
		} catch (Exception e) {
			logger.error("Exception occurred", e);
			return FAILURE;
		}
	}

	private static String usage(JCommander jc) {
		var sb = new StringBuilder();
		jc.usage(sb);
		return sb.toString();
	}

	private int doRun(String... args) throws IOException {
		//@formatter:off
		var jc = JCommander.newBuilder()
				.programName(CommandProcessor.class.getName())
				.addCommand(new Commands.Copy())
				.addCommand(new Commands.Move())
				.build();
		//@formatter:on

		try {
			jc.parse(args);
		} catch (ParameterException e) {
			System.err.println("Invalid argument: " + e.getMessage());
			return FAILURE;
		}
		if (jc.getParsedCommand() == null) {
			System.err.println(usage(jc));
			return FAILURE;
		}

		var command = jc.getCommands().get(jc.getParsedCommand()).getObjects().get(0);
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
