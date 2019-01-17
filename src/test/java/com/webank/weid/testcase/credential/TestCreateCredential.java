/*
 *       Copyright© (2018) WeBank Co., Ltd.
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

package com.webank.weid.testcase.credential;

import com.webank.weid.constant.ErrorCode;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.Credential;
import com.webank.weid.protocol.request.CreateCredentialArgs;
import com.webank.weid.protocol.request.RegisterCptArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.testcase.TestBaseServcie;
import com.webank.weid.utils.BeanUtil;
import com.webank.weid.utils.TestBaseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * createCredential method for testing CredentialService.
 *
 * @author v_wbgyang
 */
@Test(groups = "all")
public class TestCreateCredential extends TestBaseServcie {

    private static final Logger logger = LoggerFactory.getLogger(TestCreateCredential.class);
    private static CptBaseInfo cptBaseInfo = null;
    private static CreateWeIdDataResult createWeIdResultWithSetAttr = null;
    private static RegisterCptArgs registerCptArgs = null;

    @Test(groups = "ignore")
    public synchronized void testInit() {
        super.testInit();
        if (null == this.createWeIdResultWithSetAttr) {
            this.createWeIdResultWithSetAttr = super.createWeIdWithSetAttr();
        }
        if (null == this.cptBaseInfo || null == this.registerCptArgs) {
            this.registerCptArgs = TestBaseUtil.buildRegisterCptArgs(this.createWeIdResultWithSetAttr);
            this.cptBaseInfo = super.registerCpt(this.createWeIdResultWithSetAttr, this.registerCptArgs);
        }
    }

    /**
     * case：createCredential success.
     */
    @Test
    public void testCreateCredentialCase1() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.SUCCESS.getCode(), response.getErrorCode().intValue());
        Assert.assertNotNull(response.getResult());
    }

    /**
     * case: createCredentialArgs is null.
     */
    @Test(groups = "testCreateCredential")
    public void testCreateCredentialCase2() {

        ResponseData<Credential> response = credentialService.createCredential(null);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.ILLEGAL_INPUT.getCode(), response.getErrorCode().intValue());
        Assert.assertNull(response.getResult());
    }

    /**
     * case：cptId is null.
     */
    public void testCreateCredentialCase3() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.setCptId(null);

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.CREDENTIAL_CPT_NOT_EXISTS.getCode(),
            response.getErrorCode().intValue());
        Assert.assertNull(response.getResult());
    }

    /**
     * case： cptId is minus number.
     */
    @Test
    public void testCreateCredentialCase4() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.setCptId(-1);

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.SUCCESS.getCode(), response.getErrorCode().intValue());
        Assert.assertNotNull(response.getResult());
    }

    /**
     * case： cptId is not exists.
     */
    @Test
    public void testCreateCredentialCase5() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.setCptId(100000);

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.SUCCESS.getCode(), response.getErrorCode().intValue());
        Assert.assertNotNull(response.getResult());
    }

    /**
     * case： cptId is belongs to others weIdentity dId.
     */
    @Test
    public void testCreateCredentialCase6() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);

        CptBaseInfo cptBaseInfoNew = super.registerCpt(createWeIdNew);
        createCredentialArgs.setCptId(cptBaseInfoNew.getCptId());

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.SUCCESS.getCode(), response.getErrorCode().intValue());
        Assert.assertNotNull(response.getResult());
    }

    /**
     * case： issuer is null.
     */
    @Test
    public void testCreateCredentialCase7() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.setIssuer(null);

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.CREDENTIAL_ISSUER_INVALID.getCode(),
            response.getErrorCode().intValue());
        Assert.assertNull(response.getResult());
    }

    /**
     * case： issuer is invalid.
     */
    @Test
    public void testCreateCredentialCase8() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.setIssuer("di:weid:0x1111111111");

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.CREDENTIAL_ISSUER_INVALID.getCode(),
            response.getErrorCode().intValue());
        Assert.assertNull(response.getResult());
    }

    /**
     * case： issuer is not exists.
     */
    @Test
    public void testCreateCredentialCase9() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.setIssuer("did:weid:0x1111111111");

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.SUCCESS.getCode(), response.getErrorCode().intValue());
        Assert.assertNotNull(response.getResult());
    }

    /**
     * case： expirationDate <= 0.
     */
    @Test
    public void testCreateCredentialCase10() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.setExpirationDate(0L);

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.CREDENTIAL_EXPIRE_DATE_ILLEGAL.getCode(),
            response.getErrorCode().intValue());
        Assert.assertNull(response.getResult());
    }

    /**
     * case： expirationDate <= now.
     */
    @Test
    public void testCreateCredentialCase11() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.setExpirationDate(System.currentTimeMillis() - 1000000);

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.SUCCESS.getCode(), response.getErrorCode().intValue());
        Assert.assertNotNull(response.getResult());
    }

    /**
     * case： claim is null.
     */
    @Test
    public void testCreateCredentialCase12() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.setClaim(null);

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.CREDENTIAL_CLAIM_NOT_EXISTS.getCode(),
            response.getErrorCode().intValue());
        Assert.assertNull(response.getResult());
    }

    /**
     * case： claim is xxxxxxx.
     */
    @Test
    public void testCreateCredentialCase13() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.setClaim("xxxxxxxxxxxxxx");

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.SUCCESS.getCode(), response.getErrorCode().intValue());
        Assert.assertNotNull(response.getResult());
    }

    /**
     * case： weIdPrivateKey is null.
     */
    @Test
    public void testCreateCredentialCase14() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.setWeIdPrivateKey(null);

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.CREDENTIAL_ERROR.getCode(),
            response.getErrorCode().intValue());
        Assert.assertNull(response.getResult());
    }

    /**
     * case： privateKey is null.
     */
    @Test
    public void testCreateCredentialCase15() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.getWeIdPrivateKey().setPrivateKey(null);

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.CREDENTIAL_PRIVATE_KEY_NOT_EXISTS.getCode(),
            response.getErrorCode().intValue());
        Assert.assertNull(response.getResult());
    }

    /**
     * case： privateKey is xxxxxxxxxxx.
     */
    @Test
    public void testCreateCredentialCase16() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.getWeIdPrivateKey().setPrivateKey("xxxxxxxxxx");

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.CREDENTIAL_ERROR.getCode(),
            response.getErrorCode().intValue());
        Assert.assertNull(response.getResult());
    }

    /**
     * case： privateKey is 11111111111111.
     */
    @Test
    public void testCreateCredentialCase17() {

        CreateCredentialArgs createCredentialArgs =
            TestBaseUtil.buildCreateCredentialArgs(createWeIdResultWithSetAttr, cptBaseInfo);
        createCredentialArgs.getWeIdPrivateKey().setPrivateKey("11111111111111");

        ResponseData<Credential> response =
            credentialService.createCredential(createCredentialArgs);
        logger.info("createCredential result:");
        BeanUtil.print(response);

        Assert.assertEquals(ErrorCode.SUCCESS.getCode(), response.getErrorCode().intValue());
        Assert.assertNotNull(response.getResult());
    }
}
