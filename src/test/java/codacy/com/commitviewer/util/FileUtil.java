package codacy.com.commitviewer.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@UtilityClass
public class FileUtil {
    public static String getResourceAsString(String name) throws IOException {
        InputStream input = FileUtil.class.getClassLoader().getResourceAsStream(name);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            StringBuilder resultBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line);
            }

            return resultBuilder.toString();
        }
    }
}
