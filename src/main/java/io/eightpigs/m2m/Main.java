package io.eightpigs.m2m;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import io.eightpigs.m2m.util.IntellijUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Convert table to java object.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-28
 */
public class Main extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "m2m", false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                Parser parser = Parser.init(project.getBasePath());
                if (parser != null) {
                    try {
                        parser.process();
                        LocalFileSystem.getInstance().refresh(false);
                        IntellijUtils.message("m2m: convert the table to the model to complete.");
                    } catch (Exception ex) {
                        IntellijUtils.errorMsg(ex.getMessage());
                    }
                } else {
                    IntellijUtils.errorMsg("The config file (.m2m.yaml) does not exist.");
                }
            }
        });


    }
}
