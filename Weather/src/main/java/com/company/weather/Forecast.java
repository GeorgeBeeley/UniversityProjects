/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.weather;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author George
 */
public class Forecast {

    static String defaultURL = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/";
    DocumentBuilderFactory dbf;
    DocumentBuilder db;
    Document forecastDoc;
    Document geoDoc;
    XPathFactory xpfactory;
    XPath path;
    String location;
    String weather;
    String desc;
    
    public Forecast()
            throws ParserConfigurationException, MalformedURLException,
            IOException, SAXException {
        
        dbf = DocumentBuilderFactory.newInstance();
        db = dbf.newDocumentBuilder();
        xpfactory = XPathFactory.newInstance();
        path = xpfactory.newXPath();
        
    }

    public Forecast getForecast(String query)
            throws XPathExpressionException, MalformedURLException, IOException,
            SAXException, ParserConfigurationException {
        
        String url = defaultURL + getGeonameID(query);
        
        try {
            forecastDoc = db.parse(new URL(url).openStream());
        } catch (FileNotFoundException ex) {
            weather = "Error: Location not found";
            desc = "";
            return this;
        }
        
        weather = path.evaluate("/rss/channel/item/title", forecastDoc);
        desc = path.evaluate("/rss/channel/item/description", forecastDoc);
        System.out.println(weather);
        return this;
        
    }
    
    public String getGeonameID(String query)
            throws MalformedURLException, IOException, SAXException, XPathExpressionException {
        
        String searchQuery = query.replaceAll(" ", ",") + "&maxRows=1&lang=en&username=georgebeeley";
        String url = "http://api.geonames.org/search?q=" + searchQuery;
        
        geoDoc = db.parse(new URL(url).openStream());
        location = path.evaluate("/geonames/geoname/name", geoDoc);
        String geonameID = path.evaluate("/geonames/geoname/geonameId", geoDoc);
        System.out.println("GeonameID of '" + query + "' = " + geonameID);

        return geonameID;
        
    }
    
    private void saveSearch() {
        
    }
    
}