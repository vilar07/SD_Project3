xterm  -T "General Repository" -hold -e "./GenReposDeployAndRun.sh" &
sleep 1
xterm  -T "Assault Party 0" -hold -e "./AP0DeployAndRun.sh" &
xterm  -T "Assault Party 1" -hold -e "./AP1DeployAndRun.sh" &
xterm  -T "Museum" -hold -e "./MuseumDeployAndRun.sh" &
xterm  -T "Collection Site" -hold -e "./CollSiteDeployAndRun.sh" &
xterm  -T "Concentration Site" -hold -e "./ConcSiteDeployAndRun.sh" &
sleep 1
xterm  -T "Client" -hold -e "./ClientDeployAndRun.sh" &