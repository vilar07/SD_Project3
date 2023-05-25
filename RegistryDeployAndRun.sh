echo "Transfering data to the registry node."
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password scp dirRegistry.zip sd107@l040101-ws01.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the registry node."
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirRegistry.zip'
echo "Executing program at the registry node."
sshpass -f password ssh sd107@l040101-ws01.ua.pt 'cd test/HeistToTheMuseum/dirRegistry ; ./registry_com_d.sh sd107'
