package Controller;

import model.Exam;
import model.SNP;
import model.Student;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocOpener {
    private static SNP snp;
    private static String group;
    private static List<Exam> examList;
    private static List<Student> studentList;

    public static List<Student> openDoc(File file) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory parserFactory;
        SAXParser parser;
        XMLHandler handler;

        studentList = new ArrayList<>();

        handler = new XMLHandler();
        parserFactory = SAXParserFactory.newInstance();
        parser = parserFactory.newSAXParser();
        parser.parse(file, handler);
        return studentList;
    }

    private static class XMLHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String nodeName, Attributes attributes) throws SAXException {
            if (nodeName.equals("snp")) {
                snp = new SNP(
                        attributes.getValue("surname"),
                        attributes.getValue("name"),
                        attributes.getValue("patronym")
                );
            }
            if (nodeName.equals("group")) {
                group = attributes.getValue("name");
            }
            if (nodeName.equals("exams")) {
                examList = new ArrayList<>();
            }
            if (nodeName.equals("exam")) {
                examList.add(new Exam(Integer.valueOf(attributes.getValue("score"))
                ));
            }
        }

        @Override
        public void endElement(String uri, String localName, String nodeName) throws SAXException {
            if (nodeName.equals("student")) {
                studentList.add(new Student(snp, group, examList));
            }
        }
    }
}
