# SYN Flood Research Project

<h2><b>Description</b></h2>
This research project goes through the process of analyzing the network traffic of a VM on Wireshark after running a SYN flood attack program, and noting observations. The point of the project was to analyze what characteristics are indicative of certain attacks (in this case SYN Flood attacks), how we can detect those attacks, how we can prevent those attacks and how we can recover any damages produced by these attacks.
<br />


<h2><b>Languages and Utilities</b></h2>

- <b>Java</b>
- <b>Wireshark</b>

<h2><b>Environments</b></h2>

- <b>Linux Ubuntu</b>

<h2><b>Project Report</b></h2>
<p>
  The attack chosen for this project is a type of Denial-of-Service attack called a SYN flood attack. Denial-of-Service attacks’ main function is to halt a user’s access to network services through error-causing methods. As networks have a certain flow installed within them, that flow can also be congested by means of either saturating the bandwidth of a certain site, by breaking down the web servers on an application level, or by consuming the resources of a server. In this case, a SYN flood functions under the protocol type, and is used for consuming the server’s resources to deny network access to anyone that regularly uses that server.
</p>
<p>
  To understand SYN flood attacks more clearly, we must first explain how the Transmission Control Protocol (TCP) works. It’s supposed to provide a safe connection between a server and a client through a process called the “three-way handshake”, before sharing data on the application level. The way the three-way handshake works is that SYN packets are being sent from a Source IP client to a Destination IP server. In return, the Destination IP responds with a SYN/ACK packet sent to the Source IP, which is then supposed to be confirmed by the Source IP with an ACK packet sent in return to the Destination IP.
</p>
<p align="center">
  TCP Three Way Handshake: <br/>
  <img src="https://i.imgur.com/RqsrFWf.png" height="50%" width="50%" alt="SYN Flood Research Project"/>
</p>
<p>
  What causes a SYN flood attack is a large amount of faulty three-way handshake protocols that happen when a TCP connection is made. If the Source IP address is spoofed, the connection gets paused since the Source IP address doesn’t exist in the first place, so no real connections can be made. Since the three-way handshake is incomplete, the volume of half connections that are being continuously made take up all of the machine network’s available bandwidth, which cause a Denial-of-Service for the user at the Destination IP address.
</p>
<p align="center">
  SYN Flood Attacks: <br/>
  <img src="https://i.imgur.com/QZqwBDZ.png" height="50%" width="50%" alt="SYN Flood Research Project"/>
</p>
<br />
<p>
  By detecting the number of connections that are being made over short periods of time, we’ll notice abnormally large amounts of packets being sent to the destination IP address and port. Usually, the destination port number is a well-known port like port 80, and the destination IP address is always the same as the victim machine. Therefore, our first feature should be the average connections made to the destination port and destination IP address per second. Since bursts of SYN flooding can go from 0 to 2400 requests per second, once it reaches a certain threshold of requests per second, it gives enough evidence to stop the attack before it congests the network.
</p>
<p>
  By detecting the protocol and packet type, we can compare protocol packets after a one second period to determine whether we’re seeing a high amount of TCP connections, and whether the SYN packet amount far surpasses the ACK packet amount received by the destination IP address. Since other network attacks exist such as HTTP flood, UDP attacks and ICMP flood, it’s useful to know the type of protocol that is being used for the attack. In addition, the discrepancy between SYN packets and ACK packets indicates that the source IP addresses and source port numbers are spoofed since they were unable to send the final ACK packet to establish the full connection. Hence the second feature being a comparison of the percentages of TCP SYN packets over ACK packets received by the source IP address.
</p>
<p>
  In conclusion, determining the type of protocol we’re dealing with, detecting the volume of connections made under a short amount of time and detecting the ratio of SYN packets over ACK packets should be enough information to detect a SYN flood attack properly.
</p>

<h2><b>Threat Detection Program</b></h2>
<p>
  We need code that will take in the network traffic, analyze the TCP connection requests that are being sent on a time-based reference, and compare the rates of the tcp flags. Since the most popular method of packet analysis is through Wireshark, we can write a code that will take in a pcap file (Wireshark-based analysis file), analyze the number of TCP requests within one second timeframes and analyze the ratio of syn flags versus ack flags. 
</p>
<p>
  If the packets with flags “tcp.flags.syn == 1” and “tcp.flags.ack == 0” amount both surpasses a total count of 100 per second and the packets with both “tcp.flags.syn == 1” and “tcp.flags.ack == 1” amount is under 10 per second, then the code should inform the user that a SYN flood is occurring. If those conditions are not met, then the attack is not a SYN flood. 
</p>
<p>
  With python, we can implement the scapy module or pyshark to take in the info produced by Wireshark with a pcap wrapper. With java, we can implement jpcap or jNetPcap to take the necessary info from Wireshark with a pcap wrapper as well. We could also use the linux command “tcpdump” to dump the network traffic into a file with the specific TCP connections (tcpdump -r tcp -w fileToRead.txt), and analyze the fileToRead.txt to see how many TCP connections with the tags specified above are present in the network dump. For the java pseudocode <a href="https://github.com/Simon3457/SYNFloodResearch/blob/main/SYNFloodDetectionPseudocode.java">here</a>, jNetPcap was the pcap wrapper package used.
</p>

<h2><b>Proposed Features</b></h2>
<ol>
  <li>Calculating the average number of connections made to the server (destination port and IP address) over consecutive one second periods and comparing it with a relatively large number (100 connections per second, so a connection every 10 milliseconds).</li>
  <li>Calculating & comparing the rate of TCP SYN packets over ACK packets received over consecutive one second periods.</li>
</ol>

<h2><b>Sample Output</b></h2>
<p>
  Most values of TCP connections attempted per second almost always surpasses 100 connections per second, which is an abnormally large number of requests all at once.
</p>
<p align="center">
  Wireshark IO Graphs: <br/>
  <img src="https://i.imgur.com/Gm5IO2X.png" height="50%" width="50%" alt="SYN Flood Research Project"/>
</p>
<br />
<p>
  Notice the time it takes from one packet request to another. The most time it takes for two TCP requests to reach the server is ~1 millisecond.
</p>
<p align="center">
  Wireshark Output Results: <br/>
  <img src="https://i.imgur.com/wMvsuhJ.png" height="50%" width="50%" alt="SYN Flood Research Project"/>
</p>
