package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.services.BookReportService;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SchedulesTheReportTest {

    @Test
    public void scheduler_runs_report_generation() throws Exception {
        BookReportService reportService = mock(BookReportService.class);
        LibraryReportScheduler scheduler = new LibraryReportScheduler(reportService);

        scheduler.runReport();

        verify(reportService).createBookReport();
    }

    @Test
    public void report_is_triggered_by_scheduling_infrastructure() throws NoSuchMethodException {
        Method method = LibraryReportScheduler.class.getDeclaredMethod("runReport");

        Scheduled scheduled = method.getAnnotation(Scheduled.class);

        assertThat(scheduled).isNotNull();
        assertThat(scheduled.fixedRate()).isEqualTo(25000);
    }
}