package com.github.bg.admin.core.config.socketio.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.github.bg.admin.core.config.socketio.PushMessage;
import com.github.bg.admin.core.config.socketio.SocketIoService;
import com.github.bg.admin.core.util.RedisCache;
import com.github.bg.admin.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linzf
 * @since 2019-06-13
 * 类描述：socket的实现类
 */
@Service(value = "socketIOService")
public class SocketIoServiceImpl implements SocketIoService {

    private static Logger log = LoggerFactory.getLogger(SocketIoServiceImpl.class);

    /**
     * 用来存已连接的客户端
     */
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;
    @Autowired
    private RedisCache redisCache;

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     *
     * @throws Exception
     */
    @PostConstruct
    private void autoStartup() throws Exception {
        start();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     *
     * @throws Exception
     */
    @PreDestroy
    private void autoStop() throws Exception {
        stop();
    }

    /**
     * 功能描述：启动socket服务
     */
    @Override
    public void start() {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            List<String> socketTokens = getParamsByClient(client).get("socketToken");
            if (socketTokens.size() == 0) {
                log.debug("socket连接失败，失败原因：socketToken的值不能为空！");
                return;
            }
            List<String> refreshTokens = getParamsByClient(client).get("refreshToken");
            if (refreshTokens.size() == 0) {
                log.debug("socket连接失败，失败原因：refreshToken的值不能为空！");
                return;
            }
            String refreshToken = refreshTokens.get(0);
            String powerPath = redisCache.getString(refreshToken);
            if (powerPath == null || "".equals(powerPath)) {
                log.debug("socket连接失败，失败原因：无此token的用户权限信息！");
                return;
            }
            String socketToken = socketTokens.get(0);
            if (socketToken != null && !"".equals(socketToken)) {
                clientMap.put(socketToken, client);
                /**
                 * 防止不断刷新页面导致不断的增加socket的连接
                 */
                String socketTokenOld = redisCache.getString(refreshToken + "_SOCKET");
                if (socketTokenOld != null && !"".equals(socketTokenOld)) {
                    SocketIOClient socketIOClient = clientMap.get(socketTokenOld);
                    if(socketIOClient!=null){
                        clientMap.get(socketTokenOld).disconnect();
                        clientMap.remove(socketTokenOld);
                    }
                }
                log.debug("当前保持连接的socket的数为{}", clientMap.size());
                /**
                 * 与socket连接成功，将用户和socket的绑定关系保存到redis中
                 */
                redisCache.setString(refreshToken + "_SOCKET", socketToken);
            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            List<String> socketTokens = getParamsByClient(client).get("socketToken");
            if (socketTokens.size() == 0) {
                log.debug("socket连接失败，失败原因：socketToken的值不能为空！");
                return;
            }
            String socketToken = socketTokens.get(0);
            if (socketToken != null && !"".equals(socketToken)) {
                clientMap.remove(socketToken);
                client.disconnect();
            }
        });

        // 处理自定义的事件，与连接监听类似
        socketIOServer.addEventListener(PUSH_EVENT, PushMessage.class, (client, data, ackSender) -> {
            // TODO do something
        });
        socketIOServer.start();
    }

    /**
     * 功能描述：关闭socket服务
     */
    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    /**
     * 功能描述：实现消息的发送
     *
     * @param loginAccount 需要发送到的账号
     * @param content      需要发送的内容
     */
    @Override
    public void pushMessage(String loginAccount, String content) {
        /**
         * 功能描述：获取当前在线的用户的token和refreshToken
         */
        List<String> loginTokens = redisCache.queryKeys(loginAccount);
        for (String loginToken : loginTokens) {
            if (!"".equals(loginToken)) {
                // 获取当前的refreshToken的值
                String loginRefreshTokenValue = loginToken.split(":")[1];
                // 通过refreshToken来获取socketToken的值
                String socketToken = redisCache.getString(loginRefreshTokenValue + "_SOCKET");
                if (StringUtils.isNotEmpty(socketToken)) {
                    SocketIOClient client = clientMap.get(socketToken);
                    if (client != null) {
                        client.sendEvent(PUSH_EVENT, new PushMessage(socketToken, content));
                    }
                }
            }
        }

    }

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     *
     * @param client
     * @return
     */
    private Map<String, List<String>> getParamsByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        return params;
    }
}
