1.配置SSL安全协议
	使用Java创建SSL证书：在命令行执行keytool -genkey -alias tomcat -keyalg RSA
	输入密码(tomcat)、信息，最终生成.keystore文件。生成文件位于C:\Users\用户名跟目录下
2. 配置 Tomcat 以使用 keystore 文件
	打开 server.xml 找到下面被注释的这段
	<!--
	<Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
	    maxThreads="150" scheme="https" secure="true"
	    clientAuth="false" sslProtocol="TLS" />
	-->
	干掉注释，并将内容改为
	<Connector port="443" protocol="HTTP/1.1" SSLEnabled="true"
        maxThreads="150" scheme="https" secure="true"
        clientAuth="false" sslProtocol="TLS"
        keystoreFile="/Users/用户名/.keystore" keystorePass="tomcat" />
3.如果是MAVEN的TOMCAT插件，则加入如下配置
	<plugins>
        <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.0</version>
            <configuration>
                <httpsPort>8443</httpsPort>
                <keystorePass>tomcat</keystorePass>
                <keystoreFile>C:\Users\T-rency\.keystore</keystoreFile>
            </configuration>
        </plugin>
    </plugins>
4.在struts.xml配置文件中找到httpPort、httpsPort参数，配置程序的访问端口