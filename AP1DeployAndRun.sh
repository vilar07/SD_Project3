echo "Transfering data to the Assault Party 1 node."
sshpass -f password ssh sd107@l040101-ws05.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws05.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirAP1.zip sd107@l040101-ws05.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the Assault Party 1 node."
sshpass -f password ssh sd107@l040101-ws05.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirAP1.zip'
echo "Executing program at the Assault Party 1 node."
sshpass -f password ssh sd107@l040101-ws05.ua.pt 'cd test/HeistToTheMuseum/dirAP1 ; java serverSide.main.AssaultPartyMain 1 22164 l040101-ws02.ua.pt 22162'
