package manager;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;

@Command(
    name = "delete",
    header = "delete stuff",
    description = "This is the long description, decribing in great detail what, how, and why it deletes stuff.",
    mixinStandardHelpOptions = true)
@Component
class DeleteCommand implements Runnable{
    @Override
    public void run() {
        System.out.println("deleted stuff...");
    }

}
