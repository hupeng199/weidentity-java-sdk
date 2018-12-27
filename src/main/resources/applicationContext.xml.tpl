<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~       Copyright© (2018) WeBank Co., Ltd.
  ~
  ~       This file is part of weidentity-java-sdk.
  ~
  ~       weidentity-java-sdk is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU Lesser General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       (at your option) any later version.
  ~
  ~       weidentity-java-sdk is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU Lesser General Public License for more details.
  ~
  ~       You should have received a copy of the GNU Lesser General Public License
  ~       along with weidentity-java-sdk.  If not, see <https://www.gnu.org/licenses/>.
  -->

<beans xmlns:context="http://www.springframework.org/schema/context"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns="http://www.springframework.org/schema/beans"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context-3.0.xsd">

<<<<<<< HEAD:src/main/resources/applicationContext.xml
    <bean class="org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer"
        id="appConfig">
        <property name="properties">
            <props>
                <prop key="weId.contractaddress">0x5c67b33a0153758c477cb4bf4831bd4f628054ae</prop>
                <prop key="cpt.contractaddress">0x71423e47965c4cdca9d7243b3f5af0813b943299</prop>
                <prop key="issuer.contractaddress">0x0aeb3a5479da26f0435d24ce757f3f29ec2ec8e1</prop>
            </props>
        </property>
        <!--  <property name="location" value="classpath:application.properties" />-->
    </bean>
=======
  <bean class="org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer"
    id="appConfig">
    <property name="properties">
      <props>
        <prop key="weId.contractaddress">${WEID_ADDRESS}</prop>
        <prop key="cpt.contractaddress">${CPT_ADDRESS}</prop>
        <prop key="issuer.contractaddress">${ISSUER_ADDRESS}</prop>
      </props>
    </property>
    <!--  <property name="location" value="classpath:application.properties" />-->
  </bean>
>>>>>>> f04857b770999ae164494536f8e7eab508ee1743:src/main/resources/applicationContext.xml.tpl

    <bean class="org.bcos.web3j.utils.Async" id="async">
        <constructor-arg ref="pool"
            type="org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor"/>
    </bean>
    <bean class="org.bcos.web3j.utils.AttemptsConf" id="attemptsConf">
        <constructor-arg index="0" value="1200"/>
        <constructor-arg index="1" value="60"/>
    </bean>
    <bean class="org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor" id="pool">
        <property name="corePoolSize" value="150"/>
        <property name="maxPoolSize" value="4000"/>
        <property name="queueCapacity" value="20"/>
        <property name="keepAliveSeconds" value="60"/>
        <property name="rejectedExecutionHandler">
            <bean class="java.util.concurrent.ThreadPoolExecutor.AbortPolicy"/>
        </property>
    </bean>

    <context:component-scan base-package="com.webank.weid.config"/>

    <bean class="org.bcos.contract.tools.ToolConf" id="toolConf">
        <property name="systemProxyAddress" value="0xe60bb7a90cd17363b53dac55a2688125f0dc340d"/>
        <property name="privKey"
            value="bcec428d5205abe0f0cc8a734083908d9eb8563e31f943d760786edf42ad67dd"/>
        <property name="account" value="0x11508df7183e92e550b5382f2b47208f809b1eba"/>
        <property name="outPutpath" value="./output/"/>
    </bean>

<<<<<<< HEAD:src/main/resources/applicationContext.xml
    <bean class="org.bcos.channel.proxy.Server" id="server">
    </bean>
    <bean class="org.bcos.channel.client.Service" id="channelService">
        <property name="orgID" value="WB"/>
        <property name="connectSeconds" value="10"/>
        <property name="connectSleepPerMillis" value="10"/>
        <property name="allChannelConnections">
            <map>
                <entry key="WB">
                    <bean class="org.bcos.channel.handler.ChannelConnections">
                        <property name="caCertPath" value="classpath:ca.crt"/>
                        <property name="clientKeystorePath" value="classpath:client.keystore"/>
                        <property name="keystorePassWord" value="123456"/>
                        <property name="clientCertPassWord" value="123456"/>
                        <property name="connectionsStr">
                            <list>
                                <value>WeIdentity@192.168.74.130:8003</value>
                            </list>
                        </property>
                    </bean>
                </entry>
            </map>
        </property>
    </bean>
=======
  <bean class="org.bcos.channel.proxy.Server" id="server">
  </bean>
  <bean class="org.bcos.channel.client.Service" id="channelService">
    <property name="orgID" value="WB"/>
    <property name="connectSeconds" value="10"/>
    <property name="connectSleepPerMillis" value="10"/>
    <property name="allChannelConnections">
      <map>
        <entry key="WB">
          <bean class="org.bcos.channel.handler.ChannelConnections">
            <property name="caCertPath" value="classpath:ca.crt"/>
            <property name="clientKeystorePath" value="classpath:client.keystore"/>
            <property name="keystorePassWord" value="123456"/>
            <property name="clientCertPassWord" value="123456"/>
            <property name="connectionsStr">
              <list>
                ${BLOCKCHIAN_NODE_INFO}
              </list>
            </property>
          </bean>
        </entry>
      </map>
    </property>
  </bean>
>>>>>>> f04857b770999ae164494536f8e7eab508ee1743:src/main/resources/applicationContext.xml.tpl
</beans>
