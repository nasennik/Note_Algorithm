import com.innowise.Intervals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class IntervalsTest {

    @ParameterizedTest
    @MethodSource("intervalConstructionDataProvider")
    public void testIntervalConstruction(String[] intervalAndNote, String expectedNote){

        assertEquals(expectedNote, Intervals.intervalConstruction(intervalAndNote));
    }

    static Stream<Arguments> intervalConstructionDataProvider(){
        String[] IntervalAndNote = {"M2", "C", "asc"};
        return Stream.of(
                Arguments.arguments(new String[] {"M2", "C", "asc"}, "D"),
                Arguments.arguments(new String[] {"P5", "B", "asc"}, "F#"),
                Arguments.arguments(new String[] {"m2", "Bb", "dsc"}, "A"),
                Arguments.arguments(new String[] {"M3", "Cb", "dsc"}, "Abb"),
                Arguments.arguments(new String[] {"P4", "G#", "dsc"}, "D#"),
                Arguments.arguments(new String[] {"m3", "B", "dsc"}, "G#"),
                Arguments.arguments(new String[] {"m2", "Fb", "asc"}, "Gbb"),
                Arguments.arguments(new String[] {"M2", "E#", "dsc"}, "D#"),
                Arguments.arguments(new String[] {"P4", "E", "dsc"}, "B"),
                Arguments.arguments(new String[] {"m2", "D#", "asc"}, "E"),
                Arguments.arguments(new String[] {"M7", "G", "asc"}, "F#")

        );
    }

    @ParameterizedTest
    @MethodSource("intervalIdentificationDataProvider")
    public void testIntervalIdentification(String[] intervalAndNote, String expectedNote){

        assertEquals(expectedNote, Intervals.intervalIdentification(intervalAndNote));
    }

    static Stream<Arguments> intervalIdentificationDataProvider(){

        return Stream.of(
                Arguments.arguments(new String[] {"C", "D"}, "M2"),
                Arguments.arguments(new String[] {"Fb", "Gbb"}, "m2"),
                Arguments.arguments(new String[] {"G", "F#", "asc"}, "M7"),
                Arguments.arguments(new String[] {"Bb", "A", "dsc"}, "m2"),
                Arguments.arguments(new String[] {"Cb", "Abb", "dsc"}, "M3"),
                Arguments.arguments(new String[] {"G#", "D#", "dsc"}, "P4"),
                Arguments.arguments(new String[] {"E", "B", "dsc"}, "P4"),
                Arguments.arguments(new String[] {"E#", "D#", "dsc"}, "M2"),
                Arguments.arguments(new String[] {"B", "G#", "dsc"}, "m3")
        );
    }

    @Test
    public void testIncorrectNumberOfElementsInInputArray() throws RuntimeException{
       String[] args = {"M2", "G", "asc", "D"};

       Assertions.assertThrows(RuntimeException.class, () ->
               Intervals.intervalConstruction(args),  "Illegal number of elements in input array");

        String[] emptyArr = {""};

       Assertions.assertThrows(RuntimeException.class, () ->
                Intervals.intervalIdentification(emptyArr),  "Illegal number of elements in input array");

    }
}