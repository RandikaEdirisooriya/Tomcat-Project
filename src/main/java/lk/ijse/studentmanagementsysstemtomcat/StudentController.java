package lk.ijse.studentmanagementsysstemtomcat;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.studentmanagementsysstemtomcat.Dto.StudentDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/Student")
public class StudentController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo :save student
        if(!req.getContentType().toLowerCase().startsWith("application/json")||req.getContentType()==null){
        resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);/*error ek send karanwa*/
        }
        //process
      /*  BufferedReader reader=req.getReader();
        StringBuilder stringBuilder=new StringBuilder();
        var writer=resp.getWriter();

        reader.lines().forEach(line-> stringBuilder.append(line+"\n"));
        System.out.println(stringBuilder);
        writer.write(stringBuilder.toString());*/

        /*json mannupulate with parsson*/
  /*    JsonReader jsonReader= Json.createReader(req.getReader());
        JsonObject jsonObject = jsonReader.readObject();
        System.out.println(jsonObject.getString("email"));
        PrintWriter writer=resp.getWriter();
        writer.println(jsonObject.getString("email"));
*/

      /*  *//*mannupulate with array*//*
        JsonReader jsonReader1 = Json.createReader(req.getReader());
        JsonArray jsonArray = jsonReader1.readArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.getJsonObject(i);
            System.out.println(jsonObject.getString("email"));
        }*/




      /*  String id= UUID.randomUUID().toString();
        Jsonb jsonb= JsonbBuilder.create();
        StudentDto studentDto=jsonb.fromJson(req.getReader(),StudentDto.class);
        studentDto.setId(id);
        System.out.println(studentDto);*/
        Jsonb jsonb= JsonbBuilder.create();
        List<StudentDto> studentList = jsonb.fromJson(req.getReader(), new ArrayList<StudentDto>(){}.getClass().getGenericSuperclass());

        // Assign UUIDs and print each student
     studentList.forEach(System.out::println);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo :get student
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo :update student
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo :delete student
    }
}
