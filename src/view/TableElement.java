package view;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Student;

import java.util.ArrayList;
import java.util.List;

public class TableElement {
    private int rowsOnPage;
    private int currentPage = 1;
    private int numberOfPages;
    private Label paginationLabel;
    private Label itemsCountLabel;
    private Button resetSearchButton;
    private TextField rowsOnPageField;
    private TableView<Student> table;
    private ToolBar navigator;
    private ToolBar pagination;
    private Pane tableElement;
    private List<Student> defaultStudentList;
    private ObservableList<Student> studentObsList;
    private ObservableList<Student> curStudentObsList;

    private int TABLE_HEIGHT = 600;
    private int TABLE_WIDTH = 1460;
    private int DEFAULT_ROWS_ON_PAGE_NUMBER = 17;

    public enum INIT_WINDOW_LABEL {
        SNP_COLUMN_LABEL_TEXT("ФИО студэнта"),
        GROUP_COLUMN_LABEL_TEXT("Группа"),
        EXAMS_COLUMN_LABEL_TEXT("Общественная работа"),
        EXAM_SCORE_COLUMN_LABEL_TEXT("часы"),
        ROWS_ON_PAGE_LABEL_TEXT("Строк на странице: "),
        TO_BEGIN_BUTTON_LABEL_TEXT("<<"),
        TO_LEFT_BUTTON_LABEL_TEXT("<"),
        TO_RIGHT_BUTTON_LABEL_TEXT(">"),
        TO_END_BUTTON_LABEL_TEXT(">>");

        private final String label_text;

        INIT_WINDOW_LABEL(String label_text) {
            this.label_text = label_text;
        }

        public String label_text() {
            return label_text;
        }
    }


    public TableElement(List<Student> studentList, int examNumber) {
        Property sProperty = new SimpleStringProperty();
        Button toBeginButton = new Button(INIT_WINDOW_LABEL.TO_BEGIN_BUTTON_LABEL_TEXT.label_text);
        Button toLeftButton = new Button(INIT_WINDOW_LABEL.TO_LEFT_BUTTON_LABEL_TEXT.label_text);
        Button toRightButton = new Button(INIT_WINDOW_LABEL.TO_RIGHT_BUTTON_LABEL_TEXT.label_text);
        Button toEndButton = new Button(INIT_WINDOW_LABEL.TO_END_BUTTON_LABEL_TEXT.label_text);
        TableColumn<Student, String> snpCol = new TableColumn<>(INIT_WINDOW_LABEL.SNP_COLUMN_LABEL_TEXT.label_text);
        TableColumn<Student, String> groupCol = new TableColumn<>(INIT_WINDOW_LABEL.GROUP_COLUMN_LABEL_TEXT.label_text);
        TableColumn<Student, String> examsCol = new TableColumn<>(INIT_WINDOW_LABEL.EXAMS_COLUMN_LABEL_TEXT.label_text);
        List<TableColumn<Student, String>> examNumColList = new ArrayList<>();
        List<TableColumn<Student, String>> examScoreColList = new ArrayList<>();

        defaultStudentList = studentList;
        studentObsList = FXCollections.observableArrayList(defaultStudentList);
        curStudentObsList = FXCollections.observableArrayList();

        snpCol.setMinWidth(300);
        snpCol.setCellValueFactory(new PropertyValueFactory<>("alignSnp"));
        groupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
        for (int i = 0; i < examNumber; i++) {
            final int k = i;

            examScoreColList.add(new TableColumn(INIT_WINDOW_LABEL.EXAM_SCORE_COLUMN_LABEL_TEXT.label_text));
            examScoreColList.get(i).setCellValueFactory(p -> {
                        sProperty.setValue(String.valueOf(p.getValue().getExamScore(k)));
                        return sProperty;
                    }
            );
            examNumColList.add(new TableColumn(Integer.toString(i + 1)));
            examNumColList.get(i).getColumns().addAll(
                    examScoreColList.get(i));
            examsCol.getColumns().add(examNumColList.get(i));
        }

        paginationLabel = new Label();
        navigator = new ToolBar(
                toBeginButton,
                toLeftButton,
                paginationLabel,
                toRightButton,
                toEndButton
        );

        itemsCountLabel = new Label("/" + studentObsList.size() + "/");
        rowsOnPageField = new TextField();
        rowsOnPageField.setText(String.valueOf(DEFAULT_ROWS_ON_PAGE_NUMBER));
        resetSearchButton = new Button("Сбросить поиск");
        resetSearchButton.setVisible(false);
        pagination = new ToolBar(
                itemsCountLabel,
                new Separator(),
                new Label(INIT_WINDOW_LABEL.ROWS_ON_PAGE_LABEL_TEXT.label_text),
                rowsOnPageField,
                new Separator(),
                navigator,
                resetSearchButton
        );

        table = new TableView<>();
        table.setMinHeight(TABLE_HEIGHT);
        table.setMaxWidth(TABLE_WIDTH);
        table.getColumns().addAll(
                snpCol,
                groupCol,
                examsCol
        );
        table.setItems(curStudentObsList);
        setRowsOnPage();

        tableElement = new VBox();
        tableElement.getChildren().addAll(table,
                pagination);

        rowsOnPageField.setOnAction(ae -> setRowsOnPage());
        toBeginButton.setOnAction(ae -> goBegin());
        toLeftButton.setOnAction(ae -> goLeft());
        toRightButton.setOnAction(ae -> goRight());
        toEndButton.setOnAction(ae -> goEnd());
        resetSearchButton.setOnAction(ae -> {
            resetToDefaultItems();
            resetSearchButton.setVisible(false);
        });
    }

    public Pane get() {
        return tableElement;
    }

    public void rewriteDefaultList(List<Student> list) {
        defaultStudentList = list;
    }

    public void resetToDefaultItems() {
        setObservableList(defaultStudentList);
    }

    public void setObservableList(List<Student> list) {
        studentObsList = FXCollections.observableArrayList(list);
        resetSearchButton.setVisible(true);

        setRowsOnPage();
    }

    private void setRowsOnPage() {
        rowsOnPage = Integer.valueOf(rowsOnPageField.getText());
        currentPage = 1;

        refreshPage();
    }

    private void goBegin() {
        currentPage = 1;
        refreshPage();
    }

    private void goLeft() {
        if (currentPage > 1) {
            currentPage--;
        }
        refreshPage();
    }

    private void goRight() {
        if (currentPage < numberOfPages) {
            currentPage++;
        }
        refreshPage();
    }

    private void goEnd() {
        currentPage = numberOfPages;
        refreshPage();
    }

    private void refreshPage() {
        int fromIndex = (currentPage - 1) * rowsOnPage,
                toIndex = currentPage * rowsOnPage;

        if (toIndex > studentObsList.size()) {
            toIndex = studentObsList.size();
        }

        curStudentObsList.clear();
        curStudentObsList.addAll(
                studentObsList.subList(
                        fromIndex,
                        toIndex
                )
        );

        refreshPaginationLabel();
    }

    private void refreshPaginationLabel() {
        numberOfPages = (studentObsList.size() - 1) / rowsOnPage + 1;
        paginationLabel.setText(currentPage + "/" + numberOfPages);
        itemsCountLabel.setText("/" + studentObsList.size() + "/");
    }
}
