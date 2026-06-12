# FocusFlow

A Java-based to-do list application built for CIS35B Advanced Java class as a team project.

## Overview

FocusFlow helps users organize and track tasks with priorities, categories, deadlines, and subtasks. Tasks can be marked complete, sorted by deadline, and saved/loaded between sessions.

## Features

- Add, view, update, and delete tasks
- Mark tasks as completed
- Set deadlines (MM-DD-YY)
- Assign priority levels (High, Medium, Low)
- Organize tasks into categories
- Break tasks into subtasks with their own completion status
- Sort tasks by deadline
- Save and load tasks via file I/O
- GUI for adding, viewing, and managing tasks

## Project Structure

- **Task.java** – Core task object (name, due date, priority, category, completion status, subtasks)
- **Subtask.java** – Represents individual subtasks within a task
- **Priority.java / Level.java** – Priority levels (High, Medium, Low)
- **Category.java** – Task categories
- **TaskManager.java** – Handles adding, viewing, updating, deleting, and sorting tasks
- **TaskFileIO.java** – Handles saving/loading tasks to and from files
- **TaskManagerGUI.java** – Graphical interface for interacting with the app
- **Main** – Application entry point
