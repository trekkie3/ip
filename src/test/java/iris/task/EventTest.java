package iris.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    public void testGenerateEvent_success() throws TaskException {
        Event event = Event.generateEvent("Project meeting /from 2024-06-15 /to 2024-06-18");
        assertEquals("[E][ ] Project meeting (from: Jun 15 2024 to: Jun 18 2024)", event.toString());
    }

    @Test
    public void testGenerateEvent_exception() {
        try {
            Event.generateEvent("Project meeting /from 2024-06-15");
            fail();
        } catch (TaskException exception) {
            assertSame(TaskExceptionType.ARGUMENTS_MISSING, exception.exceptionType);
        }
    }
}
