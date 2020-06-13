package manager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.jline.builtins.Options.HelpException;
import org.jline.console.ArgDesc;
import org.jline.console.CmdDesc;
import org.jline.console.CommandRegistry;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.completer.SystemCompleter;
import org.jline.utils.AttributedString;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import picocli.CommandLine;
import picocli.CommandLine.Help;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.OptionSpec;

@Component @NoArgsConstructor
public class ShellCommands implements CommandRegistry {
    private @NotNull Supplier<Path> workDir;
    private @NotNull CommandLine cmd;
    private @NotNull Set<String> commands;
    private @NotNull Map<String, String> aliasCommands = new HashMap<>();

    public ShellCommands(Supplier<Path> workDir, CommandLine cmd) {
        this.workDir = workDir;
        this.cmd = cmd;

        commands = cmd.getCommandSpec().subcommands().keySet();

        for (String c : commands) {
            for (String a : cmd.getSubcommands().get(c).getCommandSpec().aliases()) {
                aliasCommands.put(a, c);
            }
        }
    }

    public boolean hasCommand(String command) {
        return commands.contains(command) || aliasCommands.containsKey(command);
    }

    public SystemCompleter compileCompleters() {
        List<String> all = new ArrayList<>();
        all.addAll(commands);
        all.addAll(aliasCommands.keySet());

        SystemCompleter out = new SystemCompleter();
        out.add(all, new PicocliCommandCompleter());

        return out;
    }

    @NoArgsConstructor
    private class PicocliCommandCompleter implements Completer {
        @Override
        public void complete(LineReader reader, ParsedLine commandLine, List<Candidate> candidates) {
            assert commandLine != null;
            assert candidates != null;
            String word = commandLine.word();
            List<String> words = commandLine.words();
            CommandLine sub = findSubcommandLine(words, commandLine.wordIndex());
            if (sub == null) {
                return;
            }
            if (word.startsWith("-")) {
                String buffer = word.substring(0, commandLine.wordCursor());
                int eq = buffer.indexOf('=');
                for (OptionSpec option : sub.getCommandSpec().options()) {
                    if (option.arity().max() == 0 && eq < 0) {
                        addCandidates(candidates, Arrays.asList(option.names()));
                    } else {
                        if (eq > 0) {
                            String opt = buffer.substring(0, eq);
                            if (Arrays.asList(option.names()).contains(opt) && option.completionCandidates() != null) {
                                addCandidates(candidates, option.completionCandidates(), buffer.substring(0, eq + 1),
                                        "", true);
                            }
                        } else {
                            addCandidates(candidates, Arrays.asList(option.names()), "", "=", false);
                        }
                    }
                }
            } else {
                addCandidates(candidates, sub.getSubcommands().keySet());
                for (CommandLine s : sub.getSubcommands().values()) {
                    addCandidates(candidates, Arrays.asList(s.getCommandSpec().aliases()));
                }
            }
        }

        private void addCandidates(List<Candidate> candidates, Iterable<String> cands) {
            addCandidates(candidates, cands, "", "", true);
        }

        private void addCandidates(List<Candidate> candidates, Iterable<String> cands, String preFix, String postFix,
                boolean complete) {
            for (String s : cands) {
                candidates.add(new Candidate(AttributedString.stripAnsi(preFix + s + postFix), s, null, null, null,
                        null, complete));
            }
        }

    }

    private CommandLine findSubcommandLine(List<String> args, int lastIdx) {
        CommandLine out = cmd;
        for (int i = 0; i < lastIdx; i++) {
            if (!args.get(i).startsWith("-")) {
                out = findSubcommandLine(out, args.get(i));
                if (out == null) {
                    break;
                }
            }
        }
        return out;
    }

    private CommandLine findSubcommandLine(CommandLine cmdline, String command) {
        for (CommandLine s : cmdline.getSubcommands().values()) {
            if (s.getCommandName().equals(command) || Arrays.asList(s.getCommandSpec().aliases()).contains(command)) {
                return s;
            }
        }
        return null;
    }

    /**
     *
     * @param command
     * @return command description for JLine TailTipWidgets to be displayed in
     *         terminal status bar.
     */
    @Override
    public CmdDesc commandDescription(List<String> args) {
        CommandLine sub = findSubcommandLine(args, args.size());
        if (sub == null) {
            return null;
        }
        CommandSpec spec = sub.getCommandSpec();
        Help cmdhelp = new picocli.CommandLine.Help(spec);
        List<AttributedString> main = new ArrayList<>();
        Map<String, List<AttributedString>> options = new HashMap<>();
        String synopsis = AttributedString
                .stripAnsi(spec.usageMessage().sectionMap().get("synopsis").render(cmdhelp).toString());
        main.add(HelpException.highlightSyntax(synopsis.trim(), HelpException.defaultStyle()));
        // using JLine help highlight because the statement below does not work well...
        // main.add(new
        // AttributedString(spec.usageMessage().sectionMap().get("synopsis").render(cmdhelp).toString()));
        for (OptionSpec o : spec.options()) {
            String key = Arrays.stream(o.names()).collect(Collectors.joining(" "));
            List<AttributedString> val = new ArrayList<>();
            for (String d : o.description()) {
                val.add(new AttributedString(d));
            }
            if (o.arity().max() > 0) {
                key += "=" + o.paramLabel();
            }
            options.put(key, val);
        }
        return new CmdDesc(main, ArgDesc.doArgNames(Arrays.asList("")), options);
    }

    @Override
    public List<String> commandInfo(String command) {
        List<String> out = new ArrayList<>();
        CommandSpec spec = cmd.getSubcommands().get(command).getCommandSpec();
        Help cmdhelp = new picocli.CommandLine.Help(spec);
        String description = AttributedString
                .stripAnsi(spec.usageMessage().sectionMap().get("description").render(cmdhelp).toString());
        out.addAll(Arrays.asList(description.split("\\r?\\n")));
        return out;
    }

    @Override
    public Object execute(CommandRegistry.CommandSession session, String command, String[] args) throws Exception {
        List<String> arguments = new ArrayList<>();
        arguments.add(command);
        arguments.addAll(Arrays.asList(args));
        cmd.execute(arguments.toArray(new String[0]));
        return null;
    }

    @Override
    public Set<String> commandNames() {
        return commands;
    }

    @Override
    public Map<String, String> commandAliases() {
        return aliasCommands;
    }

    @Override
    public CmdDesc commandDescription(String command) {
        return null;
    }

}
