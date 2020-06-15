package manager;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;

@Command(
    name = "create",
    description = "Create some stuff with minimal fuss.",
    mixinStandardHelpOptions = true)
@Component
class CreateCommand implements Runnable{
    @Override
    public void run() {
        System.out.println("Created stuff...");
    }

}
