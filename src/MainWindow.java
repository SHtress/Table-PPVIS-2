import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import Controller.Controller;
import view.View;

public class MainWindow extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage mainStage) {
        Model model = new Model(10, 0);
        Controller controller = new Controller(model);
        View view = new View(controller);

        mainStage = view.getStage();
        mainStage.show();
    }
}
