xterm  -T "RMI registry" -hold -e "./RMIRegistryDeployAndRun.sh" &
sleep 8
xterm  -T "Registry" -hold -e "./RegistryDeployAndRun.sh" &
sleep 8
xterm  -T "General Repository" -hold -e "./GenReposDeployAndRun.sh" &
sleep 2
xterm  -T "Assault Party 0" -hold -e "./AP0DeployAndRun.sh" &
sleep 2
xterm  -T "Assault Party 1" -hold -e "./AP1DeployAndRun.sh" &
sleep 2
xterm  -T "Museum" -hold -e "./MuseumDeployAndRun.sh" &
sleep 2
xterm  -T "Collection Site" -hold -e "./CollSiteDeployAndRun.sh" &
sleep 2
xterm  -T "Concentration Site" -hold -e "./ConcSiteDeployAndRun.sh" &
sleep 1
xterm  -T "Clients" -hold -e "./ClientDeployAndRun.sh" &
# increase sleep times if needed
