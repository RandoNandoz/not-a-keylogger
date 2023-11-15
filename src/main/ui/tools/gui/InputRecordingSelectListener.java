package ui.tools.gui;

import model.InputRecording;
import model.InputTime;
import ui.tools.RecordingController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class InputRecordingSelectListener implements ListSelectionListener {
    private final JList<InputTime> detailsList;
    private final RecordingController rc;
    public InputRecordingSelectListener(JList<InputTime> detailsList, RecordingController rc) {
        this.detailsList = detailsList;
        this.rc = rc;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
//        assert(e.getFirstIndex() == e.getLastIndex());

        System.out.println(e.getFirstIndex());
        System.out.println(e.getLastIndex());

        DefaultListModel<InputTime> model = new DefaultListModel<>();
        for (var x : rc.getUiList().get(e.getLastIndex()).getInputs()) {
            model.addElement(x);
        }
        detailsList.setModel(model);
        detailsList.updateUI();
    }
}
