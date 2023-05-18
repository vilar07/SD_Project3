echo "Transfering data to the Assault Party 0 node."
sshpass -f password ssh sd107@l040101-ws03.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws03.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirAP0.zip sd107@l040101-ws03.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the Assault Party 0 node."
sshpass -f password ssh sd107@l040101-ws03.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirAP0.zip'
echo "Executing program at the Assault Party 0 node."
sshpass -f password ssh sd107@l040101-ws03.ua.pt 'cd test/HeistToTheMuseum/dirAP0 ; java serverSide.main.AssaultPartyMain 0 22163 l040101-ws02.ua.pt 22162'
