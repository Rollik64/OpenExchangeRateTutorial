package com.example.rollikak47_oertutorial.OER;

import com.example.rollikak47_oertutorial.OER.Exception.UnavailableExchangeRateException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OpenExchangeRates {
    private final static String OER_URL = "http://openexchangerates.org/api/";
    private static final String LATEST = "latest.json?app_id=%s";
    private static final String HISTORICAL = "historical/%04d-%02d-%02d.json?app_id=%s";
    private final String appId;

    private final static ObjectMapper mapper = new ObjectMapper();

    public OpenExchangeRates(String appId) {
        this.appId = appId;
    }


    private Map<String, BigDecimal> updateExchangeRates(
            String downloadPath) throws UnavailableExchangeRateException {
        try {
            Map<String, BigDecimal> exchangeRates = new HashMap<>();
            String urlString = String.format(OER_URL + downloadPath, this.appId);
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            JsonNode node = mapper.readTree(conn.getInputStream());
            Iterator<Map.Entry<String, JsonNode>> fieldNames = node.get("rates").getFields();
            fieldNames.forEachRemaining(e -> exchangeRates.put(e.getKey(), e.getValue().getDecimalValue()));
            return exchangeRates;
        } catch (IOException e) {
            throw new UnavailableExchangeRateException(e.getMessage());
        }
    }


    public Map<String, BigDecimal> latest() throws UnavailableExchangeRateException {
        return updateExchangeRates(LATEST);
    }

    public Map<String, BigDecimal> historical(Calendar date)
            throws UnavailableExchangeRateException {

        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH) + 1;
        int year = date.get(Calendar.YEAR);

        String historical = String.format(HISTORICAL, year, month, day, appId);
        return updateExchangeRates(historical);

    }

    public BigDecimal currency(String currency) throws UnavailableExchangeRateException {
        return latest().get(currency);
    }

    public BigDecimal historicalCurrency(String currency,
                                         Calendar date) throws UnavailableExchangeRateException {
        return historical(date).get(currency);
    }
}