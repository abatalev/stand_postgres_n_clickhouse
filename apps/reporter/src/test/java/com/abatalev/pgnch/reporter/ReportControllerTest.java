package com.abatalev.pgnch.reporter;

import com.abatalev.pgnch.reporter.model.Action;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportControllerTest {

    @Test
    public void runReport() throws JRException, IOException {
        new ReportController(null).generateServerReport(new MockHttpServletResponse(), "actions.xlsx", "example.jrxml", "Actions", getDataSource());
    }

    private JRDataSource getDataSource() {
        return new JRBeanCollectionDataSource(addAction(new ArrayList<Action>(), "some name"));
    }

    private static List<Action> addAction(List<Action> dataList, String id) {
        Action action = new Action();
        action.setId(id);
        action.setDelta(1);
        dataList.add(action);
        return dataList;
    }
}
