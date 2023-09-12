package com.biblio.view.Book;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class addBook extends JFrame implements ActionListener {
    private boolean visibility;
    public addBook(boolean visibility) {

        this.visibility = visibility;




        setVisible(visibility);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
