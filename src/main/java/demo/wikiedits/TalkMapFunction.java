package demo.wikiedits;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditEvent;

import java.security.MessageDigest;

public class TalkMapFunction extends RichMapFunction<WikipediaEditEvent, TalkEditEvent> {
    @Override
    public TalkEditEvent map(WikipediaEditEvent wikipediaEditEvent) {
        String id = generateId(wikipediaEditEvent.getTitle(), wikipediaEditEvent.getUser());

        TalkEditEvent talkEditEvent = new TalkEditEvent();
        talkEditEvent.setId(id);
        talkEditEvent.setTitle(wikipediaEditEvent.getTitle());
        talkEditEvent.setUser(wikipediaEditEvent.getUser());
        talkEditEvent.setDiffUrl(wikipediaEditEvent.getDiffUrl());
        talkEditEvent.setSummary(wikipediaEditEvent.getSummary());
        return talkEditEvent;
    }

    private String generateId(String title, String user) {
        MessageDigest md5Digest = DigestUtils.getMd5Digest();
        md5Digest.update(title.getBytes());
        md5Digest.update(user.getBytes());
        return Hex.encodeHexString(md5Digest.digest());
    }
}
