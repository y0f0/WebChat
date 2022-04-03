javac com/nikitap/webchat/client/*.java
echo "Manifest-Version: 1.0
Created-By: Pologov Nikita
Main-Class: com.nikitap.webchat.client.Client

" > manifest.mf
jar cmf manifest.mf ../Client.jar com/nikitap/webchat/client/*.class
