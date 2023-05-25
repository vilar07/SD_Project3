xterm  -T "General Repository" -hold -e "./GenReposDeployAndRun.sh" &
sleep 4
xterm  -T "Assault Party 0" -hold -e "./AP0DeployAndRun.sh" &
sleep 4
xterm  -T "Assault Party 1" -hold -e "./AP1DeployAndRun.sh" &
sleep 4
xterm  -T "Museum" -hold -e "./MuseumDeployAndRun.sh" &
sleep 4
xterm  -T "Collection Site" -hold -e "./CollSiteDeployAndRun.sh" &
sleep 4
xterm  -T "Concentration Site" -hold -e "./ConcSiteDeployAndRun.sh" &
sleep 4
xterm  -T "Clients" -hold -e "./ClientDeployAndRun.sh" &
# increase sleep times if needed
