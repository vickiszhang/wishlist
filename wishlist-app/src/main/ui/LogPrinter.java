package ui;

import model.Event;
import model.EventLog;

// log printer that calls printLog
public class LogPrinter {

    // EFECTS: prints eventlog el to console
    void printLog(EventLog el) {

        for (Event e: el) {
            System.out.println(e);
        }

    }
}
