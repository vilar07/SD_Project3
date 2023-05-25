echo "Transfering data to the RMIregistry node."
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'mkdir -p Public/classes/interfaces'
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'rm -rf Public/classes/interfaces/*'
sshpass -f password scp dirRMIRegistry.zip sd107@l040101-ws01.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the RMIregistry node."
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirRMIRegistry.zip'
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'cd test/HeistToTheMuseum/dirRMIRegistry ; cp interfaces/*.class /home/sd107/Public/classes/interfaces ; cp set_rmiregistry_d.sh /home/sd107'
echo "Executing program at the RMIregistry node."
sshpass -f password ssh sd107@l040101-ws01.ua.pt './set_rmiregistry_d.sh sd107 22160'
