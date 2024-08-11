package jkml.cli;

import java.nio.file.Path;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

class Commands {

	private Commands() {
	}

	@Parameters(commandNames = "copy", commandDescription = "Copy a file to a target file. ")
	public static class Copy {

		@Parameter(names = "-s", description = "Source", required = true)
		private Path source;

		@Parameter(names = "-t", description = "Target", required = true)
		private Path target;

		public Path getSource() {
			return source;
		}

		public Path getTarget() {
			return target;
		}

	}

	@Parameters(commandNames = "move", commandDescription = "Move or rename a file to a target file.")
	public static class Move {

		@Parameter(names = "-s", description = "Source", required = true)
		private Path source;

		@Parameter(names = "-t", description = "Target", required = true)
		private Path target;

		public Path getSource() {
			return source;
		}

		public Path getTarget() {
			return target;
		}

	}

}
