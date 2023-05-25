echo "Compiling source code."
javac -source 1.8 -target 1.8 */*.java */*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "  RMI registry"
rm -rf dirRMIRegistry/interfaces
mkdir -p dirRMIRegistry/interfaces
cp interfaces/*.class dirRMIRegistry/interfaces
echo "  Register Remote Objects"
rm -rf dirRegistry/serverSide dirRegistry/interfaces
mkdir -p dirRegistry/serverSide dirRegistry/serverSide/main dirRegistry/serverSide/objects dirRegistry/interfaces
cp serverSide/main/ServerRegisterRemoteObject.class dirRegistry/serverSide/main
cp serverSide/objects/RegisterRemoteObject.class dirRegistry/serverSide/objects
cp interfaces/Register.class dirRegistry/interfaces
echo "  General Repository of Information"
rm -rf dirGeneralRepos/serverSide dirGeneralRepos/interfaces dirGeneralRepos/utils dirGeneralRepos/clientSide
mkdir -p dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/objects \
    dirGeneralRepos/interfaces \
    dirGeneralRepos/utils \
    dirGeneralRepos/clientSide/entities
cp serverSide/main/GeneralRepositoryMain.class dirGeneralRepos/serverSide/main
cp serverSide/objects/*.class dirGeneralRepos/serverSide/objects
cp interfaces/*.class dirGeneralRepos/interfaces
cp clientSide/entities/*.class dirGeneralRepos/clientSide/entities
cp utils/*.class dirGeneralRepos/utils
echo "  Assault Party 0"
rm -rf dirAP0/serverSide dirAP0/interfaces dirAP0/clientSide dirAP0/utils
mkdir -p dirAP0/serverSide/main dirAP0/serverSide/objects \
    dirAP0/interfaces \
    dirAP0/clientSide/entities \
    dirAP0/utils
cp serverSide/objects/*.class dirAP0/serverSide/objects
cp serverSide/main/AssaultParty0Main.class dirAP0/serverSide/main
cp interfaces/*.class dirAP0/interfaces
cp clientSide/entities/*.class dirAP0/clientSide/entities
cp utils/Constants.class utils/Room.class dirAP0/utils
echo "  Assault Party 1"
rm -rf dirAP1/serverSide dirAP1/interfaces dirAP1/clientSide dirAP1/utils
mkdir -p dirAP1/serverSide/main dirAP1/serverSide/objects \
    dirAP1/interfaces \
    dirAP1/clientSide/entities \
    dirAP1/utils
cp serverSide/objects/*.class dirAP1/serverSide/objects
cp serverSide/main/AssaultParty1Main.class dirAP1/serverSide/main
cp interfaces/*.class dirAP1/interfaces
cp clientSide/entities/*.class dirAP1/clientSide/entities
cp utils/Constants.class utils/Room.class dirAP1/utils
echo "  Museum"
rm -rf dirMuseum/serverSide dirMuseum/interfaces dirMuseum/clientSide dirMuseum/utils
mkdir -p dirMuseum/serverSide/main dirMuseum/serverSide/objects \
    dirMuseum/interfaces \
    dirMuseum/clientSide/entities \
    dirMuseum/utils
cp serverSide/objects/*.class dirMuseum/serverSide/objects
cp serverSide/main/MuseumMain.class dirMuseum/serverSide/main
cp interfaces/*.class dirMuseum/interfaces
cp clientSide/entities/OrdinaryThief.class dirMuseum/clientSide/entities
cp utils/Constants.class utils/Room.class dirMuseum/utils
echo "  Collection Site"
rm -rf dirCollSite/serverSide dirCollSite/interfaces dirCollSite/clientSide dirCollSite/utils
mkdir -p dirCollSite/serverSide/main dirCollSite/serverSide/objects \
    dirCollSite/interfaces \
    dirCollSite/clientSide/entities \
    dirCollSite/utils
cp serverSide/objects/*.class dirCollSite/serverSide/objects
cp serverSide/main/CollectionSiteMain.class dirCollSite/serverSide/main
cp interfaces/*.class dirCollSite/interfaces
cp clientSide/entities/*.class dirCollSite/clientSide/entities
cp utils/Constants.class dirCollSite/utils
echo "  Concentration Site"
rm -rf dirConcSite/serverSide dirConcSite/interfaces dirConcSite/clientSide dirConcSite/utils
mkdir -p dirConcSite/serverSide/main dirConcSite/serverSide/objects \
    dirConcSite/interfaces \
    dirConcSite/clientSide/entities \
    dirConcSite/utils
cp serverSide/objects/*.class dirConcSite/serverSide/objects
cp serverSide/main/ConcentrationSiteMain.class dirConcSite/serverSide/main
cp interfaces/*.class dirConcSite/interfaces
cp clientSide/entities/*.class dirConcSite/clientSide/entities
cp utils/Constants.class dirConcSite/utils
echo "  Client"
rm -rf dirClient/clientSide dirClient/utils dirClient/interfaces
mkdir -p dirClient/clientSide/main dirClient/clientSide/entities \
    dirClient/utils \
    dirClient/interfaces
cp clientSide/main/HeistToTheMuseum.class dirClient/clientSide/main
cp clientSide/entities/*.class dirClient/clientSide/entities
cp interfaces/*.class dirClient/interfaces
cp utils/Constants.class dirClient/utils
echo "Compressing execution environments."
echo "  RMI registry"
rm -f  dirRMIRegistry.zip
zip -rq dirRMIRegistry.zip dirRMIRegistry
echo "  Register Remote Objects"
rm -f  dirRegistry.zip
zip -rq dirRegistry.zip dirRegistry
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
