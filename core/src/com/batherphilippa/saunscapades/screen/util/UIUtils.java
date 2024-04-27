package com.batherphilippa.saunscapades.screen.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * UIUtils - métodos estáticos para la pantalla.
 */
public class UIUtils {

    /**
     * Despeja la pantalla.
     */
    public static void clearScreen() {
        // colour
        Gdx.gl.glClearColor(0, 0, 0, 1);
        // clears screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Crea un objeto de tabla VisuUI.
     * @return VisTable
     */
    public static VisTable createTableObj() {

        if (!VisUI.isLoaded()) {
            VisUI.load();
        }

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        return table;
    }
}
