package org.myproject.support.gmap;



import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.myproject.model.utils.BaseBean;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;


@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Named(value = "mapBean")
public class MapBean extends BaseBean {  
    
    private static final long serialVersionUID = 9023442633073123845L;

    private MapModel simpleModel;  

    
    private Marker marker;  
  
    public MapBean() {  
        simpleModel = new DefaultMapModel();  
          
        //Shared coordinates  
        LatLng coord1 = new LatLng(36.879466, 30.667648);  
        LatLng coord2 = new LatLng(36.883707, 30.689216);  
        LatLng coord3 = new LatLng(36.879703, 30.706707);  
        LatLng coord4 = new LatLng(36.885233, 30.702323);  
          
        //Basic marker  
//        simpleModel.addOverlay(new Marker(coord1, "Konyaalti"));  
//        simpleModel.addOverlay(new Marker(coord2, "Ataturk Parki"));  
//        simpleModel.addOverlay(new Marker(coord3, "Karaalioglu Parki"));  
//        simpleModel.addOverlay(new Marker(coord4, "Kaleici"));  
   }  
      
    public MapModel getSimpleModel() {  
        return simpleModel;  
    }  
      
    public void onMarkerSelect(OverlaySelectEvent event) {  
        marker = (Marker) event.getOverlay();  
          
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Selected", marker.getTitle()));  
    }  
      
    public Marker getMarker() {  
        return marker;  
    }  

    public void addMessage(FacesMessage message) {  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
}  
                  