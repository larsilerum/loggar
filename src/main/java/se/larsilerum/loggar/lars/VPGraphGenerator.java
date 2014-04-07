package se.larsilerum.loggar.lars;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class VPGraphGenerator implements Serializable {

    private static final long serialVersionUID = 1L;

    public String createVpGraph() throws InterruptedException {
        try {
            runCommand("rrdtool graph /tmp/dag_inomhus.png --lower-limit=15 -r --start=-1d --width=600 --height=300 " +
                    "DEF:korrektion=/home/pi/vplog/vp.rrd:korrektion:AVERAGE DEF:inomhus=/home/pi/vplog/vp.rrd:inomhus:AVERAGE " +
                    "DEF:bvinomhus=/home/pi/vplog/vp.rrd:bvinomhus:AVERAGE " +
                    "CDEF:korrInneTemp=inomhus,korrektion,- CDEF:kitav=korrInneTemp,295,TRENDNAN " +
                    "LINE1:bvinomhus#00ff00 LINE1:inomhus#a0a0ff LINE1:kitav#ff0000");

//            runCommand("rrdtool graph /tmp/vecka_inomhus.png --lower-limit=15 -r --start=-1w --width=600 --height=300 " +
//                    "DEF:korrektion=/home/pi/vplog/vp.rrd:korrektion:AVERAGE DEF:inomhus=/home/pi/vplog/vp.rrd:inomhus:AVERAGE " +
//                    "DEF:bvinomhus=/home/pi/vplog/vp.rrd:bvinomhus:AVERAGE " +
//                    "CDEF:korrInneTemp=inomhus,korrektion,- CDEF:kitav=korrInneTemp,295,TRENDNAN " +
//                    "LINE1:bvinomhus#00ff00 LINE1:inomhus#a0a0ff LINE1:kitav#ff0000");

            runCommand("rrdtool graph /tmp/dag_varmvatten.png --width=600 --height=300 --lower-limit=15 -r " +
                    "DEF:varmvatten=/home/pi/vplog/vp.rrd:varmvatten:AVERAGE " +
                    "DEF:soltemp=/home/pi/vplog/vp.rrd:soltemp:AVERAGE " +
                    "LINE1:varmvatten#ff0000 " +
                    "LINE1:soltemp#00ff00");

            runCommand("rrdtool graph /tmp/frekvens_dag.png --width=600 --height=300 " +
                    "DEF:frekvens=/home/pi/vplog/vp.rrd:frekvens:AVERAGE LINE1:frekvens#ff0000");

            runCommand("rrdtool graph /tmp/elpatron_dag.png --width=600 --height=300 " +
                    "DEF:elpatron=/home/pi/vplog/vp.rrd:elpatron:AVERAGE LINE1:elpatron#ff0000");

            runCommand("rrdtool graph /tmp/prognos_dag.png --width=600 --height=300 " +
                    "DEF:prognostemp=/home/pi/vplog/vp.rrd:prognostemp:AVERAGE " +
                    "DEF:prognossymbol=/home/pi/vplog/vp.rrd:prognossymbol:AVERAGE LINE1:prognostemp#ff0000 LINE1:prognossymbol#00ff00");

            runCommand("rrdtool graph /tmp/avluft_dag.png --width=600 --height=300 --upper-limit=30 -r " +
                    "DEF:avluft=/home/pi/vplog/vp.rrd:avluft:AVERAGE LINE1:avluft#ff0000");

            runCommand("rrdtool graph /tmp/retur_dag.png --rigid -u 40 --width=600 --height=300 " +
                    "DEF:retur=/home/pi/vplog/vp.rrd:retur:AVERAGE LINE1:retur#ff0000");

        } catch (IOException e) {
            return "ErrorPage";
        }
        return "VpPage";
    }

    private void runCommand(String string) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(string);
        process.waitFor();
    }
}