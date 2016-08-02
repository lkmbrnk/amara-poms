package nl.vpro.amara_poms.poms.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.common.io.Files;

import nl.vpro.amara_poms.Config;
import nl.vpro.amara_poms.poms.SourceFetcher;
import nl.vpro.domain.media.AVFileFormat;
import nl.vpro.domain.media.Location;
import nl.vpro.domain.media.MediaBuilder;
import nl.vpro.domain.media.Program;
import nl.vpro.domain.media.support.OwnerType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 1.3
 */
public class HaspFetcherTest {

    @Before
    public void setup() {
        Map<String, String> props = new HashMap<>();
        props.put("videofile.dir", "/e/ap/video.dir");
        props.put("hasp.source.dir", "/tmp");
        props.put("download.url.base", "http://download.omroep.nl/vpro/netinnederland/");
        Config.init(props);
    }
    @Test
    public void fetch() throws Exception {



        HaspFetcher fetcher = new HaspFetcher();

        Location location = new Location("odi+http://odi.omroep.nl/video/h264_std/20070809_dieropvang01", OwnerType.BROADCASTER);
        location.setAvFileFormat(AVFileFormat.HASP);

        Program program = MediaBuilder.program().mid("WO_NTR_425372").locations(location).build();

        SourceFetcher.FetchResult result = fetcher.fetch(program);

        System.out.println(result.destination);

    }

    @Test
    @Ignore
    public void testDamnJava() throws IOException {
        File rootDir = new File("/tmp");
        for (File f : Files.fileTreeTraverser().preOrderTraversal(rootDir)) {
            if (f.isDirectory()) {
                // do whatever you need with the file/directory
                System.out.println(f);
            }
        }
    }

    @Test
    public void testIsm() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        HaspFetcher fetcher = new HaspFetcher();
        List<HaspFetcher.Video> list = fetcher.getVideos(getClass().getResourceAsStream("/WO_NTR_425372.ism"));
        assertThat(list).hasSize(3);
        assertThat(list.get(0).src).isEqualTo("20070809_dieropvang01_264.ismv");
        assertThat(list.get(0).systemBitRate).isEqualTo(200000);

    }

}
