# FROM scratch
FROM universalrobots/ursim_e-series
# FROM ubuntu:18.04 
#CMD ["bash"]
ENV HOME=/sdk-1.13.0
RUN apt-get -y update \
&& apt-get install -y openjdk-8-jdk \
&& apt install -y software-properties-common \
&& apt install -y apt-transport-https \
&& apt-get install -y apt-utils \
&& apt-get install -y xdg-utils \
&& apt-get install -y nodejs \
&& apt-get install -y dialog \
&& apt-get install -y ant \
&& apt-get install -y curl \
&& apt-get install -y libcurl4 \
&& apt-get install -y unzip \
&& apt-get install -y python \
&& apt-get install -y maven \
&& apt-get clean \
&& rm -rf /var/lib/apt/lists/* \
&& curl https://s3-eu-west-1.amazonaws.com/ur-support-site/115819/sdk-1.13.0.zip -o sdk-1.13.0.zip \
&& unzip sdk-1.13.0.zip -d /sdk-1.13.0 \
&& rm sdk-1.13.0.zip \
&& cd /sdk-1.13.0/ \
&& mvn install:install-file -Dfile=artifacts/api/1.13.0/com.ur.urcap.api-1.13.0.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.13.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.13.0/com.ur.urcap.api-1.13.0-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.13.0 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.13.0/com.ur.urcap.api-1.13.0-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.13.0 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/archetype/com.ur.urcap.archetype-1.13.0.jar -DgroupId=com.ur.urcap -DartifactId=archetype -Dversion=1.13.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.13.0/com.ur.urcap.api-1.13.0.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.13.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.13.0/com.ur.urcap.api-1.13.0-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.13.0 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.13.0/com.ur.urcap.api-1.13.0-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.13.0 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/archetype/com.ur.urcap.archetype-1.13.0.jar -DgroupId=com.ur.urcap -DartifactId=archetype -Dversion=1.13.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.12.0/com.ur.urcap.api-1.12.0.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.12.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.12.0/com.ur.urcap.api-1.12.0-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.12.0 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.12.0/com.ur.urcap.api-1.12.0-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.12.0 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/api/1.11.0/com.ur.urcap.api-1.11.0.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.11.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.11.0/com.ur.urcap.api-1.11.0-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.11.0 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.11.0/com.ur.urcap.api-1.11.0-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.11.0 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/api/1.10.0/com.ur.urcap.api-1.10.0.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.10.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.10.0/com.ur.urcap.api-1.10.0-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.10.0 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.10.0/com.ur.urcap.api-1.10.0-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.10.0 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/api/1.9.0/com.ur.urcap.api-1.9.0.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.9.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.9.0/com.ur.urcap.api-1.9.0-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.9.0 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.9.0/com.ur.urcap.api-1.9.0-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.9.0 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/api/1.8.0/com.ur.urcap.api-1.8.0.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.8.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.8.0/com.ur.urcap.api-1.8.0-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.8.0 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.8.0/com.ur.urcap.api-1.8.0-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.8.0 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/api/1.7.0/com.ur.urcap.api-1.7.0.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.7.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.7.0/com.ur.urcap.api-1.7.0-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.7.0 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.7.0/com.ur.urcap.api-1.7.0-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.7.0 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/api/1.6.0/com.ur.urcap.api-1.6.0.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.6.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.6.0/com.ur.urcap.api-1.6.0-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.6.0 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.6.0/com.ur.urcap.api-1.6.0-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.6.0 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/api/1.5.0/com.ur.urcap.api-1.5.0.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.5.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.5.0/com.ur.urcap.api-1.5.0-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.5.0 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.5.0/com.ur.urcap.api-1.5.0-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.5.0 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/api/1.4.0/com.ur.urcap.api-1.4.0.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.4.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.4.0/com.ur.urcap.api-1.4.0-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.4.0 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.4.0/com.ur.urcap.api-1.4.0-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.4.0 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/api/1.3.0/com.ur.urcap.api-1.3.0.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.3.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.3.0/com.ur.urcap.api-1.3.0-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.3.0 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.3.0/com.ur.urcap.api-1.3.0-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.3.0 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/api/1.2.56/com.ur.urcap.api-1.2.56.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.2.56 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.2.56/com.ur.urcap.api-1.2.56-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.2.56 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.2.56/com.ur.urcap.api-1.2.56-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.2.56 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/api/1.1.0/com.ur.urcap.api-1.1.0-69.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.1.0-69 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.1.0/com.ur.urcap.api-1.1.0-69-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.1.0-69 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.1.0/com.ur.urcap.api-1.1.0-69-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.1.0-69 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/api/1.0.0/com.ur.urcap.api-1.0.0.30.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.0.0.30 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/api/1.0.0/com.ur.urcap.api-1.0.0.30-javadoc.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.0.0.30 -Dpackaging=jar -Dclassifier=javadoc -q \
&& mvn install:install-file -Dfile=artifacts/api/1.0.0/com.ur.urcap.api-1.0.0.30-sources.jar -DgroupId=com.ur.urcap -DartifactId=api -Dversion=1.0.0.30 -Dpackaging=jar -Dclassifier=sources -q \
&& mvn install:install-file -Dfile=artifacts/other/commons-httpclient-3.1.0.0.jar -DgroupId=commons-httpclient -DartifactId=commons-httpclient -Dversion=3.1.0.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/other/ws-commons-util-1.0.2.0.jar -DgroupId=org.apache.ws.commons.util -DartifactId=ws-commons-util -Dversion=1.0.2.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/other/xmlrpc-client-3.1.3.0.jar -DgroupId=org.apache.xmlrpc -DartifactId=xmlrpc-client -Dversion=3.1.3.0 -Dpackaging=jar -q \
&& mvn install:install-file -Dfile=artifacts/other/xmlrpc-common-3.1.3.0.jar -DgroupId=org.apache.xmlrpc -DartifactId=xmlrpc-common -Dversion=3.1.3.0 -Dpackaging=jar -q \
&& rm -rf samples \
&& rm -rf doc \
&& rm -rf urtool \
&& rm install.sh \
&& rm readme.txt
EXPOSE 8000
