package org.myproject.support.gmap;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Named(value = "markerLocationer")
public class MarkerLocationer implements Serializable {


    private static final long serialVersionUID = -2334598738949319253L;
    
    private MapModel draggableModel;
    private Marker marker;

    public MarkerLocationer() {
        draggableModel = new DefaultMapModel();

        //Shared coordinates  
        LatLng coord1 = new LatLng(41.017599, 28.985704);


        //Draggable  
        draggableModel.addOverlay(new Marker(coord1, "Arraste-o para a localização do projecto."));

        for (Marker marker2 : draggableModel.getMarkers()) {
            marker2.setDraggable(true);
        }
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public MapModel getDraggableModel() {
        return draggableModel;
    }
    
    public String lon, lat;

    public void onMarkerDrag(MarkerDragEvent event) {

        marker = event.getMarker();
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Localização do projeto.", "X:" + marker.getLatlng().getLat() + " Y:" + marker.getLatlng().getLng()));

        lon = String.valueOf(marker.getLatlng().getLng());
        lat = String.valueOf(marker.getLatlng().getLat());


    }


    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}