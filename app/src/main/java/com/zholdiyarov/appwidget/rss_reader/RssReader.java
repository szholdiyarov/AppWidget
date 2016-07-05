package com.zholdiyarov.appwidget.rss_reader;

import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * This class should be used DIRECTLY in the caller class to get list of RSS items.
 *
 * @author Sanzhar Zholdiyarov
 * @since 6/28/16
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