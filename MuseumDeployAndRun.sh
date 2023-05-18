echo "Transfering data to the Museum node."
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirMuseum.zip sd107@l040101-ws06.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the Museum node."
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirMuseum.zip'
echo "Executing program at the Museum node."
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'cd test/HeistToTheMuseum/dirMuseum ; java serverSide.main.MuseumMain 22165 l040101-ws02.ua.pt 22162 l040101-ws03.ua.pt 22163 l040101-ws05.ua.pt 22164'
