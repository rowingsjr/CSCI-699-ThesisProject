package com.example.csci699tw;


import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class GraphView extends Application {
    public static Pane root;
    private static Stage primary;
    private static List<MachineNode> nodes;
    private static Map<MachineNode, List<MachineNode>> adjacencyList = new HashMap<>();
    public static GraphView instance;
    public static boolean bfsInProgress = false;
    public static List<Timeline> animations = new ArrayList<>();
    private Label resultLabel;


    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        primary = primaryStage;
        root = new Pane();
        root.setPrefSize(800, 600);
        primary.setTitle("THE KRAKEN SIMULATED CYBER-ATTACK");

        resultLabel = new Label();
        resultLabel.setWrapText(true);
        root.getChildren().add(resultLabel);
        // Position the label appropriately on the screen
        resultLabel.setLayoutX(10);
        resultLabel.setLayoutY(10);

        // Retrieve cached nodes from SetupGraph
        instance = this;
        nodes = SetupGraph.createMachineNodes(instance);
        display(nodes);
        initializeAdjacencyList();
        addLegend(root);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // Position the resultLabel after the stage is shown
        Platform.runLater(() -> {
            double padding = 10.0;
            double xPosition = padding; // 10px from the left edge
            double yPosition = root.getHeight() - resultLabel.getHeight() - padding; // 10px from the bottom
            resultLabel.setLayoutX(xPosition);
            resultLabel.setLayoutY(yPosition);
        });

        // Wait for 7 seconds, then call PinCracker to attempt to crack the pin
        new Thread(() -> {
            try {
                Thread.sleep(7000); // Sleep for 7 seconds
                String result = PinCracker.crackPin("87645321"); // You can pass the target pin here
                final String resultForDisplay = result;
                if (PinCracker.correctPin)
                {
                    Platform.runLater(() -> {
                        bfsTraversal();
                        resultLabel.setText(resultForDisplay);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    // Add a method to stop all animations
    public static void stopAllAnimations() {
        animations.forEach(Timeline::stop);
        animations.clear();
    }

    private static void initializeAdjacencyList() {
        Random rand = new Random();
        // Establish adjacency; each node will be connected to 1 to 4 random other nodes.
        for (MachineNode node : nodes) {
            List<MachineNode> adjacentNodes = new ArrayList<>();
            Set<Integer> connectedIndices = new HashSet<>(); // To keep track of already connected nodes

            int connections = 1 + rand.nextInt(4); // Each node will have 1 to 4 connections
            while (adjacentNodes.size() < connections) {
                int potentialNeighborIndex = rand.nextInt(nodes.size());
                MachineNode potentialNeighbor = nodes.get(potentialNeighborIndex);

                // Ensure we don't connect a node to itself or duplicate connections
                if (!potentialNeighbor.equals(node) && connectedIndices.add(potentialNeighborIndex)) {
                    adjacentNodes.add(potentialNeighbor);
                }
            }
            adjacencyList.put(node, adjacentNodes);
        }
    }


    private void addLegend(Pane root) {
        VBox legendBox = new VBox(10);
        legendBox.getStyleClass().add("legend");
        Map<String, Color> statusColors = Map.of(
                "Running", Color.GREEN,
                "Idle", Color.YELLOW,
                "Maintenance", Color.PURPLE,
                "Stopped", Color.RED
        );

        statusColors.forEach((status, color) -> {
            HBox itemBox = new HBox(10);
            Circle colorIndicator = new Circle(10, color);
            Label label = new Label(status, colorIndicator);
            itemBox.getChildren().add(label);
            legendBox.getChildren().add(itemBox);
        });

        root.getChildren().add(legendBox);
        adjustLegendPosition(legendBox);
    }

    private void adjustLegendPosition(VBox legendBox) {
        Platform.runLater(() -> {
            double legendWidth = legendBox.getBoundsInLocal().getWidth();
            double legendHeight = legendBox.getBoundsInLocal().getHeight();
            legendBox.setLayoutX(root.getWidth() - legendWidth - 20);
            legendBox.setLayoutY(root.getHeight() - legendHeight - 20);
        });
    }

    public static void display(List<MachineNode> nodes) {
        nodes.forEach(node -> root.getChildren().add(node.createGraphic()));
    }


    public static void bfsTraversal() {
        if (nodes == null || nodes.isEmpty() || adjacencyList.isEmpty()) {
            return; // Cannot perform BFS as nodes or adjacency list is not initialized
        }

        bfsInProgress = true;
        stopAllAnimations();

        // Show warning dialog on the JavaFX Application Thread
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Security Breach");
            alert.setHeaderText("System Compromise Detected");
            alert.setContentText("This system has been compromised!");


            try {
                // Correct the path and create an Image and ImageView
                Image image = new Image("file:///C:/Users/richa/IdeaProjects/CSCI-699-ThesisWork/src/main/java/com/example/csci699tw/bonecross.jpg");
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(48); // Set as appropriate for your image
                imageView.setFitWidth(48);
                alert.setGraphic(imageView);
            } catch (Exception e) {
                // If there's an error loading the image, fall back to text
                Text errorText = new Text("⚠ Image load failed!");
                errorText.setFont(Font.font("Arial", 20));
                alert.setGraphic(errorText);
            }

            alert.showAndWait(); // Show the dialog and wait for it to be closed before continuing


            // PIN Entry Dialog
            TextInputDialog pinDialog = new TextInputDialog();
            pinDialog.setTitle("Security Check");
            int attempts = 0;
            final int MAX_ATTEMPTS = 3;
            boolean pinCorrect = false;
            final String correctPin = "12348925";

            while (attempts < MAX_ATTEMPTS && !pinCorrect) {
                pinDialog.setHeaderText("Enter Master SCADA PIN (" + (MAX_ATTEMPTS - attempts) + " attempts left)");
                pinDialog.setContentText("Please enter your PIN:");
                Optional<String> result = pinDialog.showAndWait();

                if (result.isPresent() && result.get().equals(correctPin)) {
                    pinCorrect = true;
                } else {
                    attempts++;
                    if (attempts < MAX_ATTEMPTS) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Incorrect PIN");
                        errorAlert.setHeaderText("Incorrect PIN entered");
                        errorAlert.setContentText("You have " + (MAX_ATTEMPTS - attempts) + " attempts left.");
                        errorAlert.showAndWait();
                    }
                }
            }

            if (!pinCorrect) {
                // Show fatal error alert on the final failed attempt
                Alert fatalAlert = new Alert(Alert.AlertType.ERROR);
                fatalAlert.setTitle("Fatal Error");
                fatalAlert.setHeaderText("Too many failed attempts");
                fatalAlert.setContentText("You've been locked out of the system.");
                fatalAlert.showAndWait();

                // Change the background color to fiery orange
                root.setStyle("-fx-background-color: #ff4e00;"); // Orangish color, adjust the hex as needed

                // If PIN is incorrect after 3 attempts, proceed with BFS
                Queue<MachineNode> queue = new LinkedList<>();
                Map<MachineNode, Boolean> visited = new HashMap<>();

                for (MachineNode node : nodes) {
                    visited.put(node, false);
                }

                MachineNode startNode = nodes.get(0);
                queue.add(startNode);
                visited.put(startNode, true);

                // Start BFS traversal
                processBfsQueue(queue, visited, () -> {
                    // Callback to execute after BFS completes
                    Platform.runLater(() -> {
                        for (MachineNode node : nodes) {
                            node.enableDetailOnClick();
                            node.changeColor(Color.BLACK);
                        }
                    });
                });
            }
        });
    }



    private static void processBfsQueue(Queue<MachineNode> queue, Map<MachineNode, Boolean> visited, Runnable onComplete) {
        if (queue.isEmpty()) {
            bfsInProgress = false;
            if (onComplete != null) {
               onComplete.run(); // Ensure the onComplete action is run on the JavaFX Application Thread
            }
            return;
        }

        MachineNode current = queue.poll();
        Platform.runLater(() -> current.changeColor(Color.BLACK));

        List<MachineNode> adjNodes = adjacencyList.get(current);
        for (MachineNode adj : adjNodes) {
            if (!visited.get(adj)) {
                visited.put(adj, true);
                queue.add(adj);
            }
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> processBfsQueue(queue, visited, onComplete));
        pause.play();
    }


}

