package com.ulaiber.web.utils;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.ulaiber.web.model.PushInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;


/**
 * 个人用户消息推送
 * Created by daiqingwen on 2017/9/6.
 */
public class PushtoSingle {
    //采用"Java SDK 快速入门"， "第二步 获取访问凭证 "中获得的应用配置，用户可以自行替换
    private static final Logger logger = Logger.getLogger(PushtoSingle.class);
    private static String appId = "P2zOlClUiqAdQxyAqmp5d8";
    private static String appKey = "U8kOKaQJ6g7a1PbDLs55r";
    private static String masterSecret = "H7mg8I19q58MVUp1EXIeN2";
    private static String host = "http://sdk.open.api.igexin.com/apiex.htm";
    //static String CID = "";
    //别名推送方式
    // static String Alias = "";

    /**
     *
     * @param CID 个人用户clientid
     * @param type //推送待审批的记录 0，推送已审批的记录 1 ，推送交易信息 2，推送个人身份证上传反馈 3
     * @throws Exception
     */
    public static void singlePush(String CID,int type,String content,String title) throws Exception {
        IGtPush push = new IGtPush(host, appKey, masterSecret);
        //LinkTemplate template = linkTemplateDemo();
        TransmissionTemplate template = transmissionTemplateDemo(type,content,title);
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(CID);
        target.setAlias(appId);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            logger.info(">>>>>>>>>>>>>消息推送成功的类型为：" + type);
            System.out.println(ret.getResponse().toString());
        } else {
            logger.info(">>>>>>>>>>>>>服务器响应异常,消息推送失败的类型为：" + type);
            System.out.println(">>>>>>>>>>>>服务器响应异常,消息推送失败的类型为"+ type);
        }
    }
    public static LinkTemplate linkTemplateDemo() {
        LinkTemplate template = new LinkTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);

        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle("个人信息推送");
        style.setText("TestTestTestTestTestTestTestTest");
        // 配置通知栏图标
        style.setLogo("icon.png");
        // 配置通知栏网络图标
        style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);
        // 设置打开的网址地址
        template.setUrl("http://www.baidu.com");
        return template;
    }

    /**
     * 透传信息模板.对个人消息推送
     * @return
     */
    public static TransmissionTemplate transmissionTemplateDemo(int type,String content,String title) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        PushInfo pushInfo = new PushInfo();
        pushInfo.setType(type);
        pushInfo.setContent(content);
        pushInfo.setTitle(title);
        String msg = JSONObject.fromObject(pushInfo).toString();
        template.setTransmissionContent(msg);
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        return template;
    }


}
