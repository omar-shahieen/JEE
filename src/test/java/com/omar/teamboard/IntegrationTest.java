package com.omar.teamboard;

import com.omar.teamboard.dto.ProjectDto;
import com.omar.teamboard.dto.TaskDto;
import com.omar.teamboard.dto.TeamDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class IntegrationTest {

    @Test
    public void testMainFlow() {
        // 1. Create a Team
        TeamDto teamDto = new TeamDto();
        teamDto.setName("End to End Team");
        teamDto.setOwnerId("user-1");

        String teamId = given()
            .contentType(ContentType.JSON)
            .body(teamDto)
        .when()
            .post("/teams")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .extract().path("id");

        // 2. Create a Project
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("E2E Project");
        projectDto.setTeamId(teamId);

        String projectId = given()
            .contentType(ContentType.JSON)
            .body(projectDto)
        .when()
            .post("/projects")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .extract().path("id");

        // 3. Create a Task
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("E2E Task");
        taskDto.setDescription("Testing the whole flow");
        taskDto.setProjectId(projectId);

        String taskId = given()
            .header("X-User-Id", "user-1")
            .contentType(ContentType.JSON)
            .body(taskDto)
        .when()
            .post("/tasks")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .extract().path("id");

        // 4. Complete the Task
        String statusUpdateJson = "{\"status\":\"COMPLETED\"}";
        
        given()
            .header("X-User-Id", "user-1")
            .contentType(ContentType.JSON)
            .body(statusUpdateJson)
        .when()
            .patch("/tasks/" + taskId + "/status")
        .then()
            // This will fail with 400 Bad Request because we didn't go through IN_PROGRESS
            // Let's test the error behavior
            .statusCode(400)
            .body("error", equalTo("Cannot skip IN_PROGRESS status"));

        // 5. Start the Task
        String startStatusJson = "{\"status\":\"IN_PROGRESS\"}";
        given()
            .header("X-User-Id", "user-1")
            .contentType(ContentType.JSON)
            .body(startStatusJson)
        .when()
            .patch("/tasks/" + taskId + "/status")
        .then()
            .statusCode(200);

        // 6. Complete the Task properly
        given()
            .header("X-User-Id", "user-1")
            .contentType(ContentType.JSON)
            .body(statusUpdateJson)
        .when()
            .patch("/tasks/" + taskId + "/status")
        .then()
            .statusCode(200);
    }
}
