echo "Transfering data to the Concentration Site node."
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirConcSite.zip sd107@l040101-ws07.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the Concentration Site node."
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirConcSite.zip'
echo "Executing program at the Concentration Site node."
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'cd test/HeistToTheMuseum/dirConcSite ; java serverSide.main.ConcentrationSiteMain 22167 l040101-ws02.ua.pt 22162 l040101-ws03.ua.pt 22163 l040101-ws04.ua.pt 22164 l040101-ws06.ua.pt 22166'
