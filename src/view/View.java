package view;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Exam;
import model.Student;
import Controller.Controller;

import java.util.ArrayList;
import java.util.List;

public class View {
    private final String REGEX_DIGITS_ONLY = "^\\d+$";
    private Scene        scene;
	private TableElement tableElement;
    private Controller controller;
    private Stage        stage;
    private VBox         root;
    private enum WindowType {
        DELETE, SEARCH
    }

	public View(Controller controller) {
        final int    STAGE_WIDTH  = 1460,
                     STAGE_HEIGHT = 781;
        final String STAGE_TITLE_TEXT = "Lab2";

        this.controller = controller;
        initWindow();
        stage = new Stage();
        stage.setWidth (STAGE_WIDTH);
        stage.setHeight(STAGE_HEIGHT);
        stage.setTitle (STAGE_TITLE_TEXT);
        stage.setScene(scene);
	}

	private void initWindow(){
        final String FILE_MENU_LABEL_TEXT              = "Файл",
                     EDIT_MENU_LABEL_TEXT              = "Изменить",
                     NEW_DOC_MENU_ITEM_LABEL_TEXT      = "Новый документ",
                     OPEN_DOC_MENU_ITEM_LABEL_TEXT     = "Открыть документ",
                     SAVE_DOC_MENU_ITEM_LABEL_TEXT     = "Сохранить документ",
                     ADD_ITEM_MENU_ITEM_LABEL_TEXT     = "Добавить строки",
                     SEARCH_ITEMS_MENU_ITEM_LABEL_TEXT = "Искать строки",
                     DELETE_ITEMS_MENU_ITEM_LABEL_TEXT = "Удалить строки",
                     CLOSE_APP_MENU_ITEM_LABEL_TEXT    = "Выйти",
                     NEW_DOC_BUTTON_LABEL_TEXT         = "Новый документ",
                     OPEN_DOC_BUTTON_LABEL_TEXT        = "Открыть документ",
                     SAVE_DOC_BUTTON_LABEL_TEXT        = "Сохранить документ",
                     ADD_ITEMS_BUTTON_LABEL_TEXT       = "Добавить строки",
                     SEARCH_ITEMS_BUTTON_LABEL_TEXT    = "Искать строки",
                     DELETE_ITEMS_BUTTON_LABEL_TEXT    = "Удалить строки";
        MenuItem newDocMenuItem      = new MenuItem(NEW_DOC_MENU_ITEM_LABEL_TEXT),
                 openDocMenuItem     = new MenuItem(OPEN_DOC_MENU_ITEM_LABEL_TEXT),
                 saveMenuItem        = new MenuItem(SAVE_DOC_MENU_ITEM_LABEL_TEXT),
                 addItemsMenuItem    = new MenuItem(ADD_ITEM_MENU_ITEM_LABEL_TEXT),
                 searchItemsMenuItem = new MenuItem(SEARCH_ITEMS_MENU_ITEM_LABEL_TEXT),
                 deleteItemsMenuItem = new MenuItem(DELETE_ITEMS_MENU_ITEM_LABEL_TEXT),
                 closeAppMenuItem    = new MenuItem(CLOSE_APP_MENU_ITEM_LABEL_TEXT);
        Menu     fileMenu            = new Menu(FILE_MENU_LABEL_TEXT),
                 editMenu            = new Menu(EDIT_MENU_LABEL_TEXT);
        MenuBar  menuBar             = new MenuBar();
        Button   newDocButton        = new Button(NEW_DOC_BUTTON_LABEL_TEXT),
                 openDocButton       = new Button(OPEN_DOC_BUTTON_LABEL_TEXT),
                 saveDocButton       = new Button(SAVE_DOC_BUTTON_LABEL_TEXT),
                 addItemsButton      = new Button(ADD_ITEMS_BUTTON_LABEL_TEXT),
                 searchItemsButton   = new Button(SEARCH_ITEMS_BUTTON_LABEL_TEXT),
                 deleteItemsButton   = new Button(DELETE_ITEMS_BUTTON_LABEL_TEXT);
        ToolBar  instruments;

        fileMenu.getItems().addAll(
                newDocMenuItem,
                openDocMenuItem,
                saveMenuItem,
                new SeparatorMenuItem(),
                closeAppMenuItem);
        editMenu.getItems().addAll(
                addItemsMenuItem,
                new SeparatorMenuItem(),
                searchItemsMenuItem,
                deleteItemsMenuItem);
        menuBar.getMenus().addAll(
                fileMenu,
                editMenu);

        instruments = new ToolBar(
                newDocButton,
                openDocButton,
                saveDocButton,
                new Separator(),
                addItemsButton,
                searchItemsButton,
                deleteItemsButton);

        tableElement = new TableElement(controller.getStudentList(), controller.getExamNumber());

        root = new VBox();
        root.getChildren().addAll(
                menuBar,
                instruments,
                tableElement.get());
        scene = new Scene(root);

        newDocButton.setOnAction(ae -> newDoc());
            newDocMenuItem.setOnAction(ae -> newDoc());
        openDocButton.setOnAction(ae -> openDoc());
            openDocMenuItem.setOnAction(ae -> openDoc());
        saveDocButton.setOnAction(ae -> saveDoc());
            saveMenuItem.setOnAction(ae -> saveDoc());
        addItemsButton.setOnAction(ae -> addItems());
            addItemsMenuItem.setOnAction(ae -> addItems());
        searchItemsButton.setOnAction(ae -> searchItems());
            searchItemsMenuItem.setOnAction(ae -> searchItems());
        deleteItemsButton.setOnAction(ae -> deleteItems());
            deleteItemsMenuItem.setOnAction(ae -> deleteItems());
        closeAppMenuItem.setOnAction(ae -> Platform.exit());
    }

	public Stage getStage(){
	    return stage;
    }

	private void newDoc(){
	    final String EXAM_NUM_LABEL_TEXT       = "Количество семестров: ",
                     ENTRY_NUM_LABEL_TEXT      = "Сгенерировать записей: ",
//                     INFO_LABEL_TEXT           = "\nКалі колькасць экзаменаў ці колькасць экзаменаў для генераццыі\n" +
//                                                 "не будзе ўведзена карыстальнікам, яна будзе прыраўнана\n" +
//                                                 "перадвызначанаму значэнню.\n ",
                     NEW_DOC_WINDOW_TITLE_TEXT = "Создать новый документ";
	    TextField  examNumField = new TextField("10"),
                   entNumField  = new TextField("0");
        GridPane   grid         = new GridPane();
        Pane       root         = new VBox();
	    Alert      newDocWindow;

	    grid.addRow(0,
                new Label(EXAM_NUM_LABEL_TEXT),
                examNumField
        );
        grid.addRow(1,
                new Label(ENTRY_NUM_LABEL_TEXT),
                entNumField
        );
        root.getChildren().addAll(
                grid
               // new Label(INFO_LABEL_TEXT)
        );

	    newDocWindow = createEmptyCloseableDialog();
	    newDocWindow.setTitle(NEW_DOC_WINDOW_TITLE_TEXT);
        newDocWindow.getDialogPane().setContent(root);
	    newDocWindow.show();

        ((Button)newDocWindow.getDialogPane().lookupButton(newDocWindow.getButtonTypes().get(0))).setOnAction(ae->{
            int examNumber     = 10,
                entitiesNumber = 0;

	        if(!examNumField.getText().isEmpty() & examNumField.getText().matches(REGEX_DIGITS_ONLY)){
                examNumber = Integer.valueOf(examNumField.getText());
            }
            if(!entNumField.getText().isEmpty() & entNumField.getText().matches(REGEX_DIGITS_ONLY)){
                entitiesNumber = Integer.valueOf(entNumField.getText());
            }
            controller.newDoc(examNumber, entitiesNumber);

            this.root.getChildren().remove(tableElement.get());
            tableElement = new TableElement(controller.getStudentList(), controller.getExamNumber());
            this.root.getChildren().addAll(
                    tableElement.get()
            );

	        newDocWindow.close();
        });
    }

    private void openDoc(){
        FileChooser openDocChooser = new FileChooser();

        openDocChooser.setTitle("Открыть документ");
        openDocChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Все файлы", "*.*"),
                new FileChooser.ExtensionFilter("XML-документ", "*.xml")
        );

        try {
            controller.openDoc(openDocChooser.showOpenDialog(stage));
        } catch (Exception exception){
            exception.printStackTrace();
        }

        tableElement.rewriteDefaultList(controller.getStudentList());
        tableElement.resetToDefaultItems();
    }

    private void saveDoc(){
        FileChooser saveDocChooser = new FileChooser();

        saveDocChooser.setTitle("Сохранить документ");
        saveDocChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Все файлы", "*.*"),
                new FileChooser.ExtensionFilter("XML-документ", "*.xml")
        );

        controller.saveDoc(saveDocChooser.showSaveDialog(stage));
    }

	private void addItems(){
        final String WINDOW_TITLE_TEXT      = "Добавить строки: ",
                     SURNAME_LABEL_TEXT     = "Фамилия: ",
                     NAME_LABEL_TEXT        = "Имя: ",
                     PATRONYM_LABEL_TEXT    = "Отчество: ",
                     GROUP_LABEL_TEXT       = "Группа: ",
                     EXAM_NUMBER_LABEL_TEXT = "Семестр: ",
                     EXAM_SCORE_LABEL_TEXT  = "Часы: ";
        List<Exam>   examList       = new ArrayList<>();
        TextField    surnameField   = new TextField(),
                     nameField      = new TextField(),
                     patronymField  = new TextField(),
                     groupField     = new TextField();
        class ComboElement{
            private int       selectedItem;
            private TextField examNameField,
                              examScoreField;
            private ComboBox  comboBox;

            public ComboElement(int selectedItem){
                this.selectedItem = selectedItem;
                this.comboBox     = new ComboBox<Integer>();
                this.examNameField  = new TextField("");
                this.examScoreField = new TextField("0");

                for(int i = 0; i < controller.getExamNumber(); i++){
                    comboBox.getItems().addAll(((Integer)(i+1)).toString());
                    examList.add(new Exam( 0));
                }

                comboBox.setValue(((Integer)(selectedItem)).toString());
                refreshSelected();

                comboBox.setOnAction(ae -> refreshSelected());
            }


            public TextField getExamScoreField(){
                return examScoreField;
            }

            public ComboBox get(){
                return comboBox;
            }

            public void refreshSelected(){
                try {
                    examList.get(selectedItem).setScore(Integer.valueOf(examScoreField.getText()));
                } catch (NumberFormatException e){
                    examList.get(selectedItem).setScore(0);
                }
                selectedItem = comboBox.getSelectionModel().getSelectedIndex();

                examScoreField.setText(((Integer)examList.get(selectedItem).getScore()).toString());
            }
        }
        ComboElement examComElement = new ComboElement(1);
	    GridPane     root           = new GridPane();
        Alert        addItemWindow;

        root.addRow(0,
                new Label(SURNAME_LABEL_TEXT),
                surnameField
        );
        root.addRow(1,
                new Label(NAME_LABEL_TEXT),
                nameField
        );
        root.addRow(2,
                new Label(PATRONYM_LABEL_TEXT),
                patronymField
        );
        root.addRow(3,
                new Label(GROUP_LABEL_TEXT),
                groupField
        );
        root.addRow(4,
                new Label(EXAM_NUMBER_LABEL_TEXT),
                examComElement.get()
        );

        root.addRow(5,
                new Label(EXAM_SCORE_LABEL_TEXT),
                examComElement.getExamScoreField()
        );

        addItemWindow = createEmptyCloseableDialog();
        addItemWindow.setTitle(WINDOW_TITLE_TEXT);
        addItemWindow.getDialogPane().setContent(root);
        addItemWindow.show();

        ((Button)addItemWindow.getDialogPane().lookupButton(addItemWindow.getButtonTypes().get(0))).setOnAction(ae->{
            controller.addStudent(
                    surnameField.getText(),
                    nameField.getText(),
                    patronymField.getText(),
                    groupField.getText(),
                    examList
            );
            examComElement.refreshSelected();
            tableElement.resetToDefaultItems();
            addItemWindow.close();
        });
    }

    private class RequestElement{
        final String CRITERIA_1 = "ПО ФАМИЛИИ И НОМЕРУ ГРУППЫ",
                     CRITERIA_2 = "ПО ФАМИЛИИ И КОЛИЧЕСТВУ ОБЩЕСТВЕННОЙ РАБОТЫ",
                     CRITERIA_3 = "ПО НОМЕРУ ГРУППЫ И КОЛЛИЧЕСТВУ ОБЩЕСТВЕННОЙ РАБОТЫ";
        private String       selectedItem;
        private ComboBox     criteriaComBox;
        private Button       searchButton;
        private TableElement tableElement;
        private GridPane     grid;
        private Pane         criteriaChooser,
                             root;
        private List<Label>     criteria1LabelList,
                                criteria2LabelList,
                                criteria3LabelList;
        private List<TextField> criteria1FieldList,
                                criteria2FieldList,
                                criteria3FieldList;

        public RequestElement(WindowType windowType){
            criteriaComBox = new ComboBox();
            criteriaComBox.getItems().addAll(
                    CRITERIA_1,
                    CRITERIA_2,
                    CRITERIA_3
            );
            criteriaComBox.setValue(CRITERIA_1);
            searchButton    = new Button("Искать");
            criteriaChooser = new HBox();

            criteria1LabelList = new ArrayList<>();
            criteria1FieldList = new ArrayList<>();
            criteria2LabelList = new ArrayList<>();
            criteria2FieldList = new ArrayList<>();
            criteria3LabelList = new ArrayList<>();
            criteria3FieldList = new ArrayList<>();
            initCriteriaLists();
            grid              = new GridPane();
            switchPreset();

            tableElement = new TableElement(new ArrayList<>(), controller.getExamNumber());

            this.root = new VBox();

            if(windowType == WindowType.SEARCH){
                criteriaChooser.getChildren().addAll(
                        new Label("Критерий поиска: "),
                        criteriaComBox,
                        searchButton
                );

                this.root.getChildren().addAll(
                        new Separator(),
                        new Separator(),
                        criteriaChooser,
                        grid,
                        new Separator(),
                        new Separator(),
                        tableElement.get(),
                        new Separator(),
                        new Separator(),
                        new Separator()
                );
            }

            if(windowType == WindowType.DELETE){
                criteriaChooser.getChildren().addAll(
                        new Label("Критерий поиска: "),
                        criteriaComBox
                );

                this.root.getChildren().addAll(
                        new Separator(),
                        new Separator(),
                        criteriaChooser,
                        grid
                );
            }


            criteriaComBox.setOnAction(ae -> switchPreset());
            searchButton.setOnAction(ae->{
                List<Student> studentList = search();

                tableElement.setObservableList(studentList);
            });
        }

        private void switchPreset(){
            final int CRITERIA_1_FIELD_NUMBER = 2,
                      CRITERIA_2_FIELD_NUMBER = 3,
                      CRITERIA_3_FIELD_NUMBER = 3;

            grid.getChildren().clear();
            selectedItem = criteriaComBox.getSelectionModel().getSelectedItem().toString();
            switch (selectedItem){
                case CRITERIA_1:
                    for(int i = 0; i < CRITERIA_1_FIELD_NUMBER; i++){
                        grid.addRow(i,
                                criteria1LabelList.get(i),
                                criteria1FieldList.get(i)
                        );
                    }
                    break;
                case CRITERIA_2:
                    for(int i = 0; i < CRITERIA_2_FIELD_NUMBER; i++){
                        grid.addRow(i,
                                criteria2LabelList.get(i),
                                criteria2FieldList.get(i)
                        );
                    }
                    break;
                case CRITERIA_3:
                    for(int i = 0; i < CRITERIA_3_FIELD_NUMBER; i++){
                        grid.addRow(i,
                                criteria3LabelList.get(i),
                                criteria3FieldList.get(i)
                        );
                    }
                    break;
            }
        }

        private void initCriteriaLists(){
            final String SURNAME_LABEL_TEXT       = "Фамилия: ",
                         GROUP_LABEL_TEXT         = "Номер группы: ",
                         DISCIPLINE_LABEL_TEXT    = "Семестр: ",
                         MINIMAL_SCORE_LABEL_TEXT = "Минимум часов работы: ",
                         MAXIMAL_SCORE_LABEL_TEXT = "Максимум часов работы: ";
            TextField    surnameField             = new TextField();

            criteria1LabelList.add(new Label(SURNAME_LABEL_TEXT));
            criteria1LabelList.add(new Label(GROUP_LABEL_TEXT));
            criteria1FieldList.add(surnameField);
            criteria1FieldList.add(new TextField());

            criteria2LabelList.add(new Label(SURNAME_LABEL_TEXT));
            criteria2LabelList.add(new Label(MINIMAL_SCORE_LABEL_TEXT));
            criteria2LabelList.add(new Label(MAXIMAL_SCORE_LABEL_TEXT));
            criteria2FieldList.add(surnameField);
            criteria2FieldList.add(new TextField());
            criteria2FieldList.add(new TextField());

            criteria3LabelList.add(new Label(GROUP_LABEL_TEXT));
            criteria3LabelList.add(new Label(MINIMAL_SCORE_LABEL_TEXT));
            criteria3LabelList.add(new Label(MAXIMAL_SCORE_LABEL_TEXT));
            criteria3FieldList.add(new TextField());
            criteria3FieldList.add(new TextField());
            criteria3FieldList.add(new TextField());
        }

        public Pane get(){
            return this.root;
        }

        public List search(){
            int  minimalScore,
                 maximalScore;
            List criteriaList;

            try{
                minimalScore = Integer.parseInt(criteria2FieldList.get(1).getText());
            } catch (NumberFormatException e){
                minimalScore = 0;
            }
            try{
                maximalScore = Integer.parseInt(criteria2FieldList.get(2).getText());
            } catch (NumberFormatException e){
                maximalScore = 0;
            }
            try{
                minimalScore = Integer.parseInt(criteria3FieldList.get(1).getText());
            } catch (NumberFormatException e){
                minimalScore = 0;
            }
            try{
                maximalScore = Integer.parseInt(criteria3FieldList.get(2).getText());
            } catch (NumberFormatException e){
                maximalScore = 0;
            }

            criteriaList = new ArrayList<String>();
            criteriaList.add(criteria1FieldList.get(0).getText());
            criteriaList.add(criteria2FieldList.get(1).getText());
            criteriaList.add(criteria2FieldList.get(2).getText());

            criteriaList.add(criteria1FieldList.get(1).getText());
            criteriaList.add(criteria3FieldList.get(1).getText());
            criteriaList.add(criteria3FieldList.get(2).getText());

            return controller.search(selectedItem, criteriaList);
        }
    }

    private void searchItems(){
        final String WINDOW_TITLE_TEXT = "Искать строки";
        Alert        searchItemsWindow;
        RequestElement requestElement = new RequestElement(WindowType.SEARCH);

        searchItemsWindow = createEmptyCloseableDialog();
        searchItemsWindow.setTitle(WINDOW_TITLE_TEXT);
        searchItemsWindow.getDialogPane().setContent(requestElement.get());
        searchItemsWindow.show();

        ((Button)searchItemsWindow.getDialogPane().lookupButton(searchItemsWindow.getButtonTypes().get(0))).setOnAction(
                ae-> searchItemsWindow.close()
        );
    }

    private void deleteItems(){
        final String WINDOW_TITLE_TEXT = "Удалить строки";
        Alert        deleteItemsWindow;
        RequestElement requestElement = new RequestElement(WindowType.DELETE);

        deleteItemsWindow = createEmptyCloseableDialog();
        deleteItemsWindow.setTitle(WINDOW_TITLE_TEXT);
        deleteItemsWindow.getDialogPane().setContent(requestElement.get());
        deleteItemsWindow.show();

        ((Button)deleteItemsWindow.getDialogPane().lookupButton(deleteItemsWindow.getButtonTypes().get(0))).setOnAction(ae->{
            createDeleteInfoWindow(String.valueOf(requestElement.search().size()));
            controller.delete(requestElement.search());
            tableElement.resetToDefaultItems();
            deleteItemsWindow.close();
        });
    }

    private void createDeleteInfoWindow(String deleteInfo){
        final String CLOSE_BUTTON_LABEL_TEXT = "Хорошо";
        ButtonType   closeButton       = new ButtonType(CLOSE_BUTTON_LABEL_TEXT);
	    Alert window  = new Alert(Alert.AlertType.NONE);
	    VBox  vertice = new VBox();

	    vertice.getChildren().add(new Label("Удалено" + deleteInfo + " строк."));
	    window.getDialogPane().setContent(vertice);
        window.getButtonTypes().addAll(closeButton);
        window.show();
    }

    private Alert createEmptyCloseableDialog(){
        final String CLOSE_BUTTON_LABEL_TEXT = "Дальше";
        ButtonType   closeButton       = new ButtonType(CLOSE_BUTTON_LABEL_TEXT);
        Alert        window            = new Alert(Alert.AlertType.NONE);

        window.getButtonTypes().addAll(closeButton);
        return window;
    }
}
