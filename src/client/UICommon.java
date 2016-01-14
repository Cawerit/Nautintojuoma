package client;


import javax.swing.*;

public class UICommon {

    /**
     * Estää kaikkien nappien käytön
     * @param buttons
     */
    public static void disableToggleButtons(JToggleButton[] buttons){
        setButtonsState(buttons, false);
    }

    /**
     * Sallii kaikkien annettujen nappien käytön
     * @param buttons
     */
    public static void enableToggleButtons(JToggleButton[] buttons){
        setButtonsState(buttons, true);
    }

    private static void setButtonsState(JToggleButton[] buttons, boolean to){
        for(JToggleButton btn : buttons){
            btn.setEnabled(to);
        }
    }

}
