package com.socialmedia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class SocialMediaApp {
    private JFrame frame;
    private JTextArea channelContentArea; // General channel messages
    private JTextArea userMessagesArea; // Messages specific to selected user
    private JTextField messageField;
    private DefaultListModel<String> userListModel; // List model for managing users
    private JList<String> userList; // GUI list for users
    private Map<String, List<String>> userMessages; // Stores messages received by each user
    private List<String> subscribedUsers; // Logical list for subscriptions

    public SocialMediaApp() {
        subscribedUsers = new ArrayList<>();
        userMessages = new HashMap<>();
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Social Media Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Channel content area
        JPanel channelPanel = new JPanel(new GridLayout(1, 2));
        channelPanel.setBorder(BorderFactory.createTitledBorder("Channel Content"));

        channelContentArea = new JTextArea();
        channelContentArea.setEditable(false);
        channelContentArea.setBorder(BorderFactory.createTitledBorder("General Channel Messages"));
        channelPanel.add(new JScrollPane(channelContentArea));

        userMessagesArea = new JTextArea();
        userMessagesArea.setEditable(false);
        userMessagesArea.setBorder(BorderFactory.createTitledBorder("Selected User Messages"));
        channelPanel.add(new JScrollPane(userMessagesArea));

        mainPanel.add(channelPanel, BorderLayout.CENTER);

        // Subscriber/User panel
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBorder(BorderFactory.createTitledBorder("Users"));

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add listener to display messages for the selected user
        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showUserMessages(userList.getSelectedValue());
            }
        });

        userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        // User actions
        JPanel userActions = new JPanel(new GridLayout(1, 3));

        JButton subscribeButton = new JButton("Subscribe");
        subscribeButton.addActionListener(e -> subscribeUser());

        JButton unsubscribeButton = new JButton("Unsubscribe");
        unsubscribeButton.addActionListener(e -> unsubscribeUser());

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> removeUser());

        userActions.add(subscribeButton);
        userActions.add(unsubscribeButton);
        userActions.add(removeButton);
        userPanel.add(userActions, BorderLayout.SOUTH);

        mainPanel.add(userPanel, BorderLayout.WEST);

        // Content posting controls
        JPanel contentControls = new JPanel(new BorderLayout());
        messageField = new JTextField();
        JButton publishButton = new JButton("Publish");
        publishButton.addActionListener(e -> postMessage());

        JButton newUserButton = new JButton("Add New User");
        newUserButton.addActionListener(e -> addNewUser());

        contentControls.add(messageField, BorderLayout.CENTER);
        contentControls.add(publishButton, BorderLayout.EAST);
        contentControls.add(newUserButton, BorderLayout.SOUTH);

        mainPanel.add(contentControls, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void addNewUser() {
        String userName = JOptionPane.showInputDialog(frame, "Enter new user name:");
        if (userName != null && !userName.trim().isEmpty() && !userListModel.contains(userName)) {
            userListModel.addElement(userName);
            userMessages.put(userName, new ArrayList<>()); // Initialize message list for the user
            JOptionPane.showMessageDialog(frame, "User added: " + userName);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid user name or user already exists.");
        }
    }

    private void subscribeUser() {
        String selectedUser = userList.getSelectedValue();
        if (selectedUser != null && !subscribedUsers.contains(selectedUser)) {
            subscribedUsers.add(selectedUser);
            JOptionPane.showMessageDialog(frame, "User subscribed: " + selectedUser);
        } else {
            JOptionPane.showMessageDialog(frame, "No user selected or already subscribed.");
        }
    }

    private void unsubscribeUser() {
        String selectedUser = userList.getSelectedValue();
        if (selectedUser != null && subscribedUsers.contains(selectedUser)) {
            subscribedUsers.remove(selectedUser);
            JOptionPane.showMessageDialog(frame, "User unsubscribed: " + selectedUser);
        } else {
            JOptionPane.showMessageDialog(frame, "No user selected or user is not subscribed.");
        }
    }

    private void removeUser() {
        String selectedUser = userList.getSelectedValue();
        if (selectedUser != null) {
            userListModel.removeElement(selectedUser);
            subscribedUsers.remove(selectedUser);
            userMessages.remove(selectedUser);
            userMessagesArea.setText(""); // Clear user-specific messages
            JOptionPane.showMessageDialog(frame, "User removed: " + selectedUser);
        } else {
            JOptionPane.showMessageDialog(frame, "No user selected.");
        }
    }

    private void postMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            // Add message to general channel content
            channelContentArea.append("Channel: " + message + "\n");
            messageField.setText("");

            // Notify subscribed users
            if (subscribedUsers.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No subscribers to receive the message.");
            } else {
                for (String user : subscribedUsers) {
                    userMessages.get(user).add(message); // Add message to user's message list
                }
                JOptionPane.showMessageDialog(frame, "Message sent to " + subscribedUsers.size() + " subscribers.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Message cannot be empty.");
        }
    }

    private void showUserMessages(String userName) {
        if (userName != null) {
            List<String> messages = userMessages.get(userName);
            if (messages != null && !messages.isEmpty()) {
                userMessagesArea.setText(String.join("\n", messages));
            } else {
                userMessagesArea.setText("No messages for this user.");
            }
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(SocialMediaApp::new);
    }
}
