package com.saschakiefer;

import com.saschakiefer.slipbox.framework.adapter.input.cli.CreateNewSlipNoteCommand;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.apache.maven.shared.utils.cli.CommandLineUtils;
import picocli.CommandLine;

import javax.inject.Inject;

@QuarkusMain
@TopCommand
@CommandLine.Command(name = "zettelkasten",
        mixinStandardHelpOptions = true,
        version = "0.0.1",
        subcommands = {CreateNewSlipNoteCommand.class})
public class App implements QuarkusApplication {

    @Inject
    CommandLine.IFactory factory;

    @Override
    public int run(String... args) throws Exception {
        if (args.length == 0) {
            args[0] = "--help";
        }
        if (args.length == 1) {
            args = CommandLineUtils.translateCommandline(args[0]);
        }
        return new CommandLine(this, factory).execute(args);
    }
}