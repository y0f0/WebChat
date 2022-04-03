javac com/nikitap/webchat/server/*.java
echo "Manifest-Version: 1.0
Created-By: Pologov Nikita
Main-Class: com.nikitap.webchat.server.Server

" > manifest.mf
jar cmf manifest.mf ../Server.jar com/nikitap/webchat/server/*.class
