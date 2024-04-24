package com.example.csci699tw;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MachineNode {
    // Properties for machine details
    String name;
    String type;
    String controller;
    String status;
    private GraphView graphView;


    // Graphical properties
    private double x;
    private double y;
    public Circle circle;

    // Constructor
    public MachineNode(String name, String type, String controller, String status, double x, double y, GraphView graphView) {
        this.name = name;
        this.type = type;
        this.controller = controller;
        this.status = status;
        this.x = x;
        this.y = y;
        this.graphView = graphView;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    // Method to create a graphical representation of the machine node
    public Node createGraphic()
    {
        Pane pane = new Pane();

        if(status.equals("Idle"))
        {
            circle = new Circle(x, y, 20);
            circle.setFill(Color.rgb(224, 209, 60));

            // Create the label
            Text label = new Text(name);
            double labelWidth = label.getLayoutBounds().getWidth();
            label.setX(x - labelWidth / 2); // Center the label over the circle
            label.setY(y - circle.getRadius() - 10); // Position above the circle

            pane.getChildren().add(circle);
            pane.getChildren().add(label);
        }
        else if (status.equals("Maintenance") && !GraphView.bfsInProgress)
        {
            circle = new Circle(x, y, 20);
            circle.setFill(Color.rgb(134, 34, 152));

            // Create the label
            Text label = new Text(name);
            double labelWidth = label.getLayoutBounds().getWidth();
            label.setX(x - labelWidth / 2); // Center the label over the circle
            label.setY(y - circle.getRadius() - 10); // Position above the circle

            pane.getChildren().add(circle);
            pane.getChildren().add(label);

            // Create a flashing effect
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1.5), evt -> circle.setFill(Color.WHITE)), // Switch to white
                    new KeyFrame(Duration.seconds(2.5), evt -> circle.setFill(Color.rgb(134, 34, 152)))  // Switch back to green
            );
            timeline.setCycleCount(Timeline.INDEFINITE); // Make the animation loop indefinitely
            timeline.play(); // Start the animation
            GraphView.animations.add(timeline);

        }
        else if (status.equals("Stopped"))
        {
            circle = new Circle(x, y, 20);
            circle.setFill(Color.rgb(189, 2, 44));

            // Create the label
            Text label = new Text(name);
            double labelWidth = label.getLayoutBounds().getWidth();
            label.setX(x - labelWidth / 2); // Center the label over the circle
            label.setY(y - circle.getRadius() - 10); // Position above the circle

            pane.getChildren().add(circle);
            pane.getChildren().add(label);

        }
        else
        {
            circle = new Circle(x, y, 20);
            circle.setFill(Color.rgb(5, 80, 5)); // Initial color set to green

            // Create the label
            Text label = new Text(name);
            double labelWidth = label.getLayoutBounds().getWidth();
            label.setX(x - labelWidth / 2); // Center the label over the circle
            label.setY(y - circle.getRadius() - 10); // Position above the circle

            pane.getChildren().add(circle);
            pane.getChildren().add(label);
            // Create a flashing effect

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.8), evt -> circle.setFill(Color.WHITE)), // Switch to white
                    new KeyFrame(Duration.seconds(1.5), evt -> circle.setFill(Color.rgb(5, 80, 5)))  // Switch back to green
            );
            timeline.setCycleCount(Timeline.INDEFINITE); // Make the animation loop indefinitely
            timeline.play(); // Start the animation
            GraphView.animations.add(timeline);

        }

        return pane;
    }

    public void changeColor(Color color) {
        // Directly access the circle instance variable
        if (this.circle != null) {
            Platform.runLater(() -> {
                this.circle.setFill(color);  // Change the color of the circle
                enableDetailOnClick();       // Reattach the click listener here
            });
        }
    }


    public void enableDetailOnClick() {
        if (this.circle != null) {
            this.circle.setOnMouseClicked(event -> {
                showMachineDetails();
            });
        }
    }


    private void showMachineDetails() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Machine Details");
        alert.setHeaderText("Details for " + name);
        alert.setContentText("Name: " + name +
                "\nType: " + type +
                "\nController: " + controller +
                "\nStatus: Compromised");

        alert.showAndWait();
    }


}
