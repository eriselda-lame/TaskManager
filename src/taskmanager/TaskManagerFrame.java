/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class TaskManagerFrame extends JFrame{
    private final UserProfile user;
    private final Map<String, List<Task>> allTasks;
    private final List<Task> tasks;
    private final DefaultTableModel model;
    private final JTable table;
    private final JLabel greeting = new JLabel();
    private final JLabel tracker = new JLabel();
    private final JLabel quoteLabel = new JLabel();
    private final JComboBox<String> filter =
        new JComboBox<>(new String[]{
            "All Tasks", "Study Task", "Home Task",
            "Others", "Completed Tasks",
            "Pending Tasks", "History"
        });
    private boolean nightMode = false;
    private JPanel root;
    private JPanel topPanel;
    private JPanel contentPanel;
    private static final DateTimeFormatter DATE_FORMAT=DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String[] QUOTES = {
        "The best way to get started is to quit talking and begin doing!",
        "The journey of a thousand miles begins with a single step!",
        "You don't have to see the whole staircase, just take the first step!",
        "It always seems impossible until it's done!",
        "Don't watch the clock; do what it does. Keep going!",
        "Nothing will work unless you do!",
        "Success is the sum of small efforts, repeated day-in and day-out!",
        "Focus on being productive instead of busy!",
        "What you do today can improve all your tomorrows!"
    };
    public TaskManagerFrame(UserProfile user)
        throws IOException {
        this.user=user;
        allTasks=DataStore.loadTasks();
        tasks=allTasks.computeIfAbsent(
            user.getUsername(),k->new ArrayList<>());
        model = new DefaultTableModel(
            new Object[]{
                "Done", "Task", "Category",
                "Priority", "Due Date", "Status", "ID"
            },0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column==0;
            }
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0?Boolean.class:Object.class;
            }
        };
        table = new JTable(model);
        buildUI();
        refreshTable();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
    }
    private void buildUI() {
        setTitle("Task Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(AppStyle.loadAppIcon());
        root=new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(18, 22, 18, 22));
        root.setBackground(AppStyle.PINK_LIGHT);
        setContentPane(root);
        createMenuBar();
        // Welcome section
        topPanel=new JPanel(new BorderLayout(12, 8));
        topPanel.setOpaque(false);
        JPanel welcome=new JPanel();
        welcome.setOpaque(false);
        welcome.setLayout(new BoxLayout(welcome,BoxLayout.Y_AXIS));
        greeting.setFont(new Font("SansSerif",Font.BOLD,27));
        greeting.setForeground(AppStyle.PINK_DARK);
        greeting.setText("Hello "+user.getDisplayName()+ "! You have to do:");
        JLabel subtitle = new JLabel("Small steps make lovely progress ♡");
        subtitle.setForeground(AppStyle.PINK_DARK);
        welcome.add(greeting);
        welcome.add(Box.createVerticalStrut(5));
        welcome.add(subtitle);
        topPanel.add(welcome, BorderLayout.WEST);
        JPanel trackerPanel = new JPanel();
        trackerPanel.setOpaque(false);
        trackerPanel.setLayout(new BoxLayout(trackerPanel,BoxLayout.Y_AXIS));
        tracker.setFont(new Font("SansSerif",Font.BOLD,15));
        tracker.setForeground(AppStyle.PINK_DARK);
        quoteLabel.setFont(new Font("SansSerif",Font.ITALIC,12));
        quoteLabel.setForeground(AppStyle.PINK_DARK);
        trackerPanel.add(tracker);
        trackerPanel.add(Box.createVerticalStrut(7));
        trackerPanel.add(quoteLabel);
        topPanel.add(trackerPanel, BorderLayout.EAST);
        root.add(topPanel, BorderLayout.NORTH);
        // Task area
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);
        JPanel tools = new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));
        tools.setOpaque(false);
        tools.add(new JLabel("View:"));
        tools.add(filter);
        JButton add = AppStyle.bubbleButton("+ Add Task");
        JButton delete=AppStyle.bubbleButton("Delete Selected");
        JButton toggle=AppStyle.bubbleButton("Mark Done / Pending");
        tools.add(add);
        tools.add(delete);
        tools.add(toggle);
        contentPanel.add(tools, BorderLayout.NORTH);
        table.setRowHeight(36);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(AppStyle.PINK);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        // Hide the internal ID column but keep it in the model.
        table.removeColumn(table.getColumnModel().getColumn(6));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(AppStyle.PINK, 2, true));
        contentPanel.add(scroll, BorderLayout.CENTER);
        root.add(contentPanel, BorderLayout.CENTER);
        JLabel footer = new JLabel("Made with care ♡     |     Task Manager");
        footer.setForeground(AppStyle.PINK_DARK);
        footer.setHorizontalAlignment(
            SwingConstants.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        filter.addActionListener(e -> refreshTable());
        add.addActionListener(e -> addTaskDialog());
        delete.addActionListener(e -> deleteSelectedTask());
        toggle.addActionListener(e -> toggleSelectedTask());
        model.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE
                    && e.getColumn() == 0) {
                int row = e.getFirstRow();
                if (row >= 0 && row < model.getRowCount()) {
                    String id =(String) model.getValueAt(row,6);
                    Task task = findTaskById(id);
                    if (task != null) {
                        boolean wasCompleted =
                            task.isCompleted();
                        task.setCompleted(
                            Boolean.TRUE.equals(
                                model.getValueAt(row, 0)));
                        saveData();
                        refreshTable();
                        if (!wasCompleted
                                && task.isCompleted()
                                && allTasksCompletedToday()) {
                            showCelebration();
                        }
                    }
                }
            }
        });
        applyTheme();
    }
    private void createMenuBar() {
        JMenuBar bar = new JMenuBar();
        // Profile menu
        JMenu profile = new JMenu("Profile");
        JMenuItem profileDetails =
            new JMenuItem("View Profile");
        JMenuItem deleteProfile =
            new JMenuItem("Delete Profile & Data");
        JMenuItem logOut = new JMenuItem("Log Out");
        profile.add(profileDetails);
        profile.addSeparator();
        profile.add(deleteProfile);
        profile.add(logOut);
        // Tasks menu
        JMenu taskMenu = new JMenu("Tasks");
        JMenuItem addTask = new JMenuItem("Add Task");
        JMenuItem deleteTask =
            new JMenuItem("Delete Selected Task");
        JMenuItem markDone =
            new JMenuItem("Mark Done / Pending");
        taskMenu.add(addTask);
        taskMenu.add(deleteTask);
        taskMenu.add(markDone);
        // View menu
        JMenu views = new JMenu("View");
        JMenuItem all = new JMenuItem("All Tasks");
        JMenuItem completed =
            new JMenuItem("Completed Tasks");
        JMenuItem pending =
            new JMenuItem("Pending Tasks");
        JMenuItem history = new JMenuItem("History");
        views.add(all);
        views.add(completed);
        views.add(pending);
        views.add(history);
        // Appearance menu
        JMenu appearance = new JMenu("Appearance");
        JMenuItem light = new JMenuItem("Light Mode");
        JMenuItem night = new JMenuItem("Night Mode");
        appearance.add(light);
        appearance.add(night);
        // Help menu
        JMenu help = new JMenu("Help");
        JMenuItem about =
            new JMenuItem("About Task Manager");
        help.add(about);
        bar.add(profile);
        bar.add(taskMenu);
        bar.add(views);
        bar.add(appearance);
        bar.add(help);
        setJMenuBar(bar);
        profileDetails.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this,
                "Username: " + user.getUsername()
                + "\nDisplay name: " + user.getDisplayName(),
                "Profile",
                JOptionPane.INFORMATION_MESSAGE));
        deleteProfile.addActionListener(e -> deleteProfile());
        logOut.addActionListener(e -> logOut());
        addTask.addActionListener(e -> addTaskDialog());
        deleteTask.addActionListener(e -> deleteSelectedTask());
        markDone.addActionListener(e -> toggleSelectedTask());
        all.addActionListener(e ->
            filter.setSelectedItem("All Tasks"));
        completed.addActionListener(e ->
            filter.setSelectedItem("Completed Tasks"));
        pending.addActionListener(e ->
            filter.setSelectedItem("Pending Tasks"));
        history.addActionListener(e ->
            filter.setSelectedItem("History"));
        light.addActionListener(e -> {
            nightMode = false;
            applyTheme();
        });
        night.addActionListener(e -> {
            nightMode = true;
            applyTheme();
        });
        about.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this,
                "Task Manager\n"
                + "Organize Study Task, Home Task and Others.\n"
                + "Your data is stored locally.",
                "About",
                JOptionPane.INFORMATION_MESSAGE));
    }
    private void addTaskDialog() {
        JTextField title = new JTextField(20);
        JComboBox<String> category = new JComboBox<>(
            new String[]{"Study Task", "Home Task", "Others"});
        JComboBox<String> priority = new JComboBox<>(
            new String[]{"Low", "Medium", "High"});
        JTextField due = new JTextField(
            LocalDate.now().format(DATE_FORMAT), 10);
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Task name:"));
        panel.add(title);
        panel.add(new JLabel("Category:"));
        panel.add(category);
        panel.add(new JLabel("Priority:"));
        panel.add(priority);
        panel.add(new JLabel("Due date (yyyy-MM-dd):"));
        panel.add(due);
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Add Task",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        if (title.getText().trim().isEmpty()) {
            showWarning("Please enter a task name.");
            return;
        }
        try {
            LocalDate dueDate =
                LocalDate.parse(due.getText().trim(), DATE_FORMAT);

            Task task = new Task(
                title.getText().trim(),
                (String) category.getSelectedItem(),
                (String) priority.getSelectedItem(),
                dueDate);
            tasks.add(task);
            saveData();
            filter.setSelectedItem("All Tasks");
            refreshTable();
        } catch (DateTimeParseException ex) {
            showWarning(
                "Date must use yyyy-MM-dd, for example 2026-10-25.");
        }
    }
    private void deleteSelectedTask() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showWarning("Select a task first.");
            return;
        }
        String id = (String) model.getValueAt(row, 6);
        Task task = findTaskById(id);
        if (task == null) {
            return;
        }
        int answer = JOptionPane.showConfirmDialog(
            this,
            "Delete \"" + task.getTitle() + "\"?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            tasks.remove(task);
            saveData();
            refreshTable();
        }
    }
    private void toggleSelectedTask() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showWarning("Select a task first.");
            return;
        }
        String id = (String) model.getValueAt(row, 6);
        Task task = findTaskById(id);
        if (task != null) {
            boolean wasCompleted = task.isCompleted();
            task.setCompleted(!task.isCompleted());
            saveData();
            refreshTable();
            if (!wasCompleted && task.isCompleted()
                    && allTasksCompletedToday()) {
                showCelebration();
            }
        }
    }
    private void refreshTable() {
        model.setRowCount(0);
        String selected = (String) filter.getSelectedItem();
        List<Task> visible = tasks.stream()
            .filter(t -> {
                if ("Study Task".equals(selected)
                        || "Home Task".equals(selected)
                        || "Others".equals(selected)) {
                    return t.getCategory().equals(selected);
                }
                if ("Completed Tasks".equals(selected)) {
                    return t.isCompleted();
                }
                if ("Pending Tasks".equals(selected)) {
                    return !t.isCompleted();
                }
                if ("History".equals(selected)) {
                    return t.isCompleted()
                        || !t.getCreatedDate().equals(LocalDate.now());
                }
                return true;
            })
            .sorted(Comparator.comparing(Task::getDueDate)
                .thenComparing(
                    Task::getTitle,
                    String.CASE_INSENSITIVE_ORDER))
            .collect(Collectors.toList());
        for (Task task : visible) {
            model.addRow(new Object[]{
                task.isCompleted(),
                task.getTitle(),
                task.getCategory(),
                task.getPriority(),
                task.getDueDate().format(DATE_FORMAT),
                task.isCompleted() ? "Completed" : "Pending",
                task.getId()
            });
        }
        updateTracker();
    }
    private void updateTracker() {
        long doneToday = tasks.stream()
            .filter(t -> t.isCompleted()
                && LocalDate.now().equals(t.getCompletedDate()))
            .count();
        long pending = tasks.stream()
            .filter(t -> !t.isCompleted())
            .count();
        tracker.setText(
            "Today's progress: " + doneToday
            + " completed  |  " + pending + " pending");
        if (allTasksCompletedToday()) {
            quoteLabel.setText("All done today! ♡");
        } else {
            quoteLabel.setText(
                "Keep going, one task at a time ♡");
        }
    }
    private boolean allTasksCompletedToday() {
        List<Task> today = tasks.stream()
            .filter(t -> t.getCreatedDate().equals(LocalDate.now()))
            .collect(Collectors.toList());
        return !today.isEmpty()
            && today.stream().allMatch(Task::isCompleted);
    }
    private void showCelebration() {
        String quote = QUOTES[
            new Random().nextInt(QUOTES.length)];
        JOptionPane.showMessageDialog(
            this, quote + "  ♥",
            "You did it!",
            JOptionPane.INFORMATION_MESSAGE);

        quoteLabel.setText(quote + "  ♥");
    }
    private Task findTaskById(String id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }
    private void saveData() {
        try {
            DataStore.saveTasks(allTasks);
        } catch (IOException ex) {
            showWarning(
                "Could not save tasks: " + ex.getMessage());
        }
    }
    private void deleteProfile() {
        int answer = JOptionPane.showConfirmDialog(
            this,
            "This permanently deletes your profile and all its tasks. Continue?",
            "Delete Profile",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            try {
                DataStore.deleteUserData(user.getUsername());
                new LoginFrame().setVisible(true);
                dispose();
            } catch (IOException ex) {
                showWarning(
                    "Could not delete profile data: "
                    + ex.getMessage());
            }
        }
    }
    private void logOut() {
        try {
            new LoginFrame().setVisible(true);
            dispose();
        } catch (IOException ex) {
            showWarning(
                "Could not open login: " + ex.getMessage());
        }
    }
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
            this, message, "Task Manager",
            JOptionPane.WARNING_MESSAGE);
    }
    private void applyTheme() {
        Color background = nightMode
            ? AppStyle.NIGHT : AppStyle.PINK_LIGHT;

        Color panelColor = nightMode
            ? AppStyle.NIGHT_PANEL : Color.WHITE;

        Color foreground = nightMode
            ? new Color(255, 231, 241) : AppStyle.PINK_DARK;

        root.setBackground(background);
        table.setBackground(panelColor);
        table.setForeground(nightMode
            ? Color.WHITE : new Color(65, 55, 60));
        table.setGridColor(nightMode
            ? new Color(90, 80, 100) : AppStyle.PINK);

        table.getTableHeader().setBackground(nightMode
            ? AppStyle.NIGHT_PANEL : AppStyle.PINK);
        table.getTableHeader().setForeground(nightMode
            ? Color.WHITE : AppStyle.PINK_DARK);

        greeting.setForeground(foreground);
        tracker.setForeground(foreground);
        quoteLabel.setForeground(foreground);

        root.repaint();
    }
}
