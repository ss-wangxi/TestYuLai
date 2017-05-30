// IRemoteService.aidl
package cc.snser.test.yulai.service;

import cc.snser.test.yulai.service.IRemoteCallback;

interface IRemoteService {

    void connect();
    void addCallback(IRemoteCallback callback);

}
