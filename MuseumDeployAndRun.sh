echo "Transfering data to the museum node."
sshpass -f password ssh sd107@l040101-ws03.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws03.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirMuseum.zip sd107@l040101-ws03.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the museum node."
sshpass -f password ssh sd107@l040101-ws03.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirMuseum.zip'
echo "Executing program at the museum node."
sshpass -f password ssh sd107@l040101-ws03.ua.pt 'cd test/HeistToTheMuseum/dirMuseum ; ./museum_com_d.sh sd107'
