/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskmanager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author User
 */
public class DataStore {
    private static final Path DATA_DIR=Paths.get("data");
    private static final Path USERS_FILE=DATA_DIR.resolve("users.dat");
    private static final Path TASKS_FILE =DATA_DIR.resolve("tasks.dat");
    private DataStore() {
    }
    private static void createFolder() throws IOException {
        Files.createDirectories(DATA_DIR);
    }
    @SuppressWarnings("unchecked")
    public static Map<String,UserProfile>loadUsers()
            throws IOException {
        createFolder();
        if (!Files.exists(USERS_FILE)) {
            return new LinkedHashMap<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(
                Files.newInputStream(USERS_FILE))){
            return (Map<String,UserProfile>) in.readObject();
        } catch (ClassNotFoundException|ClassCastException ex){
            throw new IOException("Could not read users.",ex);
        }
    }
    public static void saveUsers(
         Map<String,UserProfile> users) throws IOException{
        createFolder();
        try (ObjectOutputStream out=new ObjectOutputStream(
            Files.newOutputStream(USERS_FILE))){
            out.writeObject(users);
        }
    }
    @SuppressWarnings("unchecked")
    public static Map<String, List<Task>> loadTasks()
        throws IOException {
        createFolder();
        if(!Files.exists(TASKS_FILE)){
            return new LinkedHashMap<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(
            Files.newInputStream(TASKS_FILE))){
            return (Map<String, List<Task>>) in.readObject();
        } catch (ClassNotFoundException|ClassCastException ex) {
            throw new IOException("Could not read tasks.", ex);
        }
    }
    public static void saveTasks(
            Map<String, List<Task>> tasks) throws IOException {
        createFolder();
        try (ObjectOutputStream out = new ObjectOutputStream(
                Files.newOutputStream(TASKS_FILE))) {
            out.writeObject(tasks);
        }
    }
    public static void deleteUserData(String username)throws IOException{
        Map<String, UserProfile> users = loadUsers();
        users.remove(username);
        saveUsers(users);
        Map<String, List<Task>> tasks = loadTasks();
        tasks.remove(username);
        saveTasks(tasks);
    }
}
