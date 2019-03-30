package io.eightpigs.m2m;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Convert table to java object.
 *
 * @author eightpigs <eightpigs@outlook.com>
 * @date 2019-03-28
 */
public class Main extends AnAction {

    private static final String configFileName = ".mysql2Model.yaml";

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Parser parser = Parser.init(project.getBasePath());
        if (parser != null) {
            try {
                parser.process();
            } catch (Exception ex) {
                Messages.showMessageDialog(ex.getMessage(), "parse error", Messages.getErrorIcon());
            }
        }
    }
}
