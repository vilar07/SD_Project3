echo "Transfering data to the general repository node."
sshpass -f password ssh sd107@l040101-ws02.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws02.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirGeneralRepos.zip sd107@l040101-ws02.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd107@l040101-ws02.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirGeneralRepos.zip'
echo "Executing program at the general repository node."
sshpass -f password ssh sd107@l040101-ws02.ua.pt 'cd test/HeistToTheMuseum/dirGeneralRepos ; ./repos_com_d.sh sd107'
echo "Server shutdown."
sshpass -f password ssh sd107@l040101-ws02.ua.pt 'cd test/HeistToTheMuseum/dirGeneralRepos ; less heist.log'
