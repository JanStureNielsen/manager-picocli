package manager;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;
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
@Component
public class ConnectCommand implements Runnable, ExitCodeGenerator {
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
        ShellCommand.main(new String[] {});
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

}
