package com.biblio.view.Admin;
import com.biblio.app.Controllers.BookController;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class index extends JFrame {

    private BookController book;
    private JButton statistic,
            all_book,
            available_book,
            brr_book,
            lost_book,
            user,
            awaiting_list,
            logout;
    public index() throws SQLException {
        book = new BookController();

        setTitle("Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();


        statistic = new JButton("Statistic");
        all_book = new JButton("All books");
        available_book = new JButton("Available books");
        brr_book = new JButton("Borrowed books");
        lost_book = new JButton("Lost books");
        user = new JButton("Users");
        awaiting_list = new JButton("Awaiting list");
        logout = new JButton("Logout");

        setButtons(statistic);
        setButtons(all_book);
        setButtons(available_book);
        setButtons(brr_book);
        setButtons(lost_book);
        setButtons(user);
        setButtons(awaiting_list);
        setButtons(logout);

        int y = 100;

        System.out.println(screenHeight);

        statistic.setBounds(50, 100 + y, 170, 30);
        all_book.setBounds(50, 150+ y, 170, 30);
        available_book.setBounds(50, 200+ y, 170, 30);
        brr_book.setBounds(50, 250+ y, 170, 30);
        lost_book.setBounds(50, 300+ y, 170, 30);
        user.setBounds(50, 350+ y, 170, 30);
        awaiting_list.setBounds(50, 400+ y, 170, 30);
        logout.setBounds(50, 450+ y, 170, 30);


        add(statistic);
        add(all_book);
        add(available_book);
        add(brr_book);
        add(lost_book);
        add(user);
        add(awaiting_list);
        add(logout);
        setVisible(true);
        // ouharrioutman@gmail.com
        // 68767498739879
    }

    private int incrementY(int y) {
        y = y + 100;
        return y + 100;
    }

    private JButton setButtons(JButton butt) {
        butt.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        butt.setContentAreaFilled(false);
        butt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return butt;
    }

}
