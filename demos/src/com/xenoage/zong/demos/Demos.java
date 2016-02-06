package com.xenoage.zong.demos;

import com.xenoage.utils.EnumUtils;
import com.xenoage.zong.Zong;
import com.xenoage.zong.demos.minimal.MinimalDemo;
import com.xenoage.zong.demos.simplegui.SimpleGuiDemo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Shows a window, where the user can select a demo application to run.
 *
 * @author Andreas Wenger
 */
public class Demos
    extends Application {

    @RequiredArgsConstructor
    private enum Demo {
        MinimalDemo("Minimal demo: file conversion"),
        SimpleGUIDemo("Simple GUI demo: score viewer");

        private static final int defaultDemo = 1;
        private final String description;
    }

    public static void main(String... args)
        throws Exception {
        Application.launch(Demos.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //demo given by parameter?
        Demo demo = null;
        if (getParameters().getUnnamed().size() > 0) {
            demo = EnumUtils.getEnumValue(getParameters().getUnnamed().get(0), Demo.values());
        }
        else {
            //show available demos in terminal
            System.out.println("Available demos, for direct launching:");
            for (Demo d : Demo.values())
                System.out.println(" - task 'run" + d.name() + "': " + d.description);
        }

        //select demo in GUI
        if (demo == null) {
            List<String> choices = new ArrayList<>();
            for (Demo d : Demo.values())
                choices.add(d.description);

            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(Demo.defaultDemo), choices);
            dialog.setTitle(Zong.projectFamilyName + " Demos");
            dialog.setHeaderText("Please select a demo to run.");
            dialog.setContentText("Demo:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                int demoIndex = choices.indexOf(result.get());
                if (demoIndex > -1)
                    demo = Demo.values()[demoIndex];
            }
        }

        if (demo == Demo.MinimalDemo) {
            MinimalDemo.main();
            Platform.exit();
        }
        else if (demo == Demo.SimpleGUIDemo) {
            SimpleGuiDemo.initZong();
            new SimpleGuiDemo().start(primaryStage);
        }
    }
}
