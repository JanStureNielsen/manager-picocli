package manager;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;

@Command(
    name = "create",
    mixinStandardHelpOptions = true,
    header = "create stuff",
    description = "Create some stuff with minimal fuss.")
@Component
class CreateCommand implements Runnable{
    @Override
    public void run() {
        System.out.println("Created stuff...");
    }

}
