package com.example.csci699tw;

import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SetupGraph
{
    private static List<MachineNode> cachedNodes = null;
    public static void main(String[] args)
    {
        Application.launch(GraphView.class, args);
    }
    public static List<MachineNode> createMachineNodes(GraphView graphViewInstance)
    {
        if (cachedNodes != null)
        {
            return cachedNodes;
        }
        List<MachineNode> nodes = new ArrayList<>();
        String[] names = {"StampMaster 3000", "FlexiBender Pro", "PolyMolder X2", "LaserCutter Edge", "PressMate Titan",
        "DrillRig Commander", "PlasmaPro Elite", "CNC PrecisionCraft", "ForgeMaster 500", "GrindWiz Industrial",
        "PunchPress Power", "RoboWelder AI", "SheetShear Max", "PipeBender Ultra", "PackLine Speedster"};
        String[] types = {"CNC", "Lathe", "Drill", "Milling Machine", "Grinder"};
        //Colors for status Running = Flashing Green, Idle = Yellow, Maintenance = Purple, Stopped Equals Red
        String[] statuses = {"Running", "Idle", "Maintenance", "Stopped"};
        String[] plcController = {"S7-300", "S7-400", "S7-1200", "S7-1500"};

        Random random = new Random();

        // Example positions for 15 nodes
        double[][] positions = new double[][]{
                {100, 100}, {200, 50}, {300, 100}, {400, 150}, {500, 100},
                {600, 50}, {700, 100}, {100, 200}, {200, 250}, {300, 200},
                {400, 250}, {500, 200}, {600, 250}, {700, 200}, {400, 350}
        };

        for (int i = 1; i < positions.length; i++) {
            int randomNames = random.nextInt(names.length);
            int randomTypes = random.nextInt(types.length);
            int randomStatus = random.nextInt(statuses.length);
            int randomPlcController = random.nextInt(plcController.length);
            String name = names[randomNames];
            String type = types[randomTypes];
            String controller = plcController[randomPlcController];
            String status = statuses[randomStatus];
            double x = positions[i][0];
            double y = positions[i][1];

            // Create a new MachineNode with the randomized attributes and positions.
            nodes.add(new MachineNode(name, type, controller, status, x, y, graphViewInstance));

        }

        return nodes;
    }

}

