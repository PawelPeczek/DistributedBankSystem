//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.2
//
// <auto-generated>
//
// Generated from file `bank-interface.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package Bank;

public class LoginResponse extends com.zeroc.Ice.Value
{
    public LoginResponse()
    {
        this.accountType = AccountType.STANDARD;
        this.secretToken = "";
    }

    public LoginResponse(BankAccountPrx account, AccountType accountType, String secretToken)
    {
        this.account = account;
        this.accountType = accountType;
        this.secretToken = secretToken;
    }

    public BankAccountPrx account;

    public AccountType accountType;

    public String secretToken;

    public LoginResponse clone()
    {
        return (LoginResponse)super.clone();
    }

    public static String ice_staticId()
    {
        return "::Bank::LoginResponse";
    }

    @Override
    public String ice_id()
    {
        return ice_staticId();
    }

    /** @hidden */
    public static final long serialVersionUID = 5328645586201211141L;

    /** @hidden */
    @Override
    protected void _iceWriteImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice(ice_staticId(), -1, true);
        ostr_.writeProxy(account);
        AccountType.ice_write(ostr_, accountType);
        ostr_.writeString(secretToken);
        ostr_.endSlice();
    }

    /** @hidden */
    @Override
    protected void _iceReadImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        account = BankAccountPrx.uncheckedCast(istr_.readProxy());
        accountType = AccountType.ice_read(istr_);
        secretToken = istr_.readString();
        istr_.endSlice();
    }
}
