package com.translate.res_translate;

import com.translate.res_translate.engine.Engine;
import com.translate.res_translate.engine.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InputDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea jTextArea;

    public InputDialog() {
        setContentPane(contentPane);
        setModal(true);

        //弹窗大小、以及居中显示
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(null);

        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        new Engine(jTextArea.getText(), state -> {
            switch (state) {
                case FORMAT_FAIL:
                    errorToast("输入的格式不正确，请检查确认。");
                    break;
                case NET_FAIL:
                    errorToast("可能网络请求失败。");
                    break;
                case FAIL:
                    errorToast("发生未知错误导致失败......");
                    break;
                case SUCCESS:
                    successToast("成功！！！");
                    break;
            }
            dispose();
        }).start();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void errorToast(String content) {
        JOptionPane.showMessageDialog(null, content, "提示", JOptionPane.ERROR_MESSAGE);
    }

    public static void successToast(String content) {
        JOptionPane.showMessageDialog(null, content, "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        InputDialog dialog = new InputDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
