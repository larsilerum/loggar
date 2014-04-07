package se.larsilerum.loggar.halar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class HalarTempGenerator implements Serializable {

    private static final long serialVersionUID = 1L;

    public String createGraphs() throws InterruptedException {
        try {
            runCommand("rrdtool graph /tmp/tempdygn.png --width=600 --height=300 " +
                    "DEF:solprimar=/home/pi/templog/sol.rrd:solprimar:AVERAGE " +
                    "DEF:tanktopp=/home/pi/templog/sol.rrd:tanktopp:AVERAGE " +
                    "DEF:tankbotten=/home/pi/templog/sol.rrd:tankbotten:AVERAGE " +
                    "LINE1:solprimar#00ff00 LINE1:tanktopp#ff0000 LINE1:tankbotten#0000ff");

            runCommand("rrdtool graph /tmp/tempvecka.png --width=600 --height=300 --start=-1w " +
                    "DEF:solprimar=/home/pi/templog/sol.rrd:solprimar:AVERAGE " +
                    "DEF:tanktopp=/home/pi/templog/sol.rrd:tanktopp:AVERAGE " +
                    "DEF:tankbotten=/home/pi/templog/sol.rrd:tankbotten:AVERAGE " +
                    "LINE1:solprimar#00ff00 LINE1:tanktopp#ff0000 LINE1:tankbotten#0000ff");


        } catch (IOException e) {
            return "ErrorPage";
        }
        return "HalarTempPage";
    }

    private void runCommand(String string) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(string);
        process.waitFor();
    }
}