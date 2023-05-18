echo "Transfering data to the Collection Site node."
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirCollSite.zip sd107@l040101-ws06.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the Collection Site node."
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirCollSite.zip'
echo "Executing program at the Collection Site node."
sshpass -f password ssh sd107@l040101-ws06.ua.pt 'cd test/HeistToTheMuseum/dirCollSite ; java serverSide.main.CollectionSiteMain 22166 l040101-ws02.ua.pt 22162 l040101-ws03.ua.pt 22163 l040101-ws04.ua.pt 22164 l040101-ws05.ua.pt 22165'
