package org.example;

public class Main {
    public static void main(String[] args) throws Exception {
        ApiService apiService = new ApiService();
        System.out.println(apiService.getUserById(1));
        System.out.println(apiService.getUserByUsername("Bret"));
        System.out.println(apiService.createUser("{\"name\":\"Test User\",\"username\":\"testuser\",\"email\":\"testuser@example.com\"}"));
        System.out.println(apiService.updateUser(1, "{\"name\":\"Updated User\",\"username\":\"updateduser\",\"email\":\"updateduser@example.com\"}"));
        System.out.println(apiService.deleteUser(1));
        System.out.println(apiService.getLastPostCommentsByUserId(1));
        System.out.println(apiService.getOpenTodosByUserId(1));
    }
}