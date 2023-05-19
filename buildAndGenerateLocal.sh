echo "Compiling source code."
javac */*.java */*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "  General Repository of Information"
rm -rf dirGeneralRepos
mkdir -p dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/objects \
    dirGeneralRepos/interfaces \
    dirGeneralRepos/utils \
    dirGeneralRepos/clientSide/entities
cp serverSide/main/GeneralRepositoryMain.class dirGeneralRepos/serverSide/main
cp serverSide/objects/GeneralRepository.class dirGeneralRepos/serverSide/objects
cp interfaces/*.class dirGeneralRepos/interfaces
cp clientSide/entities/*.class dirGeneralRepos/clientSide/entities
cp utils/*.class dirGeneralRepos/utils
echo "  Assault Party 0"
rm -rf dirAP0
mkdir -p dirAP0/serverSide/main dirAP0/serverSide/objects \
    dirAP0/interfaces \
    dirAP0/clientSide/entities \
    dirAP0/utils
cp serverSide/objects/AssaultParty*.class dirAP0/serverSide/objects
cp serverSide/main/AssaultParty0Main.class dirAP0/serverSide/main
cp interfaces/*.class dirAP0/interfaces
cp clientSide/entities/*.class dirAP0/clientSide/entities
cp utils/Constants.class utils/Room.class dirAP0/utils
echo "  Assault Party 1"
rm -rf dirAP1
mkdir -p dirAP1/serverSide/main dirAP1/serverSide/objects \
    dirAP1/interfaces \
    dirAP1/clientSide/entities \
    dirAP1/utils
cp serverSide/objects/AssaultParty*.class dirAP1/serverSide/objects
cp serverSide/main/AssaultParty1Main.class dirAP1/serverSide/main
cp interfaces/*.class dirAP1/interfaces
cp clientSide/entities/*.class dirAP1/clientSide/entities
cp utils/Constants.class utils/Room.class dirAP1/utils
echo "  Museum"
rm -rf dirMuseum
mkdir -p dirMuseum/serverSide/main dirMuseum/serverSide/objects \
    dirMuseum/interfaces \
    dirMuseum/clientSide/entities \
    dirMuseum/utils
cp serverSide/objects/Museum.class dirMuseum/serverSide/objects
cp serverSide/main/MuseumMain.class dirMuseum/serverSide/main
cp interfaces/*.class dirMuseum/interfaces
cp clientSide/entities/OrdinaryThief.class dirMuseum/clientSide/entities
cp utils/Constants.class utils/Room.class dirMuseum/utils
echo "  Collection Site"
rm -rf dirCollSite
mkdir -p dirCollSite/serverSide/main dirCollSite/serverSide/objects \
    dirCollSite/interfaces \
    dirCollSite/clientSide/entities \
    dirCollSite/utils
cp serverSide/objects/CollectionSite.class dirCollSite/serverSide/objects
cp serverSide/main/CollectionSiteMain.class dirCollSite/serverSide/main
cp interfaces/*.class dirCollSite/interfaces
cp clientSide/entities/*.class dirCollSite/clientSide/entities
cp utils/Constants.class dirCollSite/utils
echo "  Concentration Site"
rm -rf dirConcSite
mkdir -p dirConcSite/serverSide/main dirConcSite/serverSide/objects \
    dirConcSite/interfaces \
    dirConcSite/clientSide/entities \
    dirConcSite/utils
cp serverSide/objects/ConcentrationSite.class dirConcSite/serverSide/objects
cp serverSide/main/ConcentrationSiteMain.class dirConcSite/serverSide/main
cp interfaces/*.class dirConcSite/interfaces
cp clientSide/entities/*.class dirConcSite/clientSide/entities
cp utils/Constants.class dirConcSite/utils
echo "  Client"
rm -rf dirClient
mkdir -p dirClient/clientSide/main dirClient/clientSide/entities \
    dirClient/utils \
    dirClient/interfaces
cp clientSide/main/HeistToTheMuseum.class dirClient/clientSide/main
cp clientSide/entities/*.class dirClient/clientSide/entities
cp interfaces/*.class dirClient/interfaces
cp utils/Constants.class dirClient/utils
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
