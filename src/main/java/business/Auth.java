package business;

import db.UserDAO;
import models.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Stateless
public class Auth {
    @EJB
    private UserDAO userDAO;

    public String register(String login, String password) {
        User user = userDAO.findUser(login);
        if (user != null) {
            return "User already exists";
        }
        user = userDAO.createUser(login, password);
        if (user == null) {
            return "Error";
        }
        return user.getId().toString();
    }

    public String login(String login, String password) {
        User user = userDAO.findUser(login);
        if (user == null) {
            return "No such user found";
        }
        boolean result = userDAO.checkPassword(user, password);
        if (result) {
            user.generateAccessToken();
            userDAO.saveUser(user);
            return Base64.getEncoder().encodeToString((login + (char) (31) + user.getAccessToken()).getBytes(StandardCharsets.UTF_8));
        }
        return "Not authorized";
    }
    public User getUserByToken(String base64){
        String token = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
        String[] split = token.split(String.valueOf((char) (31)));
        String username = split[0];
        return userDAO.findUser(username);
    }
    public boolean checkAuth(String base64) {
        try {
            String token = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
            String[] split = token.split(String.valueOf((char) (31)));
            String username = split[0];
            String accessToken = split[1];
            User user = userDAO.findUser(username);
            return user.getAccessToken().equals(accessToken);
        }
        catch (Throwable e){
            return false;
        }
    }
    public boolean checkAuth(User user, String base64) {
        try {
            String token = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
            String[] split = token.split(String.valueOf((char) (31)));
            String accessToken = split[1];
            return user.getAccessToken().equals(accessToken);
        }
        catch (Throwable e){
            return false;
        }
    }
}
