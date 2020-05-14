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
    private int                rowsOnPage,
                               currentPage = 1,
                               numberOfPages;
    private Label              paginationLabel,
                               itemsCountLabel;
    private Button             resetSearchButton;
    private TextField          rowsOnPageField;
    private TableView<Student> table;
    private ToolBar            navigator,
                               pagination;
    private Pane               tableElement;
    private List<Student> defaultStudentList;
    private ObservableList<Student> studentObsList,
                                    curStudentObsList;

    public TableElement(List<Student> studentList, int examNumber){
        final int    TABLE_HEIGHT                 = 600,
                     TABLE_WIDTH                  = 1460,
                     DEFAULT_ROWS_ON_PAGE_NUMBER  = 17;
        final String SNP_COLUMN_LABEL_TEXT        = "ФИО студэнта",
                     GROUP_COLUMN_LABEL_TEXT      = "Группа",
                     EXAMS_COLUMN_LABEL_TEXT      = "Общественная работа",
                     EXAM_NAME_COLUMN_LABEL_TEXT  = "название",
                     EXAM_SCORE_COLUMN_LABEL_TEXT = "часы",
                     ROWS_ON_PAGE_LABEL_TEXT      = "Строк на странице: ",
                     TO_BEGIN_BUTTON_LABEL_TEXT   = "<<",
                     TO_LEFT_BUTTON_LABEL_TEXT    = "<",
                     TO_RIGHT_BUTTON_LABEL_TEXT   = ">",
                     TO_END_BUTTON_LABEL_TEXT     = ">>";
        Property  sProperty       = new SimpleStringProperty();
        Button    toBeginButton   = new Button(TO_BEGIN_BUTTON_LABEL_TEXT),
                  toLeftButton    = new Button(TO_LEFT_BUTTON_LABEL_TEXT),
                  toRightButton   = new Button(TO_RIGHT_BUTTON_LABEL_TEXT),
                  toEndButton     = new Button(TO_END_BUTTON_LABEL_TEXT);
        TableColumn<Student, String> snpCol   = new TableColumn<>(SNP_COLUMN_LABEL_TEXT),
                                     groupCol = new TableColumn<>(GROUP_COLUMN_LABEL_TEXT),
                                     examsCol = new TableColumn<>(EXAMS_COLUMN_LABEL_TEXT),
                                     examNameCol;
        List<TableColumn<Student, String>> examNumColList   = new ArrayList<>(),
                                           examNameColList  = new ArrayList<>(),
                                           examScoreColList = new ArrayList<>();

        defaultStudentList = studentList;
        studentObsList     = FXCollections.observableArrayList(defaultStudentList);
        curStudentObsList  = FXCollections.observableArrayList();

        snpCol.setMinWidth(300);
        snpCol.setCellValueFactory(new PropertyValueFactory<>("alignSnp"));
        groupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
        for(int i=0; i < examNumber; i++){
            final int k = i;
         //   examNameCol = new TableColumn(EXAM_NAME_COLUMN_LABEL_TEXT);
         //   examNameCol.setMinWidth(250);
         //   examNameColList.add(examNameCol);
//            TableColumn<Student, String> studentStringTableColumn = examNameColList.get(i);
//            studentStringTableColumn.setCellValueFactory(p -> {
//                   // sProperty.setValue(String.valueOf(p.getValue().getExamName(k)));
//                    return sProperty;
//                }
            //);
            examScoreColList.add(new TableColumn(EXAM_SCORE_COLUMN_LABEL_TEXT));
            examScoreColList.get(i).setCellValueFactory(p -> {
                    sProperty.setValue(String.valueOf(p.getValue().getExamScore(k)));
                    return sProperty;
                }
            );
            examNumColList.add(new TableColumn(Integer.toString(i+1)));
            examNumColList.get(i).getColumns().addAll(
                  //  examNameColList.get(i),
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
                new Label(ROWS_ON_PAGE_LABEL_TEXT),
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
        resetSearchButton.setOnAction(ae->{
            resetToDefaultItems();
            resetSearchButton.setVisible(false);
        });
    }

    public Pane get(){
        return tableElement;
    }

    public void rewriteDefaultList(List<Student> list){
        defaultStudentList = list;
    }

    public void resetToDefaultItems(){
        setObservableList(defaultStudentList);
    }

    public void setObservableList(List<Student> list){
        studentObsList = FXCollections.observableArrayList(list);
        resetSearchButton.setVisible(true);

        setRowsOnPage();
    }

    private void setRowsOnPage(){
        rowsOnPage = Integer.valueOf(rowsOnPageField.getText());
        currentPage = 1;

        refreshPage();
    }

    private void goBegin(){
        currentPage = 1;
        refreshPage();
    }

    private void goLeft(){
        if(currentPage > 1){
            currentPage--;
        }
        refreshPage();
    }

    private void goRight(){
        if(currentPage < numberOfPages){
            currentPage++;
        }
        refreshPage();
    }

    private void goEnd(){
        currentPage = numberOfPages;
        refreshPage();
    }

    private void refreshPage(){
        int fromIndex = (currentPage - 1) * rowsOnPage,
            toIndex   =  currentPage      * rowsOnPage;

        if(toIndex > studentObsList.size()){
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

    private void refreshPaginationLabel(){
        numberOfPages = (studentObsList.size() - 1) / rowsOnPage + 1;
        paginationLabel.setText(currentPage + "/" + numberOfPages);
        itemsCountLabel.setText("/" + studentObsList.size() + "/");
    }
}
