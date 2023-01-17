import org.example.MoexParser.DataProcessor;
import org.example.MoexParser.DataProcessor.TickersMap;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoexParserTest {
    private static final boolean SHORT_FILE = true;

    private static String getfilePath (boolean SHORT_FILE){
        if (SHORT_FILE){
            return "/home/daniil/IdeaProjects/SpecialCurse/src/main/resources/t.txt";
        }
        return "/home/daniil/IdeaProjects/SpecialCurse/src/main/resources/trades.txt";
    }
    private TickersMap _getMoexData_when_provided_file_name() throws FileNotFoundException {
        //prepare
        var dataProcessor = new DataProcessor();
        //act
        var tickerProcessor = new DataProcessor()
                .getTickersMap(getfilePath(true));
        return tickerProcessor;
    }
        @Test
    public void getMoexData_when_provided_wrong_file_name(){
        // Кажется этот тест бессмысленный...
        var dataProcessor = new DataProcessor();
        var hasFileErr = 0;
        try {
            var tickers = dataProcessor.getTickersMap("random_file.txt");
        } catch (FileNotFoundException e) {
            hasFileErr = -1;
        }
        assertEquals(-1, hasFileErr);

    }
    @Test
    public void getMoexData_when_provided_long_file(){
        // Кажется этот тест бессмысленный...
        var dataProcessor = new DataProcessor();
        var hasFileErr = 0;
        TickersMap tickers = null;
        try {
            tickers = dataProcessor.getTickersMap(getfilePath(false));
        } catch (FileNotFoundException e) {
            hasFileErr = -1;
        }
        assertEquals(1, tickers != null ? 1: -1);
    }




    //TODO: Сделать сравнение короткого файла против ручного прочтения файла (без словерей) и сравнение результата с полученным.
}
