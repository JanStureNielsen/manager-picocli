package manager;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;

@Command(
    name = "list",
    description = "List some stuff with minimal fuss...",
    mixinStandardHelpOptions = true)
@Component
class ListCommand implements Runnable{
    @Override
    public void run() {
        System.out.println("Listed stuff...");
    }

}
