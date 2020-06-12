package manager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

import picocli.CommandLine;

@Component
public class ApplicationRunner implements CommandLineRunner, ExitCodeGenerator {
    private int exitCode;

    @Override
    public void run(String... args) throws Exception {
        exitCode = new CommandLine(new ConnectCommand()).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

}
