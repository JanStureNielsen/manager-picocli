package manager;

import javax.validation.constraints.NotNull;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IFactory;
import picocli.CommandLine.Option;

@Command(
    name = "manager",
    description = "The manager CLI and shell...",
    showAtFileInUsageHelp = true,
    mixinStandardHelpOptions = true,
    subcommands = {
        ListCommand.class,
        CreateCommand.class,
        DeleteCommand.class
    })
@Component @RequiredArgsConstructor
public class ConnectCommand implements Runnable, ExitCodeGenerator {
	private final @NotNull ShellCommand shell;
	private final @NotNull IFactory picocliFactory;

    @Option(
        names        = {"-u", "--username"},
        description  = "username...")
    private String username;

    @Option(
        names        = {"-p", "--password"},
        description  = "password...")
    private String password;

    private int exitCode;

    @Override
    public void run() {
        System.out.println("Launching shell...");

        exitCode = new CommandLine(shell, picocliFactory).execute(new String[] {});
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

}
