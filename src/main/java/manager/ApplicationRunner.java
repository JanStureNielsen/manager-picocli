package manager;

import javax.validation.constraints.NotNull;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@Component @RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner, ExitCodeGenerator {
    private final @NotNull IFactory picocliFactory;
    private final @NotNull ConnectCommand connectCommand;

    private int exitCode;

    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(connectCommand, picocliFactory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

}
