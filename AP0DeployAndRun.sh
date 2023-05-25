echo "Transfering data to the assault party 0 node."
sshpass -f password ssh sd107@l040101-ws05.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws05.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirAP0.zip sd107@l040101-ws05.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the assault party 0 node."
sshpass -f password ssh sd107@l040101-ws05.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirAP0.zip'
echo "Executing program at the assault party 0 node."
sshpass -f password ssh sd107@l040101-ws05.ua.pt 'cd test/HeistToTheMuseum/dirAP0 ; ./ap0_com_d.sh sd107'
