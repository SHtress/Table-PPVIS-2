package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Model {
    private List<Student> studentList;
    private int examNumber;

    public Model(int examNumber, int entitiesNumber) {
        this.examNumber = examNumber;
        if (entitiesNumber > 0) {
            studentList = generateEntities(entitiesNumber);
        } else {
            studentList = new ArrayList<>();
        }
    }

    private List<Student> generateEntities(int entitiesNumber) {
        List<Student> students = new ArrayList<>();
        List<Exam> examList;

        for (int i = 0; i < entitiesNumber; i++) {
            examList = new ArrayList<>();
            for (int j = 0; j < examNumber; j++) {
                examList.add(new Exam(new Random().nextInt(10) + 1));
            }
            students.add(
                    new Student(new SNP(RandomizationData.reqSurname(),
                            RandomizationData.reqName(),
                            RandomizationData.reqPatronym()),
                            (char) (65 + new Random().nextInt(25)) + "" + (char) (65 + new Random().nextInt(25)) + new Random().nextInt(9999),
                            examList)
            );
        }
        return students;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public void addStudent(Student student) {
        studentList.add(student);
    }

    public int getExamNumber() {
        return examNumber;
    }
}
