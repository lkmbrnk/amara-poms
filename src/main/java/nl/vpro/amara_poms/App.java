package nl.vpro.amara_poms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.vpro.domain.classification.ClassificationServiceLocator;
import nl.vpro.domain.media.MediaClassificationService;


/**
 *  Main app
 */
public class App  {
    private final static Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws IOException {

        LOG.info("Started...");
        ClassificationServiceLocator.setInstance(new MediaClassificationService());
        // check lock file
        Config.init();
        Path path = Paths.get(Config.getRequiredConfig("process.lock.filepath"));
        if (Files.exists(path)) {
            LOG.error("Another AmaraPomsPublisher process is running ({} exists since {}) -> will quit", path, Files.getLastModifiedTime(path).toInstant());
            System.exit(Config.ERROR_LOCKFILE_EXISTS);
        }
        int exitCode = -1;
        try {
            Files.createFile(path);
            LOG.info("Wrote lock file {}", path);
            LOG.info("=AMARA PUBLISHER (creating new tasks)======================================");
            new AmaraPublisher(Config.getFetcher()).processPomsCollection();
            LOG.info("=POMS PUBLISHER (handling finished tasks)==================================");
            new PomsPublisher().processAmaraTasks();
            exitCode = 0;
        } catch (Config.Error e) {
            LOG.error(e.getMessage(), e);
            exitCode = e.getErrorCode();
        } catch (Throwable t) {
            LOG.error(t.getMessage(), t);
            exitCode = 1;
        } finally {
            LOG.info("removing lockfile {}", path);
            Files.delete(path);
            Config.getPomsClient().shutdown();
            LOG.info("=ready=================================");
            LOG.info("=======================================");
        }
        System.exit(exitCode);

    }
}
