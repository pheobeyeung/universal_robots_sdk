# Universal Robots SDK
This is the Universal Robots SDK, integrated into a Visual Studio Code development container through Docker. This allows developers and users to develop and deploy URCaps more efficiently, without requiring a virtual machine. 

_**Prerequisites:** You must have [Git](https://git-scm.com/downloads), [Visual Studio Code](https://code.visualstudio.com/download), and [Docker](https://www.docker.com/products/docker-desktop/) installed and configured._


If you are using Windows, you need to perform these steps before proceeding:
1. Open Git Bash from the Start menu as adminstrator.
2. Run ```git config --system core.longpaths true``` in the terminal.
3. Ensure that your wsl engine is installed correctly and up to date.
   

## Setup
1. Open Docker Desktop to ensure that the program is running.
2. Install the [Remote Development extension pack](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.vscode-remote-extensionpack) for Visual Studio Code.
3. Open Visual Studio Code.
4. Type ```Ctrl+Shift+G``` to open source control. It should also be the third icon from the top on the left side of the screen.  
5. Enter ```https://github.com/pheobeyeung/universal_robots_sdk``` as the remote repository to clone, and pick a desired location to save onto your drive.
6. Open the cloned repository in VSCode, and reopen in Dev container when prompted. This may take several minutes to load. 
   _6a.  If the prompt does not automatically pop up, you can reopen the folder in a dev container by accessing the command palette through:_
    * ```Ctrl+Shift+P``` _on Windows/Linux_
    * ```Command+Shift+P``` _on Mac_
   _and search for_ **Dev Containers: Reopen in Container**
7. Type ```Ctrl+`+Shift``` to open the Terminal. Congrats! The SDK environment is now successfully setup!


## Development
The [Universal Robots API](https://www.universal-robots.com/articles/ur/urplus-resources/api-reference-docs/) provides insight to the capabilities of what you can do with a URCap. There are also PDF tutorials in the ```/universal_robots_sdk/doc``` folder as well.
URCap java swing examples can be found in the ```/universal_robots_sdk/samples/``` folder. 
The ```myURCaps/``` folder is referenced in the .gitignore file, meaning that the contents are not tracked by git. Put your own projects inside this directory to ensure that it is unaffected when pulling SDK updates.

#### New URCap
In order to create a new URCap, enter these commands:
```$ cd ~/universal_robots_sdk```
```$ ./newURCap.sh```

Fill out the appropriate forms and generate a new maven project. 

#### Build URCap
In order to build the URCap, navigate to the folder that contains the project folder. This folder should contain the pom.xml file. From there, edit the pom.xml file so that the line that asks for: 
```<ursim.home></ursim.home>``` on line 38 is replaced with 
```<ursim.home>/ursim</ursim.home>```
To build to URCap and deploy to URSim, run this command in the terminal:
```mvn install -P ursim``` 

The .urcap file can be found in the /target folder of the project folder.


To build URCap without deploying to URSim, run this command in the terminal: 
```mvn install```
The .urcap file can be found in the /target folder of the project folder.

#### URSim
To run URSim, run these commands in the terminal:
```$ cd /universal_robots_sdk```
```$ ./startURSim.sh```
If the simulator does not work, try to refresh the link that opens on the browser, or open this [link](http://localhost:6080/vnc.html?host=localhost&port=6080) manually to start the PolyScope simulator.




