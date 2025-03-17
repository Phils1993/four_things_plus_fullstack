package app.controllers;

import app.entities.Task;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.TaskMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("login", ctx -> login(ctx, connectionPool));
        app.get("logout", ctx -> logout(ctx));
        app.get("createuser", ctx -> ctx.render("createuser.html"));
        app.post("createuser", ctx -> createUser(ctx,connectionPool));
    }
    private static void createUser(Context ctx, ConnectionPool connectionPool) {
        //hent form parametre
        String username = ctx.formParam("username");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        if(password1.equals(password2))
        {

            try
            {
                UserMapper.createuser(username,password1,connectionPool);
                ctx.attribute("message", "You are now created" + username
                + ". Now you have to login");
                ctx.render("index.html");
            }
            catch (DatabaseException e) {
                ctx.attribute("message", "Passwords already exists, try again or login");
                ctx.render("createuser.html");
            }
        }
        else {
            ctx.attribute("message", "Passwords do not match, try again");
            ctx.render("createuser.html");
        }
    }



    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }


    public static void login(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        //hent form parametre
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        try {
            User user = UserMapper.login(username, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            // hvis ja, send videre til task siden
            List<Task> taskList = TaskMapper.getAllTasksPerUser(user.getUserId(), connectionPool);
            ctx.attribute("taskList", taskList);
            ctx.render("task.html");
        } catch (DatabaseException e) {
            // hvis nej, send tilbage til login side med fejl besked
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }

        // tjek om bruger findes i databaser med de angivne username og password

    }




}
