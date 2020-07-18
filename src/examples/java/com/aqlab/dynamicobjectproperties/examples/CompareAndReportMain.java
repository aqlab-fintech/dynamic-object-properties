package com.aqlab.dynamicobjectproperties.examples;

import com.aqlab.dynamicobjectproperties.property.BeanProperty;
import com.aqlab.dynamicobjectproperties.property.ObjectProperty;
import com.aqlab.dynamicobjectproperties.util.ObjectPropertyUtils;
import com.google.common.base.Joiner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DEMONSTRATION ONLY. UNMAINTAINED CODE. USE AT YOUR OWN RISK.
 * <p>
 * Compare 2 ReportLine objects and report the fields which are different
 */
public class CompareAndReportMain {
    public static class ReportLine {
        private final LocalDate reportDate;
        private final String reportedBy;
        private final String subject;
        private final String details;
        private final String reviewedBy;
        private final int version;

        public ReportLine(final LocalDate reportDate, final String reportedBy, final String subject, final String details, final String reviewedBy, final int version) {
            this.reportDate = reportDate;
            this.reportedBy = reportedBy;
            this.subject = subject;
            this.details = details;
            this.reviewedBy = reviewedBy;
            this.version = version;
        }

        public LocalDate getReportDate() {
            return reportDate;
        }

        public String getReportedBy() {
            return reportedBy;
        }

        public String getSubject() {
            return subject;
        }

        public String getDetails() {
            return details;
        }

        public String getReviewedBy() {
            return reviewedBy;
        }

        public int getVersion() {
            return version;
        }
    }

    public static void main(final String[] argv) {
        final ReportLine line1 = new ReportLine(
                LocalDate.of(2020, 1, 1),
                "Chan",
                "Weather is hot",
                "Today's highest temperature is 42 celcius!",
                "Wong",
                1);
        final ReportLine line2 = new ReportLine(
                LocalDate.of(2020, 1, 2),
                "Chan",
                "Weather is hot!",
                "Today's highest temperature is 42 celcius!",
                "Cheung",
                2);
        final List<ObjectProperty<ReportLine>> diffProperties = new ArrayList<>(ObjectPropertyUtils.diff(line1, line2, ReportLine.class));

        System.out.println("The following fields are different");
        System.out.println();
        System.out.print("LineNo,");
        System.out.println(Joiner.on(',').join(diffProperties.stream().map(p -> ((BeanProperty<ReportLine>) p).getPropertyName()).iterator()));
        System.out.println("--------------------------------------------");
        System.out.print("1,");
        // ObjectPropertyUtils.createList is demonstrated below - even though this is not the most efficient way to achieve the same goal
        System.out.println(Joiner.on(',').join(ObjectPropertyUtils.createList(line1, diffProperties)));
        System.out.print("2,");
        System.out.println(Joiner.on(',').join(ObjectPropertyUtils.createList(line2, diffProperties)));
        System.out.println("--------------------------------------------");
        System.out.println("END OF REPORT");
    }
}
