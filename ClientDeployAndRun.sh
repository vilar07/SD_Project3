echo "Transfering data to the client node."
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirClient.zip sd107@l040101-ws09.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the client node."
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirClient.zip'
echo "Executing program at the client node."
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'cd test/HeistToTheMuseum/dirClient ; ./client_com_d.sh sd107'
