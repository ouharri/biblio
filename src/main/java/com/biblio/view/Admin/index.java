package com.biblio.view.Admin;

import com.biblio.app.Controllers.BookController;
import com.biblio.app.Models.Role;
import com.biblio.app.Models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class index extends JFrame implements ActionListener {

    private User User;
    private BookController book;
    private JButton statistic,
            all_book,
            available_book,
            brr_book,
            lost_book,
            user,
            awaiting_list,
            logout;


    public index(User u) throws SQLException {
        book = new BookController();
        User = u;

        setTitle("Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        ImageIcon logoIcon = new ImageIcon("assets/images/app/user.png");
        Image UserLogo = logoIcon.getImage();

        ImageIcon Statistic_icon = new ImageIcon("assets/icon/statistics.png");
        ImageIcon Book_icon = new ImageIcon("assets/icon/book.png");
        ImageIcon Available_icon = new ImageIcon("assets/icon/available.png");
        ImageIcon Borrowed_icon = new ImageIcon("assets/icon/borrow.png");
        ImageIcon Lost_icon = new ImageIcon("assets/icon/lost.png");
        ImageIcon Users_icon = new ImageIcon("assets/icon/user.png");
        ImageIcon Awaiting_icon = new ImageIcon("assets/icon/waiting.png");
        ImageIcon Logout_icon = new ImageIcon("assets/icon/logout.png");

        statistic = new JButton("  Statistic",Statistic_icon);
        all_book = new JButton("  All books",Book_icon);
        available_book = new JButton("  Available books",Available_icon);
        brr_book = new JButton("  Borrowed books",Borrowed_icon);
        lost_book = new JButton("  Lost books",Lost_icon);
        user = new JButton("  Users",Users_icon);
        awaiting_list = new JButton("  Awaiting list",Awaiting_icon);
        logout = new JButton("  Logout",Logout_icon);

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

        JLabel logoLabel = new JLabel(new ImageIcon(UserLogo));
        logoLabel.setBounds(50, 50, 64, 64);

        StringBuilder user_roles = new StringBuilder();
        user_roles.append(" [");
        u.getRoles().forEach(role -> {
            user_roles.append(role.getRole()).append(", ");
        });
        user_roles.setLength(user_roles.length() - 1);
        user_roles.append("]");

        JLabel user_name = new JLabel(u.getFirst_name() + " " + u.getLast_name());
        user_name.setFont(new Font("Arial", Font.PLAIN, 25));
        user_name.setBounds(127, 70, 200,17);

        JLabel user_contact = new JLabel((u.getEmail() != null ? u.getEmail() : u.getPhone() != null ? u.getPhone() : u.getCnie()));
        user_contact.setFont(new Font("Arial", Font.PLAIN, 13));
        user_contact.setBounds(127, 90, 300,15);

//        JLabel user_role = new JLabel(user_roles.toString());
//        user_role.setFont(new Font("Arial", Font.PLAIN, 9));
//        user_role.setBounds(127, 110, 300,15);

        statistic.setBounds(50, 100 + y, 200, 30);
        all_book.setBounds(50, 155 + y, 200, 30);
        available_book.setBounds(50, 210 + y, 200, 30);
        brr_book.setBounds(50, 265 + y, 200, 30);
        lost_book.setBounds(50, 320 + y, 200, 30);
        user.setBounds(50, 375 + y, 200, 30);
        awaiting_list.setBounds(50, 430 + y, 200, 30);
        logout.setBounds(50, 485 + y, 200, 30);

        statistic.addActionListener(this);
        all_book.addActionListener(this);
        available_book.addActionListener(this);
        brr_book.addActionListener(this);
        lost_book.addActionListener(this);
        user.addActionListener(this);
        awaiting_list.addActionListener(this);
        logout.addActionListener(this);

        add(user_name);
        add(user_contact);
//        add(user_role);

        add(logoLabel);
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
        butt.setBorderPainted(false);
        butt.setHorizontalAlignment(SwingConstants.LEFT);
        butt.setContentAreaFilled(false);
        butt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Font police = new Font("SansSerif", Font.BOLD, 17);
        butt.setFont(police);

        return butt;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logout) {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new com.biblio.view.Authentication.Signing();
            }

        }

    }


}
