package demo.wikiedits;

import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.test.util.MiniClusterWithClientResource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Map;

import demo.wikiedits.LinesFromFileSource.WikipediaEditEventFromFileSource;

import static org.junit.Assert.assertEquals;

public class WikipediaAnalysisTest {

    private StreamExecutionEnvironment see;
    private WikipediaEditEventFromFileSource wikipediaEditsSource;
    private TalkEditEventCollectingSink talkEditsSink;

    @ClassRule
    public static MiniClusterWithClientResource flinkCluster =
            new MiniClusterWithClientResource(
                    new MiniClusterResourceConfiguration.Builder()
                            .setNumberSlotsPerTaskManager(4)
                            .setNumberTaskManagers(4)
                            .build());

    @Before
    public void setUp() {
        see = StreamExecutionEnvironment.getExecutionEnvironment();

        wikipediaEditsSource = new WikipediaEditEventFromFileSource("src/test/resources/demo/wikiedits/samples.txt");

        talkEditsSink = new TalkEditEventCollectingSink();
    }

    @Test
    public void testTalkEdits() throws Exception {
        new WikipediaAnalysis(wikipediaEditsSource, talkEditsSink)
                .build(see);

        see.execute();

        Map<String, TalkEditEvent> talkEdits = talkEditsSink.getValues();
        assertEquals(4, talkEdits.size());

        TalkEditEvent talkEditEvent1 = talkEdits.get("d3a500719b126a042d8d6ddbea8cc336");
        assertEquals("Talk:Hagia Sophia", talkEditEvent1.getTitle());
        assertEquals("Orjen", talkEditEvent1.getUser());

        TalkEditEvent talkEditEvent2 = talkEdits.get("d3b5598aadffb04b77c75f3be9bf8e6f");
        assertEquals("Talk:Dire wolf", talkEditEvent2.getTitle());
        assertEquals("WolfmanSF", talkEditEvent2.getUser());
    }

    @After
    public void tearDown() {
        talkEditsSink.clear();
    }
}