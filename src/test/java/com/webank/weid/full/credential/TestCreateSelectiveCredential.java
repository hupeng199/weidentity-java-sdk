/*
 *       Copyright© (2018-2019) WeBank Co., Ltd.
 *
 *       This file is part of weidentity-java-sdk.
 *
 *       weidentity-java-sdk is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       weidentity-java-sdk is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with weidentity-java-sdk.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.webank.weid.full.credential;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webank.weid.common.BeanUtil;
import com.webank.weid.full.TestBaseServcie;
import com.webank.weid.full.TestBaseUtil;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.Credential;
import com.webank.weid.protocol.base.CredentialWrapper;
import com.webank.weid.protocol.request.CptMapArgs;
import com.webank.weid.protocol.request.CreateCredentialArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.util.JsonUtil;

/**
 * @author v_wbpenghu
 */
public class TestCreateSelectiveCredential extends TestBaseServcie {

    private static final Logger logger = LoggerFactory.getLogger(TestCreateSelectiveCredential.class);
    private static Credential credential = null;

    @Override
    public void testInit() {
        super.testInit();
        if (cptBaseInfo == null || credential == null) {
            cptBaseInfo = super.registerCpt(createWeIdResultWithSetAttr);
            CreateCredentialArgs createCredentialArgs =
                TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
            ResponseData<CredentialWrapper> response =
                credentialService.createCredential(createCredentialArgs);
            credential = response.getResult().getCredential();
        }
    }

    /**
     * ok
     */
    @Test
    public void TestCreateSelectiveCredential1() {

        Map<String, Integer> disclosureMap = new HashMap<>();
        disclosureMap.put("name", 1);
        disclosureMap.put("gender", 0);
        disclosureMap.put("age", 1);

        ResponseData<CredentialWrapper> responseData = credentialService
            .createSelectiveCredential(credential, JsonUtil.objToJsonStr(disclosureMap));
        BeanUtil.print(responseData);
        Assert.assertTrue(responseData.getErrorCode() == 0);
    }

    /**
     * ok
     *
     * @ todo 状态不能识别。 0,1,2
     */
    @Test
    public void TestCreateSelectiveCredential2() {

        Map<String, Integer> disclosureMap = new HashMap<>();
        disclosureMap.put("name", 2);
        disclosureMap.put("gender", 1);
        disclosureMap.put("age", 0);

        ResponseData<CredentialWrapper> responseData = credentialService
            .createSelectiveCredential(credential, JsonUtil.objToJsonStr(disclosureMap));
        BeanUtil.print(responseData);
    }

    /**
     * @ todo 状态为 1,1,2
     */
    @Test
    public void TestCreateSelectiveCredential3() {

        Map<String, Integer> disclosureMap = new HashMap<>();
        disclosureMap.put("name", 1);
        disclosureMap.put("gender", 1);
        disclosureMap.put("age", 2);

        ResponseData<CredentialWrapper> responseData = credentialService
            .createSelectiveCredential(credential, JsonUtil.objToJsonStr(disclosureMap));
        BeanUtil.print(responseData);
    }

    /**
     * @ todo 披露claim中不存在的字段。
     */
    @Test
    public void TestCreateSelectiveCredential4() {

        Map<String, Integer> disclosureMap = new HashMap<>();
        disclosureMap.put("name", 1);
        disclosureMap.put("gender", 1);
        disclosureMap.put("age", 0);
        disclosureMap.put("test", 1);

        ResponseData<CredentialWrapper> responseData = credentialService
            .createSelectiveCredential(credential, JsonUtil.objToJsonStr(disclosureMap));
        BeanUtil.print(responseData);
    }

    /**
     * @ todo disclosureMap中的字段少于claim中的字段。
     */
    @Test
    public void TestCreateSelectiveCredential5() {

        Map<String, Integer> disclosureMap = new HashMap<>();
        disclosureMap.put("name", 1);
        disclosureMap.put("gender", 1);

        ResponseData<CredentialWrapper> responseData = credentialService
            .createSelectiveCredential(credential, JsonUtil.objToJsonStr(disclosureMap));
        BeanUtil.print(responseData);
    }

    /**
     * @ todo 签名为null
     */
    @Test
    public void TestCreateSelectiveCredential6() {
        Credential newCredential = copyCredential(credential);
        newCredential.setSignature(null);
        Map<String, Integer> disclosureMap = new HashMap<>();
        disclosureMap.put("name", 1);
        disclosureMap.put("gender", 1);
        disclosureMap.put("age", 0);

        ResponseData<CredentialWrapper> responseData = credentialService
            .createSelectiveCredential(newCredential, JsonUtil.objToJsonStr(disclosureMap));
        BeanUtil.print(responseData);
    }

    /**
     * @ todo 篡改签名
     */
    @Test
    public void TestCreateSelectiveCredential7() {
        Credential newCredential = copyCredential(credential);
        newCredential.setSignature(newCredential.getSignature() + "x");
        Map<String, Integer> disclosureMap = new HashMap<>();
        disclosureMap.put("name", 1);
        disclosureMap.put("gender", 1);
        disclosureMap.put("age", 0);

        ResponseData<CredentialWrapper> responseData = credentialService
            .createSelectiveCredential(newCredential, JsonUtil.objToJsonStr(disclosureMap));
        BeanUtil.print(responseData);
    }

    /**
     * @ todo 篡改claim字段中的内容
     */
    @Test
    public void TestCreateSelectiveCredential8() {
        Credential newCredential = copyCredential(credential);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "test name");
        paramMap.put("age", "18");
        paramMap.put("gender", "女");

        newCredential.setClaim(paramMap);

        Map<String, Integer> disclosureMap = new HashMap<>();
        disclosureMap.put("name", 1);
        disclosureMap.put("gender", 1);
        disclosureMap.put("age", 0);

        ResponseData<CredentialWrapper> responseData = credentialService
            .createSelectiveCredential(newCredential, JsonUtil.objToJsonStr(disclosureMap));
        BeanUtil.print(responseData);
    }

    /**
     * @ todo 篡改Cpid，设置为null
     */
    @Test
    public void TestCreateSelectiveCredential9() {
        Credential newCredential = copyCredential(credential);
        newCredential.setCptId(null);
        Map<String, Integer> disclosureMap = new HashMap<>();
        disclosureMap.put("name", 1);
        disclosureMap.put("gender", 1);
        disclosureMap.put("age", 1);

        ResponseData<CredentialWrapper> responseData = credentialService
            .createSelectiveCredential(newCredential, JsonUtil.objToJsonStr(disclosureMap));
        BeanUtil.print(responseData);
    }

    /**
     * @ todo 篡改Cpid，设置为不存在的cptid
     */
    @Test
    public void TestCreateSelectiveCredential10() {
        Credential newCredential = copyCredential(credential);
        newCredential.setCptId(-1);
        Map<String, Integer> disclosureMap = new HashMap<>();
        disclosureMap.put("name", 1);
        disclosureMap.put("gender", 1);
        disclosureMap.put("age", 1);

        ResponseData<CredentialWrapper> responseData = credentialService
            .createSelectiveCredential(newCredential, JsonUtil.objToJsonStr(disclosureMap));
        BeanUtil.print(responseData);
    }

    /**
     * @ todo 两次披露，相同的字段，应该结果不同
     */
    @Test
    public void TestCreateSelectiveCredential11() {

        Credential newCredential = copyCredential(credential);
        Map<String, Integer> disclosureMap = new HashMap<>();
        disclosureMap.put("name", 0);
        disclosureMap.put("gender", 0);
        disclosureMap.put("age", 0);

        ResponseData<CredentialWrapper> responseData = credentialService
            .createSelectiveCredential(newCredential, JsonUtil.objToJsonStr(disclosureMap));

        CreateCredentialArgs createCredentialArgs = TestBaseUtil
            .buildCreateCredentialArgs(createWeIdNew);
        CptMapArgs cptMapArgs = TestBaseUtil.buildCptArgs(super.createWeIdWithSetAttr());
        CptBaseInfo cptBaseInfo = this.registerCpt(createWeIdNew, cptMapArgs);
        createCredentialArgs.setCptId(cptBaseInfo.getCptId());


        Credential credential = super.createCredential(createCredentialArgs).getCredential();

        ResponseData<CredentialWrapper> responseData1 = credentialService
            .createSelectiveCredential(credential, JsonUtil.objToJsonStr(disclosureMap));

        BeanUtil.print(responseData);
        BeanUtil.print(responseData1);
        Assert.assertNotEquals(responseData.getResult().getCredential().getClaim().get("name"),responseData1.getResult().getCredential().getClaim().get("name"));
    }
}
