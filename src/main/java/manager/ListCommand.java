package manager;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;

@Command(
    name = "ls",
    aliases = "list",
    description = "List stuff...",
    mixinStandardHelpOptions = true,
    header = "list stuff...")
@Component
class ListCommand implements Runnable{
    @Override
    public void run() {
        System.out.println("Listed stuff...");
    }

}
