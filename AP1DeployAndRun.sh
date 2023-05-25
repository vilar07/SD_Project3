echo "Transfering data to the assault party 1 node."
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirAP1.zip sd107@l040101-ws06.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the assault party 1 node."
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirAP1.zip'
echo "Executing program at the assault party 1 node."
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'cd test/HeistToTheMuseum/dirAP1 ; ./ap1_com_d.sh sd107'
