========================================================================================
  Port Forwading Tunnel
========================================================================================

 Port Forwarding Tunnel is to forward connection from specific port to other port under 
same network group. The software objective is to learn how any application layer protocols 
are working. 

Compiling method is easy and well known.

$> javac Tunnel.java

Execution methos is easy and well known.

$> java Tunnel ["forwarded destination host name" "forwarded destination port" 
"tunnel channel service port"] . . .

For example, if you have to forward a port 80 of a host 192.168.1.3 to a port 8080 of a 
host 192.168.1.4, you can execute the tunnel on host 192.168.1.3 by using option following:

$> java Tunnel 192.168.1.4 8080 80
                                                  |
                                                  
 Forwarded destination    Port Forwarding Tunnel  |  Any clients from Any network group
 
|-------------------------|    |-------------------------|  |  |--------------------|

|   192.168.1.4:8080      |<==>|    192.168.1.3:80       |<=|=>|   Any clients      |

|-------------------------|    |-------------------------|  |  |--------------------|

                                                  |
                                                  
                                                  |
                                                  
