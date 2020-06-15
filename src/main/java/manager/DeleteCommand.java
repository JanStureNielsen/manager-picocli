package manager;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;

@Command(
    name = "delete",
    description = "Deletes some stuff with minimal fuss...",
    mixinStandardHelpOptions = true)
@Component
class DeleteCommand implements Runnable{
    @Override
    public void run() {
        System.out.println("deleted stuff...");
    }

}
