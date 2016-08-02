package nl.vpro.amara_poms.poms.fetchers;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import nl.vpro.amara_poms.Config;
import nl.vpro.domain.media.MediaBuilder;

/**
 * @author Michiel Meeuwissen
 * @since 1.12
 */
public class M4vWithLsFetcherTest {

    @Before
    public void init() {
        Map<String, String> config = new HashMap<>();
        config.put("h264.source.dir", "/tmp");
        config.put("h264.source.dir.depth", "0");
        Config.init();

        Config.init(config);


    }
    @Test
    public void fetch() throws Exception {
        M4vWithLsFetcher fetcher = new M4vWithLsFetcher();

        fetcher.fetch(MediaBuilder.program().mid("MID_1234").build());


    }

}
