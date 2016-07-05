package com.zholdiyarov.appwidget.rss_reader;

import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by szholdiyarov on 6/28/16.
 */
public class RssReader {
    private String text;

    public RssReader(String text) {
        this.text = text;
    }

    public List<RssItem> getItems() throws Exception {
        final SAXParserFactory factory = SAXParserFactory.newInstance();
        final SAXParser saxParser = factory.newSAXParser();
        //Creates a new RssHandler which will do all the parsing.
        final RssHandler handler = new RssHandler();
        //Pass SaxParser the RssHandler that was created.

        InputSource source = new InputSource();
        source.setCharacterStream(new StringReader(text));
        saxParser.parse(source, handler);
        return handler.getRssItemList();
    }
}