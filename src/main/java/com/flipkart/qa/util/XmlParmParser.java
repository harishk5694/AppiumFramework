package com.flipkart.qa.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses XML test data files
 *
 */
public class XmlParmParser {
    private static XmlParmParser instance = new XmlParmParser();
    
    public static List<TestParams> parse(String filePath, String setName) throws Exception {
        try {
            File file = new File(filePath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            ParamsHandler paraHandler = instance.new ParamsHandler(setName);
            saxParser.parse(file, paraHandler);
            return paraHandler.getResult();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new Exception(e);
        }
    }
    
    public class ParamsHandler extends DefaultHandler {
        private List<TestParams> result = new ArrayList<>();
        private boolean logParameter = false;
        private int depth;
        private String setName;
        
        private String testName;
        private Map<String, Object> parameters;
        private String paramName;
        private String paramType;
        private String rawValue;
        
        ParamsHandler(String setName) {
            super();
            this.setName = setName;
        }
        
        public List<TestParams> getResult() {
            return result;
        }
        
        @Override
        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
            switch (qName) {
            case "parameter":
                if (logParameter) {
                    paramName = atts.getValue("name");
                    paramType = atts.getValue("type");
                    if (paramType == null) {
                        paramType = "string";
                    }
                    
                    rawValue = "";
                }
                
                break;
            case "set":
                // Check if set is good
                String name = atts.getValue("name");
                // If yes, start logging
                if (setName.isEmpty() || setName.equalsIgnoreCase(name)) {
                    logParameter = true;
                    depth = 0;
                } else {
                    depth++;
                }
                
                // If no, do nothing
                break;
            case "test":
                if (logParameter) {
                    testName = atts.getValue("name");
                    // Create new Map
                    parameters = new HashMap<String, Object>();
                }
                
                break;
            default:
                // Add text to raw value
                rawValue += String.format("<%s>", qName);
            }
        }
        
        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            rawValue += new String(ch, start, length);
        }
        
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            switch (qName) {
            case "set":
                // Check if set is good
                // If yes, stop logging
                if (logParameter && depth == 0) {
                    logParameter = false;
                } else {
                    depth--;
                }
                break;
            case "test":
                if (logParameter) {
                    // change map to Parameters and add to list
                    result.add(TestParams.create(testName, parameters));
                }
                
                break;
            case "parameter":
                if (logParameter) {
                    Function<String,Stream<String>> getStrings = (value)->{
                        List<String> matches = new ArrayList<String>();
                        Matcher matcher = Pattern.compile("<li>(.*)</li>").matcher(value);
                        while(matcher.find()) matches.add(matcher.group(1).replaceAll("</?li>", ""));
                        return matches.stream();
                    };
                    Object value;
                    switch (paramType.toLowerCase()) {
                    case "null":
                        value = null;
                        break;
                    case "integer":
                        value = Integer.valueOf(rawValue);
                        break;
                    case "float":
                        value = Float.valueOf(rawValue);
                        break;
                    case "double":
                        value = Double.valueOf(rawValue);
                        break;
                    case "boolean":
                        value = Boolean.valueOf(rawValue);
                        break;
                    case "list-integer":
                        value = getStrings.apply(rawValue).map((val)->Integer.valueOf(val)).collect(Collectors.toList());
                        break;
                    case "list-float":
                        value = getStrings.apply(rawValue).map((val)->Float.valueOf(val)).collect(Collectors.toList());
                        break;
                    case "list-double":
                        value = getStrings.apply(rawValue).map((val)->Double.valueOf(val)).collect(Collectors.toList());
                        break;
                    case "list-boolean":
                        value = getStrings.apply(rawValue).map((val)->Boolean.valueOf(val)).collect(Collectors.toList());
                        break;
                    case "list-string":
                    case "list":
                        value = getStrings.apply(rawValue).collect(Collectors.toList());
                        break;
                    case "string":
                    default:
                        value = rawValue;
                    }
                    
                    parameters.put(paramName, value);
                }
                
                break;
            default:
                // Add tag to raw value
                rawValue += String.format("</%s>", qName);
            }
        }
        
    }
}
