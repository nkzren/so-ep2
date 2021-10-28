package br.usp;

import br.usp.executor.ThreadExecutor;
import br.usp.utils.ResourcesReader;
import org.apache.log4j.Logger;

import java.util.List;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    private final ResourcesReader reader;

    public Main(ResourcesReader reader) {
        this.reader = reader;
    }

    public void init() {
        LOGGER.info("Starting readers-writers");

        List<String> words = reader.readLines("bd.txt");
        LOGGER.info(words.size());

        ThreadExecutor executor = new ThreadExecutor();
        executor.start();
    }

    public static void main(String[] args) {
        Main main = new Main(new ResourcesReader());

        main.init();
    }
}
