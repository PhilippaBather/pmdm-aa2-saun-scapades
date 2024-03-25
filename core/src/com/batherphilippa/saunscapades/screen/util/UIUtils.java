package com.batherphilippa.saunscapades.scene.util;

import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

public class UIUtils {

    public static VisTable createTableObj() {

        if (!VisUI.isLoaded()) {
            VisUI.load();
        }

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        return table;
    }
}
