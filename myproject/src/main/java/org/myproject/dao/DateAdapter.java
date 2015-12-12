package org.myproject.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;


// http://stackoverflow.com/questions/18921196/jaxb-empty-element-unmarshalling

public class DateAdapter extends XmlAdapter<String, Date>{

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date unmarshal(String v) throws Exception {
        if(v.length() == 0) {
            return null;
        }
        return dateFormat.parse(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
        if(null == v) {
            return null;
        }
        return dateFormat.format(v);
    }

}