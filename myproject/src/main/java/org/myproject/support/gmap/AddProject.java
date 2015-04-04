package org.myproject.support.gmap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class AddProject {


    String lat = new MarkerLocationer().lat;
    String lon = new MarkerLocationer().lon;


    private String projectName, projectExp, projectCoordLong, projectCoordLat;
    /**
     * Creates a new instance of AddProjectToDB
     */

    public String Save()  {

         System.out.println("Projecto" + projectName);
         System.out.println("Projecto" + projectExp);
         System.out.println("Latitude" + lat);
         System.out.println("Latitude" + lon);

        return "0";

    }
}