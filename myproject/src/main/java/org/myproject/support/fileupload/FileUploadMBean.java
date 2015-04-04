package org.myproject.support.fileupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.myproject.model.utils.BaseBean;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;


@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "fileUploadMBean")
public class FileUploadMBean extends BaseBean {

	private static final long serialVersionUID = 6082454691890193587L;

	public void handleFileUpload(FileUploadEvent event)  {
        try {
            String path = (String) FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")
       		     + "/resources/images/photos/";

            File targetFolder = new File(path);
            
            System.out.println("Name : " + event.getFile().getFileName());

            // To correct latin chars
            String targetName = new String(event.getFile().getFileName().getBytes(Charset.defaultCharset()), "UTF-8");
            System.out.println("Name : " + targetName);

            InputStream inputStream = event.getFile().getInputstream();
            OutputStream out = new FileOutputStream(new File(targetFolder, targetName));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            
            inputStream.close();
            out.flush();
            out.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
