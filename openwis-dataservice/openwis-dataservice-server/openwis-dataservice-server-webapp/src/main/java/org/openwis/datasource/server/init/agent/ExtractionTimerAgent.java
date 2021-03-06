/**
 * 
 */
package org.openwis.datasource.server.init.agent;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.openwis.dataservice.common.timer.ExtractionTimerService;
import org.openwis.dataservice.common.util.ConfigServiceFacade;
import org.openwis.dataservice.common.util.JndiUtils;
import org.openwis.datasource.server.init.DataServiceTimerConfiguration;
import org.openwis.datasource.server.init.ServerAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Short Description goes here. <P>
 * Explanation goes here. <P>
 * 
 */
public class ExtractionTimerAgent implements ServerAgent {

   /**
    * The logger.
    */
   private static Logger logger = LoggerFactory.getLogger(ExtractionTimerAgent.class);

   /** The Constant SERVICE_URL. */
   private final String serviceUrl;

   /** The Constant TIMER_PERIOD. */
   private final long timerPeriod;
   
   public ExtractionTimerAgent() {
      this.serviceUrl = ConfigServiceFacade.getInstance().getString(DataServiceTimerConfiguration.EXTRACTION_TIMER_URL_KEY);
      this.timerPeriod = ConfigServiceFacade.getInstance().getLong(DataServiceTimerConfiguration.EXTRACTION_TIMER_PERIOD_KEY);
   }

   /**
    * Shutdown.
    *
    * @throws Exception the exception
    * {@inheritDoc}
    * @see org.openwis.datasource.server.init.ServerAgent#shutdown()
    */
   @Override
   public void shutdown() throws Exception {
      try {
         logger.info("Extraction Timer Agent is shutting down.");

         InitialContext initialContext;
         try {
            initialContext = new InitialContext();
            ExtractionTimerService customTimer = (ExtractionTimerService) initialContext
                  .lookup(serviceUrl);
            customTimer.destroy();
         } catch (NamingException e) {
            logger.error("Context not found.", e);
         }
         logger.info("Extraction Timer Agent shutdown completed."); //$NON-NLS-1$
      } catch (Throwable t) {
         logger.error("Error during Extraction Timer Agent shutdown.", t); //$NON-NLS-1$
      }
   }

   /**
    * Startup.
    *
    * @throws Exception the exception
    * {@inheritDoc}
    * @see org.openwis.datasource.server.init.ServerAgent#startup()
    */
   @Override
   public void startup() throws Exception {
      try {
         logger.info("Extraction Timer Agent started, looking for {}.", serviceUrl);

         InitialContext initialContext;
         try {
            initialContext = new InitialContext();
            ExtractionTimerService customTimer = (ExtractionTimerService) initialContext
                  .lookup(serviceUrl);
            customTimer.start(timerPeriod);
         } catch (NamingException e) {
            logger.error("Context not found.", e);
         }
      } catch (Throwable t) {
         logger.error("Error during Extraction Timer initialisation.", t); //$NON-NLS-1$
      }
   }
}
