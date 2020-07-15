package demo.wikiedits;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditEvent;
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditsSource;

public class WikipediaAnalysis {

    private final SourceFunction<WikipediaEditEvent> wikipediaEditsSource;
    private final SinkFunction<TalkEditEvent> talkEditsSink;

    public WikipediaAnalysis(SourceFunction<WikipediaEditEvent> wikipediaEditsSource, SinkFunction<TalkEditEvent> talkEditsSink) {
        this.wikipediaEditsSource = wikipediaEditsSource;
        this.talkEditsSink = talkEditsSink;
    }

    void build(StreamExecutionEnvironment see) {
        see.addSource(wikipediaEditsSource, TypeInformation.of(WikipediaEditEvent.class))
                .rebalance()
                .filter(edit -> edit != null)   // NOSONAR  Flink works with lambda,
                .filter(edit -> edit.isTalk())  // NOSONAR  but no method reference
                .map(new TalkMapFunction())
                .addSink(talkEditsSink);
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment see = StreamExecutionEnvironment.getExecutionEnvironment();

        new WikipediaAnalysis(
                new WikipediaEditsSource(),
                new PrintSinkFunction<>()).build(see);

        see.execute();
    }
}
