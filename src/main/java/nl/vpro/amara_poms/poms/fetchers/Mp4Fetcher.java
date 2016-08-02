package nl.vpro.amara_poms.poms.fetchers;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import nl.vpro.amara_poms.poms.SourceFetcher;
import nl.vpro.domain.media.AVFileFormat;
import nl.vpro.domain.media.Location;
import nl.vpro.domain.media.Program;

/**
 * @author Michiel Meeuwissen
 * @since 1.3
 */
@Slf4j
public class Mp4Fetcher implements SourceFetcher {

    @Override
    public FetchResult fetch(Program program) {
        //File dest = new File(Config.getRequiredConfig("videofile.dir"), program.getMid() + ".mp4");
        for (Location location : program.getLocations()) {
            if (location.isPublishable() && location.getAvFileFormat() == AVFileFormat.MP4) {
                URI url = URI.create(location.getProgramUrl());
                //IOUtils.copy(url.openStream(), new FileOutputStream(dest));
                return FetchResult.succes(url);
            }
        }
        return FetchResult.notable();

    }
}