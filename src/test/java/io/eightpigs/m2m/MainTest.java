package io.eightpigs.m2m;

import io.eightpigs.m2m.util.StringUtils;
import org.junit.Test;

public class MainTest {

    @Test
    public void lowerCamelCaseTest() {
        String name = "user_id";
        assert StringUtils.lowerCamelCase(name).equals("userId");
    }

    @Test
    public void upperCamelCaseTest() {
        String name = "user_info";
        assert StringUtils.upperCamelCase(name).equals("UserInfo");
    }

    @Test
    public void indentTest() {
        String space = Vars.INDENT_STYLES.get("space").apply(4);
        assert space.equals("    ");

        String tab = Vars.INDENT_STYLES.get("tab").apply(1);
        assert tab.equals("\t");
    }

    @Test
    public void parserTest() {
        String userHome = System.getProperty("user.home");
        String path = userHome + "/Workspace/java/m2m/";
        Parser parser = Parser.init(path);
        if (parser != null) {
            try {
                parser.process();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
