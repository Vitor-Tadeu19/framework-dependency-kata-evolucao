package com.gregorriegler.frameworkdependency;

import com.gregorriegler.frameworkdependency.scheduler.LibraryReportScheduler;
import com.gregorriegler.frameworkdependency.infrastructure.report.BookReportFileWriter;
import com.gregorriegler.frameworkdependency.services.BookReportService;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SchedulesTheReportTest {

    @Test
    public void scheduler_runs_report_generation() throws Exception {
        BookReportService reportService = mock(BookReportService.class);
        BookReportFileWriter writer = mock(BookReportFileWriter.class);
        when(reportService.generateBookReport()).thenReturn("123,title");
        LibraryReportScheduler scheduler = new LibraryReportScheduler(reportService, writer);

        scheduler.runReport();

        verify(writer).write("123,title");
    }

    @Test
    public void report_is_triggered_by_scheduling_infrastructure() throws NoSuchMethodException {
        Method method = LibraryReportScheduler.class.getDeclaredMethod("runReport");

        assertThat(method.getAnnotation(Scheduled.class)).isNotNull();
        assertThat(method.getAnnotation(Scheduled.class).fixedRate()).isEqualTo(25_000);
    }
}
