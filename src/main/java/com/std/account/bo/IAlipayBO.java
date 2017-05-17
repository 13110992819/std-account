package com.std.account.bo;

import com.std.account.domain.CallbackResult;

public interface IAlipayBO {

    public void doBizCallback(CallbackResult callbackResult);
}
