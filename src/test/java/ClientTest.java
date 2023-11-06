import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ClientTest {

    @Test
    public void nicknameTest() {
        String nickname = "Petya";
        Assertions.assertNotNull(nickname);
    }
}