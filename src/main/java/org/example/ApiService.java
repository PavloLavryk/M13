package org.example;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
class Post {
    int id;
    int userId;
    String title;
    String body;
}
class Todo {
    int id;
    int userId;
    String title;
    boolean completed;
}

public class ApiService {
    private static final String API_URL = "https://jsonplaceholder.typicode.com/users";
    private final Gson gson = new Gson();

    private String sendRequest(String requestUrl, String method, String body) throws Exception {
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        if (body != null && !body.isEmpty()) {
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = body.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();
        return content.toString();
    }

    public String getUserById(int id) throws Exception {
        return sendRequest(API_URL + "/" + id, "GET", null);
    }

    public String getUserByUsername(String username) throws Exception {
        return sendRequest(API_URL + "?username=" + username, "GET", null);
    }

    public String createUser(String json) throws Exception {
        return sendRequest(API_URL, "POST", json);
    }

    public String updateUser(int id, String json) throws Exception {
        return sendRequest(API_URL + "/" + id, "PUT", json);
    }

    public String deleteUser(int id) throws Exception {
        return sendRequest(API_URL + "/" + id, "DELETE", null);
    }

    public String getLastPostCommentsByUserId(int userId) throws Exception {

        String postsJson = sendRequest(API_URL + "/" + userId + "/posts", "GET", null);

        Type listType = new TypeToken<List<Post>>() {}.getType();
        List<Post> posts = gson.fromJson(postsJson, listType);
        int lastPostId = posts.get(posts.size() - 1).id;


        String commentsJson = sendRequest("https://jsonplaceholder.typicode.com/posts/" + lastPostId + "/comments", "GET", null);


        writeToFile("user-" + userId + "-post-" + lastPostId + "-comments.json", commentsJson);

        return commentsJson;
    }

    private void writeToFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getOpenTodosByUserId(int userId) throws Exception {
        // Get the user's todos
        String todosJson = sendRequest(API_URL + "/" + userId + "/todos", "GET", null);
        // Parse the todos and filter for those where completed = false
        Type listType = new TypeToken<List<Todo>>() {}.getType();
        List<Todo> todos = gson.fromJson(todosJson, listType);
        List<Todo> openTodos = todos.stream().filter(todo -> !todo.completed).collect(Collectors.toList());
        return gson.toJson(openTodos);
    }
}
