package tylerbank.web.app.TylerBank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Runnable to fetch and store exchange rates from the API every 60 hours.
 * @since v2.3
 */
@Component
public class ExchangeRateScheduleTaskRunnerComponent implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(ExchangeRateScheduleTaskRunnerComponent.class);
    private final ExchangeRateService rateService;
    private final ScheduledExecutorService scheduler;

    public ExchangeRateScheduleTaskRunnerComponent(ExchangeRateService rateService, ScheduledExecutorService scheduler) {
        this.rateService = rateService;
        this.scheduler = scheduler;
    }

    /**
     * This method will run every 60 hours to fetch the exchange rates from the API and store them in the database.
     * @param args
     * @throws Exception
     * @since v2.3
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info("Calling the currency API endpoint for exchange rates");
        scheduler.scheduleWithFixedDelay(rateService::getExchangeRate, 0, 60, TimeUnit.HOURS);
        logger.info("Ended calling the currency API endpoint for exchange rates");
    }
}
