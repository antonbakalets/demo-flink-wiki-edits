package demo.wikiedits;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditEvent;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class LinesFromFileSource<T> implements SourceFunction<T> {

    private final String filepath;

    public LinesFromFileSource(String filepath) {
        this.filepath = filepath;
    }

    protected abstract T convert(String line);

    @Override
    public void run(SourceContext<T> ctx) throws Exception {

        List<String> lines;
        try (Stream<String> stream = Files.lines(Paths.get(filepath))) {
            lines = stream.collect(Collectors.toList());
        }

        for (String line : lines) {
            synchronized (ctx.getCheckpointLock()) {
                ctx.collect(convert(line));
            }
        }
    }

    @Override
    public void cancel() {
        // do nothing
    }

    public static class WikipediaEditEventFromFileSource extends LinesFromFileSource<WikipediaEditEvent> {
        public WikipediaEditEventFromFileSource(String filepath) {
            super(filepath);
        }

        @Override
        protected WikipediaEditEvent convert(String event) {
            return WikipediaEditEvent.fromRawEvent(System.currentTimeMillis(), "test-channel", event);
        }
    }
}
