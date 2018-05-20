package com.quora.service;

import org.apache.ibatis.ognl.enhance.ContextClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤 - Trie树/单词查找树/字典树
 *
 * @author: leon
 * @date: 2018/5/4 10:38
 * @version: 1.0
 */
@Service
public class SensitiveWordsService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveWordsService.class);

    //构造前缀树Tries

    //前缀树节点
    private class TrieNode {

        private boolean isEnd = false; //是否为某一个敏感词的结尾

        //当前节点下所有的子节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }


        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        //是否为该敏感词的结尾
        boolean isKeyWordEnd() {
            return isEnd;
        }

        void setKeyWordEnd(boolean isEnd) {
            this.isEnd = isEnd;
        }

    }

    //初始化Bean, 读取敏感词文本, 这里按行读取, 敏感词按行分隔
    //该方法最终构造完整前缀树Trie
    @Override
    public void afterPropertiesSet() throws Exception {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            is = ContextClassLoader.getSystemResourceAsStream("SensitiveWords.txt");
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String linetxt;
            while ((linetxt = br.readLine()) != null) {
                //System.out.println(linetxt);
                addSensitiveWord(linetxt.trim());
            }
        } catch (Exception e) {
            logger.error("读取敏感词文本错误: " + e.getMessage());
        } finally {
            if (br != null) br.close();
            if (isr != null) isr.close();
            if (is != null) is.close();
        }

    }

    private TrieNode root = new TrieNode(); //构造的前缀树的根节点

    /**
     * 向前缀树加入一个敏感词, 到达敏感词最后一个字符时, 将该前缀树节点中isEnd = true
     * @param lineTxt 敏感词
     */
    private void addSensitiveWord(String lineTxt) {
        TrieNode tempNode = root;
        for (int i = 0; i < lineTxt.length(); ++i) {
            Character ch = lineTxt.charAt(i);
            if (isSymbol(ch)) {
                continue;
            }

            TrieNode node = tempNode.getSubNode(ch);
            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(ch, node);
            }
            tempNode = node; //当前节点指向下一节点
            if (i == lineTxt.length() - 1)
                tempNode.setKeyWordEnd(true); //该节点为敏感词结尾
        }
    }




    /**
     * 利用前缀树对输入文本进行过滤(核心)
     * @param input 输入文本
     * @return 过滤后的文本
     *//*
    public String filter(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }

        StringBuilder res = new StringBuilder();
        String replacement = "***"; //敏感词替代
        TrieNode tempNode = root; //前缀树指针
        int begin = 0; //指针3
        int cur = 0; //指针2 - 移动到结尾则标志结束
        while (cur < input.length()) {
            char ch = input.charAt(cur);

            //比如这样的也能被过滤, *暴๑乛◡乛๑力*
            if (isSymbol(ch)) {
                //非法字符, 不进行过滤
                if (tempNode == root) {
                    res.append(ch);
                    begin++;
                }
                cur++;
                continue;
            }
            tempNode = tempNode.getSubNode(ch);

            if (tempNode == null) {
                //如果前缀树下个节点为空
                res.append(input.charAt(begin));
                cur = begin+1;
                begin = cur;
                tempNode = root;
            } else if (tempNode.isKeyWordEnd()) {
                //前缀树下一个节点不为空, 且找到敏感词
                res.append(replacement);
                cur++;
                begin = cur;
                tempNode = root;
            } else {
                //前缀树下一个节点不为空, 且 不是敏感词
                cur++;
            }
        }
        res.append(input.substring(begin));
        return res.toString();

    }*/

    /**
     * 利用前缀树对输入文本进行过滤(核心)
     * @param input 输入文本
     * @return 过滤后的文本
     */
    public String filter(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }

        StringBuilder res = new StringBuilder();
        String replacement = "***"; //敏感词替代
        TrieNode tempNode = root; //前缀树指针
        int begin = 0; //指针2- 移动到结尾则标志结束
        int cur = 0; //指针3
        while (begin < input.length()) {
            char ch = input.charAt(cur);

            //比如这样的也能被过滤, *暴๑乛◡乛๑力*
            if (isSymbol(ch)) {
                //非法字符, 不进行过滤
                if (tempNode == root) {
                    res.append(ch);
                    begin++;
                }
                cur++;
                continue;
            }
            tempNode = tempNode.getSubNode(ch);

            if (tempNode == null) {
                //如果前缀树下个节点为空
                res.append(input.charAt(begin));
                cur = begin+1;
                begin = cur;
                tempNode = root;
            } else if (tempNode.isKeyWordEnd()) {
                //前缀树下一个节点不为空, 且找到敏感词
                res.append(replacement);
                cur++;
                begin = cur;
                tempNode = root;
            } else {
                //前缀树下一个节点不为空, 且 不是敏感词, 继续移动指针进行比较
                cur++;
            }
        }
        res.append(input.substring(begin)); //最后一次没有匹配成功, 添加剩余的字符串
        return res.toString();

    }
    //判断该字符是不是无效字符(数字,字母,中文汉字以外的均为无效字符)
    private boolean isSymbol(char c) {
        //东亚文字: 0x2E80 - 0x9FFF
        //中文c >= 0x4E00 &&  c <= 0x9FA5 根据字节码判断
        return !(Character.isAlphabetic(c) || Character.isDigit(c)) && (c < 0x4E00 || c > 0x9FA5);
    }

    //测试
    public static void main(String[] args) {
        SensitiveWordsService sensitiveWordsService = new SensitiveWordsService();
        sensitiveWordsService.addSensitiveWord("暴力");
        String input = "ji, <暴(力>**了";
        System.out.println(sensitiveWordsService.filter(input));

    }

}
