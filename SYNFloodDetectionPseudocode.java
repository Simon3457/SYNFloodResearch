import java.io.File; 	
import org.jnetpcap.Pcap;	
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.tcpip.Tcp;	
import org.jnetpcap.protocol.network.Ip4;
	
public class synFlood {
  static String folder = "/home/(folder_where_your_pcap_file_is_located)";
  static double count = 0;
  
  public static void main(String[] args) {	
    File file = new File(folder);
    String fileName = folder + file.getName();
    StringBuilder errbuf = new StringBuilder();
    Pcap pcap = Pcap.openOffline(fileName, errbuf);
    if(pcap == null) {
      System.err.println(errbuf);
      return;
    }
    final int sCount = 0;
    final int aCount = 0;
    final int count = 0;
    final long t1 = 0;
    final long t2 = 0;
    pcap.loop(1000, new JPacketHandler<StringBuilder>() {
    Tcp tcp = new Tcp();
    Ip4 ip = new Ip4();
    Ip4.Timestamp ts = new Ip4.Timestamp();
    public void nextPacket(JPacket packet, StringBuilder errbuf) {
        if (packet.hasHeader(Tcp.ID)) {
          packet.getHeader(tcp);
          if(tcp.flags_SYN())
            sCount++;
          if(tcp.flags_SYN())
            aCount++;
        }
        if (packet.hasHeader(JProtocol.IP4_ID)) {
          ip = packet.getHeader(ip);
          if(ip.hasSubHeader(ts)) {
            Ip4.Timestamp ts1 = ip.getSubHeader(ts);
            t1 = ts1.timestamp(1);
          }
          if(t1 < t2 + 10) {
            t2 = t1;
            count++;
          }
        }
      }
    }, errbuf);
    if(count > 500 && sCount > aCount*10) {
      System.out.println("SYN Flood detected.");
    } else {
      System.out.println("SYN Flood not detected");
    }
    return;
  }
}
