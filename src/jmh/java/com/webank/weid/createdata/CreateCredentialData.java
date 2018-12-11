package com.webank.weid.createdata;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.webank.weid.base.JmhBase;
import com.webank.weid.base.JmhUtil;
import com.webank.weid.full.TestBaseUtil;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.request.CreateCredentialArgs;
import com.webank.weid.protocol.request.RegisterCptArgs;
import com.webank.weid.protocol.request.SetAuthenticationArgs;
import com.webank.weid.protocol.request.SetPublicKeyArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class CreateCredentialData extends JmhBase {

    private List<CreateCredentialArgs> createCredentialArgsList = new ArrayList<>();
    private int index = 0;
    private static int maxSize = 1000;
    private int initThreadNum = 10;

    @Test
    public void initstart() {
        JmhUtil util = new JmhUtil();
        System.out.println("init data start,current block:" + util.getBlockNumber());
//        initCreateCredentialData();
//        System.out.println("init data end,current block:" + util.getBlockNumber());
    }

    public void initCreateCredentialData() {
        for (int i = 0; i <= initThreadNum; i++) {
            new Thread(() -> {
                while (createCredentialArgsList.size() <= maxSize) {
                    CreateWeIdDataResult createWeIdResult = weIdService.createWeId().getResult();
                    try {
                        SetAuthenticationArgs setAuthenticationArgs = TestBaseUtil
                            .buildSetAuthenticationArgs(createWeIdResult);
                        weIdService.setAuthentication(setAuthenticationArgs);
                        SetPublicKeyArgs setPublicKeyArgs = TestBaseUtil
                            .buildSetPublicKeyArgs(createWeIdResult);
                        weIdService.setPublicKey(setPublicKeyArgs);

                        RegisterCptArgs registerCptArgs = TestBaseUtil
                            .buildRegisterCptArgs(createWeIdResult);
                        ResponseData<CptBaseInfo> response = cptService
                            .registerCpt(registerCptArgs);

                        CptBaseInfo cptBaseInfo = response.getResult();

                        CreateCredentialArgs createCredentialArgs =
                            TestBaseUtil.buildCreateCredentialArgs(createWeIdResult, cptBaseInfo);
                        credentialService.createCredential(createCredentialArgs);

                        createCredentialArgsList.add(createCredentialArgs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        try {
            while (createCredentialArgsList.size() <= maxSize) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            FileWriter file = new FileWriter("E:\\createCredentialData.txt");
            ObjectMapper mapper = new ObjectMapper();
            String s = mapper.writeValueAsString(createCredentialArgsList);
            file.write(s);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
