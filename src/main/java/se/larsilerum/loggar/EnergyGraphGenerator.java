package se.larsilerum.loggar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class EnergyGraphGenerator implements Serializable {

    private static final long serialVersionUID = 1L;
    private PowerType type;
    private String startTime = "-1d";
    private String endTime = "now";
    private String diff = "86400";
    private String errorMessage ="No Error";

    public String createEnergyGraph() {
        try {
            String rrdDir = System.getProperty("rrd.energi.dir");
            System.out.println("rrdDir = " + rrdDir);
            createGraph("-1h", "now", "effekt-1h", PowerType.POWER, rrdDir);
            createGraph("-6h", "now", "effekt-6h", PowerType.POWER, rrdDir);
            createGraph("-1d", "now", "effekt-1d", PowerType.POWER, rrdDir);
            createGraph("-1m", "now", "energi-1m", PowerType.ENERGY, rrdDir);
            createGraph("-1y", "now", "energi-1y", PowerType.ENERGY, rrdDir);
        } catch (IOException e) {
            return "ErrorPage";
        } catch (InterruptedException e) {
            return "ErrorPage";
        }
        return "EnPage";
    }

    public String createCompareGraph() {
        String command = "rrdtool graph /tmp/compare.png --lower-limit=0 --width=600 --height=300 --start=" + startTime + " --end=" + endTime +
                " DEF:effekt1=/home/pi/energilog/energi.rrd:effekt:AVERAGE" +
                " DEF:effekt2=/home/pi/energilog/energi.rrd:effekt:AVERAGE:end=" + endTime + "-" + diff + ":start=" + startTime + "-" + diff +
                " LINE1:effekt1#ff0000" +
                " SHIFT:effekt2:" + diff +
                " LINE1:effekt2#00ff00";

        System.out.println(command);

        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            return "CompareGraphPage";
        } catch (IOException e) {
            errorMessage = e.getMessage() + "\n" + command;
            return "ErrorPage";
        } catch (InterruptedException e) {
            errorMessage = e.getMessage() + "\n" + command;
            return "ErrorPage";
        }
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private void createGraph(String start, String end, String fileName, PowerType type, String rrdDir) throws IOException, InterruptedException {
        String createCommand ;
        if (type.equals(PowerType.ENERGY)) {
            createCommand = getEnergyString(start, fileName, rrdDir);
        } else {
            createCommand = getPowerString(start, end, fileName, rrdDir);
        }
        Process process = Runtime.getRuntime().exec(createCommand);
        process.waitFor();

    }

    private String getEnergyString(String start, String fileName, String rrdDir) {
        return "rrdtool graph /tmp/" + fileName + ".png --lower-limit=0 --width=600 --height=300 --step 86400 --start=" +
                start + " DEF:effekt=" + rrdDir + "/energi.rrd:effekt:AVERAGE CDEF:energidygn=effekt,24,*,1000,/ CDEF:energiMedel=energidygn,1000000,TRENDNAN LINE1:energidygn#cccccc LINE1:energiMedel#ff0000";
    }

    private String getPowerString(String start, String end, String fileName, String rrdDir) {
        return "rrdtool graph /tmp/" + fileName + ".png --lower-limit=0 --width=600 --height=300 --start=" + start + " --end=" + end +
            " DEF:effekt=" + rrdDir + "/energi.rrd:effekt:AVERAGE LINE1:effekt#ff0000";
    }


    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getDiff() {
        return diff;
    }

    public PowerType getType() {
        return type;
    }

    public void setType(PowerType type) {
        this.type = type;
    }


}