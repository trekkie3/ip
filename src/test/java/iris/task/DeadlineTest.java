package iris.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void testGenerateDeadline_success() throws TaskException {
        Deadline event = Deadline.generateDeadline("Project meeting /by 2024-06-15");
        assertEquals("[D][ ] Project meeting (by: Jun 15 2024)", event.toString());
    }

    @Test
    public void testGenerateDeadline_exception() {
        try {
            Deadline.generateDeadline("Project meeting");
            fail();
        } catch (TaskException exception) {
            assertSame(TaskExceptionType.ARGUMENTS_MISSING, exception.exceptionType);
        }
    }
}
