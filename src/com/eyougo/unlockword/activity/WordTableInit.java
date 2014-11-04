package com.eyougo.unlockword.activity;

import com.eyougo.unlockword.data.WordItem;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by mei on 8/5/14.
 */
public class WordTableInit {

    public static void main(String[] args) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        InputStream inputStream = null;
        XMLReader xmlReader;
        try {
            xmlReader = saxParserFactory.newSAXParser().getXMLReader();
            xmlReader.setContentHandler(new WordXmlHandler("word_kaoyan"));
            inputStream = new FileInputStream("assets/kaoyan.xml");
            InputSource inputSource = new InputSource(inputStream);
            inputSource.setEncoding("UTF-8");
            xmlReader.parse(inputSource);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static class WordXmlHandler extends DefaultHandler {
        private String tableName;
        private WordItem wordItem;
        private String tagName;
        int rows = 0;

        public WordXmlHandler(String tableName) {
            super();
            this.tableName = tableName;
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            tagName = localName;
            if (tagName == null  || tagName.equals("")){
                tagName = qName;
            }
            if (qName.equals("item")) {
                wordItem = new WordItem();
            }
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            //解析一个词的内容
            String text = new String(ch, start, length);
            if (text != null) {
                text = text.trim();
                text = text.replaceAll("\n", "");
                text = text.replaceAll("'", "''");
            }
            if (tagName.equals("word")) {
                if (wordItem.getWord() != null) {
                    wordItem.setWord(wordItem.getWord().trim()+text);
                }else {
                    wordItem.setWord(text);
                }
            }else if (tagName.equals("trans")) {
                if (wordItem.getTrans() != null) {
                    wordItem.setTrans(wordItem.getTrans().trim()+text);
                }else {
                    wordItem.setTrans(text);
                }
            }else if (tagName.equals("phonetic")) {
                if (wordItem.getPhonetic() != null) {
                    wordItem.setPhonetic(wordItem.getPhonetic().trim()+text);
                }else {
                    wordItem.setPhonetic(text);
                }
            }else if (tagName.equals("tags")) {
                if (wordItem.getTags() != null) {
                    wordItem.setTags(wordItem.getTags().trim()+text);
                }else {
                    wordItem.setTags(text);
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (qName.equals("item")) {

                System.out.println("INSERT OR IGNORE INTO "+ tableName + " (word, trans, phonetic, tags) " +
                        "values ('"+ wordItem.getWord() +"', '"+wordItem.getTrans()+
                        "', '"+wordItem.getPhonetic()+ "','"+ wordItem.getTags()+"');");
                rows++;
                if (rows % 500 == 0 ){
                    System.out.println("rows"+rows);System.out.println();System.out.println();
                    System.out.println();System.out.println();System.out.println();System.out.println();System.out.println();
                    System.out.println();System.out.println();System.out.println();System.out.println();
                    System.out.println();System.out.println();System.out.println();System.out.println();
                }
            }
        }



    }
}
