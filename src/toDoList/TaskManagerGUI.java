package toDoList; // david: I think i add package?
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.border.*; // cosmetics

import java.time.*;
// calendar and time import
import com.github.lgooddatepicker.components.DateTimePicker;
public class TaskManagerGUI extends JFrame {

    TaskManager manager = new TaskManager();

    // input fields
    JTextField nField;
    DateTimePicker dField;
    JComboBox<String> pBox;
    JComboBox<String> cBox;

    // tsk display
    DefaultListModel<String> listModel;
    JList<String> taskList;
    DefaultListModel<String> weekModel;

    CardLayout cardLayout;
    JPanel viewPanel;

    YearMonth currentMonth = YearMonth.now();

    JPanel calendarGrid;
    JLabel monthTitle;

    LocalDate currentWeekStart;
    JLabel weekTitle;
    JPanel weekTasksPanel;

    JPanel dueTasksPanel;
    String dueFilter = "All";




    public TaskManagerGUI() {
        setTitle("Task Manager");

        setSize(1400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color textColor = new Color(242,233,228);
        Color fieldColor = new Color(74,78,105);
        Color borderColor = new Color(154,140,152);

        currentWeekStart = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()%7);

        // input pannel at da top
        //JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // left sidebar
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setPreferredSize(new Dimension(320, 0));
        inputPanel.setBackground(new Color(34,34,59));

        //huge line broken up
        inputPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1,
                        new Color(74,78,105)), BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        nField = new JTextField();

        nField.setMaximumSize(new Dimension(240,40));
        dField = new DateTimePicker();

        dField.setMaximumSize(new Dimension(240,40));
        dField.datePicker.setBackground(fieldColor);
        dField.datePicker.getComponentDateTextField().setBackground(fieldColor);
        dField.datePicker.getComponentDateTextField().setForeground(textColor);
        dField.timePicker.getComponentTimeTextField().setBackground(fieldColor);
        dField.timePicker.getComponentTimeTextField().setForeground(textColor);

        dField.setMaximumSize(new Dimension(240,40));
        pBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        pBox.setMaximumSize(new Dimension(240,40));
        cBox = new JComboBox<>(new String[]{"School", "Work", "Personal", "Other"});
        cBox.setMaximumSize(new Dimension(240,40));

        JButton addBtn = new JButton("✦   Add Task   ✦");
        addBtn.setMaximumSize(new Dimension(240, 40));
        nField.setBackground(fieldColor);
        nField.setForeground(textColor);
        nField.setCaretColor(textColor);

        pBox.setBackground(fieldColor);
        pBox.setForeground(textColor);

        cBox.setBackground(fieldColor);
        cBox.setForeground(textColor);

        addBtn.setBackground(new Color(154,140,152));

        addBtn.setForeground(textColor);

        nField.setBorder(BorderFactory.createLineBorder(borderColor));
        pBox.setBorder(BorderFactory.createLineBorder(borderColor));
        cBox.setBorder(BorderFactory.createLineBorder(borderColor));

        dField.datePicker.getComponentDateTextField().setBorder(BorderFactory.createLineBorder(borderColor));
        dField.timePicker.getComponentTimeTextField().setBorder(BorderFactory.createLineBorder(borderColor));
        //end of new code

        inputPanel.add(Box.createVerticalStrut(20));
// new code
        JLabel nameLabel = new JLabel("Name of task:");
        JLabel dueLabel = new JLabel("Due when:");
        JLabel priLabel = new JLabel("Priority:");
        JLabel catLabel = new JLabel("Category:");
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        priLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        catLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Color textColor = new Color(242,233,228);
        //Color fieldColor = new Color(74,78,105);
        //Color borderColor = new Color(154,140,152);

        nameLabel.setForeground(textColor);
        dueLabel.setForeground(textColor);
        priLabel.setForeground(textColor);
        catLabel.setForeground(textColor);

        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        nField.setAlignmentX(Component.LEFT_ALIGNMENT);
        dueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dField.setAlignmentX(Component.LEFT_ALIGNMENT);
        priLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        catLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputPanel.add(nameLabel);
        inputPanel.add(Box.createVerticalStrut(6));
        inputPanel.add(nField);

        //inputPanel.add(Box.createVerticalStrut(18));
        inputPanel.add(Box.createVerticalStrut(18));
        inputPanel.add(dueLabel);
        inputPanel.add(Box.createVerticalStrut(6));
        inputPanel.add(dField);
        inputPanel.add(Box.createVerticalStrut(18));
        inputPanel.add(priLabel);
        inputPanel.add(Box.createVerticalStrut(6));
        inputPanel.add(pBox);
        inputPanel.add(Box.createVerticalStrut(18));
        inputPanel.add(catLabel);
        inputPanel.add(Box.createVerticalStrut(6));
        inputPanel.add(cBox);
        inputPanel.add(Box.createVerticalStrut(20));

        inputPanel.add(addBtn);
// end of new code


        // task llist
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);

        // many many buttons T-T
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton deleteBtn = new JButton("Delete");
        JButton completeBtn = new JButton("Mark Complete");
        JButton sortBtn = new JButton("Sort by Deadline");
        JButton saveBtn = new JButton("Save");
        JButton loadBtn = new JButton("Load");

        buttonPanel.add(deleteBtn);
        buttonPanel.add(completeBtn);
        buttonPanel.add(sortBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(loadBtn);

        // do sctions
        addBtn.addActionListener(e -> {

            String name = nField.getText().trim();

            // calendar and time selec
            String date = "";
            if (dField.getDateTimeStrict() != null) {
                date = dField.getDateTimeStrict().toString();
            }

            String priority = (String) pBox.getSelectedItem();
            String category = (String) cBox.getSelectedItem();

            if (name.isEmpty()) return;


            Task task = new Task(name, date, priority, category);

            manager.addTask(task);
            refreshCalendar();
            refreshWeekView();
            refreshDueDateView();

            listModel.addElement(task.toString());

            nField.setText("");
            dField.clear();
        });

        deleteBtn.addActionListener(e -> {

            int selected = taskList.getSelectedIndex();

            if (selected == -1) return;


            manager.deleteTask(selected);

            listModel.remove(selected);

        });

        completeBtn.addActionListener(e -> {

            int selected = taskList.getSelectedIndex();

            if (selected == -1) return;


            manager.markComplete(selected);

            refreshList();
            refreshWeekView();
            refreshCalendar();
            refreshDueDateView();

        });

        sortBtn.addActionListener(e -> {

                        manager.sortByDeadline();

            refreshList();
            refreshWeekView();
            refreshDueDateView();
            refreshCalendar();

        });

        saveBtn.addActionListener(e -> {

        

            JOptionPane.showMessageDialog(this, "fake save for now");



        });

        loadBtn.addActionListener(e -> {

            JOptionPane.showMessageDialog(this, "fake load for now");

        });


        /*add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);*/

        // new code

        // top controls

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 20));
        JLabel appTitle = new JLabel("Focus Flow");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        appTitle.setForeground(new Color(242,233,228));

        topPanel.add(appTitle);
        topPanel.add(Box.createHorizontalStrut(40));

        

        JButton monthBtn = new JButton("Month");

        JButton weekBtn = new JButton("Week");

        JButton dueBtn = new JButton("Due Date");

        topPanel.setBackground(new Color(34,34,59));
        topPanel.add(monthBtn);
        topPanel.add(weekBtn);
        topPanel.add(dueBtn);

        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(74,78,105)));

        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        monthBtn.setFont(buttonFont);
        monthBtn.setBackground(new Color(74,78,105));
        monthBtn.setForeground(new Color(242,233,228));
        monthBtn.setFocusPainted(false);
        monthBtn.setBorderPainted(false);

        weekBtn.setFont(buttonFont);
        weekBtn.setBackground(new Color(74,78,105));
        weekBtn.setForeground(new Color(242,233,228));
        weekBtn.setFocusPainted(false);
        weekBtn.setBorderPainted(false);

        dueBtn.setFont(buttonFont);
        dueBtn.setBackground(new Color(74,78,105));
        dueBtn.setForeground(new Color(242,233,228));
        dueBtn.setFocusPainted(false);
        dueBtn.setBorderPainted(false);

        monthBtn.setBorder(BorderFactory.createEmptyBorder(12,22,12,22));
        weekBtn.setBorder(BorderFactory.createEmptyBorder(12,22,12,22));
        dueBtn.setBorder(BorderFactory.createEmptyBorder(12,22,12,22));


// center views

        cardLayout = new CardLayout();

        viewPanel = new JPanel(cardLayout);

        // month panel

        JPanel monthPanel = new JPanel(new BorderLayout());
        monthPanel.setBackground(new Color(34,34,59));

        calendarGrid = new JPanel(new GridLayout(0,7,8,8));
        calendarGrid.setBackground(new Color(34,34,59));

        monthPanel.setBackground(new Color(34,34,59));
        monthTitle = new JLabel("", SwingConstants.CENTER);

        JButton prevMonthBtn = new JButton("◀"); // forward and back month buttons
        JButton nextMonthBtn = new JButton("▶");

        prevMonthBtn.setBackground(new Color(74,78,105));
        nextMonthBtn.setBackground(new Color(74,78,105));
        prevMonthBtn.setForeground(textColor);
        nextMonthBtn.setForeground(textColor);
        prevMonthBtn.setFocusPainted(false);
        nextMonthBtn.setFocusPainted(false);
        prevMonthBtn.setBorderPainted(false);
        nextMonthBtn.setBorderPainted(false);

        monthTitle.setForeground(textColor);
        monthTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        calendarGrid.add(monthTitle, BorderLayout.NORTH);

        String[] days = {"Sun","Mon","Tue","Wed", "Thu","Fri","Sat"};
        for(String day : days) {
            JLabel header = new JLabel(day);
            header.setForeground(new Color(242,233,228));
            header.setHorizontalAlignment(SwingConstants.CENTER);
            header.setFont(new Font("SansSerif", Font.BOLD, 16));
            calendarGrid.add(header);
        }
        for(int i = 1; i <= 35; i++) {
            JPanel dayCell = new JPanel();
            dayCell.setLayout(new BoxLayout(dayCell, BoxLayout.Y_AXIS));
            dayCell.setBackground(new Color(74,78,105));
            dayCell.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
            JLabel dayNumber = new JLabel(String.valueOf(i));
            dayNumber.setForeground(new Color(242,233,228));
            dayCell.add(dayNumber);
            dayCell.add(Box.createVerticalStrut(8));

            if(i ==4) {
                JButton task1 = new JButton("Study Calc");
                task1.setBackground(new Color(146,104,104));
                task1.setForeground(Color.WHITE);
                dayCell.add(task1);
            }
            if(i == 12) {
                JButton task2 = new JButton("<html><strike>Lab Report</strike></html>");
                task2.setBackground(new Color(154,140,152));
                task2.setForeground(Color.WHITE);
                dayCell.add(task2);
            }
            if(i == 22) {
                JButton task3 = new JButton("Groceries");
                task3.setBackground(new Color(110,120,180));
                task3.setForeground(Color.WHITE);
                dayCell.add(task3);
            }
            calendarGrid.add(dayCell);
        }

        JPanel monthHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        monthHeader.setBackground(new Color(34,34,59));
        monthHeader.add(prevMonthBtn);
        monthHeader.add(monthTitle);
        monthHeader.add(nextMonthBtn);

        monthPanel.add(monthHeader, BorderLayout.NORTH);

        monthPanel.add(calendarGrid, BorderLayout.CENTER);
        refreshCalendar();

        prevMonthBtn.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            refreshCalendar();
        });

        nextMonthBtn.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            refreshCalendar();
        });


// fake calendar placeholder



// WEEK PANEL 05/25

        JPanel weekPanel = new JPanel(new BorderLayout());
        weekPanel.setBackground(new Color(34,34,59));
        //JLabel weekTitle = new JLabel("WEEK VIEW", SwingConstants.CENTER);
        //weekTitle.setForeground(textColor);
        //weekTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        //weekPanel.add(weekTitle, BorderLayout.NORTH);

// real task checklist 05/22

        JPanel weekHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        weekHeader.setBackground(new Color(34,34,59));

        JButton prevWeekBtn = new JButton("◀");
        JButton nextWeekBtn = new JButton("▶");

        weekTitle = new JLabel();
        weekTitle.setForeground(textColor);
        weekTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        prevWeekBtn.setBackground(new Color(74,78,105));
        nextWeekBtn.setBackground(new Color(74,78,105));
        prevWeekBtn.setForeground(textColor);
        nextWeekBtn.setForeground(textColor);
        prevWeekBtn.setFocusPainted(false);
        nextWeekBtn.setFocusPainted(false);
        prevWeekBtn.setBorderPainted(false);
        nextWeekBtn.setBorderPainted(false);

        weekHeader.add(prevWeekBtn);
        weekHeader.add(weekTitle);
        weekHeader.add(nextWeekBtn);

        weekTasksPanel = new JPanel();
        weekTasksPanel.setBackground(new Color(34,34,59));
        weekTasksPanel.setLayout(new BoxLayout(weekTasksPanel, BoxLayout.Y_AXIS));

        weekPanel.add(weekHeader, BorderLayout.NORTH);
        weekPanel.add(new JScrollPane(weekTasksPanel), BorderLayout.CENTER);

        prevWeekBtn.addActionListener(e -> {
            currentWeekStart = currentWeekStart.minusWeeks(1);
            refreshWeekView();

        });

        nextWeekBtn.addActionListener(e -> {
            currentWeekStart = currentWeekStart.plusWeeks(1);
            refreshWeekView();
        });

        refreshWeekView();



// Due ddate paneL 05/22

        JPanel duePanel = new JPanel(new BorderLayout());
        duePanel.setBackground(new Color(34,34,59));
        JLabel dueTitle = new JLabel("DUE DATE VIEW", SwingConstants.CENTER);
        dueTitle.setForeground(textColor);
        dueTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        duePanel.add(dueTitle, BorderLayout.NORTH);

// fake deadline list
        // 06/ 02 real due date implement
        JPanel dueTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        dueTopPanel.setBackground(new Color(34,34,59));
        JButton allDueBtn = new JButton("All");
        JButton activeDueBtn = new JButton("Active");
        JButton completedDueBtn = new JButton("Completed");

        // button top due date format pretty
        JButton[] dueButtons = {
                allDueBtn,
                activeDueBtn,
                completedDueBtn};
        for(JButton btn : dueButtons) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(new Color(74,78,105));
            btn.setForeground(new Color(242,233,228));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        }



        dueTopPanel.add(allDueBtn);
        dueTopPanel.add(activeDueBtn);
        dueTopPanel.add(completedDueBtn);
        duePanel.add(dueTopPanel, BorderLayout.NORTH);
        dueTasksPanel = new JPanel();
        dueTasksPanel.setBackground(new Color(34,34,59));
        dueTasksPanel.setLayout(new BoxLayout(dueTasksPanel, BoxLayout.Y_AXIS));
        duePanel.add(new JScrollPane(dueTasksPanel), BorderLayout.CENTER);
        allDueBtn.addActionListener(e -> {
            dueFilter = "All";
            refreshDueDateView();
        });
        activeDueBtn.addActionListener(e -> {
            dueFilter = "Active";
            refreshDueDateView();
        });
        completedDueBtn.addActionListener(e -> {
            dueFilter = "Completed";
            refreshDueDateView();
        });
        refreshDueDateView();

        // end of 06/02

        viewPanel.add(monthPanel, "MONTH");
        viewPanel.add(weekPanel, "WEEK");
        viewPanel.add(duePanel, "DUE");


// button switching

        monthBtn.addActionListener(e -> cardLayout.show(viewPanel, "MONTH"));
        weekBtn.addActionListener(e -> cardLayout.show(viewPanel, "WEEK"));
        dueBtn.addActionListener(e -> cardLayout.show(viewPanel, "DUE"));

// final layout

        add(inputPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);
        add(viewPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(242,233,228));

        saveBtn.setBackground(new Color(74,78,105));
        saveBtn.setForeground(textColor);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        loadBtn.setBackground(new Color(74,78,105));
        loadBtn.setForeground(textColor);
        loadBtn.setFocusPainted(false);
        loadBtn.setBorderPainted(false);

        bottomPanel.add(saveBtn);
        bottomPanel.add(loadBtn);

        add(bottomPanel, BorderLayout.SOUTH);
// end of new code



        setVisible(true);
    }

    // time calendar code 05/22
    private void refreshCalendar() {
        calendarGrid.removeAll();
        String[] days = {"Sun","Mons","Tue", "Wed","Thu","Fri","Sat"};

        for(String day : days) {
            JLabel header = new JLabel(day, SwingConstants.CENTER);
            header.setForeground(new Color(242,233,228));
            header.setFont(new Font("SansSerif", Font.BOLD, 16));

            calendarGrid.add(header);
        }
        monthTitle.setText(currentMonth.getMonth() + " " + currentMonth.getYear());
        LocalDate firstDay = currentMonth.atDay(1);
        int offset = firstDay.getDayOfWeek().getValue()%7;
        int daysInMonth = currentMonth.lengthOfMonth();

        for(int i=0;i<offset;i++) {
            calendarGrid.add(new JLabel(""));
        }

        for(int day=1; day<=daysInMonth; day++) {
            JPanel dayCell = new JPanel();
            dayCell.setLayout(new BoxLayout(dayCell, BoxLayout.Y_AXIS));
            dayCell.setBackground(new Color(74,78,105));
            dayCell.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
            JLabel dayNumber = new JLabel(String.valueOf(day));
            dayNumber.setForeground(new Color(242,233,228));
            dayCell.add(dayNumber);

            LocalDate currentDate = currentMonth.atDay(day);
            int shownTasks = 0;
            int hiddenTasks = 0;

            for(Task t : manager.getTasks()) {
                if(t.dueDate != null && t.dueDate.equals(currentDate)) {
                    if(shownTasks >= 1) {
                        hiddenTasks++;
                        continue;
                    }

                    JButton taskButton = new JButton(t.name);

                    if(t.priority.equals("High")) {
                        taskButton.setBackground(new Color(146,104,104));
                    }
                    else if(t.priority.equals("Medium")) {
                       taskButton.setBackground(new Color(154,140,152));
                    }
                    else {
                        taskButton.setBackground(new Color(110,120,180));
                    }
                    taskButton.setForeground(Color.WHITE);
                    taskButton.setFocusPainted(false);
                    //taskButton.setBorderPainted(false);

                    //rlly long line broken up into 2
                    taskButton.setBorder(BorderFactory.createCompoundBorder(new LineBorder(taskButton.getBackground(),
                            1, true), BorderFactory.createEmptyBorder(4,8,4,8)));

                    taskButton.setMargin(new Insets(6, 10, 6, 10));
                    taskButton.setAlignmentX(Component.LEFT_ALIGNMENT);

                    if(t.completed) {
                        taskButton.setText("<html><strike>" + t.name + "</strike></html>");
                    }
                    //dayCell.add(taskButton);
                    dayCell.add(Box.createVerticalStrut(8));
                    dayCell.add(taskButton);
                    dayCell.add(Box.createVerticalStrut(3));

                    /*JPanel bubbleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
                    bubbleWrapper.setOpaque(false);
                    bubbleWrapper.add(taskButton);
                    dayCell.add(bubbleWrapper);*/

                    shownTasks++;
                }
            }

            if(hiddenTasks > 0) {
                JLabel moreLabel = new JLabel("+" + hiddenTasks + " more...");
                moreLabel.setForeground(new Color(200,190,210));
                moreLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
                dayCell.add(moreLabel);
            }
            calendarGrid.add(dayCell);


        }

        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

    // week view tab 05/24
    private void refreshWeekView() {
        weekTasksPanel.removeAll();
        weekTitle.setText(currentWeekStart.getMonth() + " " +
                        currentWeekStart.getDayOfMonth() + " - " +
                        currentWeekStart.plusDays(6).getMonth() + " " +
                        currentWeekStart.plusDays(6).getDayOfMonth());

        for(int i = 0; i < 7; i++) {
            LocalDate currentDay = currentWeekStart.plusDays(i);
            JPanel row = new JPanel();row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
            row.setBackground(new Color(74,78,105));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            row.setPreferredSize(new Dimension(1000, 80));

            JLabel dayLabel = new JLabel(currentDay.getDayOfWeek() + " " + currentDay.getDayOfMonth());
            dayLabel.setForeground(new Color(242,233,228));
            dayLabel.setPreferredSize(new Dimension(130, 25));

            JPanel dayHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
            dayHeader.setOpaque(false);
            dayHeader.add(dayLabel);
            row.add(dayHeader);

            boolean found = false;
            ArrayList<Task> sortedTasks = new ArrayList<>(manager.getTasks());

            sortedTasks.sort((a,b) -> {
                if(a.dueTime == null && b.dueTime == null) {return 0;}
                if(a.dueTime == null) {return 1;}
                if(b.dueTime == null) {return -1;}
                return a.dueTime.compareTo(b.dueTime);
            });

            for(Task t : sortedTasks) {
                if(t.dueDate != null && t.dueDate.equals(currentDay)) {
                    found = true;

                    JCheckBox check = new JCheckBox(t.name);
                    check.setSelected(t.completed);
                    check.setForeground(new Color(242,233,228));
                    check.setBackground(new Color(74,78,105));

                    check.addActionListener(e -> {
                        t.completed = check.isSelected();
                        refreshCalendar();
                    });

                    JPanel taskRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
                    taskRow.setOpaque(false);

                    JLabel circle = new JLabel(t.completed ? "✓" : "");
                    circle.setPreferredSize(new Dimension(22,22));
                    circle.setHorizontalAlignment(SwingConstants.CENTER);
                    circle.setOpaque(true);
                    if(t.completed) {
                        circle.setBackground(new Color(17, 14, 62));
                        circle.setForeground(new Color(53, 51, 126));
                    }
                    else {
                        circle.setBackground(new Color(74,78,105));
                        circle.setForeground(new Color(154,140,152));
                    }
                    circle.setBorder(BorderFactory.createLineBorder(new Color(154,140,152), 1, true));
                    circle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    // 06/02
                    circle.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            t.completed = !t.completed;
                            refreshWeekView();
                            refreshCalendar();
                            refreshList();
                        }
                    });
                    // end of 06/02

                    circle.setForeground(new Color(242,233,228));


                    taskRow.add(circle);


                    // 06/02
                    JLabel taskName;

                    if(t.completed) {
                        taskName = new JLabel("<html><strike>" + t.name + "</strike></html>");
                    }
                    else {
                        taskName = new JLabel(t.name);
                    }
                    // end of 06/02

                    taskName.setForeground(new Color(242,233,228));
                    taskName.setPreferredSize(new Dimension(220,25));
                    taskRow.add(taskName);

                    JLabel priorityBadge = new JLabel(" " + t.priority + " ");
                    if(t.priority.equals("High")) {
                        priorityBadge.setBackground(new Color(146,104,104));
                    }
                    else if(t.priority.equals("Medium")) {
                        priorityBadge.setBackground(new Color(154,140,152));
                    }
                    else {
                        priorityBadge.setBackground(new Color(110,120,180));
                    }

                    priorityBadge.setOpaque(true);
                    priorityBadge.setForeground(Color.WHITE);
                    priorityBadge.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                    taskRow.add(priorityBadge);

                    JLabel timeLabel = new JLabel("- " + (t.dueTime != null ? t.dueTime : ""));
                    timeLabel.setForeground(new Color(200,190,210));
                    taskRow.add(timeLabel);

                    row.add(taskRow);

                }
            }
            if(!found) {
                JLabel empty = new JLabel("—");
                empty.setForeground(new Color(200,190,210));
                row.add(empty);
            }
            weekTasksPanel.add(row);
            weekTasksPanel.add(Box.createVerticalStrut(8));
        }
        weekTasksPanel.revalidate();
        weekTasksPanel.repaint();
    }
    // end of 05/27


    // 06/02
    private void refreshDueDateView() {
        dueTasksPanel.removeAll();

        ArrayList<Task> sortedTasks = new ArrayList<>(manager.getTasks());

        sortedTasks.sort((a,b) -> {
            if(a.dueDate == null && b.dueDate == null) return 0;
            if(a.dueDate == null) return 1;
            if(b.dueDate == null) return -1;

            int dateCompare = a.dueDate.compareTo(b.dueDate);
            if(dateCompare != 0) return dateCompare;

            if(a.dueTime == null && b.dueTime == null) return 0;
            if(a.dueTime == null) return 1;
            if(b.dueTime == null) return -1;

            return a.dueTime.compareTo(b.dueTime);
        });

        for(Task t : sortedTasks) {
            if(dueFilter.equals("Active") && t.completed) continue;
            if(dueFilter.equals("Completed") && !t.completed) continue;

            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
            row.setBackground(new Color(74,78,105));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            // o6/02
            JLabel status = new JLabel(t.completed ? "✓" : "");

            status.setPreferredSize(new Dimension(22,22));
            status.setHorizontalAlignment(SwingConstants.CENTER);
            status.setOpaque(true);

            if(t.completed) {
                status.setBackground(new Color(17, 14, 62));
            }
            else {
                status.setBackground(new Color(74,78,105));
            }

            status.setForeground(new Color(242,233,228));

            status.setBorder(
                    BorderFactory.createLineBorder(
                            new Color(154,140,152),
                            1,
                            true));

            status.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            status.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    t.completed = !t.completed;
                    refreshDueDateView();
                    refreshWeekView();
                    refreshCalendar();
                    refreshList();
                }
            });

            row.add(status);
            // end of 06/02

            JLabel dateLabel = new JLabel(t.dueDate != null ? t.dueDate.toString() : "No date");
            dateLabel.setForeground(new Color(242,233,228));
            dateLabel.setPreferredSize(new Dimension(110, 25));
            row.add(dateLabel);

            JLabel nameLabel = new JLabel(t.completed ? "<html><strike>" + t.name + "</strike></html>" : t.name);
            nameLabel.setForeground(new Color(242,233,228));
            nameLabel.setPreferredSize(new Dimension(250, 25));
            row.add(nameLabel);

            JLabel priorityBadge = new JLabel(" " + t.priority + " ");

            if(t.priority.equals("High")) {
                priorityBadge.setBackground(new Color(146,104,104));
            }
            else if(t.priority.equals("Medium")) {
                priorityBadge.setBackground(new Color(154,140,152));
            }
            else {
                priorityBadge.setBackground(new Color(110,120,180));
            }

            priorityBadge.setOpaque(true);
            priorityBadge.setForeground(Color.WHITE);
            priorityBadge.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            row.add(priorityBadge);

            JLabel timeLabel = new JLabel("- " + (t.dueTime != null ? t.dueTime : ""));
            timeLabel.setForeground(new Color(200,190,210));
            row.add(timeLabel);

            dueTasksPanel.add(row);
            dueTasksPanel.add(Box.createVerticalStrut(8));
        }

        dueTasksPanel.revalidate();
        dueTasksPanel.repaint();
    }

    // end of 06/02


    // reload display
    private void refreshList() {

        listModel.clear();

        for (Task t : manager.getTasks()) {
            listModel.addElement(t.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TaskManagerGUI::new);
    }
}


// temporary task class for testing
/*
class Task {

    String name;
    String date;
    String priority;
    String category;
    boolean completed;
    LocalDate dueDate;
    LocalTime dueTime;

    public Task(String name, String date, String priority, String category) {
        this.name = name;
        this.date = date;
        this.priority = priority;
        this.category = category;
        completed = false;
        this.date = date;

        try {
            if(!date.isEmpty()) {
                dueDate = LocalDate.parse(date.substring(0,10));
                dueTime = LocalTime.parse(date.substring(11,16));
            }
        }
        catch(Exception e) {
            dueDate = null;
            dueTime = null;
        }
    }

    public String toString() {

        String status = completed ? " ✓" : "";

        return "[" + priority + "] " + name + " | " + date + " | " + category + status;
    }
}


// temporary task manager class for testing
class TaskManager {

    private ArrayList<Task> tasks = new ArrayList<>();

    public void addTask(Task t) {
        tasks.add(t);
    }

    public void deleteTask(int index) {
        tasks.remove(index);
    }

    public void markComplete(int index) {
        tasks.get(index).completed = true;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void sortByDeadline() {

        tasks.sort((a,b) ->
                a.date.compareTo(b.date));

    }
}
*/

