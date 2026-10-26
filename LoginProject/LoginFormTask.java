import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class User {
    private String username;
    private String password;
    private String role;

    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

class WelcomePage extends Frame {
    WelcomePage(User user) {
        super("Welcome");

        setLayout(new GridLayout(3, 1, 5, 5));

        Label greeting = new Label("Hello, " + user.getUsername(), Label.CENTER);
        greeting.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(greeting);

        Label roleLabel = new Label("Role: " + user.getRole(), Label.CENTER);
        add(roleLabel);

        Button close = new Button("Close");
        Panel buttonPanel = new Panel(new FlowLayout());
        buttonPanel.add(close);
        add(buttonPanel);

        close.addActionListener(e -> dispose());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(320, 180);
        setLocationRelativeTo(null);
    }
}

public class LoginFormTask extends Frame {
    private final List<User> users = new ArrayList<>();
    private int wrongPasswordAttempts = 0;

    public LoginFormTask() {
        super("Login");
        loadUsers();

        setLayout(new GridBagLayout());
        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));
        form.add(new Label("Username:", Label.RIGHT));
        TextField username = new TextField(18);
        form.add(username);

        form.add(new Label("Password:", Label.RIGHT));
        TextField password = new TextField(18);
        password.setEchoChar('*');
        form.add(password);

        form.add(new Label("Role:", Label.RIGHT));
        Choice role = new Choice();
        role.add("Student");
        role.add("TA");
        role.add("Professor");
        form.add(role);

        content.add(form, BorderLayout.CENTER);

        Label message = new Label(" ", Label.CENTER);
        message.setForeground(Color.RED);
        content.add(message, BorderLayout.SOUTH);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        Button cancel = new Button("Cancel");
        Button login = new Button("Login");
        buttons.add(cancel);
        buttons.add(login);

        Panel mainPanel = new Panel(new BorderLayout(10, 15));
        mainPanel.add(content, BorderLayout.CENTER);
        mainPanel.add(buttons, BorderLayout.SOUTH);
        add(mainPanel);

        login.addActionListener(e -> {
            System.out.println("login button clicked!");
            String enteredUsername = username.getText().trim();
            String enteredPassword = password.getText();

            if (enteredUsername.isEmpty() || enteredPassword.trim().isEmpty()) {
                message.setText("Please fill in username and password.");
                return;
            }

            User user = findUser(enteredUsername);
            if (user == null) {
                message.setText("Username not found.");
                return;
            }

            if (!user.getPassword().equals(enteredPassword)) {
                wrongPasswordAttempts++;

                if (wrongPasswordAttempts >= 3) {
                    login.setEnabled(false);
                    message.setText("Login disabled after 3 incorrect passwords.");
                } else {
                    message.setText("Incorrect password. Attempts left: "
                            + (3 - wrongPasswordAttempts));
                }
                return;
            }

            if (!user.getRole().equals(role.getSelectedItem())) {
                message.setText("Incorrect role selected.");
                return;
            }

            message.setForeground(new Color(0, 128, 0));
            message.setText("Login successful!");
            new WelcomePage(user).setVisible(true);
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(420, 250);
        setLocationRelativeTo(null);
    }

    private void loadUsers() {
        users.add(new User("student1", "stud123", "Student"));
        users.add(new User("ta1", "ta123", "TA"));
        users.add(new User("prof1", "prof123", "Professor"));
    }

    private User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new LoginFormTask().setVisible(true);
    }
}
