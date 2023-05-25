echo "Transfering data to the concentration site node."
sshpass -f password ssh sd107@l040101-ws08.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd107@l040101-ws08.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirConcSite.zip sd107@l040101-ws08.ua.pt:test/HeistToTheMuseum
echo "Decompressing data sent to the concentration site node."
sshpass -f password ssh sd107@l040101-ws08.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirConcSite.zip'
echo "Executing program at the concentration site node."
sshpass -f password ssh sd107@l040101-ws08.ua.pt 'cd test/HeistToTheMuseum/dirConcSite ; ./conc_com_d.sh sd107'
