echo "Transfering data to the Client node."
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirClient.zip sd107@l040101-ws09.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the Client Site node."
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirClient.zip'
echo "Executing program at the Client Site node."
sshpass -f password ssh sd107@l040101-ws09.ua.pt 'cd test/HeistToTheMuseum/dirClient ; java clientSide.main.HeistToTheMuseum 22168 l040101-ws02.ua.pt 22162 l040101-ws03.ua.pt 22163 l040101-ws05.ua.pt 22164 l040101-ws06.ua.pt 22165 l040101-ws07.ua.pt 22166 l040101-ws08.ua.pt 22167'
