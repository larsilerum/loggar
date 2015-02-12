package se.larsilerum.loggar.lars;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@ManagedBean
@SessionScoped
public class KaminHandler {

    public void enterKamin() {
        File file = new File("/home/pi/vplog/kamin.txt");
        try {
            PrintWriter printWriter = new PrintWriter(file);
            Date date = new Date();
            printWriter.append(Long.toString(date.getTime()));
            printWriter.append("\n");
            printWriter.append(Long.toString(date.getTime() + 1000 * 3600 * 3));
            printWriter.append("\n");
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
