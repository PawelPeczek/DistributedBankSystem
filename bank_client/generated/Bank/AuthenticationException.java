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

public class AuthenticationException extends com.zeroc.Ice.UserException
{
    public AuthenticationException()
    {
        this.reason = "Invalid token";
    }

    public AuthenticationException(Throwable cause)
    {
        super(cause);
        this.reason = "Invalid token";
    }

    public AuthenticationException(String reason)
    {
        this.reason = reason;
    }

    public AuthenticationException(String reason, Throwable cause)
    {
        super(cause);
        this.reason = reason;
    }

    public String ice_id()
    {
        return "::Bank::AuthenticationException";
    }

    public String reason;

    /** @hidden */
    @Override
    protected void _writeImpl(com.zeroc.Ice.OutputStream ostr_)
    {
        ostr_.startSlice("::Bank::AuthenticationException", -1, true);
        ostr_.writeString(reason);
        ostr_.endSlice();
    }

    /** @hidden */
    @Override
    protected void _readImpl(com.zeroc.Ice.InputStream istr_)
    {
        istr_.startSlice();
        reason = istr_.readString();
        istr_.endSlice();
    }

    /** @hidden */
    public static final long serialVersionUID = -7658018572990129127L;
}
