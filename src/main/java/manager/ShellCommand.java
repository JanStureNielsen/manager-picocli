package manager;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.constraints.NotNull;

import org.fusesource.jansi.AnsiConsole;
import org.jline.builtins.SystemRegistry;
import org.jline.builtins.SystemRegistryImpl;
import org.jline.builtins.Widgets.TailTipWidgets;
import org.jline.builtins.Widgets.TailTipWidgets.TipType;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.Parser;
import org.jline.reader.Reference;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import picocli.CommandLine;
import picocli.CommandLine.Command;

//
// https://github.com/remkop/picocli/blob/master/picocli-shell-jline3/src/test/java/picocli/shell/jline3/example/Example.java
//
@Command(name = "",
description = {
    "Example interactive shell with completion and autosuggestions. " +
            "Hit @|magenta <TAB>|@ to see available commands.",
            "Hit @|magenta ALT-S|@ to toggle tailtips.",
    ""},
footer = {"", "Press Ctl-D to exit."},
subcommands = {
    ListCommand.class,
    CreateCommand.class,
    DeleteCommand.class
})
@Component @RequiredArgsConstructor @Getter
public class ShellCommand  implements Runnable, ExitCodeGenerator {
    private final @NotNull ShellCommandRegistry commands;
    private LineReaderImpl reader;
    private PrintWriter out;

	private int exitCode;

    @Override
    public int getExitCode() {
        return exitCode;
    }

    @Override
    public void run() {
        CommandLine cmd = new CommandLine(this);

        AnsiConsole.systemInstall();
        try {
            ShellCommandRegistry shellCommands = new ShellCommandRegistry(ShellCommand::workDir, cmd);

            Parser parser = new DefaultParser();
            Terminal terminal = TerminalBuilder.builder().build();

            SystemRegistry systemRegistry = new SystemRegistryImpl(parser, terminal, ShellCommand::workDir, null);
            systemRegistry.setCommandRegistries(shellCommands);

            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(systemRegistry.completer())
                    .parser(parser)
                    .variable(LineReader.LIST_MAX, 60)   // max tab completion candidates
                    .build();

            setReader(reader);
            new TailTipWidgets(reader, systemRegistry::commandDescription, 5, TipType.COMPLETER);
            KeyMap<Binding> keyMap = reader.getKeyMaps().get("main");
            keyMap.bind(new Reference("tailtip-toggle"), KeyMap.alt("s"));

            String prompt = "manager> ";
            String rightPrompt = null;

            // start the shell and process input until the user quits with Ctrl-D
            String line;
            while (true) {
                try {
                    systemRegistry.cleanUp();
                    line = reader.readLine(prompt, rightPrompt, (MaskingCallback) null, null);
                    Object result = systemRegistry.execute(line);
                    if (!"".equals(line) && null == result) {
                        System.out.println(String.format("'%s' command not found", line));
                    }
                } catch (UserInterruptException e) {
                    // Ignore
                } catch (EndOfFileException e) {
                    return;
                } catch (Exception e) {
                    systemRegistry.trace(e);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void setReader(LineReader reader){
        this.reader = (LineReaderImpl)reader;
        out = reader.getTerminal().writer();
    }

    private static Path workDir() {
        return Paths.get(System.getProperty("user.dir"));
    }

}
