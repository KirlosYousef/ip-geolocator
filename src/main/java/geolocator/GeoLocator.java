package geolocator;

import java.net.URL;

import java.io.IOException;

import com.google.gson.Gson;

import com.google.common.net.UrlEscapers;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

public class GeoLocator {

    private static Logger logger = LoggerFactory.getLogger(GeoLocator.class);

    public static final String GEOLOCATOR_SERVICE_URI = "http://ip-api.com/json/";

    private static Gson GSON = new Gson();

    public GeoLocator() {
    }

    public GeoLocation getGeoLocation() throws IOException {
        return getGeoLocation(null);
    }

    public GeoLocation getGeoLocation(String ipAddrOrHost) throws IOException {
        URL url;
        logger.debug("Retrieving data..");
        if (ipAddrOrHost != null) {
            ipAddrOrHost = UrlEscapers.urlPathSegmentEscaper().escape(ipAddrOrHost);
            url = new URL(GEOLOCATOR_SERVICE_URI + ipAddrOrHost);
            logger.debug("Retrieving using the following ip: {}", ipAddrOrHost);
        } else {
            url = new URL(GEOLOCATOR_SERVICE_URI);
        }
        logger.debug("Retrieving data from: {}", url);

        String s = IOUtils.toString(url, "UTF-8");
        logger.info("JSON data retrieved: {}", s);

        GeoLocation geoLocation = GSON.fromJson(s, GeoLocation.class);

        if (geoLocation.getCountry() == null) {
            logger.warn("Entered argument \"{}\" might be wrong!", ipAddrOrHost);
        }
        return geoLocation;
    }

    public static void main(String[] args) throws IOException {
        logger.debug("Starting country data retrieving..");

        try {
            String arg = args.length > 0 ? args[0] : null;
            GeoLocation country = new GeoLocator().getGeoLocation(arg);

            if (country.getCountry() != null) {
                logger.info("Country data has been successfully retrieved for: {}", country.getQuery());
            } else {
                logger.error("Country Not Found!");
            }

            logger.info("The task has been completed!");
        } catch (IOException e) {
            logger.error("Error: {}", e.getMessage());
        }
    }

}
