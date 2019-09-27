/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arep.awslambdagateway.services;

import spark.Request;
import spark.Response;
import static spark.Spark.*;

import java.util.Arrays;
import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * clase encargada de realizar la conexion con la pagina web por medio del framwork spark
 *
 * @author Ospina
 * 
 * @version (a version 22/8/19)
 */

public class SparkWebApp {

    public static void main(String[] args) {
        port(getPort());
        get("/", (req, res) -> inputDataPage(req, res));
        get("/results", (req, res) -> resultsPage(req, res));
    }

    private static String inputDataPage(Request req, Response res) {
        String pageContent
                = "<!DOCTYPE html>"
                + "<html>"
                + "<body>"
                + "<h2>Cuadrado</h2>"
                + "<form action=\"/results\">"
                + "  Colocar el valor del numero que se desee realizar el cuadrado :<br>"
                + "  <input type=\"text\" name=\"Valores\">"
                + "  <br><br>"
                + "  <input type=\"submit\" value=\"Submit\">"
                + "</form>"
                + "<p>If you click the \"Submit\" button, the form-data will be sent to a page called \"/results\".</p>"
                + "</body>"
                + "</html>";
        return pageContent;
    }

    private static String resultsPage(Request req, Response res) {
      Integer num = lecturaDatos(req,res);
      Integer newNum = 0;
        try {
            URL urlLamb = new URL("https://v5trdbx2f0.execute-api.us-east-1.amazonaws.com/beta?value=" + Integer.toString(num));
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlLamb.openStream()));
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                //System.out.println(inputLine);
                newNum = Integer.parseInt(inputLine);
            }  
        } catch (MalformedURLException ex) {
            Logger.getLogger(SparkWebApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SparkWebApp.class.getName()).log(Level.SEVERE, null, ex);
        }
      

      String pageContent
               = "<!DOCTYPE html>"
               + "<html>"
               + "<body>"
               + "<h2>Answer</h2>"
               + "<form action=\"/results\">"
               + "  Datos: "+num
               + "  <br>"
               + "  La potencia es: "+ newNum
               + "</form>"
               + "</body>"
               + "</html>";
      return pageContent;
    }

    private static Integer lecturaDatos(Request req, Response res){

        String respuesta = req.queryParams("Valores");
        List<String> array = Arrays.asList(respuesta.split(","));
        int num = 0;
        
        for (String i : array){
           
           num = Integer.parseInt(i);	

        }
        return num;
    }
    /**
     * This method reads the default port as specified by the PORT variable in
     * the environment.
     *
     * Heroku provides the port automatically so you need this to run the
     * project on Heroku.
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

}
