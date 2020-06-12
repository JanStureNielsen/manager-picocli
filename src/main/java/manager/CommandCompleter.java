package manager;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import lombok.RequiredArgsConstructor;
import picocli.AutoComplete;
import picocli.CommandLine.Model.CommandSpec;

@RequiredArgsConstructor //@Component
public class CommandCompleter implements Completer {
    private final @NotNull CommandSpec commandSpec;

    /**
     * Populates <i>candidates</i> with a list of possible completions for the <i>command line</i>.
     *
     * The list of candidates will be sorted and filtered by the LineReader, so that
     * the list of candidates displayed to the user will usually be smaller than
     * the list given by the completer.  Thus it is not necessary for the completer
     * to do any matching based on the current buffer.  On the contrary, in order
     * for the typo matcher to work, all possible candidates for the word being
     * completed should be returned.
     *
     * @param reader        The line reader
     * @param line          The parsed command line
     * @param candidates    The {@link List} of candidates to populate
     */
    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String[] words = new String[line.words().size()];
        words = line.words().toArray(words);
        List<CharSequence> cs = new ArrayList<CharSequence>();
        AutoComplete.complete(commandSpec,
                words,
                line.wordIndex(),
                0,
                line.cursor(),
                cs);

        for (CharSequence c: cs){
            candidates.add(new Candidate((String)c)); 
        }
    }

}
