package com.translate.res_translate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class EntranceAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        InputDialog inputDialog = new InputDialog();
        inputDialog.setVisible(true);
    }
}
