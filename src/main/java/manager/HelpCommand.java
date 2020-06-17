package manager;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;

@Command(
    name = "help",
    description = "Create some stuff with minimal fuss...",
    mixinStandardHelpOptions = true)
@Component
class HelpCommand implements Runnable{
    @Override
    public void run() {
        System.out.println("Commands...");
    }

}
