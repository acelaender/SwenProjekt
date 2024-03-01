import at.fhtw.swen.mctg.models.Card;
import at.fhtw.swen.mctg.models.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ModelsTest {

    @Test
    public void cardTest(){

        Card card1 = new Card(1, "monster1", new Element(1, "water", 3), 10, "monster", "water");
        Card card2 = new Card(2, "monster2", new Element(2, "fire", 1), 10, "monster", "fire");
        Card card3 = new Card(3, "monster3", new Element(3, "normal", 2), 10, "monster", "normal");

        Assertions.assertEquals(true, card1.isVulnerable("normal"));
        Assertions.assertEquals(true, card2.isVulnerable("water"));
        Assertions.assertEquals(true, card3.isVulnerable("fire"));
        Assertions.assertEquals(false, card3.isVulnerable("water"));
    }

}
