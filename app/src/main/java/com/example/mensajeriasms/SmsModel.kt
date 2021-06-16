package com.example.mensajeriasms

class SmsModel(
        var _id: String,
        var _address: String,
        var _sms: String,
        var _readState: String,
        var _time: String,
        var _folderName: String
) {
    constructor(): this("","","","","","")

    override fun toString(): String {
        return "SmsModel(_id='$_id', _address='$_address', _sms='$_sms', _readState='$_readState', _time='$_time', _folderName='$_folderName')"
    }


}