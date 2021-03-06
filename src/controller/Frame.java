package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import main.DataDealer;
import main.Main;
import model.Shape;

import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

import static controller.Sketch.ShapeType;

public class Frame {
    private static Frame instance;
    @FXML
    private ColorPicker strokeColor;
    @FXML
    private ColorPicker fillColor;
    @FXML
    private Spinner<Integer> lineWidth;
    @FXML
    private Button rectangle;
    @FXML
    private Button triangle;
    @FXML
    private Button circle;
    @FXML
    private Button ellipse;
    @FXML
    private Button line;
    @FXML
    private Label coordinates;
    @FXML
    private Button delete;
    @FXML
    private Button move;
    @FXML
    private Button resize;

    private Stack<Shape> undone = new Stack<>();

    public static Frame getInstance() {
        return instance;
    }

    @FXML
    private void initialize() {
        instance = this;
        rectangle.setDisable(false);
        triangle.setDisable(false);
        circle.setDisable(false);
        ellipse.setDisable(false);
        line.setDisable(true);
        strokeColor.setValue(Color.BLACK);
        strokeColor.valueProperty().addListener(l -> Sketch.getInstance().setStrokeColor(strokeColor.getValue()));
        fillColor.valueProperty().addListener(l -> Sketch.getInstance().setFillColor(fillColor.getValue()));
        lineWidth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        lineWidth.valueProperty().addListener(l -> Sketch.getInstance().setLineWidth(lineWidth.getValue()));
    }

    @FXML
    private void rectanglePressed() {
        rectangle.setDisable(true);
        triangle.setDisable(false);
        ellipse.setDisable(false);
        circle.setDisable(false);
        line.setDisable(false);
        Sketch.getInstance().setShapeType(ShapeType.RECTANGLE);
    }

    @FXML
    private void trianglePressed() {
        rectangle.setDisable(false);
        triangle.setDisable(true);
        circle.setDisable(false);
        ellipse.setDisable(false);
        line.setDisable(false);
        Sketch.getInstance().setShapeType(ShapeType.TRIANGLE);
    }

    @FXML
    private void circlePressed() {
        rectangle.setDisable(false);
        triangle.setDisable(false);
        circle.setDisable(true);
        ellipse.setDisable(false);
        line.setDisable(false);
        Sketch.getInstance().setShapeType(ShapeType.CIRCLE);
    }

    @FXML
    private void linePressed() {
        rectangle.setDisable(false);
        triangle.setDisable(false);
        circle.setDisable(false);
        ellipse.setDisable(false);
        line.setDisable(true);
        Sketch.getInstance().setShapeType(ShapeType.LINE);
    }

    @FXML
    private void ellipsePressed() {
        rectangle.setDisable(false);
        triangle.setDisable(false);
        circle.setDisable(false);
        ellipse.setDisable(true);
        line.setDisable(false);
        Sketch.getInstance().setShapeType(ShapeType.ELLIPSE);
    }

    @FXML
    private void movePressed() {
        move.setDisable(true);
        resize.setDisable(false);
        delete.setDisable(false);
        Sketch.getInstance().setMoveFlag(true);
        Sketch.getInstance().setResizeFlag(false);
        Sketch.getInstance().setDeleteFlag(false);
    }

    @FXML
    private void resizePressed() {
        resize.setDisable(true);
        move.setDisable(false);
        delete.setDisable(false);
        Sketch.getInstance().setMoveFlag(false);
        Sketch.getInstance().setResizeFlag(true);
        Sketch.getInstance().setDeleteFlag(false);
    }

    @FXML
    private void deletePressed() {
        move.setDisable(false);
        resize.setDisable(false);
        delete.setDisable(true);
        Sketch.getInstance().setMoveFlag(false);
        Sketch.getInstance().setResizeFlag(false);
        Sketch.getInstance().setDeleteFlag(true);

    }

    @FXML
    public void undoPressed() {
        if (!Main.getInstance().getShapes().isEmpty()) {
            undone.push(Main.getInstance().getShapes().pop());
            undone.peek().remove();
        }
    }

    @FXML
    private void redoPressed() {
        if (!undone.isEmpty()) {
            Main.getInstance().getShapes().push(undone.pop());
            Main.getInstance().getShapes().peek().draw();
        }
    }

    @FXML
    private void newPressed() {
        if (Main.getInstance().getRoot().getCenter().equals(Sketch.getInstance().getCanvas())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("New File");
            alert.setHeaderText("New File");
            alert.setContentText("Are you sure that you want to exit this file, and create a new file?\nUnsaved changes will be lost.");

            Optional<ButtonType> res = alert.showAndWait();
            if (res.get() != ButtonType.OK)
                return;
        }
        TextInputDialog dialog = new TextInputDialog("1195x629");
        dialog.setTitle("Select width and height");
        dialog.setHeaderText("Canvas Width-Height");
        dialog.setContentText("Width x Height:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(w -> {
            String[] dims = w.split("x");
            try {
                Sketch.getInstance().setMainWidth(Double.valueOf(dims[0]));
                Sketch.getInstance().setMainHeight(Double.valueOf(dims[1]));
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error reading dimensions.\nPlease use the correct format.").show();
                return;
            }
            Main.getInstance().getShapes().forEach(model.Shape::remove);
            Main.getInstance().getShapes().clear();
            Main.getInstance().getPrimaryStage().setTitle("Paint - Untitled");

            Main.getInstance().getRoot().setCenter(Sketch.getInstance().getCanvas());
        });
    }

    @FXML
    private void openPressed() {
        DataDealer.load();

        if (Objects.equals(Main.getInstance().getPrimaryStage().getTitle(), "Paint"))
            Main.getInstance().getPrimaryStage().setTitle("Paint - Untitled");
        if (!Main.getInstance().getRoot().getCenter().equals(Sketch.getInstance().getCanvas()))
            Main.getInstance().getRoot().setCenter(Sketch.getInstance().getCanvas());
    }

    @FXML
    private void savePressed() {
        DataDealer.save();
    }

    public Label getCoordinates() {
        return coordinates;
    }

    public Button getDelete() {
        return delete;
    }

    public Button getMove() {
        return move;
    }

    public Button getResize() {
        return resize;
    }
}
