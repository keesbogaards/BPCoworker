package de.bridgephone.coworker.bplogger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.io.IOException;

public class BPLoggerLog4J {
    private static final String TAG="BPLogger";
    ConfigurationBuilder<BuiltConfiguration> builder;
    private  String workDir;

    public BPLoggerLog4J(String workDir){
        this.workDir=workDir;
    }

    public void runTimeConfigurationBuilder(){

    }


    public void runTimeConfigurationBuilderBaeldung(){

        this.builder = ConfigurationBuilderFactory.newConfigurationBuilder();


        /*
        Configuring Appenders
        Let's tell the builder where to send each log line by configuring an appender:
        */
        AppenderComponentBuilder console = builder.newAppender("stdout", "Console");

        AppenderComponentBuilder file  = builder.newAppender("log", "File");
        file.addAttribute("fileName","target/coworker.log");
//        file.addAttribute("fileName", workDir+"coworker.log");

//        AppenderComponentBuilder rollingFile = builder.newAppender("rolling", "RollingFile");
//        rollingFile.addAttribute("fileName", "target/rolling.log");
//        rollingFile.addAttribute("filePattern", "rolling-%d{MM-dd-yy}.log.gz");

        /*
        While most new methods don't support this, newAppender(name, plugin) allows us to give the appender a name,
        which will turn out to be important later on. These appenders, we've called stdout and log, though we could've named them anything.
        We've also told the builder which appender plugin (or, more simply, which kind of appender) to use.
        Console and File refer to Log4j 2's appenders for writing to standard out and the file system, respectively.

        Though Log4j 2 supports several appenders, configuring them using Java can be a bit tricky since
        AppenderComponentBuilder is a generic class for all appender types.

        This makes it have methods like addAttribute and addComponent instead of setFileName and addTriggeringPolicy:
        */







    /*
        Configuring Filters
        We can add filters to each of our appenders, which decide on each log line whether it should be appended or not.
        Let's use the MarkerFilter plugin on our console appender:
        */
        FilterComponentBuilder flow = builder.newFilter("MarkerFilter", Filter.Result.ACCEPT, Filter.Result.DENY);
        flow.addAttribute("marker", "FLOW");
        console.add(flow);
        /*
        Note that this new method doesn't allow us to name the filter, but it does ask us to indicate what to do if the filter passes or fails.
        In this case, we've kept it simple, stating that if the MarkerFilter passes, then ACCEPT the logline. Otherwise, DENY it.
        Note in this case that we don't append this to the builder but instead to the appenders that we want to use this filter.
        */


        //Configuring Layouts
        //Next, let's define the layout for each log line. In this case, we'll use the PatternLayout plugin:
        LayoutComponentBuilder standard = builder.newLayout("PatternLayout");
        standard.addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable");
        console.add(standard);
        file.add(standard);
//        rollingFile.add(standard);
        //Again, we've added these directly to the appropriate appenders instead of to the builder directly.
/*
        Configuring the Root Logger

        Now that we know where logs will be shipped to, we want to configure which logs will go to each destination.
        The root logger is the highest logger, kind of like Object in Java. This logger is what will be used by default unless overridden.
        So, let's use a root logger to set the default logging level to ERROR and the default appender to our stdout appender from above:
        */

        builder.add(console);
        builder.add(file);
//        builder.add(rollingFile);

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.TRACE);
        rootLogger.add(builder.newAppenderRef("stdout"));
        rootLogger.add(builder.newAppenderRef("log"));
//        rootLogger.add(builder.newAppenderRef("rolling"));
        builder.add(rootLogger);
        /*
        To point our logger at a specific appender, we don't give it an instance of the builder. Instead, we refer to it by the name that we gave it earlier.
        */

        /*
        Configuring Additional Loggers
        Child loggers can be used to target specific packages or logger names.

        Let's add a logger for the de package in our application, setting the logging level to DEBUG and having those go to our log appender:
        */
        LoggerComponentBuilder logger = builder.newLogger("de", Level.DEBUG);
        logger.add(builder.newAppenderRef("log"));
        logger.addAttribute("additivity", true);
        builder.add(logger);

        try {
            builder.writeXmlConfiguration(System.out);
        } catch (IOException ex) {
// TODO define a message to the user, something is terrygly wrong
        }

        Configurator.initialize(builder.build());
        builder.setDestination("target/con.");



        Logger LOG = LogManager.getLogger("de");
        LOG.info("BPLogger  start logging");
    }
}
