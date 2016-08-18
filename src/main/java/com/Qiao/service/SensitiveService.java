package com.Qiao.service;

import org.apache.commons.lang.CharUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by white and black on 2016/8/12.
 */
@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger= Logger.getLogger(SensitiveService.class);
    private TreeNode rootNode=new TreeNode();
    private boolean isSymbol(char c){
        int ic=(int) c;
        //东亚文字 0x2E80-0x9FFF
        return !CharUtils.isAsciiAlphanumeric(c)&&(ic<0x2E80||ic>0x9FFF);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode=new TreeNode();
        try{
            InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read=new InputStreamReader(is);
            BufferedReader bufferedReader=new BufferedReader(read);
            String lineTxt;
            while ((lineTxt=bufferedReader.readLine())!=null){
                addWord(lineTxt.trim());
            }
            read.close();
        }catch (Exception e){
            logger.error("读取敏感词文件失败"+e.getMessage());
        }
    }
    //增加关键词
    private void addWord(String lineTxt){
        TreeNode tempNode=rootNode;
        for(int i=0;i<lineTxt.length();++i){
            Character c=lineTxt.charAt(i);
            if(isSymbol(c))
                continue;
            TreeNode node=tempNode.getSubNode(c);
            if(node==null){
                node=new TreeNode();
                tempNode.addSubNode(c,node);
            }
            tempNode=node;
            if(i==lineTxt.length()-1){
                tempNode.setkeywordEnd(true);
            }
        }
    }

    private  class TreeNode{
        //是不是关键词的结尾
        private boolean end=false;
        //当前所有节点
        private Map<Character,TreeNode> subNode=new HashMap<Character,TreeNode>();
        public void addSubNode(Character key,TreeNode node){
            subNode.put(key,node);
        }
        public TreeNode getSubNode(Character key){
            return subNode.get(key);
        }
        boolean isKeywordEnd(){
            return end;
        }
        void setkeywordEnd(boolean end){
            this.end=end;
        }
    }
    public String filter(String text){
        if(StringUtils.isEmpty(text)){
            return text;
        }
        StringBuffer result=new StringBuffer();
        String replacement="***";
        TreeNode tempNode=rootNode;
        int begin=0;
        int position=0;
        while (position<text.length()){
            char c=text.charAt(position);
            if(isSymbol(c)){
                if(tempNode==rootNode){
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            tempNode=tempNode.getSubNode(c);
            if(tempNode==null){
                result.append(text.charAt(begin));
                position=begin+1;
                begin=position;
                tempNode=rootNode;
            }else if(tempNode.isKeywordEnd()){
                result.append(replacement);
                position=position+1;
                begin=position;
                tempNode=rootNode;
            }else{
                ++position;
            }
        }
        result.append(text.substring(begin));
        return result.toString();
    }
    public static void main(String[] args){
        SensitiveService s=new SensitiveService();
        s.addWord("色情");
        s.addWord("赌博");
        System.out.println(s.filter("你好色情，你好赌博"));
    }

}
