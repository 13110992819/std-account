package com.std.account.bo;

import com.std.account.domain.CallbackResult;

public interface IAlipayBO {
    public CallbackResult doCallbackAPP(String result);

    public void doBizCallback(CallbackResult callbackResult);
}
