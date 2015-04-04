package org.myproject.support.gmap;


import javax.annotation.PostConstruct; 
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
  
import javax.inject.Named;

import org.myproject.model.utils.BaseBean;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;
 
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Named(value = "addMarkersView")
public class AddMarkersView extends BaseBean {
     
    private static final long serialVersionUID = 8822766802243081444L;

    private MapModel emptyModel;
      
    private String title;
      
    private double lat;
      
    private double lng;
  
    @PostConstruct
    public void init() {
        emptyModel = new DefaultMapModel();
    }
      
    public MapModel getEmptyModel() {
        return emptyModel;
    }
      
    public String getTitle() {
        return title;
    }
  
    public void setTitle(String title) {
        this.title = title;
    }
  
    public double getLat() {
        return lat;
    }
  
    public void setLat(double lat) {
        this.lat = lat;
    }
  
    public double getLng() {
        return lng;
    }
  
    public void setLng(double lng) {
        this.lng = lng;
    }
      
    public void addMarker() {
        Marker marker = new Marker(new LatLng(lat, lng), title);
        emptyModel.addOverlay(marker);
          
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Lat:" + lat + ", Lng:" + lng));
    }
}
