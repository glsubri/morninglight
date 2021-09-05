# Morning Light
MorningLight est  initialement un travail de bachelor fait à la HEIG-VD. C'est un réveil simulateur d'aube style DIY, accompagné d'une application Android.

## Comment faire sa propre lampe simulateur d'aube ?

Le but de ce projet est de pouvoir transformer une lampe de chevet quelconque en réveil simulateur d'aube.

Trois étapes sont nécessaire pour réaliser ce projet:

1. Imprimer en 3D les fichiers fournis dans le dossier `impression_3d`. (_Ou les designer soi-même_.)
2. Compiler et uploader le code fourni dans le dossier `arduino` sur un Arduino Nano 33 BLE.
3. Compiler et uploader le code fourni dans le dossier `android` sur son smartphone Android.

### Système embarqué

Ce projet ne fonctionne actuellement qu'avec un [Arduino Nano 33 BLE](https://docs.arduino.cc/hardware/nano-33-ble). En plus de l'Arduino, il faut posséder un dimmer AC, disponible chez [RobotDyn](https://robotdyn.com/ac-light-dimmer-module-1-channel-3-3v-5v-logic-ac-50-60hz-220v-110v.html).

Afin de pouvoir compiler et uploader le code nécessaire au fonctionnement du système embarqué, il faut installer [PlatformIO](https://platformio.org/).

### Application mobile

Seuls les smartphones Android sont actuellement supportés. Pour compiler l'application Android, il faut utiliser la version 2020.3.1 Beta 4 d'Android Studio. Certaines dépendances ne sont pas actuelles, mais devraient être mise à jour prochainement.
