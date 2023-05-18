echo "Compiling source code."
javac */*.java */*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "  General Repository of Information"
rm -rf dirGeneralRepos
mkdir -p dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/entities dirGeneralRepos/serverSide/sharedRegions \
    dirGeneralRepos/serverSide/interfaces dirGeneralRepos/commInfra dirGeneralRepos/clientSide/entities \
    dirGeneralRepos/clientSide/stubs dirGeneralRepos/utils
cp serverSide/entities/GeneralRepositoryProxyAgent.class dirGeneralRepos/serverSide/entities
cp serverSide/sharedRegions/GeneralRepository.class dirGeneralRepos/serverSide/sharedRegions
cp serverSide/main/GeneralRepositoryMain.class dirGeneralRepos/serverSide/main
cp serverSide/interfaces/GeneralRepositoryInterface.class serverSide/interfaces/MasterThiefClone.class \
    serverSide/interfaces/OrdinaryThiefClone.class dirGeneralRepos/serverSide/interfaces
cp commInfra/*.class dirGeneralRepos/commInfra
cp clientSide/entities/*.class dirGeneralRepos/clientSide/entities
cp clientSide/stubs/*.class dirGeneralRepos/clientSide/stubs
cp utils/*.class dirGeneralRepos/utils
echo "  Assault Party 0"
rm -rf dirAP0
mkdir -p dirAP0/serverSide/main dirAP0/serverSide/entities dirAP0/serverSide/sharedRegions \
    dirAP0/serverSide/interfaces dirAP0/commInfra dirAP0/clientSide/entities \
    dirAP0/clientSide/stubs dirAP0/utils
cp serverSide/entities/AssaultPartyProxyAgent.class dirAP0/serverSide/entities
cp serverSide/sharedRegions/AssaultParty*.class dirAP0/serverSide/sharedRegions
cp serverSide/main/AssaultPartyMain.class dirAP0/serverSide/main
cp serverSide/interfaces/AssaultPartyInterface.class serverSide/interfaces/MasterThiefClone.class \
    serverSide/interfaces/OrdinaryThiefClone.class dirAP0/serverSide/interfaces
cp commInfra/*.class dirAP0/commInfra
cp clientSide/entities/*.class dirAP0/clientSide/entities
cp clientSide/stubs/*.class dirAP0/clientSide/stubs
cp utils/Constants.class utils/Room.class dirAP0/utils
echo "  Assault Party 1"
rm -rf dirAP1
mkdir -p dirAP1/serverSide/main dirAP1/serverSide/entities dirAP1/serverSide/sharedRegions \
    dirAP1/serverSide/interfaces dirAP1/commInfra dirAP1/clientSide/entities \
    dirAP1/clientSide/stubs dirAP1/utils
cp serverSide/entities/AssaultPartyProxyAgent.class dirAP1/serverSide/entities
cp serverSide/sharedRegions/AssaultParty*.class dirAP1/serverSide/sharedRegions
cp serverSide/main/AssaultPartyMain.class dirAP1/serverSide/main
cp serverSide/interfaces/AssaultPartyInterface.class serverSide/interfaces/MasterThiefClone.class \
    serverSide/interfaces/OrdinaryThiefClone.class dirAP1/serverSide/interfaces
cp commInfra/*.class dirAP1/commInfra
cp clientSide/entities/*.class dirAP1/clientSide/entities
cp clientSide/stubs/*.class dirAP1/clientSide/stubs
cp utils/Constants.class utils/Room.class dirAP1/utils
echo "  Museum"
rm -rf dirMuseum
mkdir -p dirMuseum/serverSide/main dirMuseum/serverSide/entities dirMuseum/serverSide/sharedRegions \
    dirMuseum/serverSide/interfaces dirMuseum/commInfra dirMuseum/clientSide/entities \
    dirMuseum/clientSide/stubs dirMuseum/utils
cp serverSide/entities/MuseumProxyAgent.class dirMuseum/serverSide/entities
cp serverSide/sharedRegions/Museum.class dirMuseum/serverSide/sharedRegions
cp serverSide/main/MuseumMain.class dirMuseum/serverSide/main
cp serverSide/interfaces/MuseumInterface.class serverSide/interfaces/MasterThiefClone.class \
    serverSide/interfaces/OrdinaryThiefClone.class dirMuseum/serverSide/interfaces
cp commInfra/*.class dirMuseum/commInfra
cp clientSide/entities/*.class dirMuseum/clientSide/entities
cp clientSide/stubs/*.class dirMuseum/clientSide/stubs
cp utils/Constants.class utils/Room.class dirMuseum/utils
echo "  Collection Site"
rm -rf dirCollSite
mkdir -p dirCollSite/serverSide/main dirCollSite/serverSide/entities dirCollSite/serverSide/sharedRegions \
    dirCollSite/serverSide/interfaces dirCollSite/commInfra dirCollSite/clientSide/entities \
    dirCollSite/clientSide/stubs dirCollSite/utils
cp serverSide/entities/CollectionSiteProxyAgent.class dirCollSite/serverSide/entities
cp serverSide/sharedRegions/CollectionSite.class dirCollSite/serverSide/sharedRegions
cp serverSide/main/CollectionSiteMain.class dirCollSite/serverSide/main
cp serverSide/interfaces/CollectionSiteInterface.class serverSide/interfaces/MasterThiefClone.class \
    serverSide/interfaces/OrdinaryThiefClone.class dirCollSite/serverSide/interfaces
cp commInfra/*.class dirCollSite/commInfra
cp clientSide/entities/*.class dirCollSite/clientSide/entities
cp clientSide/stubs/*.class dirCollSite/clientSide/stubs
cp utils/Constants.class dirCollSite/utils
echo "  Concentration Site"
rm -rf dirConcSite
mkdir -p dirConcSite/serverSide/main dirConcSite/serverSide/entities dirConcSite/serverSide/sharedRegions \
    dirConcSite/serverSide/interfaces dirConcSite/commInfra dirConcSite/clientSide/entities \
    dirConcSite/clientSide/stubs dirConcSite/utils
cp serverSide/entities/ConcentrationSiteProxyAgent.class dirConcSite/serverSide/entities
cp serverSide/sharedRegions/ConcentrationSite.class dirConcSite/serverSide/sharedRegions
cp serverSide/main/ConcentrationSiteMain.class dirConcSite/serverSide/main
cp serverSide/interfaces/ConcentrationSiteInterface.class serverSide/interfaces/MasterThiefClone.class \
    serverSide/interfaces/OrdinaryThiefClone.class dirConcSite/serverSide/interfaces
cp commInfra/*.class dirConcSite/commInfra
cp clientSide/entities/*.class dirConcSite/clientSide/entities
cp clientSide/stubs/*.class dirConcSite/clientSide/stubs
cp utils/Constants.class dirConcSite/utils
echo "  Client"
rm -rf dirClient
mkdir -p dirClient/clientSide/main dirClient/clientSide/entities dirClient/clientSide/stubs dirClient/utils \
         dirClient/commInfra
cp clientSide/main/HeistToTheMuseum.class dirClient/clientSide/main
cp clientSide/entities/*.class dirClient/clientSide/entities
cp clientSide/stubs/*.class dirClient/clientSide/stubs
cp utils/Constants.class dirClient/utils
cp commInfra/*.class dirClient/commInfra
echo "Compressing execution environments."
echo "  General Repository of Information"
rm -f  dirGeneralRepos.zip
zip -rq dirGeneralRepos.zip dirGeneralRepos
echo "  Assault Party 0"
rm -f  dirAP0.zip
zip -rq dirAP0.zip dirAP0
echo "  Assault Party 1"
rm -f  dirAP1.zip
zip -rq dirAP1.zip dirAP1
echo "  Museum"
rm -f  dirMuseum.zip
zip -rq dirMuseum.zip dirMuseum
echo "  Collection Site"
rm -f  dirCollSite.zip
zip -rq dirCollSite.zip dirCollSite
echo "  Concentration Site"
rm -f  dirConcSite.zip
zip -rq dirConcSite.zip dirConcSite
echo "  Client"
rm -f  dirClient.zip
zip -rq dirClient.zip dirClient
echo "Deploying and decompressing execution environments."
mkdir -p /home/diogo/test/HeistToTheMuseum
rm -rf /home/diogo/test/HeistToTheMuseum/*
cp dirGeneralRepos.zip /home/diogo/test/HeistToTheMuseum
cp dirAP0.zip /home/diogo/test/HeistToTheMuseum
cp dirAP1.zip /home/diogo/test/HeistToTheMuseum
cp dirMuseum.zip /home/diogo/test/HeistToTheMuseum
cp dirCollSite.zip /home/diogo/test/HeistToTheMuseum
cp dirConcSite.zip /home/diogo/test/HeistToTheMuseum
cp dirClient.zip /home/diogo/test/HeistToTheMuseum
cd /home/diogo/test/HeistToTheMuseum
unzip -q dirGeneralRepos.zip
unzip -q dirAP0.zip
unzip -q dirAP1.zip
unzip -q dirMuseum.zip
unzip -q dirCollSite.zip
unzip -q dirConcSite.zip
unzip -q dirClient.zip
