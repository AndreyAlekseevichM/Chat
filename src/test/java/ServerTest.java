import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ServerTest {

    @Test
    public void writeMsgTest() throws IOException {
        String msg = "Test message";
        File file = new File("./file.log");
        long beforeLength = file.length();
        Server.writeFileServer(msg);
        long afterLength = file.length();
        boolean afterLengthOverBefore = afterLength > beforeLength;
        String fileContent = read(file.getPath());
        boolean haveTestMsg = fileContent.substring(fileContent.length() - msg.length()).contains(msg);
        Assertions.assertTrue(afterLengthOverBefore && haveTestMsg);
    }

    static String read(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}