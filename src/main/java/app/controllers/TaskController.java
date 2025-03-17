package app.controllers;

import app.entities.Task;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.TaskMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class TaskController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("addtask", ctx -> addtask(ctx, connectionPool));
        app.post("done", ctx -> done(ctx, true, connectionPool));
        app.post("undo", ctx -> done(ctx, false, connectionPool));
        app.post("deletetask", ctx -> deleteTask(ctx, connectionPool));

    }

    private static void deleteTask(Context ctx, ConnectionPool connectionPool) {
        User user = ctx.sessionAttribute("currentUser");

        try {
            int taskId = Integer.parseInt(ctx.formParam("taskId"));
            TaskMapper.delete(taskId, connectionPool);
            List<Task> taskList = TaskMapper.getAllTasksPerUser(user.getUserId(), connectionPool);
            ctx.attribute("taskList", taskList);
            ctx.render("task.html");
        } catch (DatabaseException | NumberFormatException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");

        }
    }

    private static void done(Context ctx, boolean done, ConnectionPool connectionPool) {
        User user = ctx.sessionAttribute("currentUser");
        try {
            int taskId = Integer.parseInt(ctx.formParam("taskId"));
            TaskMapper.setDoneTo(done, taskId, connectionPool);
            List<Task> taskList = TaskMapper.getAllTasksPerUser(user.getUserId(), connectionPool);
            ctx.attribute("taskList", taskList);
            ctx.render("task.html");
        } catch (DatabaseException | NumberFormatException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");

        }

    }

    private static void addtask(Context ctx, ConnectionPool connectionPool) {
        String taskname = ctx.formParam("taskname");

        if (taskname.length() > 3) {
            User user = ctx.sessionAttribute("currentUser");
            try {
                Task newTask = TaskMapper.addTask(user, taskname, connectionPool);
                List<Task> taskList = TaskMapper.getAllTasksPerUser(user.getUserId(), connectionPool);
                ctx.attribute("taskList", taskList);
                ctx.render("task.html");
            } catch (DatabaseException e) {
                ctx.attribute("message", "Noget gik galt. Prv evt. igen");
                ctx.render("task.html");
            }
        }
    }
}
