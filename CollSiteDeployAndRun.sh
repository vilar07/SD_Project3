echo "Transfering data to the collection site node."
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirCollSite.zip sd107@l040101-ws07.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the collection site node."
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirCollSite.zip'
echo "Executing program at the collection site node."
sshpass -f password ssh sd107@l040101-ws07.ua.pt 'cd test/HeistToTheMuseum/dirCollSite ; ./coll_com_d.sh sd107'
