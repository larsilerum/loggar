package se.larsilerum.loggar.halar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: larslarsson
 * Date: 26/03/14
 * Time: 22:49
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@SessionScoped
public class HalarCustomDiagramGenerator {

    private List<String> selectedDataSources;
    private Date endDate = Calendar.getInstance().getTime();
    private Date startDate = new Date(endDate.getTime() - 1000*24*3600);

    public String createGraph() throws InterruptedException {
        try {
            System.out.println("startDate = " + startDate.getTime());
            System.out.println("endDate = " + endDate.getTime());
            String command = getCommandString(startDate, endDate);
            runCommand(command);

        } catch (IOException e) {
            return "ErrorPage";
        }
        return "CustomGraphPage";
    }

    private String getCommandString(Date startDate, Date endDate) {
        StringBuilder command = new StringBuilder("rrdtool graph /tmp/custom.png --start " + startDate.getTime()/1000 +
                " --end " + endDate.getTime()/1000 + " --width=600 --height=300 " +
                "DEF:solprimar=/home/pi/templog/sol.rrd:solprimar:AVERAGE " +
                "DEF:tanktopp=/home/pi/templog/sol.rrd:tanktopp:AVERAGE " +
                "DEF:tankbotten=/home/pi/templog/sol.rrd:tankbotten:AVERAGE ");

        for (String selectItem : selectedDataSources) {
            System.out.println("selectItem = " + selectItem);
            if (selectItem.equals(DataSources.SOLPRIMAR.name())) {
                command.append("LINE1:solprimar#00ff00 ");
            }
            if (selectItem.equals(DataSources.TANKTOPP.name())) {
                command.append("LINE1:tanktopp#ff0000 ");
            }
            if (selectItem.equals(DataSources.TANKBOTTEN.name())) {
                command.append("LINE1:tankbotten#0000ff ");
            }
        }
        return command.toString();
    }

    public DataSources[] getDataSources() {
        return DataSources.values();
    }

    public void setSelectedDataSources(List<String> selectedDataSources) {
        this.selectedDataSources = selectedDataSources;
    }

    public List<String> getSelectedDataSources() {
        return selectedDataSources;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    private void runCommand(String string) throws IOException, InterruptedException {
        System.out.println("string = " + string);
        Process process = Runtime.getRuntime().exec(string);
        process.waitFor();
    }
}
