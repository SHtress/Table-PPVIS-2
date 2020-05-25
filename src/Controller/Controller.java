package Controller;

import model.Exam;
import model.Model;
import model.SNP;
import model.Student;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private Model model;

    public Controller(Model model) {
        this.model = model;
    }

    public List<Student> getStudentList() {
        return model.getStudentList();
    }

    public int getExamNumber() {
        return model.getExamNumber();
    }

    public void newDoc(int examNumber, int entitiesNumber) {
        this.model = new Model(examNumber, entitiesNumber);
    }

    public void addStudent(String surname, String name, String patronym, String group, List<Exam> examList) {
        model.addStudent(
                new Student(new SNP(surname, name, patronym), group, examList)
        );
    }

    public void openDoc(File file) {
        try {
            model.setStudentList(DocOpener.openDoc(file));
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    public void saveDoc(File file) {
        List<Student> studentList = model.getStudentList();
        Element students;
        Element student;
        Element snp;
        Element group;
        Element exams;
        Element exam;
        Attr surname;
        Attr name;
        Attr patronym;
        Attr groupName;
        Attr examScore;
        Document doc;
        DocumentBuilderFactory docBuilderFactory;
        DocumentBuilder docBuilder;
        TransformerFactory transformerFactory;
        Transformer transformer;
        DOMSource source;
        StreamResult streamResult;

        try {
            docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();

            students = doc.createElement("students");
            doc.appendChild(students);

            for (Student studenti : studentList) {
                surname = doc.createAttribute("surname");
                surname.setValue(studenti.getSnp().getSurname());
                name = doc.createAttribute("name");
                name.setValue(studenti.getSnp().getName());
                patronym = doc.createAttribute("patronym");
                patronym.setValue(studenti.getSnp().getPatronym());
                snp = doc.createElement("snp");
                snp.setAttributeNode(surname);
                snp.setAttributeNode(name);
                snp.setAttributeNode(patronym);

                group = doc.createElement("group");
                groupName = doc.createAttribute("name");
                groupName.setValue(studenti.getGroup());
                group.setAttributeNode(groupName);

                exams = doc.createElement("exams");
                for (int j = 0; j < model.getExamNumber(); j++) {
                    examScore = doc.createAttribute("score");
                    examScore.setValue(((Integer) studenti.getExamScore(j)).toString());

                    exam = doc.createElement("exam");
                    exam.setAttributeNode(examScore);
                    exams.appendChild(exam);
                }

                student = doc.createElement("student");
                student.appendChild(snp);
                student.appendChild(group);
                student.appendChild(exams);
                students.appendChild(student);
            }

            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            source = new DOMSource(doc);
            streamResult = new StreamResult(file);
            transformer.transform(source, streamResult);
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    public List search(String selectedItem, List<String> criteriaList) {
        final String SURNAME = criteriaList.get(0),
                CRITERIA_1 = "ПО ФАМИЛИИ И НОМЕРУ ГРУППЫ",
                CRITERIA_2 = "ПО ФАМИЛИИ И КОЛИЧЕСТВУ ОБЩЕСТВЕННОЙ РАБОТЫ",
                CRITERIA_3 = "ПО НОМЕРУ ГРУППЫ И КОЛЛИЧЕСТВУ ОБЩЕСТВЕННОЙ РАБОТЫ";
        List<Student> studentList = getStudentList();
        List resultList;

        System.out.println(criteriaList.get(3));
        resultList = new ArrayList<Student>();

        switch (selectedItem) {
            case CRITERIA_2:
                final String MIN_SCORE = criteriaList.get(1);
                final String MAX_SCORE = criteriaList.get(2);
                Integer studentScore_2 = 0;


                for (Student student : studentList) {
                    studentScore_2 = 0;
                    for (Exam exam : student.getExamList()) {
                        studentScore_2 += exam.getScore();
                    }
                    if (student.getSurname().equals(SURNAME) && studentScore_2 <= Integer.valueOf(MAX_SCORE) && studentScore_2 >= Integer.valueOf(MIN_SCORE)) {
                        resultList.add(student);
                    }
                }
                break;
            case CRITERIA_1:
                final String GROUP = criteriaList.get(3);
                System.out.println(GROUP);
                for (Student student : studentList) {
                    if (student.getSurname().equals(SURNAME) & student.getGroup().equals(GROUP)) {
                        resultList.add(student);
                    }
                }
                break;
            case CRITERIA_3:
                final String MIN_SCORE_3 = criteriaList.get(4);
                final String MAX_SCORE_3 = criteriaList.get(5);
                final String GROUP_3 = criteriaList.get(3);
                Integer studentScore_3 = 0;


                for (Student student : studentList) {
                    studentScore_3 = 0;
                    for (Exam exam : student.getExamList()) {
                        studentScore_3 += exam.getScore();
                    }

                    if (student.getGroup().equals(GROUP_3) && studentScore_3 <= Integer.valueOf(MAX_SCORE_3) && studentScore_3 >= Integer.valueOf(MIN_SCORE_3)) {
                        resultList.add(student);
                    }

                }
                break;
        }

        return resultList;
    }

    public void delete(List<Student> indexList) {
        for (Student student : indexList) {
            getStudentList().remove(student);
        }
    }
}
