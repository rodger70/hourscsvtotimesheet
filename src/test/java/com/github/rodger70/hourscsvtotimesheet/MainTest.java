package com.github.rodger70.hourscsvtotimesheet;

import java.io.File;
import java.net.URL;
import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    void testMain() {
        URL url = this.getClass().getResource("/test.csv");
                
        try {
            Main.main(new String[] { "-o",  url.getPath().substring(0, url.getPath().lastIndexOf(File.separator)), url.getPath() });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Main method failed", e);
        }
        // If we reach here, the main method executed without throwing an exception
        System.out.println("Main method executed successfully.");
    }
}
