package com.hitsz.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpServerLogin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        String username = req.getParameter("username");
        String userpwd = req.getParameter("password");

        String result = "";
        boolean isExist = false;

        if("admin".equals(username) && "123456".equals(userpwd)){
            System.out.println("Login Suceess");
            isExist = true;
        }
        if (isExist == true){
            result = "success";
        }else {
            result ="failed";
        }
        out.write(result);
        out.flush();
        out.close();
        System.out.println(result);
    }
}
