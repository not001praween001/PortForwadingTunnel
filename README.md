========================================================================================
  Port Forwading Tunnel
========================================================================================

```text
 ____            _   _____                                _ _             
|  _ \ ___  _ __| |_|  ___|__  _ ____      ____ _ _ __ __| (_)_ __   __ _ 
| |_) / _ \| '__| __| |_ / _ \| '__\ \ /\ / / _` | '__/ _` | | '_ \ / _` |
|  __/ (_) | |  | |_|  _| (_) | |   \ V  V / (_| | | | (_| | | | | | (_| |
|_|   \___/|_|   \__|_|  \___/|_|    \_/\_/ \__,_|_|  \__,_|_|_| |_|\__, |
                                                                    |___/ 
   
 _____                       _ 
|_   _|   _ _ __  _ __   ___| |
  | || | | | '_ \| '_ \ / _ \ |
  | || |_| | | | | | | |  __/ |
  |_| \__,_|_| |_|_| |_|\___|_|


```

 Port Forwarding Tunnel is to forward connection from specific port to other port under 
same network group. The software objective is to learn how any application layer protocols 
are working. 

Compiling method is easy and well known.
```shell
javac Tunnel.java
```

Execution methos is easy and well known.
```shell
java Tunnel ["forwarded destination host name" "forwarded destination port" 
```
"tunnel channel service port"] . . .

For example, if you have to forward a port 80 of a host 192.168.1.3 to a port 8080 of a 
host 192.168.1.4, you can execute the tunnel on host 192.168.1.3 by using option following:

```shell
java Tunnel 192.168.1.4 8080 80
```

```text
|=======================================================================================|
|                                   Operation model                                     |
|=======================================================================================|
|                          |    |                         |  |  |   Any clients from    |
|Destination forwarded port|    |      Tunnel port        |  |  | outside network group |
|--------------------------|    |-------------------------|  |  |-----------------------|
|   192.168.1.4:8080       |<==>|    192.168.1.3:80       |<=|=>|      Any clients      |
|--------------------------|    |-------------------------|  |  |-----------------------|

```
                                                  
