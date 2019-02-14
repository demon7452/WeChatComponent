package com.xiong.wechat.lib.official;

import com.google.common.collect.Maps;
import com.xiong.wechat.lib.properties.WeChatProperty;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class CryptUtil {

    @Resource
    private WeChatProperty weChatProperty;


    public Map<String, String> decryptCallbackMessage(InputStream inputStream){
        Map<String, String> map = Maps.newHashMap();

        InputSource inputSource = new InputSource(inputStream);
        SAXReader reader = new SAXReader();

        try {
            Document document = reader.read(inputStream);

            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();

            for (Element e : elementList) {
                map.put(e.getName(), e.getText());
            }

            inputStream.close();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }

        return map;
    }
}
