package com.saschakiefer;

import com.saschakiefer.slipbox.framework.adapter.input.cli.CreateNewSlipNoteCommand;
import com.saschakiefer.slipbox.framework.adapter.input.cli.RootCommand;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.apache.maven.shared.utils.cli.CommandLineUtils;
import picocli.CommandLine;

@QuarkusMain
public class App implements QuarkusApplication {
    @Override
    public int run(String... args) throws Exception {
        if (args.length == 0) {
            return 0;
        }
        if (args.length == 1) {
            args = CommandLineUtils.translateCommandline(args[0]);
        }
        return new CommandLine(new RootCommand())
                .addSubcommand(CreateNewSlipNoteCommand.class)
                .execute(args);
    }
}